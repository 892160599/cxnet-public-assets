package com.cxnet.flow.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.flow.domain.NodeBaseEntity;
import com.cxnet.flow.domain.SequenceFlowVo;
import com.cxnet.flow.domain.TaskNodeVo;
import com.cxnet.flow.model.domain.HistoricTaskVo;
import com.cxnet.project.system.config.domain.SysConfig;
import com.cxnet.project.system.config.mapper.SysConfigMapper;
import com.cxnet.project.system.config.service.SysConfigServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.rpc.domain.system.deptrpc.SysDeptRpc;
import com.cxnet.rpc.service.supervise.SysSuperviseMatterServiceRpc;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.flow.model.mapper.SysModelMapper;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.role.domain.SysRole;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.util.IoUtil;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.rest.dto.repository.ProcessDefinitionDiagramDto;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: cxnet
 * @description: ??????????????????
 * @author: Mr.Cai
 * @create: 2020-08-07 17:39
 **/
@Component
@Slf4j
public class TaskUtils {


    @Autowired(required = false)
    private RepositoryService repositoryService;

    @Autowired(required = false)
    private RuntimeService runtimeService;

    @Autowired(required = false)
    private TaskService taskService;

    @Autowired(required = false)
    private HistoryService historyService;

    @Autowired(required = false)
    private ProcessEngine engine;

    @Autowired(required = false)
    private SysModelMapper sysModelMapper;

    @Autowired(required = false)
    private SysSuperviseMatterServiceRpc sysSuperviseMatterServiceRpc;

    private static TaskUtils taskUtils;

    /**
     * ????????????
     */
    private static final Map<String, String> SUBMIT_FLAG_MAP = new HashMap<>();

    /**
     * ??????????????????
     */
    private static String backType = "1";

    @PostConstruct
    public void init() {
        taskUtils = this;
        taskUtils.repositoryService = repositoryService;
        taskUtils.runtimeService = runtimeService;
        taskUtils.taskService = taskService;
        taskUtils.historyService = historyService;
        taskUtils.sysModelMapper = sysModelMapper;
        taskUtils.engine = engine;
        taskUtils.sysSuperviseMatterServiceRpc = sysSuperviseMatterServiceRpc;
    }

    static {
        SUBMIT_FLAG_MAP.put("1", "??????");
        SUBMIT_FLAG_MAP.put("2", "??????");
        SUBMIT_FLAG_MAP.put("3", "??????");
    }

    /**
     * ???????????????
     */
    private static final String DEPT_LEAD_CODE = "001_";

    /**
     * ????????????
     */
    private static final String FG_LEAD_CODE = "002_";

    /**
     * ???????????????
     */
    private static final String SUPERVISE_CODE = "009_";

    /**
     * ?????????????????????????????????????????????id??????
     *
     * @return
     */
    public static List<String> getRunTaskByUserName() {
        String username = SecurityUtils.getUsername();
        // ????????????????????????
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().taskAssignee(username).list();
        // ????????????????????????
        List<Task> taskCandidateUserList = taskUtils.taskService.createTaskQuery().taskCandidateUser(username).list();
        List<Task> taskListAll = new ArrayList<>();
        taskListAll.addAll(taskAssigneeList);
        taskListAll.addAll(taskCandidateUserList);
        return taskListAll.stream().map(Task::getProcessInstanceId).distinct().collect(Collectors.toList());
    }

    /**
     * ??????status?????????????????????????????????????????????id??????
     *
     * @return
     */
    public static List<String> getRunTaskByUserNameAndStatus(String status) {
        if (StringUtils.isEmpty(status)) {
            return CollectionUtil.newArrayList();
        }
        switch (status) {
            // ??????
            case "1":
                return getRunTaskByUserName();
            // ??????
            case "2":
                List<String> hisTaskByRoles = getHisTaskByRoles();
                hisTaskByRoles.removeAll(getRunTaskByUserName());
                return hisTaskByRoles;
            // ??????
            case "3":
                List<String> runTaskByUserName = getRunTaskByUserName();
                runTaskByUserName.addAll(getHisTaskByRoles());
                return runTaskByUserName;
            default:
                return CollectionUtil.newArrayList();
        }
    }

    /**
     * ???????????????????????????????????????????????????id??????
     *
     * @return
     */
    public static List<String> getHisTaskByRoles() {
        List<HistoricTaskInstance> hisTasks = taskUtils.historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee(SecurityUtils.getUsername())
                .listPage(0, 1000);
        if (CollectionUtils.isEmpty(hisTasks)) {
            return CollectionUtil.newArrayList();
        }
        return hisTasks.stream().map(HistoricTaskInstance::getProcessInstanceId).distinct().collect(Collectors.toList());
    }

    /**
     * ????????????????????????
     *
     * @param modelCode ??????????????????
     * @return
     */
    public static Map<String, Object> getDefinitionByModelCode(String modelCode) {
        if (StringUtils.isEmpty(modelCode)) {
            return null;
        }
        String processKey = BillUtils.getProcessKey(modelCode);
        Map<String, Object> map = new HashMap<>(8);
        map.put("processInstanceId", "");
        map.put("historicTaskVoList", "");
        map.put("flowNode", getFlowNodeByKey(processKey));
        return map;
    }

