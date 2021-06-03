package com.cxnet.rpc.domain.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同支付明细
 *
 * @author ssw
 * @since 2020-11-02 14:02:29
 */
@Data
@ApiModel(description = "合同支付明细")
@TableName("CON_PAY_DETAIL")
public class ConPayDetailRpc {
    private static final long serialVersionUID = 427777299963248440L;

    @ApiModelProperty("主键ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String conPayId;

    @ApiModelProperty("合同支付明细单据号单号")
    private String conPayBill;

    @ApiModelProperty("报销单据号")
    private String expBill;

    @ApiModelProperty("合同主表ID")
    private String fromConId;

    @ApiModelProperty("计划内容")
    private String planContent;

    @ApiModelProperty("付款条件")
    private String conPayCondition;

    @ApiModelProperty("付款金额")
    @DecimalMin(value = "0", message = "金额不能小于0！")
    private BigDecimal conPayMoney;

    @ApiModelProperty("付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date conPayTime;

    @ApiModelProperty("状态")
    private String conPayStatus;

    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @ApiModelProperty("付款比例")
    private String conPayRate;

}
