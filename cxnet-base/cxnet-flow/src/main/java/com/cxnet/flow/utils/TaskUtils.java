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
 * @description: 工作流工具类
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
     * 审核动作
     */
    private static final Map<String, String> SUBMIT_FLAG_MAP = new HashMap<>();

    /**
     * 退回控制规则
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
        SUBMIT_FLAG_MAP.put("1", "送审");
        SUBMIT_FLAG_MAP.put("2", "通过");
        SUBMIT_FLAG_MAP.put("3", "退回");
    }

    /**
     * 部门负责人
     */
    private static final String DEPT_LEAD_CODE = "001_";

    /**
     * 分管领导
     */
    private static final String FG_LEAD_CODE = "002_";

    /**
     * 内控监督岗
     */
    private static final String SUPERVISE_CODE = "009_";

    /**
     * 获取当前用户下所有流程任务实例id集合
     *
     * @return
     */
    public static List<String> getRunTaskByUserName() {
        String username = SecurityUtils.getUsername();
        // 查询所有指定任务
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().taskAssignee(username).list();
        // 查询所有候选任务
        List<Task> taskCandidateUserList = taskUtils.taskService.createTaskQuery().taskCandidateUser(username).list();
        List<Task> taskListAll = new ArrayList<>();
        taskListAll.addAll(taskAssigneeList);
        taskListAll.addAll(taskCandidateUserList);
        return taskListAll.stream().map(Task::getProcessInstanceId).distinct().collect(Collectors.toList());
    }

    /**
     * 根据status获取当前用户下所有流程任务实例id集合
     *
     * @return
     */
    public static List<String> getRunTaskByUserNameAndStatus(String status) {
        if (StringUtils.isEmpty(status)) {
            return CollectionUtil.newArrayList();
        }
        switch (status) {
            // 待办
            case "1":
                return getRunTaskByUserName();
            // 已办
            case "2":
                List<String> hisTaskByRoles = getHisTaskByRoles();
                hisTaskByRoles.removeAll(getRunTaskByUserName());
                return hisTaskByRoles;
            // 全部
            case "3":
                List<String> runTaskByUserName = getRunTaskByUserName();
                runTaskByUserName.addAll(getHisTaskByRoles());
                return runTaskByUserName;
            default:
                return CollectionUtil.newArrayList();
        }
    }

    /**
     * 获取当前用户下所有流程历史任务实例id集合
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
     * 查询流程定义信息
     *
     * @param modelCode 单据类型编码
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
     * 查询流程历史节点信息
     *
     * @param piid 流程id
     * @return 流程信息
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
                // 计算历时
                String diachronic = "";
                if (v.getDurationInMillis() != null) {
                    diachronic = DateUtils.timeSubtract(v.getStartTime(), v.getEndTime());
                }
                HistoricTaskVo historicTaskVo = new HistoricTaskVo();
                BeanUtil.copyProperties(v, historicTaskVo);
                historicTaskVo.setDiachronic(diachronic);
                // 用户名转换
                String assignee = v.getAssignee();
                historicTaskVo.setAssignee(taskUtils.sysModelMapper.getNickNameByUserName(assignee));
                historicTaskVoList.add(historicTaskVo);
            }
        });

        // 查询所有指定任务
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().processInstanceId(piid).list();
        if (CollectionUtil.isNotEmpty(taskAssigneeList)) {
            Task task = taskAssigneeList.get(0);
            JSONObject description = JSONObject.parseObject(task.getDescription());
            String preemptMode = description.getString("preemptMode");
            List<String> taskIds = taskAssigneeList.stream().map(Task::getId).distinct().collect(Collectors.toList());
            String joinTaskIds = String.join("','", taskIds);
            List<SysUser> nickNames;
            // 添加待办用户
            switch (preemptMode) {
                // 抢占模式
                case "1":
                    nickNames = taskUtils.sysModelMapper.selectUserCandidateList(joinTaskIds);
                    break;
                // 会签模式
                case "2":
                    nickNames = taskUtils.sysModelMapper.selectUserList(joinTaskIds);
                    break;
                default:
                    throw new CustomException("流程配置异常！");
            }
            String userNames = nickNames.stream().map(SysUser::getNickName).distinct().collect(Collectors.joining(","));
            HistoricTaskVo historicTaskVo = new HistoricTaskVo();
            historicTaskVo.setName(task.getName());
            historicTaskVo.setDescription("{\"sameUnit\":\"1\",\"condition\":\"\",\"preemptMode\":\"1\",\"sameDept\":\"1\",\"submitFlag\":\"待办\"}");
            historicTaskVo.setAssignee(userNames);
            historicTaskVoList.add(historicTaskVo);
        }
        List<NodeBaseEntity> flowNode = getFlowNodeByPiid(piid);
        replenishNode(flowNode, historicTaskVoList, taskAssigneeList);
        // todo 无用节点隐藏
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
     * 根据审批记录补充流程节点信息
     *
     * @param flowNode           所有流程节点
     * @param historicTaskVoList 历史任务
     * @param taskAssigneeList   当前任务
     */
    private static void replenishNode(List<NodeBaseEntity> flowNode, List<HistoricTaskVo> historicTaskVoList, List<Task> taskAssigneeList) {
        if (CollectionUtil.isEmpty(flowNode) || CollectionUtil.isEmpty(historicTaskVoList)) {
            return;
        }
        Task task = null;
        if (CollectionUtil.isNotEmpty(taskAssigneeList)) {
            task = taskAssigneeList.get(0);
        }
        // 根据节点去重
        Map<String, HistoricTaskVo> historicTaskVoMap = historicTaskVoList.stream().filter(v -> v.getStartTime() != null)
                .collect(Collectors.toMap(HistoricTaskVo::getTaskDefinitionKey, Function.identity(), (c1, c2) -> c1.getStartTime().compareTo(c2.getStartTime()) > 0 ? c1 : c2));
        // 退回箭头
        List<SequenceFlowVo> sequenceFlowVoBacks = new ArrayList<>();
        for (NodeBaseEntity nodeBaseEntity : flowNode) {
            // 设置节点
            if (!"sequenceFlow".equals(nodeBaseEntity.getType())) {
                HistoricTaskVo historicTaskVo = historicTaskVoMap.get(nodeBaseEntity.getId());
                // 已办节点
                if (ObjectUtil.isNotNull(historicTaskVo)) {
                    nodeBaseEntity.setStartTime(historicTaskVo.getStartTime());
                    nodeBaseEntity.setEndTime(historicTaskVo.getEndTime());
                    nodeBaseEntity.setUsers(historicTaskVo.getAssignee());
                    nodeBaseEntity.setStatus(ProcessConstant.STATUS_2);
                    TaskNodeVo taskNodeVo = (TaskNodeVo) nodeBaseEntity;
                    JSONObject description = JSONObject.parseObject(historicTaskVo.getDescription());
                    setDescription(taskNodeVo, description);
                }
                // 待办节点
                if (ObjectUtil.isNotNull(task) && nodeBaseEntity.getId().equals(task.getTaskDefinitionKey())) {
                    nodeBaseEntity.setStatus(ProcessConstant.STATUS_1);
                }
                // 结束节点
                if (ObjectUtil.isNull(task) && "endEvent".equals(nodeBaseEntity.getType())) {
                    nodeBaseEntity.setStatus(ProcessConstant.STATUS_2);
                }
                continue;
            }
            // 设置箭头
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
                        // 退回
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
     * 查询任务节点信息
     *
     * @param piid 流程实例id
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
     * 查询任务节点信息
     *
     * @param key 流程key
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
     * 查询任务节点信息
     *
     * @param processDefinitionId 流程定义id
     * @return
     */
    private static List<NodeBaseEntity> getFlowNodeByProcessDefinitionId(String processDefinitionId) {
        if (StringUtils.isEmpty(processDefinitionId)) {
            return null;
        }
        // 保存所有节点
        List<NodeBaseEntity> taskNodeVos = new ArrayList<>();
        BpmnModelInstance bpmnModelInstance = taskUtils.repositoryService.getBpmnModelInstance(processDefinitionId);
        Collection<StartEvent> startEvents = bpmnModelInstance.getModelElementsByType(StartEvent.class);
        Collection<UserTask> userTasks = bpmnModelInstance.getModelElementsByType(UserTask.class);
        Collection<EndEvent> endEvents = bpmnModelInstance.getModelElementsByType(EndEvent.class);
        Collection<SequenceFlow> sequenceFlow = bpmnModelInstance.getModelElementsByType(SequenceFlow.class);
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) taskUtils.repositoryService.getProcessDefinition(processDefinitionId);
        // 开始节点
        startEvents.forEach(v -> taskNodeVos.add(buildTaskNodeVo(v, processDefinition, v.getElementType().getTypeName())));
        // 用户节点
        userTasks.forEach(v -> taskNodeVos.add(buildTaskNodeVo(v, processDefinition, v.getElementType().getTypeName())));
        // 结束节点
        endEvents.forEach(v -> taskNodeVos.add(buildTaskNodeVo(v, processDefinition, v.getElementType().getTypeName())));
        // 箭头
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
     * @param typeName             节点类型
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
     * 设置节点配置信息
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
     * 设置节点位置
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
     * 获取部署的xml字符串
     *
     * @param piid 流程id
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
            log.error("错误原因:{}", e.getMessage(), e);
        }
        return processDefinitionDiagramDto;
    }

    /**
     * 批量执行任务
     *
     * @param pidds     流程实例id集合
     * @param variables 流程变量
     */
    public static void complete(List<String> pidds, Map<String, Object> variables) {
        if (CollectionUtils.isEmpty(pidds)) {
            throw new CustomException("未查询到流程数据！");
        }
        pidds.forEach(v -> complete(v, variables));
    }

    /**
     * 执行任务
     *
     * @param pid       实例id
     * @param variables 流程变量
     */
    public static List<Task> complete(String pid, Map<String, Object> variables) {
        // 判断是否是待办人员=》完成当前任务=》流程未结束的话获取最新任务节点信息=》根据节点配置查出待办人员并进行加签操作
        List<Task> listAllDistinct = taskUtils.getUserTaskListByUserNameAndPid(pid);
        // 完成任务
        for (Task t : listAllDistinct) {
            String taskId = t.getId();
            t.setDescription(budDescription(variables, t));
            taskUtils.taskService.saveTask(t);
            variables.put("users", ProcessConstant.GUEST);
            variables.put("assigneeList", Arrays.asList(ProcessConstant.GUEST));
            taskUtils.taskService.complete(taskId, variables);
        }
        // 添加任务可执行人
        return taskUtils.addTaskUsers(pid, listAllDistinct.get(0).getTaskDefinitionKey());
    }

    /**
     * 添加任务可执行人
     *
     * @param pid                  实例id
     * @param oldTaskDefinitionKey 任务节点key
     */
    private List<Task> addTaskUsers(String pid, String oldTaskDefinitionKey) {
        // 查询最新任务
        List<Task> tasks = taskUtils.taskService.createTaskQuery().processInstanceId(pid).list();
        if (CollectionUtil.isNotEmpty(tasks) && !tasks.get(0).getTaskDefinitionKey().equals(oldTaskDefinitionKey)) {
            // 查询当前节点角色组
            List<String> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toList());
            List<String> groupIdList = taskUtils.sysModelMapper.selectGroupIdList(String.join("','", taskIds));
            Task task = tasks.get(0);
            String taskId = task.getId();
            String description = task.getDescription();
            if (StringUtils.isEmpty(description)) {
                throw new CustomException("流程配置异常！");
            }
            // 流程所属单位部门编码
            String actUnitId = String.valueOf(taskUtils.taskService.getVariable(taskId, ProcessConstant.UNIT_ID));
            String actDeptId = String.valueOf(taskUtils.taskService.getVariable(taskId, ProcessConstant.DEPT_ID));
            String createBy = String.valueOf(taskUtils.taskService.getVariable(taskId, ProcessConstant.CREATE_BY));
            if (log.isInfoEnabled()) {
                log.info("审批的单位id:{}, 部门id：{}", actUnitId, actDeptId);
            }
            // 流程节点配置信息
            JSONObject jsonObject = JSON.parseObject(description);
            String sameUnit = jsonObject.getString("sameUnit");
            String sameDept = jsonObject.getString("sameDept");
            String preemptMode = jsonObject.getString("preemptMode");
            String superviseId = jsonObject.getString("superviseId");
            // 待审批用户
            List<String> nextUsers;
            // 当前是首节点的话，直接指定制单人为待办人
            if (isFirstTask(pid)) {
                task.setAssignee(createBy);
                taskUtils.taskService.saveTask(task);
                taskUtils.taskService.deleteCandidateUser(taskId, ProcessConstant.GUEST);
                return tasks;
            }
            if (groupIdList.contains(DEPT_LEAD_CODE) || groupIdList.contains(FG_LEAD_CODE)) {
                // 分管领导/部门领导
                nextUsers = getLeadUsers(actDeptId, groupIdList);
            } else if (ProcessConstant.SAME_DEPT.equals(sameDept)) {
                // 同部门
                nextUsers = getNextUsers(actUnitId, actDeptId, groupIdList);
            } else if (ProcessConstant.SAME_UNIT.equals(sameUnit)) {
                // 同单位
                nextUsers = getNextUsers(actUnitId, groupIdList);
            } else {
                // 角色组
                nextUsers = getNextUsers(groupIdList);
            }

            if (CollectionUtil.isEmpty(nextUsers) && StringUtils.isEmpty(task.getAssignee())) {
                throw new CustomException("流程配置异常,未找到下个节点审核人！");
            }

            if (log.isInfoEnabled()) {
                log.info("可以处理下级节点的用户id列表：{}", ObjectUtil.isNull(nextUsers) ? "" : JSON.toJSONString(nextUsers));
            }

            // 添加待办用户
            switch (preemptMode) {
                // 抢占模式
                case "1":
                    nextUsers.forEach(v -> taskUtils.taskService.addCandidateUser(taskId, v));
                    taskUtils.taskService.deleteCandidateUser(taskId, ProcessConstant.GUEST);
                    break;
                // 会签模式
                case "2":
                    nextUsers.forEach(v ->
                            taskUtils.runtimeService.createProcessInstanceModification(pid)
                                    .startBeforeActivity(task.getTaskDefinitionKey())
                                    .setVariable("assignee", v)
                                    .execute());
                    // todo 减签
                    taskUtils.taskService.complete(taskId);
                    break;
                default:
                    throw new CustomException("流程配置异常！");
            }
            // 内控监督岗业务处理
            if (StringUtils.isNotEmpty(superviseId)) {
                Map<String, Object> variables = taskService.getVariables(taskId);
                sysSuperviseMatterServiceRpc.addSysSuperviseMatter(superviseId, variables);
            }

        }
        return tasks;
    }

    /**
     * 查询中心领导/分管领导用户
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
     * 查询实例待办任务，并指派组任务
     *
     * @param pid
     * @return
     */
    private List<Task> getUserTaskListByUserNameAndPid(String pid) {
        String username = SecurityUtils.getUsername();
        // 查询指定任务
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
        // 查询候选任务
        List<Task> taskCandidateUserList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskCandidateUser(username).list();
        if (CollectionUtil.isEmpty(taskAssigneeList) && CollectionUtil.isEmpty(taskCandidateUserList)) {
            throw new CustomException("无权操作！");
        }
        List<Task> taskListAll = new ArrayList<>();
        // 指派组任务
        for (Task v : taskCandidateUserList) {
            taskUtils.taskService.claim(v.getId(), username);
            taskListAll.add(taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskId(v.getId()).taskAssignee(username).singleResult());
        }
        // 合并去重
        taskListAll.addAll(taskAssigneeList);
        return taskListAll.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(Task::getId))), ArrayList::new)
        );
    }

    /**
     * 根据实例id查询是否是本人待办
     *
     * @param pid
     * @return true false
     */
    public static boolean isToDo(String pid) {
        String username = SecurityUtils.getUsername();
        // 查询指定任务
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
        // 查询候选任务
        List<Task> taskCandidateUserList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskCandidateUser(username).list();
        return CollectionUtil.isNotEmpty(taskAssigneeList) || CollectionUtil.isNotEmpty(taskCandidateUserList);
    }

    /**
     * 批量执行任务
     *
     * @param pidds                流程实例id集合
     * @param commonProcessRequest 流程提交参数对象
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
                throw new CustomException("审核状态异常！");
        }
        complete(pidds, variables);
    }

    /**
     * 根据processKey启动流程实例
     *
     * @param processKey 流程key
     * @return 实例id
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
            throw new CustomException("流程启动失败！");
        }
        return piid;
    }

    /**
     * 根据业务类型启动流程实例
     *
     * @param billTypeCode 单据类型代码
     * @return 实例id
     */
    public static String startProcessInstanceByBillTypeCode(String billTypeCode) {
        String processKey = BillUtils.getProcessKey(billTypeCode);
        return startProcessInstanceByProcessKey(processKey);
    }

    /**
     * 根据业务类型启动流程实例并校验当前流程版本
     *
     * @param billTypeCode      单据 单据类型代码
     * @param processInstanceId 流程实例id
     * @return 最新流程实例
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
     * 根据业务类型和单位id启动流程实例
     *
     * @param billTypeCode 单据类型代码
     * @param unitId       单位id
     * @return 实例id
     */
    public static String startProcessInstanceByBillTypeCodeAndUnit(String billTypeCode, String unitId) {
        String processKey = BillUtils.getProcessKeyByUnit(billTypeCode, unitId);
        return startProcessInstanceByProcessKey(processKey);
    }

    /**
     * 根据业务类型和单位id启动流程实例并校验当前流程版本
     *
     * @param billTypeCode      单据 单据类型代码
     * @param processInstanceId 流程实例id
     * @return 最新流程实例
     */
    public static String startProcessInstanceByBillTypeCodeAndUnitId(String billTypeCode, String processInstanceId) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (StringUtils.isEmpty(processInstanceId)) {
            return startProcessInstanceByBillTypeCodeAndUnit(billTypeCode, user.getDeptId());
        }
        // 退回到首节点使用新流程
        if (TaskUtils.isFirstTask(processInstanceId)) {
            processInstanceId = checkFlowVersonAndStartNewProcessInstance(billTypeCode, processInstanceId, user.getDeptId());
        }
        return processInstanceId;
    }

    /**
     * 获取启动流程变量
     *
     * @param commonProcessRequest
     * @return
     */
    public static Map<String, Object> getStartVariables(CommonProcessRequest commonProcessRequest) {
        return getVariables(commonProcessRequest, ProcessConstant.GO, null);
    }

    /**
     * 获取启动流程变量
     *
     * @param commonProcessRequest
     * @return
     */
    public static Map<String, Object> getStartVariables(CommonProcessRequest commonProcessRequest, Object o) {
        commonProcessRequest.setSubmitFlag("1");
        return getVariables(commonProcessRequest, ProcessConstant.GO, o);
    }

    /**
     * 获取送审流程变量map
     *
     * @param commonProcessRequest
     * @return
     */
    public static Map<String, Object> getGoVariables(CommonProcessRequest commonProcessRequest) {
        return getVariables(commonProcessRequest, ProcessConstant.GO, null);
    }

    /**
     * 获取送审流程变量map
     *
     * @param commonProcessRequest 流程提交参数对象
     * @param o                    要生成的流程变量对象
     * @return
     */
    public static Map<String, Object> getGoVariables(CommonProcessRequest commonProcessRequest, Object o) {
        commonProcessRequest.setSubmitFlag("2");
        return getVariables(commonProcessRequest, ProcessConstant.GO, o);
    }

    /**
     * 获取退回流程变量map
     *
     * @param commonProcessRequest
     * @return
     */
    public static Map<String, Object> getBackVariables(CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return getVariables(commonProcessRequest, ProcessConstant.BACK, null);
    }

    /**
     * 获取流程变量map
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
     * 根据流程id判断流程是否结束
     *
     * @param piid 流程实例id
     * @return true结束 false未结束
     */
    public static boolean isEnd(String piid) {
        return taskUtils.runtimeService.createProcessInstanceQuery().processInstanceId(piid).singleResult() == null;
    }

    /**
     * 根据流程id判断是否是首节点
     *
     * @param piid 流程实例id
     * @return true是首节点 false不是首节点
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
     * 获取首节点
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
     * 构建 DESCRIPTION （旧）
     *
     * @param variables
     * @return
     */
    private static String budDescriptionOld(Map<String, Object> variables, Task task) {
        HashMap<String, Object> descriptionMap = new HashMap<>(16);
        String submitFlag = (String) variables.get(ProcessConstant.SUBMIT_FLAG);
        // 审核信息
        descriptionMap.put("condition", "");
        descriptionMap.put(ProcessConstant.SUBMIT_FLAG, SUBMIT_FLAG_MAP.get(submitFlag));
        descriptionMap.put(ProcessConstant.OPINION, variables.get(ProcessConstant.OPINION));
        descriptionMap.put(ProcessConstant.WRITE_SIGN, variables.get(ProcessConstant.WRITE_SIGN));
        // 审批动作、任务执行状态转换
        String status = "1".equals(TaskUtils.backType) ? "9" : "7";
        descriptionMap.put("actStatus", "3".equals(submitFlag) ? status : submitFlag);
        // 节点备注信息
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
     * 构建 DESCRIPTION （新）
     *
     * @param variables 值
     * @return 结果
     */
    private static String budDescription(Map<String, Object> variables, Task task) {
        JSONObject jsonObject = new JSONObject();
        String description = task.getDescription();
        String submitFlag = (String) variables.get(ProcessConstant.SUBMIT_FLAG);
        // 节点备注信息
        if (StringUtils.isNotEmpty(description)) {
            jsonObject = JSON.parseObject(description);
            jsonObject.remove("condition");
            jsonObject.remove(ProcessConstant.SUBMIT_FLAG);
            jsonObject.remove(ProcessConstant.OPINION);
            jsonObject.remove("actStatus");
        }
        // 审核信息
        jsonObject.put("condition", "");
        jsonObject.put(ProcessConstant.SUBMIT_FLAG, SUBMIT_FLAG_MAP.get(submitFlag));
        jsonObject.put(ProcessConstant.OPINION, variables.get(ProcessConstant.OPINION));
        // 审批动作、任务执行状态转换
        String status = "1".equals(TaskUtils.backType) ? "9" : "7";
        jsonObject.put("actStatus", "3".equals(submitFlag) ? status : submitFlag);
        return JSON.toJSONString(jsonObject);
    }

    /**
     * 创建空流程
     *
     * @param key  流程标识
     * @param name 流程名称
     * @return Deployment
     */
    public static Deployment createDeploymentByKeyAndName(String key, String name) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(name)) {
            throw new CustomException("流程标识或名称不能为空！");
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
            throw new CustomException("流程部署失败,请联系管理员！");
        }
        return deploy;
    }

    /**
     * 根据deploymentId删除
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
            throw new CustomException("已存在流程实例，不可删除！");
        }
    }

    /**
     * 根据deploymentId查询
     *
     * @param deploymentId id
     */
    public static ProcessDefinition selectDeployment(String deploymentId) {
        if (StringUtils.isEmpty(deploymentId)) {
            return null;
        }
        ProcessDefinition processDefinition = taskUtils.repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        if (processDefinition == null) {
            throw new CustomException("流程不存在！");
        }
        return processDefinition;
    }

    /**
     * 根据deploymentId暂停流程
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
     * 获取角色集合
     *
     * @return 结果
     */
    private static List<String> getRoles() {
        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        return roles.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
    }

    /**
     * 查询下级用户
     *
     * @param unitId 单位id
     * @param roles  角色
     * @return 结果
     */
    private static List<String> getNextUsers(String unitId, List<String> roles) {
        List<String> users = taskUtils.sysModelMapper.selectUserNamesByUnitId(unitId);
        List<String> usersByroles = getNextUsers(roles);
        users.retainAll(usersByroles);
        return users;
    }

    /**
     * 查询下级用户
     *
     * @param unitId 单位id
     * @param deptId 部门id
     * @param roles  角色
     * @return 结果
     */
    private static List<String> getNextUsers(String unitId, String deptId, List<String> roles) {
        List<String> users = taskUtils.sysModelMapper.getUsersByUnitIdAndDeptId(deptId);
        List<String> usersByroles = getNextUsers(roles);
        users.retainAll(usersByroles);
        return users;
    }

    /**
     * 查询下级用户
     *
     * @param roles 角色
     * @return 结果
     */
    private static List<String> getNextUsers(List<String> roles) {
        return taskUtils.sysModelMapper.getUsersByRoles(String.join("','", roles));
    }

    /**
     * 查询最新流程节点name
     *
     * @param piid 流程id
     * @return 结果
     */
    public static String getTaskNameByPiid(String piid) {
        Task task = taskUtils.taskService.createTaskQuery().processInstanceId(piid).list().get(0);
        return task.getName();
    }

    /**
     * 查询最新节点信息 description
     *
     * @param pid 流程id
     * @return 结果
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
        // 查询指定任务
        List<Task> taskAssigneeList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
        // 查询候选任务
        List<Task> taskCandidateUserList = taskUtils.taskService.createTaskQuery().processInstanceId(pid).taskCandidateUser(username).list();
        // TODO 收回状态另考虑
        if (CollectionUtil.isNotEmpty(taskAssigneeList) || CollectionUtil.isNotEmpty(taskCandidateUserList)) {
            // 流程节点配置信息
            return JSON.parseObject(description);
        }
        return new JSONObject();
    }

    /**
     * 校验当前用户是否可以收回
     *
     * @param piid 流程id
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
     * 批量收回
     * todo 连续收回问题
     *
     * @param pidds                流程id
     * @param commonProcessRequest 参数
     */
    public static void backTaskBack(List<String> pidds, CommonProcessRequest commonProcessRequest) {
        String activityId = "";
        Map<String, Object> backVariables = getBackVariables(commonProcessRequest);

        if (ObjectUtil.isNull(commonProcessRequest.getOpinion())) {
            commonProcessRequest.setOpinion("");
        }
        for (String piid : pidds) {
            if (!checkBackTaskBack(piid)) {
                throw new CustomException("无权操作！");
            }
            List<Task> tasks = taskUtils.taskService.createTaskQuery().processInstanceId(piid).list();
            if (CollectionUtil.isEmpty(tasks)) {
                throw new CustomException("流程异常，请刷新后重试！");
            }
            Task task = tasks.get(0);
            ActivityInstance tree = taskUtils.runtimeService.getActivityInstance(piid);
            // 退回到上级
            List<String> taskDefKeyList = taskUtils.sysModelMapper.selectTaskDefKeyOrder(piid);
            for (int i = 0; i < taskDefKeyList.size(); i++) {
                if (task.getTaskDefinitionKey().equals(taskDefKeyList.get(i)) && i > 0) {
                    activityId = taskDefKeyList.get(i - 1);
                }
            }
            if (StringUtils.isEmpty(activityId)) {
                throw new CustomException("未找到收回节点");
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
            // 删除记录
            List<HistoricTaskInstance> hisTasks = taskUtils.historyService
                    .createHistoricTaskInstanceQuery()
                    .processInstanceId(piid)
                    .list();
            hisTasks = hisTasks.stream().sorted(Comparator.comparing(HistoricTaskInstance::getStartTime)).collect(Collectors.toList());
            if (hisTasks.size() < 3) {
                throw new CustomException("流程查询异常，请刷新后重试！");
            }
            taskUtils.historyService.deleteHistoricTaskInstance(hisTasks.get(hisTasks.size() - 1).getId());
            taskUtils.historyService.deleteHistoricTaskInstance(hisTasks.get(hisTasks.size() - 2).getId());
        }
    }

    /**
     * 批量退回
     *
     * @param pidds                流程id
     * @param commonProcessRequest 参数
     */
    public static void backProcess(List<String> pidds, CommonProcessRequest commonProcessRequest) {
        // 查询退回控制规则
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
                    // 退回到发起节点
                    resultList = taskUtils.historyService
                            .createHistoricActivityInstanceQuery()
                            .processInstanceId(piid)
                            .activityType("userTask")
                            .finished()
                            .orderByHistoricActivityInstanceEndTime()
                            .asc()
                            .list();
                    if (resultList == null || resultList.size() <= 0) {
                        throw new CustomException("未找到发起节点");
                    }
                    activityId = resultList.get(0).getActivityId();
                    break;
                case "2":
                    // 退回到上级
                    List<String> taskDefKeyList = taskUtils.sysModelMapper.selectTaskDefKeyOrder(piid);
                    for (int i = 0; i < taskDefKeyList.size(); i++) {
                        if (task.getTaskDefinitionKey().equals(taskDefKeyList.get(i)) && i > 0) {
                            activityId = taskDefKeyList.get(i - 1);
                        }
                    }
                    if (StringUtils.isEmpty(activityId)) {
                        throw new CustomException("未找到上一节点");
                    }
                    break;
                default:
                    throw new CustomException("退回类型异常！");
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
     * 判断流程版本是否存在新版本，存在即替换到新版本
     *
     * @param billTypeCode      单据编码
     * @param processInstanceId 原流程实例id
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
            throw new CustomException("流程异常，请联系管理员！");
        }
        int version = list.get(0).getVersion();
        if (processDefinitionVersion != version) {
            String newProcessInstanceId = startProcessInstanceByBillTypeCode(billTypeCode);
            log.info("{}流程实例更新:{} ==> {}", billTypeCode, processInstanceId, newProcessInstanceId);
            return newProcessInstanceId;
        }
        return processInstanceId;
    }

    /**
     * 判断流程版本是否存在新版本，存在即替换到新版本（单位条件）
     *
     * @param billTypeCode      单据编码
     * @param processInstanceId 原流程实例id
     * @param unitId            单位id
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
            throw new CustomException("流程异常，请联系管理员！");
        }
        int version = list.get(0).getVersion();
        if (processDefinitionVersion != version) {
            String newProcessInstanceId = startProcessInstanceByBillTypeCodeAndUnit(billTypeCode, unitId);
            log.info("{}流程实例更新:{} ==> {},单位{}", billTypeCode, processInstanceId, newProcessInstanceId, unitId);
            return newProcessInstanceId;
        }
        return processInstanceId;
    }


}
