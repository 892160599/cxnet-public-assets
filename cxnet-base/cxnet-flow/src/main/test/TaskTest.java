/**
 * @Auther: Administrator
 * @Date: 2020-10-10 15:16
 * @Description:
 */

import com.cxnet.common.constant.AjaxResult;
import com.cxnet.flow.model.mapper.SysModelMapper;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.ModelInstance;
import org.camunda.bpm.model.xml.instance.DomDocument;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: cxnet
 * @description:
 * @author: Mr.Cai
 * @create: 2020-10-10 15:16
 **/
@RestController
@Api(tags = "工作流测试")
@RequestMapping("/task/test")
public class TaskTest extends BaseController {

    @Autowired(required = false)
    private TaskService taskService;

    @Autowired(required = false)
    private RepositoryService repositoryService;

    @Autowired(required = false)
    private ProcessEngine processEngine;

    @Autowired(required = false)
    private IdentityService identityService;

    @Autowired(required = false)
    private SysModelMapper sysModelMapper;

    @Autowired(required = false)
    private RuntimeService runtimeService;

    /**
     * 执行任务
     */
    @GetMapping
    @ApiOperation("执行任务")
    @Transactional
    public void executionTaskById(String taskId) {
        Map<String, Object> variables = new HashMap<>(0);
        String[] assigneeList = {"办理人A", "办理人B", "办理人C", "办理人D"};
        // assigneeList 就是配置的多实例集合名称，集合的元素就是一个个的办理人
        variables.put("assigneeList", Arrays.asList(assigneeList));
        taskService.complete(taskId, variables);
        System.out.println("提交申请完成");

        /*HashMap<String, Object> map = new HashMap<>();
        map.put(ProcessConstant.ACT_UNIT_CODE,"02");
        map.put(ProcessConstant.ACT_DEPT_CODE,"0201");
        TaskUtils.complete(taskId,map);*/

//        taskService.deleteTask(taskId,true);
    }

    /**
     * 获取任务节点信息
     */
    @GetMapping("/getTask")
    @ApiOperation("获取任务节点信息")
    public AjaxResult getTask(String taskId) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();


        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(taskDefinitionKey);
        return AjaxResult.success(111);
    }

    /**
     * 获取任务节点信息
     */
    @GetMapping("/getUserTask")
    @ApiOperation("获取任务节点信息")
    public AjaxResult getUserTask(String pid, String userName) {
        // 查询指定任务
        List<Task> taskAssigneeList = taskService.createTaskQuery().processInstanceId(pid).taskAssignee(userName).list();
        // 查询候选任务
        List<Task> taskCandidateUserList = taskService.createTaskQuery().processInstanceId(pid).taskCandidateUser(userName).list();
        /*f(CollectionUtil.isEmpty(taskAssigneeList) && CollectionUtil.isEmpty(taskCandidateUserList)){
            throw new CustomException("无权操作！");
        }*/
        List<Task> taskListAll = new ArrayList<>();
        // 合并去重
        taskListAll.addAll(taskAssigneeList);
        taskListAll.addAll(taskCandidateUserList);
        ArrayList<Task> listAllDistinct = taskListAll.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(Task::getId))), ArrayList::new)
        );
        List<String> taskIds = listAllDistinct.stream().map(Task::getId).collect(Collectors.toList());
        List<String> groupIdList = sysModelMapper.selectGroupIdList(String.join("','", taskIds));
        return AjaxResult.success(groupIdList);
    }


    /**
     * 执行任务
     *
     * @param pid 实例id
     */
    @GetMapping("/gogogo")
    public void complete(String pid) {
        Map<String, Object> variables = new HashMap<>(10);
        TaskUtils.complete(pid, variables);
    }

}