    /**
     * ??????????????????????????????
     *
     * @param piid ??????id
     * @return ????????????
     */
    public static Map<String, Object> getHistoryByPiid(String piid) {
        if (StringUtils.isEmpty(piid)) {
            return null;
        }
        List<HistoricTaskInstance> hisTasks = taskUtils.historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(piid)
                .list();

        hisTasks = hisTasks.stream().sorted(Comparator.comparing(HistoricTaskInstance::getStartTime)).collect(Collectors.toList());

        List<HistoricTaskVo> historicTaskVoList = new ArrayList<>();
        hisTasks.forEach(v -> {
            String submitFlag = JSONObject.parseObject(v.getDescription()).getString("submitFlag");
            if (StringUtils.isNotEmpty(v.getAssignee()) && !ProcessConstant.GUEST.equals(v.getAssignee()) && StringUtils.isNotEmpty(submitFlag)) {
                // ????????????
                String diachronic = "";
                if (v.getDurationInMillis() != null) {
                    diachronic = DateUtils.timeSubtract(v.getStartTime(), v.getEndTime());
                }
                HistoricTaskVo historicTaskVo = new HistoricTaskVo();
                BeanUtil.copyProperties(v, historicTaskVo);
                historicTaskVo.setDiachronic(diachronic);
                // ???????????????
                String assignee = v.getAssignee();
                historicTaskVo.setAssignee(taskUtils.sysModelMapper.getNickNameByUserName(assignee));
                historicTaskVoList.add(historicTaskVo);
            }
        });

        // ????????????????????????
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().processInstanceId(piid).list();
        if (CollectionUtil.isNotEmpty(taskAssigneeList)) {
            Task task = taskAssigneeList.get(0);
            JSONObject description = JSONObject.parseObject(task.getDescription());
            String preemptMode = description.getString("preemptMode");
            List<String> taskIds = taskAssigneeList.stream().map(Task::getId).distinct().collect(Collectors.toList());
            String joinTaskIds = String.join("','", taskIds);
            List<SysUser> nickNames;
            // ??????????????????
            switch (preemptMode) {
                // ????????????
                case "1":
                    nickNames = taskUtils.sysModelMapper.selectUserCandidateList(joinTaskIds);
                    break;
                // ????????????
                case "2":
                    nickNames = taskUtils.sysModelMapper.selectUserList(joinTaskIds);
                    break;
                default:
                    throw new CustomException("?????????????????????");
            }
            String userNames = nickNames.stream().map(SysUser::getNickName).distinct().collect(Collectors.joining(","));
            HistoricTaskVo historicTaskVo = new HistoricTaskVo();
            historicTaskVo.setName(task.getName());
            historicTaskVo.setDescription("{\"sameUnit\":\"1\",\"condition\":\"\",\"preemptMode\":\"1\",\"sameDept\":\"1\",\"submitFlag\":\"??????\"}");
            historicTaskVo.setAssignee(userNames);
            historicTaskVoList.add(historicTaskVo);
        }
        List<NodeBaseEntity> flowNode = getFlowNodeByPiid(piid);
        replenishNode(flowNode, historicTaskVoList, taskAssigneeList);
        // todo ??????????????????
        if (isEnd(piid)) {
//            trimNode();
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("processInstanceId", piid);
        map.put("historicTaskVoList", historicTaskVoList);
        map.put("flowNode", flowNode);
        return map;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param flowNode           ??????????????????
     * @param historicTaskVoList ????????????
     * @param taskAssigneeList   ????????????
     */
    private static void replenishNode(List<NodeBaseEntity> flowNode, List<HistoricTaskVo> historicTaskVoList, List<Task> taskAssigneeList) {
        if (CollectionUtil.isEmpty(flowNode) || CollectionUtil.isEmpty(historicTaskVoList)) {
            return;
        }
        Task task = null;
        if (CollectionUtil.isNotEmpty(taskAssigneeList)) {
            task = taskAssigneeList.get(0);
        }
        // ??????????????????
        Map<String, HistoricTaskVo> historicTaskVoMap = historicTaskVoList.stream().filter(v -> v.getStartTime() != null)
                .collect(Collectors.toMap(HistoricTaskVo::getTaskDefinitionKey, Function.identity(), (c1, c2) -> c1.getStartTime().compareTo(c2.getStartTime()) > 0 ? c1 : c2));
        // ????????????
        List<SequenceFlowVo> sequenceFlowVoBacks = new ArrayList<>();
        for (NodeBaseEntity nodeBaseEntity : flowNode) {
            // ????????????
            if (!"sequenceFlow".equals(nodeBaseEntity.getType())) {
                HistoricTaskVo historicTaskVo = historicTaskVoMap.get(nodeBaseEntity.getId());
                // ????????????
                if (ObjectUtil.isNotNull(historicTaskVo)) {
                    nodeBaseEntity.setStartTime(historicTaskVo.getStartTime());
                    nodeBaseEntity.setEndTime(historicTaskVo.getEndTime());
                    nodeBaseEntity.setUsers(historicTaskVo.getAssignee());
                    nodeBaseEntity.setStatus(ProcessConstant.STATUS_2);
                    TaskNodeVo taskNodeVo = (TaskNodeVo) nodeBaseEntity;
                    JSONObject description = JSONObject.parseObject(historicTaskVo.getDescription());
                    setDescription(taskNodeVo, description);
                }
                // ????????????
                if (ObjectUtil.isNotNull(task) && nodeBaseEntity.getId().equals(task.getTaskDefinitionKey())) {
                    nodeBaseEntity.setStatus(ProcessConstant.STATUS_1);
                }
                // ????????????
                if (ObjectUtil.isNull(task) && "endEvent".equals(nodeBaseEntity.getType())) {
                    nodeBaseEntity.setStatus(ProcessConstant.STATUS_2);
                }
                continue;
            }
            // ????????????
            if ("sequenceFlow".equals(nodeBaseEntity.getType())) {
                for (NodeBaseEntity v : flowNode) {
                    if ("sequenceFlow".equals(v.getType())) {
                        continue;
                    }
                    TaskNodeVo taskNodeVo = (TaskNodeVo) v;
                    if (CollectionUtil.isNotEmpty(taskNodeVo.getTargetFlowIds()) && taskNodeVo.getTargetFlowIds().contains(nodeBaseEntity.getId())) {
                        nodeBaseEntity.setStatus(v.getStatus());
                        nodeBaseEntity.setStartTime(v.getStartTime());
                        nodeBaseEntity.setEndTime(v.getEndTime());
                        // ??????
                        if (ProcessConstant.STATUS_7.equals(v.getStatus()) || ProcessConstant.STATUS_9.equals(v.getStatus())) {
                            SequenceFlowVo sequenceFlowVo = new SequenceFlowVo();
                            BeanUtil.copyProperties(nodeBaseEntity, sequenceFlowVo);
                            sequenceFlowVo.setStatus(ProcessConstant.STATUS_2);
                            sequenceFlowVoBacks.add(sequenceFlowVo);
                            continue;
                        }
                    }
                }
            }
        }
        flowNode.addAll(sequenceFlowVoBacks);
    }

    /**
     * ????????????????????????
     *
     * @param piid ????????????id
     * @return
     */
    private static List<NodeBaseEntity> getFlowNodeByPiid(String piid) {
        if (StringUtils.isEmpty(piid)) {
            return null;
        }
        HistoricProcessInstance hisProIns = taskUtils.historyService.createHistoricProcessInstanceQuery().processInstanceId(piid).singleResult();
        return getFlowNodeByProcessDefinitionId(hisProIns.getProcessDefinitionId());
    }

    /**
     * ????????????????????????
     *
     * @param key ??????key
     * @return
     */
    private static List<NodeBaseEntity> getFlowNodeByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        List<HistoricProcessInstance> hisProIns = taskUtils.historyService.createHistoricProcessInstanceQuery().processDefinitionKey(key).orderByProcessDefinitionVersion().desc().list();
        if (CollectionUtil.isEmpty(hisProIns)) {
            return null;
        }
        return getFlowNodeByProcessDefinitionId(hisProIns.get(0).getProcessDefinitionId());
    }

    /**
     * ????????????????????????
     *
     * @param processDefinitionId ????????????id
     * @return
     */
    private static List<NodeBaseEntity> getFlowNodeByProcessDefinitionId(String processDefinitionId) {
        if (StringUtils.isEmpty(processDefinitionId)) {
            return null;
        }
        // ??????????????????
        List<NodeBaseEntity> taskNodeVos = new ArrayList<>();
        BpmnModelInstance bpmnModelInstance = taskUtils.repositoryService.getBpmnModelInstance(processDefinitionId);
        Collection<StartEvent> startEvents = bpmnModelInstance.getModelElementsByType(StartEvent.class);
        Collection<UserTask> userTasks = bpmnModelInstance.getModelElementsByType(UserTask.class);
        Collection<EndEvent> endEvents = bpmnModelInstance.getModelElementsByType(EndEvent.class);
        Collection<SequenceFlow> sequenceFlow = bpmnModelInstance.getModelElementsByType(SequenceFlow.class);
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) taskUtils.repositoryService.getProcessDefinition(processDefinitionId);
        // ????????????
        startEvents.forEach(v -> taskNodeVos.add(buildTaskNodeVo(v, processDefinition, v.getElementType().getTypeName())));
        // ????????????
        userTasks.forEach(v -> taskNodeVos.add(buildTaskNodeVo(v, processDefinition, v.getElementType().getTypeName())));
        // ????????????
        endEvents.forEach(v -> taskNodeVos.add(buildTaskNodeVo(v, processDefinition, v.getElementType().getTypeName())));
        // ??????
        sequenceFlow.forEach(v -> {
            String flowId = v.getId();
            TransitionImpl transition = processDefinition.findTransition(flowId);
            SequenceFlowVo sequenceFlowVo = new SequenceFlowVo();
            List<Integer> waypoints = transition.getWaypoints();
            if (CollectionUtil.isNotEmpty(waypoints)) {
                List<Map<String, Integer>> waypointList = new ArrayList<>();
                Map<String, Integer> waypoint = null;
                for (int i = 0; i < waypoints.size(); i++) {
                    if (i % 2 == 1) {
                        waypoint = new HashMap<>(2);
                        waypoint.put("x", waypoints.get(i - 1));
                        waypoint.put("y", waypoints.get(i));
                        waypointList.add(waypoint);
                    }
                }
                sequenceFlowVo.setWaypoints(waypointList);
            }
            sequenceFlowVo.setId(flowId);
            sequenceFlowVo.setName(v.getName());
            sequenceFlowVo.setType(v.getElementType().getTypeName());
            sequenceFlowVo.setTargetNodeId(v.getTarget().getId());
            sequenceFlowVo.setSourceNodeId(v.getSource().getId());
            taskNodeVos.add(sequenceFlowVo);
        });
        return taskNodeVos;
    }

