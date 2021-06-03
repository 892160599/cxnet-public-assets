package com.cxnet.rpc.domain.expense;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 报销经费来源子表
 *
 * @author makejava
 * @since 2020-09-14 10:42:39
 */
@Data
@ApiModel(description = "报销经费来源子表")
public class ExpFunds extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = 239694589009090166L;

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("经费来源id")
    private String fundsId;
    @ApiModelProperty("单位项目名称")
    private String projectName;
    @ApiModelProperty("单位项目编码")
    private String projectCode;
    @ApiModelProperty("预算金额")
    private BigDecimal planMoney;
    @ApiModelProperty("可用余额")
    private BigDecimal planBalance;
    @ApiModelProperty("本次支出金额")
    private BigDecimal fundsMoney;
    @ApiModelProperty("指标或用款计划（1：指标 2：用款计划）")
    private String balanceOrPlan;
    @ApiModelProperty("支付方式")
    private String paytypeCode;
    @ApiModelProperty("扩展字段3")
    private String expExtend3;
    @ApiModelProperty("扩展字段4")
    private String expExtend4;
    @ApiModelProperty("扩展字段5")
    private String expExtend5;
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;
    @ApiModelProperty("报销信息id")
    private String expId;
    @ApiModelProperty("报销单据号(编码器生成)")
    private String expBill;
    @ApiModelProperty("单位id")
    private String unitId;
    @ApiModelProperty("单位编码")
    private String unitCode;
    @ApiModelProperty("单位名称")
    private String unitName;
    @ApiModelProperty("部门id")
    private String deptId;
    @ApiModelProperty("部门编码")
    private String deptCode;
    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("指标/用款计划id")
    private String balId;

    @ApiModelProperty("指标/用款计划Code")
    private String balCode;
    @ApiModelProperty("摘要")
    private String explain;
    @ApiModelProperty("部门经济分类")
    private String expecoName;

    @ApiModelProperty("是否控制金额")
    @TableField(exist = false)
    private String controlMoney;

}