package com.cxnet.rpc.domain;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.cxnet.common.utils.poi.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 合同履约计划实体 con_implement_plan
 *
 * @author rw
 * @date 2020-07-06
 */
@Data
@ApiModel("合同履约计划")
public class ImplementPlan extends RpcBaseEntity {

    /**
     * 计划id
     */
    @ApiModelProperty("计划id")
    private String planId;
    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private String conId;
    /**
     * 状态
     */
    @Excel(name = "状态")
    @ApiModelProperty("状态")
    private String status;
    /**
     * 计划内容
     */
    @Excel(name = "计划内容")
    @ApiModelProperty("计划内容")
    private String planContent;
    /**
     * 付款比率
     */
    @Excel(name = "付款比率")
    @ApiModelProperty("付款比率")
    private String payRate;
    /**
     * 付款金额
     */
    @Excel(name = "付款金额")
    @ApiModelProperty("付款金额")
    private String payMoney;
    /**
     * 付款时间
     */
    @Excel(name = "付款时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("付款时间")
    private Date payTime;
    /**
     * 付款条件
     */
    @Excel(name = "付款条件")
    @ApiModelProperty("付款条件")
    private String payTerm;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    private String executeId;


    public String toJSONString() {
        return JSON.toJSONString(this);
    }

}