    /**
     * ModelElementInstance ==> TaskNodeVo
     *
     * @param modelElementInstance
     * @param processDefinition
     * @param typeName             ????????????
     * @return TaskNodeVo
     */
    private static TaskNodeVo buildTaskNodeVo(ModelElementInstance modelElementInstance, ProcessDefinitionEntity processDefinition, String typeName) {
        TaskNodeVo taskNodeVo = new TaskNodeVo();
        if (StringUtils.isEmpty(typeName) || ObjectUtil.isNull(modelElementInstance) || ObjectUtil.isNull(processDefinition)) {
            return taskNodeVo;
        }
        switch (typeName) {
            case "startEvent":
                StartEvent startEvent = (StartEvent) modelElementInstance;
                taskNodeVo = new TaskNodeVo(startEvent);
                taskNodeVo.setStatus(ProcessConstant.STATUS_2);
                break;
            case "userTask":
                UserTask userTask = (UserTask) modelElementInstance;
                taskNodeVo = new TaskNodeVo(userTask);
                Documentation next = userTask.getDocumentations().iterator().next();
                if (next != null) {
                    JSONObject description = JSONObject.parseObject(next.getTextContent());
                    setDescription(taskNodeVo, description);
                }

                break;
            case "endEvent":
                EndEvent endEvent = (EndEvent) modelElementInstance;
                taskNodeVo = new TaskNodeVo(endEvent);
                break;
            default:
        }
        setBounds(processDefinition, taskNodeVo);
        return taskNodeVo;
    }

