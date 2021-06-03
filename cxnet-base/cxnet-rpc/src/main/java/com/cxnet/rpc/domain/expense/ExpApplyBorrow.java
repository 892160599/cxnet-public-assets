package com.cxnet.rpc.domain.expense;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 报销借款单
 *
 * @author caixx
 * @since 2020-09-23 13:52:13
 */
@Data
@ApiModel(description = "报销借款单")
public class ExpApplyBorrow implements Serializable {
    private static final long serialVersionUID = 472927787504066241L;

    @ApiModelProperty("申请主表id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String expId;

    @ApiModelProperty("申请主表单据号(编码器生成)")
    private String expBill;

    @ApiModelProperty("借款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date borTime;

    @ApiModelProperty("还款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    @ApiModelProperty("借款人")
    private String borBy;

    @ApiModelProperty("借款人名称")
    private String borName;

    @ApiModelProperty("扩展字段1")
    private String expExtend1;

    @ApiModelProperty("扩展字段2")
    private String expExtend2;

    @ApiModelProperty("扩展字段3")
    private String expExtend3;

    @ApiModelProperty("扩展字段4")
    private String expExtend4;

    @ApiModelProperty("扩展字段5")
    private String expExtend5;

    @ApiModelProperty("已还金额")
    private BigDecimal payMoney;

    @ApiModelProperty("已冲金额")
    private BigDecimal rushMoney;

    @ApiModelProperty("借款金额")
    private BigDecimal borMoney;


    @ApiModelProperty("前置申请ID")
    private String fromId;

    @ApiModelProperty("前置申请编号")
    private String fromBill;


}