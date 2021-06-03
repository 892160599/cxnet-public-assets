package com.cxnet.framework.quartz.domain;

import com.cxnet.common.utils.poi.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 定时任务配置管理
 *
 * @author cxnet
 */
@Data
@ApiModel("定时任务配置管理")
public class ScheduleJobBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    @Excel(name = "任务ID")
    @ApiModelProperty("任务ID")
    private Long jobId;

    @Excel(name = "任务名称")
    @ApiModelProperty("任务名称")
    private String jobName;

    @Excel(name = "调用目标字符串名称")
    @ApiModelProperty("调用目标字符串名称")
    private String beanName;

    /**
     * 参数格式：
     * String ： 'str'  —— 含有单引号
     * Double :  10D	—— 用D或d结尾的数字
     * Long   :  10L 	—— 用L或l结尾的数字
     * Float  :  10F 	—— 用F或f结尾的数字
     * Integer : 10	—— 纯数字
     */
    @Excel(name = "参数")
    @ApiModelProperty("参数")
    private String params;

    @Excel(name = "CRON表达式")
    @ApiModelProperty("CRON表达式")
    private String cronExpression;

    @Excel(name = "任务状态 0：正常 1：暂停")
    @ApiModelProperty("任务状态  0：正常  1：暂停")
    private Integer status;

    @Excel(name = "备注")
    @ApiModelProperty("备注")
    private String remark;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Excel(name = "创建人")
    @ApiModelProperty("创建人")
    private String createBy;

    @Excel(name = "创建人姓名")
    @ApiModelProperty("创建人姓名")
    private String createName;

    @Excel(name = "更新人")
    @ApiModelProperty("更新人")
    private String updateBy;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    private Integer pageNum;
    private Integer pageSize;

    private String searchValue;

    private List<Long> jobIds;

}