    /**
     * ????????????????????????
     *
     * @param taskNodeVo
     * @param description
     */
    private static void setDescription(TaskNodeVo taskNodeVo, JSONObject description) {
        if (description != null && ObjectUtil.isNotNull(taskNodeVo)) {
            taskNodeVo.setPreemptMode(description.getString("preemptMode"));
            taskNodeVo.setSameUnit(description.getString("sameUnit"));
            taskNodeVo.setSameDept(description.getString("sameDept"));
            taskNodeVo.setOpinion(description.getString("opinion"));
            taskNodeVo.setWriteSign(description.getString("writeSign"));
            String actStatus = description.getString("actStatus");
            if (ProcessConstant.STATUS_7.equals(actStatus) || ProcessConstant.STATUS_9.equals(actStatus)) {
                taskNodeVo.setStatus(actStatus);
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param processDefinition
     * @param taskNodeVo
     */
    private static void setBounds(ProcessDefinitionEntity processDefinition, TaskNodeVo taskNodeVo) {
        if (ObjectUtil.isNull(taskNodeVo) || StringUtils.isEmpty(taskNodeVo.getId())) {
            return;
        }
        ActivityImpl activity = processDefinition.findActivity(taskNodeVo.getId());
        if (ObjectUtil.isNotNull(activity)) {
            taskNodeVo.setX(activity.getX());
            taskNodeVo.setY(activity.getY());
            taskNodeVo.setHeight(activity.getHeight());
            taskNodeVo.setWidth(activity.getWidth());
        }
    }

    /**
     * ???????????????xml?????????
     *
     * @param piid ??????id
     * @return ProcessDefinitionDiagramDto
     */
    private ProcessDefinitionDiagramDto getProcessDefinitionDiagramDto(String piid) {
        if (StringUtils.isEmpty(piid)) {
            return null;
        }
        String processDefinitionId = taskUtils.runtimeService.createProcessInstanceQuery().processInstanceId(piid).singleResult().getProcessDefinitionId();
        ProcessDefinitionDiagramDto processDefinitionDiagramDto = null;
        InputStream processModelIn = null;
        processModelIn = taskUtils.repositoryService.getProcessModel(processDefinitionId);
        byte[] processModel = IoUtil.readInputStream(processModelIn, "processModelBpmn20Xml");
        try {
            processDefinitionDiagramDto = ProcessDefinitionDiagramDto.create(processDefinitionId, new String(processModel, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("????????????:{}", e.getMessage(), e);
        }
        return processDefinitionDiagramDto;
    }

    /**
     * ??????????????????
     *
     * @param pidds     ????????????id??????
     * @param variables ????????????
     */
    public static void complete(List<String> pidds, Map<String, Object> variables) {
        if (CollectionUtils.isEmpty(pidds)) {
            throw new CustomException("???????????????????????????");
        }
        pidds.forEach(v -> complete(v, variables));
    }

    /**
     * ????????????
     *
     * @param pid       ??????id
     * @param variables ????????????
     */
    public static List<Task> complete(String pid, Map<String, Object> variables) {
        // ???????????????????????????=?????????????????????=??????????????????????????????????????????????????????=????????????????????????????????????????????????????????????
        List<Task> listAllDistinct = taskUtils.getUserTaskListByUserNameAndPid(pid);
        // ????????????
        for (Task t : listAllDistinct) {
            String taskId = t.getId();
            t.setDescription(budDescription(variables, t));
            taskUtils.taskService.saveTask(t);
            variables.put("users", ProcessConstant.GUEST);
            variables.put("assigneeList", Arrays.asList(ProcessConstant.GUEST));
            taskUtils.taskService.complete(taskId, variables);
        }
        // ????????????????????????
        return taskUtils.addTaskUsers(pid, listAllDistinct.get(0).getTaskDefinitionKey());
    }

    /**
     * ????????????????????????
     *
     * @param pid                  ??????id
     * @param oldTaskDefinitionKey ????????????key
     */
    private List<Task> addTaskUsers(String pid, String oldTaskDefinitionKey) {
        // ??????????????????
        List<Task> tasks = taskUtils.taskService.createTaskQuery().processInstanceId(pid).list();
        if (CollectionUtil.isNotEmpty(tasks) && !tasks.get(0).getTaskDefinitionKey().equals(oldTaskDefinitionKey)) {
            // ???????????????????????????
            List<String> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toList());
            List<String> groupIdList = taskUtils.sysModelMapper.selectGroupIdList(String.join("','", taskIds));
            Task task = tasks.get(0);
            String taskId = task.getId();
            String description = task.getDescription();
            if (StringUtils.isEmpty(description)) {
                throw new CustomException("?????????????????????");
            }
            // ??????????????????????????????
            String actUnitId = String.valueOf(taskUtils.taskService.getVariable(taskId, ProcessConstant.UNIT_ID));
            String actDeptId = String.valueOf(taskUtils.taskService.getVariable(taskId, ProcessConstant.DEPT_ID));
            String createBy = String.valueOf(taskUtils.taskService.getVariable(taskId, ProcessConstant.CREATE_BY));
            if (log.isInfoEnabled()) {
                log.info("???????????????id:{}, ??????id???{}", actUnitId, actDeptId);
            }
            // ????????????????????????
            JSONObject jsonObject = JSON.parseObject(description);
            String sameUnit = jsonObject.getString("sameUnit");
            String sameDept = jsonObject.getString("sameDept");
            String preemptMode = jsonObject.getString("preemptMode");
            String superviseId = jsonObject.getString("superviseId");
            // ???????????????
            List<String> nextUsers;
            // ????????????????????????????????????????????????????????????
            if (isFirstTask(pid)) {
                task.setAssignee(createBy);
                taskUtils.taskService.saveTask(task);
                taskUtils.taskService.deleteCandidateUser(taskId, ProcessConstant.GUEST);
                return tasks;
            }
            if (groupIdList.contains(DEPT_LEAD_CODE) || groupIdList.contains(FG_LEAD_CODE)) {
                // ????????????/????????????
                nextUsers = getLeadUsers(actDeptId, groupIdList);
            } else if (ProcessConstant.SAME_DEPT.equals(sameDept)) {
                // ?????????
                nextUsers = getNextUsers(actUnitId, actDeptId, groupIdList);
            } else if (ProcessConstant.SAME_UNIT.equals(sameUnit)) {
                // ?????????
                nextUsers = getNextUsers(actUnitId, groupIdList);
            } else {
                // ?????????
                nextUsers = getNextUsers(groupIdList);
            }

            if (CollectionUtil.isEmpty(nextUsers) && StringUtils.isEmpty(task.getAssignee())) {
                throw new CustomException("??????????????????,?????????????????????????????????");
            }

            if (log.isInfoEnabled()) {
                log.info("?????????????????????????????????id?????????{}", ObjectUtil.isNull(nextUsers) ? "" : JSON.toJSONString(nextUsers));
            }

            // ??????????????????
            switch (preemptMode) {
                // ????????????
                case "1":
                    nextUsers.forEach(v -> taskUtils.taskService.addCandidateUser(taskId, v));
                    taskUtils.taskService.deleteCandidateUser(taskId, ProcessConstant.GUEST);
                    break;
                // ????????????
                case "2":
                    nextUsers.forEach(v ->
                            taskUtils.runtimeService.createProcessInstanceModification(pid)
                                    .startBeforeActivity(task.getTaskDefinitionKey())
                                    .setVariable("assignee", v)
                                    .execute());
                    // todo ??????
                    taskUtils.taskService.complete(taskId);
                    break;
                default:
                    throw new CustomException("?????????????????????");
            }
            // ???????????????????????????
            if (StringUtils.isNotEmpty(superviseId)) {
                Map<String, Object> variables = taskService.getVariables(taskId);
                sysSuperviseMatterServiceRpc.addSysSuperviseMatter(superviseId, variables);
            }

        }
        return tasks;
    }

    /**
     * ??????????????????/??????????????????
     *
     * @param actDeptId
     * @param groupIdList
     * @return
     */
    private List<String> getLeadUsers(String actDeptId, List<String> groupIdList) {
        List<String> users = new ArrayList();
        SysDeptRpc sysDeptRpc = sysModelMapper.selectDeptById(actDeptId);
        if (groupIdList.contains(DEPT_LEAD_CODE) && StringUtils.isNotEmpty(sysDeptRpc.getDeptLeadCode())) {
            users.add(sysDeptRpc.getDeptLeadCode());
        }
        if (groupIdList.contains(FG_LEAD_CODE) && StringUtils.isNotEmpty(sysDeptRpc.getFgLeadCode())) {
            users.add(sysDeptRpc.getFgLeadCode());
        }
        return users;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param pid
     * @return
     */
    private List<Task> getUserTaskListByUserNameAndPid(String pid) {
        String username = SecurityUtils.getUsername();
        // ??????????????????
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
        // ??????????????????
        List<Task> taskCandidateUserList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskCandidateUser(username).list();
        if (CollectionUtil.isEmpty(taskAssigneeList) && CollectionUtil.isEmpty(taskCandidateUserList)) {
            throw new CustomException("???????????????");
        }
        List<Task> taskListAll = new ArrayList<>();
        // ???????????????
        for (Task v : taskCandidateUserList) {
            taskUtils.taskService.claim(v.getId(), username);
            taskListAll.add(taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskId(v.getId()).taskAssignee(username).singleResult());
        }
        // ????????????
        taskListAll.addAll(taskAssigneeList);
        return taskListAll.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(Task::getId))), ArrayList::new)
        );
    }

    /**
     * ????????????id???????????????????????????
     *
     * @param pid
     * @return true false
     */
    public static boolean isToDo(String pid) {
        String username = SecurityUtils.getUsername();
        // ??????????????????
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
        // ??????????????????
        List<Task> taskCandidateUserList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskCandidateUser(username).list();
        return CollectionUtil.isNotEmpty(taskAssigneeList) || CollectionUtil.isNotEmpty(taskCandidateUserList);
    }

    /**
     * ??????????????????
     *
     * @param pidds                ????????????id??????
     * @param commonProcessRequest ????????????????????????
     */
    public static void complete(List<String> pidds, CommonProcessRequest commonProcessRequest) {
        String submitFlag = commonProcessRequest.getSubmitFlag();
        Map<String, Object> variables;
        switch (submitFlag) {
            case "1":
                variables = getStartVariables(commonProcessRequest);
                break;
            case "2":
                variables = getGoVariables(commonProcessRequest);
                break;
            case "3":
                variables = getBackVariables(commonProcessRequest);
                break;
            default:
                throw new CustomException("?????????????????????");
        }
        complete(pidds, variables);
    }

    /**
     * ??????processKey??????????????????
     *
     * @param processKey ??????key
     * @return ??????id
     */
    public static String startProcessInstanceByProcessKey(String processKey) {
        String piid;
        try {
            Map<String, Object> map = new HashMap<>(1);
            map.put("users", SecurityUtils.getUsername());
            piid = taskUtils.runtimeService.startProcessInstanceByKey(processKey, map).getId();
            List<Task> tasks = taskUtils.taskService.createTaskQuery().processInstanceId(piid).list();
            tasks.forEach(v -> {
                v.setAssignee(SecurityUtils.getUsername());
                taskUtils.taskService.saveTask(v);
            });
        } catch (Exception e) {
            throw new CustomException("?????????????????????");
        }
        return piid;
    }

    /**
     * ????????????????????????????????????
     *
     * @param billTypeCode ??????????????????
     * @return ??????id
     */
    public static String startProcessInstanceByBillTypeCode(String billTypeCode) {
        String processKey = BillUtils.getProcessKey(billTypeCode);
        return startProcessInstanceByProcessKey(processKey);
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param billTypeCode      ?????? ??????????????????
     * @param processInstanceId ????????????id
     * @return ??????????????????
     */
    public static String startProcessInstanceByBillTypeCode(String billTypeCode, String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)) {
            return startProcessInstanceByBillTypeCode(billTypeCode);
        }
        if (TaskUtils.isFirstTask(processInstanceId)) {
            processInstanceId = checkFlowVersonAndStartNewProcessInstance(billTypeCode, processInstanceId);
        }
        return processInstanceId;
    }

