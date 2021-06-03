package com.cxnet.flow.domain;

import lombok.Data;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.UserTask;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: cxnet-internal-control
 * @description: 任务节点vo
 * @author: Mr.Cai
 * @create: 2020-12-10 10:54
 **/
@Data
public class TaskNodeVo extends NodeBaseEntity {

    private int x = -1;
    private int y = -1;
    private int width = -1;
    private int height = -1;
    private String sameUnit;
    private String sameDept;
    private String preemptMode;
    private String opinion;
    /**
     * 手写签批
     */
    private String writeSign;
    private List<String> targetFlowIds;
    private List<String> sourceFlowIds;

    public TaskNodeVo() {
    }

    private TaskNodeVo(String id, String name, String type, List<String> targetFlowIds, List<String> sourceFlowIds) {
        super(id, name, type);
        this.targetFlowIds = targetFlowIds;
        this.sourceFlowIds = sourceFlowIds;
    }

    public TaskNodeVo(StartEvent startEvent) {
        this(startEvent.getId(), startEvent.getName(), startEvent.getElementType().getTypeName(), startEvent.getIncoming().stream().map(SequenceFlow::getId).collect(Collectors.toList()), startEvent.getOutgoing().stream().map(SequenceFlow::getId).collect(Collectors.toList()));
    }

    public TaskNodeVo(UserTask userTask) {
        this(userTask.getId(), userTask.getName(), userTask.getElementType().getTypeName(), userTask.getIncoming().stream().map(SequenceFlow::getId).collect(Collectors.toList()), userTask.getOutgoing().stream().map(SequenceFlow::getId).collect(Collectors.toList()));
    }

    public TaskNodeVo(EndEvent endEvent) {
        this(endEvent.getId(), endEvent.getName(), endEvent.getElementType().getTypeName(), endEvent.getIncoming().stream().map(SequenceFlow::getId).collect(Collectors.toList()), endEvent.getOutgoing().stream().map(SequenceFlow::getId).collect(Collectors.toList()));
    }


}
