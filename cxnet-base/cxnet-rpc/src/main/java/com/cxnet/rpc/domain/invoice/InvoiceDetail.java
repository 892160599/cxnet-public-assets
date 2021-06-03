package com.cxnet.rpc.domain.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 发票信息明细
 *
 * @author renwei
 * @since 2021-05-20 14:52:06
 */
@Data
@ApiModel(description = "发票信息明细")
public class InvoiceDetail implements Serializable {
    private static final long serialVersionUID = -80071485344947149L;

    @ApiModelProperty("发票主表id")
    private String invoiceId;
    @ApiModelProperty("发票明细id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String invoiceDetailId;

    @ApiModelProperty("货物名称")
    private String hwmc;

    @ApiModelProperty("规格型号")
    private String ggxh;

    @ApiModelProperty("单位")
    private String dw;

    @ApiModelProperty("数量")
    private String sl;

    @ApiModelProperty("单价")
    private String dj;

    @ApiModelProperty("金额")
    private String je;

    @ApiModelProperty("税率")
    private String shlv;

    @ApiModelProperty("税额")
    private String se;

}