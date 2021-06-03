package com.cxnet.rpc.domain;

import com.alibaba.fastjson.JSON;
import com.cxnet.common.utils.poi.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * 采购单采购资金信息实体 pur_purchase_index
 *
 * @author caixx
 * @date 2020-07-23
 */
@Data
@ApiModel("采购单采购资金信息")
public class PurPurchaseIndex extends RpcBaseEntity {

    /**
     * 采购单id
     */
    @ApiModelProperty("采购单id")
    private String purId;
    /**
     * 项目编码
     */
    @Excel(name = "项目编码")
    @ApiModelProperty("项目编码")
    private String projectCode;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    @ApiModelProperty("项目名称")
    private String projectName;
    /**
     * 指标id
     */
    @ApiModelProperty("指标id")
    private String balId;
    /**
     * 指标编码
     */
    @Excel(name = "指标编码")
    @ApiModelProperty("指标编码")
    private String balCode;
    /**
     * 预算总金额
     */
    @Excel(name = "预算总金额")
    @ApiModelProperty("预算总金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    //private BigDecimal totalAmt;

    private BigDecimal planMoney;
    /**
     * 预算结余金额
     */
    @Excel(name = "预算结余金额")
    @ApiModelProperty("预算结余金额")
    //private BigDecimal balAmt;

    private BigDecimal planBalance;
    /**
     * 实际使用金额
     */
    @Excel(name = "实际使用金额")
    @ApiModelProperty("实际使用金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    //private BigDecimal proThisMoney;

    private BigDecimal fundsMoney;
    /**
     * 本次使用金额
     */
    @Excel(name = "本次使用金额")
    @ApiModelProperty("本次使用金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal proOriginalMoney;
    /**
     * 指标或用款计划（1：指标 2：用款计划）
     */
    @ApiModelProperty("指标或用款计划（1：指标 2：用款计划）")
    private String balanceOrPlan;
    /**
     * 部门编码
     */
    @Excel(name = "部门编码")
    @ApiModelProperty("部门编码")
    private String deptCode;
    /**
     * 部门部门名称
     */
    @Excel(name = "部门部门名称")
    @ApiModelProperty("部门部门名称")
    private String deptName;
    /**
     * 部门项目代码
     */
    @Excel(name = "部门项目代码")
    @ApiModelProperty("部门项目代码")
    private String depproCode;
    /**
     * 部门项目名称
     */
    @Excel(name = "部门项目名称")
    @ApiModelProperty("部门项目名称")
    private String depproName;
    @ApiModelProperty("是否控制金额0：控制 1：不控制")
    private String controlMoney;

    @ApiModelProperty("备用字段")
    private String expExtend1;
    private String expExtend2;
    private String expExtend3;
    private String expExtend4;
    private String expExtend5;

    /**
     * 支付方式代码
     */
    @Excel(name = "支付方式代码")
    @ApiModelProperty("支付方式代码")
    private String paytypeCode;


    /**
     * 预算总金额
     */
    @Excel(name = "预算总金额")
    @ApiModelProperty("预算总金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal totalAmt;
    /**
     * 预算结余金额
     */
    @Excel(name = "预算结余金额")
    @ApiModelProperty("预算结余金额")
    private BigDecimal balAmt;
    /**
     * 实际使用金额
     */
    @Excel(name = "实际使用金额")
    @ApiModelProperty("实际使用金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal proThisMoney;

    @ApiModelProperty("部门经济分类")
    private String expecoName;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

}