    /**
     * ???????????????????????????id??????????????????
     *
     * @param billTypeCode ??????????????????
     * @param unitId       ??????id
     * @return ??????id
     */
    public static String startProcessInstanceByBillTypeCodeAndUnit(String billTypeCode, String unitId) {
        String processKey = BillUtils.getProcessKeyByUnit(billTypeCode, unitId);
        return startProcessInstanceByProcessKey(processKey);
    }

    /**
     * ???????????????????????????id?????????????????????????????????????????????
     *
     * @param billTypeCode      ?????? ??????????????????
     * @param processInstanceId ????????????id
     * @return ??????????????????
     */
    public static String startProcessInstanceByBillTypeCodeAndUnitId(String billTypeCode, String processInstanceId) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (StringUtils.isEmpty(processInstanceId)) {
            return startProcessInstanceByBillTypeCodeAndUnit(billTypeCode, user.getDeptId());
        }
        // ?????????????????????????????????
        if (TaskUtils.isFirstTask(processInstanceId)) {
            processInstanceId = checkFlowVersonAndStartNewProcessInstance(billTypeCode, processInstanceId, user.getDeptId());
        }
        return processInstanceId;
    }

    /**
     * ????????????????????????
     *
     * @param commonProcessRequest
     * @return
     */
    public static Map<String, Object> getStartVariables(CommonProcessRequest commonProcessRequest) {
        return getVariables(commonProcessRequest, ProcessConstant.GO, null);
    }

    /**
     * ????????????????????????
     *
     * @param commonProcessRequest
     * @return
     */
    public static Map<String, Object> getStartVariables(CommonProcessRequest commonProcessRequest, Object o) {
        commonProcessRequest.setSubmitFlag("1");
        return getVariables(commonProcessRequest, ProcessConstant.GO, o);
    }

    /**
     * ????????????????????????map
     *
     * @param commonProcessRequest
     * @return
     */
    public static Map<String, Object> getGoVariables(CommonProcessRequest commonProcessRequest) {
        return getVariables(commonProcessRequest, ProcessConstant.GO, null);
    }

    /**
     * ????????????????????????map
     *
     * @param commonProcessRequest ????????????????????????
     * @param o                    ??????????????????????????????
     * @return
     */
    public static Map<String, Object> getGoVariables(CommonProcessRequest commonProcessRequest, Object o) {
        commonProcessRequest.setSubmitFlag("2");
        return getVariables(commonProcessRequest, ProcessConstant.GO, o);
    }

    /**
     * ????????????????????????map
     *
     * @param commonProcessRequest
     * @return
     */
    public static Map<String, Object> getBackVariables(CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return getVariables(commonProcessRequest, ProcessConstant.BACK, null);
    }

    /**
     * ??????????????????map
     *
     * @param commonProcessRequest
     * @param submitFlag
     * @return
     */
    public static Map<String, Object> getVariables(CommonProcessRequest commonProcessRequest, String submitFlag, Object o) {
        Map<String, Object> variables = new HashMap<>(100);
        if (ObjectUtil.isNotNull(o)) {
            BeanUtil.beanToMap(o, variables, true, true);
        }
        variables.put(ProcessConstant.GO, "");
        variables.put(ProcessConstant.BACK, "");
        variables.put(submitFlag, "1");
        variables.put(ProcessConstant.SUBMIT_FLAG, commonProcessRequest.getSubmitFlag());
        variables.put(ProcessConstant.OPINION, commonProcessRequest.getOpinion());
        variables.put(ProcessConstant.WRITE_SIGN, commonProcessRequest.getWriteSign());
        return variables;
    }

    /**
     * ????????????id????????????????????????
     *
     * @param piid ????????????id
     * @return true?????? false?????????
     */
    public static boolean isEnd(String piid) {
        return taskUtils.runtimeService.createProcessInstanceQuery().processInstanceId(piid).singleResult() == null;
    }

    /**
     * ????????????id????????????????????????
     *
     * @param piid ????????????id
     * @return true???????????? false???????????????
     */
    public static boolean isFirstTask(String piid) {
        List<Task> list = taskUtils.taskService.createTaskQuery().processInstanceId(piid).list();
        if (CollectionUtil.isEmpty(list)) {
            return false;
        }
        Task task = list.get(0);
        String taskDefinitionKey = task.getTaskDefinitionKey();
        return taskDefinitionKey.equals(getFirstTaskNode(piid).getActivityId());
    }

    /**
     * ???????????????
     *
     * @param piid
     * @return
     */
    public static HistoricActivityInstance getFirstTaskNode(String piid) {
        List<HistoricActivityInstance> list = taskUtils.historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(piid)
                .activityType("userTask")
                .list();
        List<HistoricActivityInstance> hais = list.stream().sorted(Comparator.comparing(HistoricActivityInstance::getStartTime, Comparator.reverseOrder()).reversed()).collect(Collectors.toList());
        HistoricActivityInstance historicActivityInstance = hais.get(0);
        return historicActivityInstance;
    }

    /**
     * ?????? DESCRIPTION ?????????
     *
     * @param variables
     * @return
     */
    private static String budDescriptionOld(Map<String, Object> variables, Task task) {
        HashMap<String, Object> descriptionMap = new HashMap<>(16);
        String submitFlag = (String) variables.get(ProcessConstant.SUBMIT_FLAG);
        // ????????????
        descriptionMap.put("condition", "");
        descriptionMap.put(ProcessConstant.SUBMIT_FLAG, SUBMIT_FLAG_MAP.get(submitFlag));
        descriptionMap.put(ProcessConstant.OPINION, variables.get(ProcessConstant.OPINION));
        descriptionMap.put(ProcessConstant.WRITE_SIGN, variables.get(ProcessConstant.WRITE_SIGN));
        // ???????????????????????????????????????
        String status = "1".equals(TaskUtils.backType) ? "9" : "7";
        descriptionMap.put("actStatus", "3".equals(submitFlag) ? status : submitFlag);
        // ??????????????????
        String description = task.getDescription();
        if (StringUtils.isNotEmpty(description)) {
            JSONObject jsonObject = JSON.parseObject(description);
            descriptionMap.put("sameUnit", jsonObject.getString("sameUnit"));
            descriptionMap.put("sameDept", jsonObject.getString("sameDept"));
            descriptionMap.put("preemptMode", jsonObject.getString("preemptMode"));
        }
        return JSON.toJSONString(descriptionMap);
    }

    /**
     * ?????? DESCRIPTION ?????????
     *
     * @param variables ???
     * @return ??????
     */
    private static String budDescription(Map<String, Object> variables, Task task) {
        JSONObject jsonObject = new JSONObject();
        String description = task.getDescription();
        String submitFlag = (String) variables.get(ProcessConstant.SUBMIT_FLAG);
        // ??????????????????
        if (StringUtils.isNotEmpty(description)) {
            jsonObject = JSON.parseObject(description);
            jsonObject.remove("condition");
            jsonObject.remove(ProcessConstant.SUBMIT_FLAG);
            jsonObject.remove(ProcessConstant.OPINION);
            jsonObject.remove("actStatus");
        }
        // ????????????
        jsonObject.put("condition", "");
        jsonObject.put(ProcessConstant.SUBMIT_FLAG, SUBMIT_FLAG_MAP.get(submitFlag));
        jsonObject.put(ProcessConstant.OPINION, variables.get(ProcessConstant.OPINION));
        // ???????????????????????????????????????
        String status = "1".equals(TaskUtils.backType) ? "9" : "7";
        jsonObject.put("actStatus", "3".equals(submitFlag) ? status : submitFlag);
        return JSON.toJSONString(jsonObject);
    }

    /**
     * ???????????????
     *
     * @param key  ????????????
     * @param name ????????????
     * @return Deployment
     */
    public static Deployment createDeploymentByKeyAndName(String key, String name) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(name)) {
            throw new CustomException("????????????????????????????????????");
        }
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" id=\"Definitions_1vmwf84\" targetNamespace=\"http://bpmn.io/schema/bpmn\" exporter=\"Camunda Modeler\" exporterVersion=\"3.7.3\">\n" +
                "  <bpmn:process id=\"" + key + "\" name=\"" + name + "\" isExecutable=\"true\">\n" +
                "    <bpmn:documentation></bpmn:documentation>" +
                "      <bpmn:startEvent id=\"StartEvent_1\">\n" +
                "        <bpmn:documentation></bpmn:documentation>\n" +
                "        <bpmn:outgoing>Flow_17grt1z</bpmn:outgoing>\n" +
                "      </bpmn:startEvent>" +
                "  </bpmn:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"" + key + "\">\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_1\">\n" +
                "        <dc:Bounds x=\"179\" y=\"79\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn:definitions>\n";

        Deployment deploy = null;
        try {
            deploy = taskUtils.repositoryService.createDeployment().name(name)
                    .source(name).tenantId("admin")
                    .addString(name + ".bpmn20.xml", text).deploy();
        } catch (Exception e) {
            throw new CustomException("??????????????????,?????????????????????");
        }
        return deploy;
    }

    /**
     * ??????deploymentId??????
     *
     * @param deploymentId id
     */
    public static void deleteDeployment(String deploymentId) {
        if (StringUtils.isEmpty(deploymentId)) {
            return;
        }
        try {
            taskUtils.repositoryService.deleteDeployment(deploymentId, true);
        } catch (Exception e) {
            throw new CustomException("???????????????????????????????????????");
        }
    }

    /**
     * ??????deploymentId??????
     *
     * @param deploymentId id
     */
    public static ProcessDefinition selectDeployment(String deploymentId) {
        if (StringUtils.isEmpty(deploymentId)) {
            return null;
        }
        ProcessDefinition processDefinition = taskUtils.repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        if (processDefinition == null) {
            throw new CustomException("??????????????????");
        }
        return processDefinition;
    }

    /**
     * ??????deploymentId????????????
     *
     * @param deploymentId id
     */
    public static void suspendProcessDefinitionById(String deploymentId) {
        if (StringUtils.isEmpty(deploymentId)) {
            return;
        }
        taskUtils.repositoryService.suspendProcessDefinitionById(deploymentId);
    }

    /**
     * ??????????????????
     *
     * @return ??????
     */
    private static List<String> getRoles() {
        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        return roles.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
    }

    /**
     * ??????????????????
     *
     * @param unitId ??????id
     * @param roles  ??????
     * @return ??????
     */
    private static List<String> getNextUsers(String unitId, List<String> roles) {
        List<String> users = taskUtils.sysModelMapper.selectUserNamesByUnitId(unitId);
        List<String> usersByroles = getNextUsers(roles);
        users.retainAll(usersByroles);
        return users;
    }

    /**
     * ??????????????????
     *
     * @param unitId ??????id
     * @param deptId ??????id
     * @param roles  ??????
     * @return ??????
     */
    private static List<String> getNextUsers(String unitId, String deptId, List<String> roles) {
        List<String> users = taskUtils.sysModelMapper.getUsersByUnitIdAndDeptId(deptId);
        List<String> usersByroles = getNextUsers(roles);
        users.retainAll(usersByroles);
        return users;
    }

    /**
     * ??????????????????
     *
     * @param roles ??????
     * @return ??????
     */
    private static List<String> getNextUsers(List<String> roles) {
        return taskUtils.sysModelMapper.getUsersByRoles(String.join("','", roles));
    }

    /**
     * ????????????????????????name
     *
     * @param piid ??????id
     * @return ??????
     */
    public static String getTaskNameByPiid(String piid) {
        Task task = taskUtils.taskService.createTaskQuery().processInstanceId(piid).list().get(0);
        return task.getName();
    }

    /**
     * ???????????????????????? description
     *
     * @param pid ??????id
     * @return ??????
     */
    public static JSONObject checkEditPermis(String pid) {
        String username = SecurityUtils.getUsername();
        List<HistoricTaskInstance> hisTasks = taskUtils.historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(pid)
                .list();
        if (CollectionUtil.isEmpty(hisTasks) || hisTasks.size() < 2 || TaskUtils.isFirstTask(pid) || TaskUtils.isEnd(pid)) {
            return new JSONObject();
        }
        hisTasks = hisTasks.stream().sorted(Comparator.comparing(HistoricTaskInstance::getStartTime).reversed()).collect(Collectors.toList());
        HistoricTaskInstance historicTaskInstance = hisTasks.get(0);
        String description = historicTaskInstance.getDescription();
        // ??????????????????
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
        // ??????????????????
        List<Task> taskCandidateUserList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskCandidateUser(username).list();
        // TODO ?????????????????????
        if (CollectionUtil.isNotEmpty(taskAssigneeList) || CollectionUtil.isNotEmpty(taskCandidateUserList)) {
            // ????????????????????????
            return JSON.parseObject(description);
        }
        return new JSONObject();
    }

    /**
     * ????????????????????????????????????
     *
     * @param piid ??????id
     */
    public static boolean checkBackTaskBack(String piid) {
        String username = SecurityUtils.getUsername();
        List<HistoricTaskInstance> hisTasks = taskUtils.historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(piid)
                .list();
        if (CollectionUtil.isEmpty(hisTasks) || hisTasks.size() < 2 || TaskUtils.isFirstTask(piid) || TaskUtils.isEnd(piid)) {
            return false;
        }
        hisTasks = hisTasks.stream().sorted(Comparator.comparing(HistoricTaskInstance::getStartTime)).collect(Collectors.toList());
        HistoricTaskInstance historicTaskInstance = hisTasks.get(hisTasks.size() - 2);
        return username.equals(historicTaskInstance.getAssignee());
    }

    /**
     * ????????????
     * todo ??????????????????
     *
     * @param pidds                ??????id
     * @param commonProcessRequest ??????
     */
    public static void backTaskBack(List<String> pidds, CommonProcessRequest commonProcessRequest) {
        String activityId = "";
        Map<String, Object> backVariables = getBackVariables(commonProcessRequest);

        if (ObjectUtil.isNull(commonProcessRequest.getOpinion())) {
            commonProcessRequest.setOpinion("");
        }
        for (String piid : pidds) {
            if (!checkBackTaskBack(piid)) {
                throw new CustomException("???????????????");
            }
            List<Task> tasks = taskUtils.taskService.createTaskQuery().processInstanceId(piid).list();
            if (CollectionUtil.isEmpty(tasks)) {
                throw new CustomException("????????????????????????????????????");
            }
            Task task = tasks.get(0);
            ActivityInstance tree = taskUtils.runtimeService.getActivityInstance(piid);
            // ???????????????
            List<String> taskDefKeyList = taskUtils.sysModelMapper.selectTaskDefKeyOrder(piid);
            for (int i = 0; i < taskDefKeyList.size(); i++) {
                if (task.getTaskDefinitionKey().equals(taskDefKeyList.get(i)) && i > 0) {
                    activityId = taskDefKeyList.get(i - 1);
                }
            }
            if (StringUtils.isEmpty(activityId)) {
                throw new CustomException("?????????????????????");
            }
            taskUtils.taskService.createComment(task.getId(), piid, commonProcessRequest.getOpinion());
            task.setDescription(budDescription(backVariables, task));
            taskUtils.taskService.saveTask(task);
            backVariables.put("users", ProcessConstant.GUEST);
            backVariables.put("assigneeList", Arrays.asList(ProcessConstant.GUEST));
            taskUtils.runtimeService
                    .createProcessInstanceModification(piid)
                    .cancelActivityInstance(getInstanceIdForActivity(tree, task.getTaskDefinitionKey()))
                    .startBeforeActivity(activityId)
                    .setVariablesLocal(backVariables)
                    .execute();
            taskUtils.addTaskUsers(piid, task.getTaskDefinitionKey());
            // ????????????
            List<HistoricTaskInstance> hisTasks = taskUtils.historyService
                    .createHistoricTaskInstanceQuery()
                    .processInstanceId(piid)
                    .list();
            hisTasks = hisTasks.stream().sorted(Comparator.comparing(HistoricTaskInstance::getStartTime)).collect(Collectors.toList());
            if (hisTasks.size() < 3) {
                throw new CustomException("??????????????????????????????????????????");
            }
            taskUtils.historyService.deleteHistoricTaskInstance(hisTasks.get(hisTasks.size() - 1).getId());
            taskUtils.historyService.deleteHistoricTaskInstance(hisTasks.get(hisTasks.size() - 2).getId());
        }
    }

    /**
     * ????????????
     *
     * @param pidds                ??????id
     * @param commonProcessRequest ??????
     */
    public static void backProcess(List<String> pidds, CommonProcessRequest commonProcessRequest) {
        // ????????????????????????
        String backType = taskUtils.sysModelMapper.selectByCode();
        if (StringUtils.isNotEmpty(backType)) {
            TaskUtils.backType = backType;
        }
        String activityId = "";
        List<HistoricActivityInstance> resultList;
        String username = SecurityUtils.getUsername();
        Map<String, Object> backVariables = getBackVariables(commonProcessRequest);

        if (ObjectUtil.isNull(commonProcessRequest.getOpinion())) {
            commonProcessRequest.setOpinion("");
        }

        for (String piid : pidds) {
            Task task = taskUtils.getUserTaskListByUserNameAndPid(piid).get(0);
            ActivityInstance tree = taskUtils.runtimeService.getActivityInstance(piid);
            switch (TaskUtils.backType) {
                case "1":
                    // ?????????????????????
                    resultList = taskUtils.historyService
                            .createHistoricActivityInstanceQuery()
                            .processInstanceId(piid)
                            .activityType("userTask")
                            .finished()
                            .orderByHistoricActivityInstanceEndTime()
                            .asc()
                            .list();
                    if (resultList == null || resultList.size() <= 0) {
                        throw new CustomException("?????????????????????");
                    }
                    activityId = resultList.get(0).getActivityId();
                    break;
                case "2":
                    // ???????????????
                    List<String> taskDefKeyList = taskUtils.sysModelMapper.selectTaskDefKeyOrder(piid);
                    for (int i = 0; i < taskDefKeyList.size(); i++) {
                        if (task.getTaskDefinitionKey().equals(taskDefKeyList.get(i)) && i > 0) {
                            activityId = taskDefKeyList.get(i - 1);
                        }
                    }
                    if (StringUtils.isEmpty(activityId)) {
                        throw new CustomException("?????????????????????");
                    }
                    break;
                default:
                    throw new CustomException("?????????????????????");
            }

            taskUtils.taskService.createComment(task.getId(), piid, commonProcessRequest.getOpinion());
            task.setDescription(budDescription(backVariables, task));
            taskUtils.taskService.saveTask(task);
            taskUtils.taskService.claim(task.getId(), username);
            backVariables.put("users", ProcessConstant.GUEST);
            backVariables.put("assigneeList", Arrays.asList(ProcessConstant.GUEST));
            taskUtils.runtimeService
                    .createProcessInstanceModification(piid)
                    .cancelActivityInstance(getInstanceIdForActivity(tree, task.getTaskDefinitionKey()))
                    .startBeforeActivity(activityId)
                    .setVariablesLocal(backVariables)
                    .execute();
            taskUtils.addTaskUsers(piid, task.getTaskDefinitionKey());
        }
    }

    private static String getInstanceIdForActivity(ActivityInstance activityInstance, String activityId) {
        ActivityInstance instance = getChildInstanceForActivity(activityInstance, activityId);
        if (instance != null) {
            return instance.getId();
        }
        return null;
    }

    private static ActivityInstance getChildInstanceForActivity(ActivityInstance activityInstance, String activityId) {
        if (activityId.equals(activityInstance.getActivityId())) {
            return activityInstance;
        }

        for (ActivityInstance childInstance : activityInstance.getChildActivityInstances()) {
            ActivityInstance instance = getChildInstanceForActivity(childInstance, activityId);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param billTypeCode      ????????????
     * @param processInstanceId ???????????????id
     * @return newProcessInstanceId
     */
    private static String checkFlowVersonAndStartNewProcessInstance(String billTypeCode, String processInstanceId) {
        String processKey = BillUtils.getProcessKey(billTypeCode);
        HistoricProcessInstance hisProIns = taskUtils.historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Integer processDefinitionVersion = hisProIns.getProcessDefinitionVersion();
        List<ProcessDefinition> list = taskUtils.repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .orderByProcessDefinitionVersion().desc().list();
        if (CollectionUtil.isEmpty(list)) {
            throw new CustomException("????????????????????????????????????");
        }
        int version = list.get(0).getVersion();
        if (processDefinitionVersion != version) {
            String newProcessInstanceId = startProcessInstanceByBillTypeCode(billTypeCode);
            log.info("{}??????????????????:{} ==> {}", billTypeCode, processInstanceId, newProcessInstanceId);
            return newProcessInstanceId;
        }
        return processInstanceId;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param billTypeCode      ????????????
     * @param processInstanceId ???????????????id
     * @param unitId            ??????id
     * @return newProcessInstanceId
     */
    private static String checkFlowVersonAndStartNewProcessInstance(String billTypeCode, String processInstanceId, String unitId) {
        String processKey = BillUtils.getProcessKeyByUnit(billTypeCode, unitId);
        HistoricProcessInstance hisProIns = taskUtils.historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Integer processDefinitionVersion = hisProIns.getProcessDefinitionVersion();
        List<ProcessDefinition> list = taskUtils.repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .orderByProcessDefinitionVersion().desc().list();
        if (CollectionUtil.isEmpty(list)) {
            throw new CustomException("????????????????????????????????????");
        }
        int version = list.get(0).getVersion();
        if (processDefinitionVersion != version) {
            String newProcessInstanceId = startProcessInstanceByBillTypeCodeAndUnit(billTypeCode, unitId);
            log.info("{}??????????????????:{} ==> {},??????{}", billTypeCode, processInstanceId, newProcessInstanceId, unitId);
            return newProcessInstanceId;
        }
        return processInstanceId;
    }


}
