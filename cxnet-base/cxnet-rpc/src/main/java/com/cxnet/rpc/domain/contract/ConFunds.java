package com.cxnet.rpc.domain.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.rpc.domain.RpcBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 合同经费来源
 *
 * @author ssw
 * @since 2020-11-14 13:39:19
 */
@Data
@ApiModel(description = "合同经费来源")
public class ConFunds extends RpcBaseEntity implements Serializable {
    private static final long serialVersionUID = 443950904926701954L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String fundsId;

    @ApiModelProperty("单位项目名称")
    private String projectName;

    @ApiModelProperty("单位项目编码")
    private String projectCode;

    @ApiModelProperty("预算金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal planMoney;

    @ApiModelProperty("可用余额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal planBalance;

    @ApiModelProperty("本次支出金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal fundsMoney;

    @ApiModelProperty("合同id")
    private String conId;

    @ApiModelProperty("部门编码")
    private String deptCode;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("支付方式")
    private String paytypeCode;

    @ApiModelProperty("扩展字段5")
    private String expExtend5;

    @ApiModelProperty("扩展字段4")
    private String expExtend4;

    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @ApiModelProperty("指标/用款计划id")
    private String balId;

    @ApiModelProperty("指标/用款计划code")
    private String balCode;

    @ApiModelProperty("指标或用款计划（1：指标 2：用款计划）")
    private String balanceOrPlan;

}