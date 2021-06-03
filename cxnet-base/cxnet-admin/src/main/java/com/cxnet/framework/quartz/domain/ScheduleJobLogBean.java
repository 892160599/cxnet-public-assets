package com.cxnet.framework.quartz.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 定时任务日志
 */
@Data
public class ScheduleJobLogBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务日志ID")
    private Long logId;
    @ApiModelProperty("任务ID")
    private Long jobId;
    @ApiModelProperty("SPRING BEAN名称")
    private String beanName;
    @ApiModelProperty("参数")
    private String params;
    @ApiModelProperty("任务状态 0：成功 1：失败")
    private Integer status;
    @ApiModelProperty("失败信息")
    private String error;
    @ApiModelProperty("耗时(单位：毫秒)")
    private Integer times;
    @ApiModelProperty("创建时间")
    private Date createTime;

    private Date startTime;
    private Date endTime;

    private Integer pageNum;
    private Integer pageSize;

    private String searchValue;

    private List<Long> logIds;

}
