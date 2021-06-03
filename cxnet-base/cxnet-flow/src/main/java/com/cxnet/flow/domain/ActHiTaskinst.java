package com.cxnet.flow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author makejava
 * @since 2021-05-21 10:10:06
 */
@Data
@ApiModel(description = "工作流ACT_HI_TASKINST")
public class ActHiTaskinst implements Serializable {
    private static final long serialVersionUID = 641076702555469522L;

    @ApiModelProperty("$column.comment")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id_;

    @ApiModelProperty("$column.comment")
    private String taskDefKey_;

    @ApiModelProperty("$column.comment")
    private String procDefKey_;

    @ApiModelProperty("$column.comment")
    private String procDefId_;

    @ApiModelProperty("$column.comment")
    private String rootProcInstId_;

    @ApiModelProperty("$column.comment")
    private String procInstId_;

    @ApiModelProperty("$column.comment")
    private String executionId_;

    @ApiModelProperty("$column.comment")
    private String caseDefKey_;

    @ApiModelProperty("$column.comment")
    private String caseDefId_;

    @ApiModelProperty("$column.comment")
    private String caseInstId_;

    @ApiModelProperty("$column.comment")
    private String caseExecutionId_;

    @ApiModelProperty("$column.comment")
    private String actInstId_;

    @ApiModelProperty("$column.comment")
    private String parentTaskId_;

    @ApiModelProperty("$column.comment")
    private String name_;

    @ApiModelProperty("$column.comment")
    private String description_;

    @ApiModelProperty("$column.comment")
    private String owner_;

    @ApiModelProperty("$column.comment")
    private String assignee_;

    @ApiModelProperty("$column.comment")
    private Date startTime_;

    @ApiModelProperty("$column.comment")
    private Date endTime_;

    @ApiModelProperty("$column.comment")
    private Integer duration_;

    @ApiModelProperty("$column.comment")
    private String deleteReason_;

    @ApiModelProperty("$column.comment")
    private Integer priority_;

    @ApiModelProperty("$column.comment")
    private Date dueDate_;

    @ApiModelProperty("$column.comment")
    private Date followUpDate_;

    @ApiModelProperty("$column.comment")
    private String tenantId_;

    @ApiModelProperty("$column.comment")
    private Date removalTime_;

}