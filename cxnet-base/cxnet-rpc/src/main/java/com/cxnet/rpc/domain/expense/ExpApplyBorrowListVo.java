package com.cxnet.rpc.domain.expense;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * 报销借款单list
 *
 * @author caixx
 * @since 2020-09-17 14:54:41
 */
@Data
@ApiModel(description = "报销借款单list")
public class ExpApplyBorrowListVo {

    @ApiModelProperty("主表id")
    private String expId;
    @ApiModelProperty("借款时间")
    private String applyDate;
    @ApiModelProperty("借款单支付日期")
    private String payTime;
    @ApiModelProperty("主表单据号")
    private String expBill;
    @ApiModelProperty("借款人")
    private String applyPerson;
    @ApiModelProperty("借款事由")
    private String applyReason;
    @ApiModelProperty("借款金额")
    private BigDecimal applyMoneySum;
    @ApiModelProperty("已还金额")
    private BigDecimal payMoney;
    @ApiModelProperty("已冲金额")
    private BigDecimal rushMoney;
    @ApiModelProperty("经费来源")
    private List<ExpFunds> expFunds;

}