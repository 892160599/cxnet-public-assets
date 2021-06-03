package com.cxnet.rpc.domain.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author rw
 * @date 2021/1/11
 */
@Data
@ApiModel(description = "发票服务配置Vo")
public class InvoiceIndexVo implements Serializable {
    private static final long serialVersionUID = 6779209643841292765L;
    @ApiModelProperty("发票主表")
    @Valid
    private InvoiceIndex invoiceIndex;
    @ApiModelProperty("发票附件子表")
    private InvoiceAnnex invoiceAnnex;

    @ApiModelProperty("发票服务配置")
    private InvoiceConfig invoiceConfig;

    @ApiModelProperty("发票业务参数")
    private InvoiceParam invoiceParam;

    @ApiModelProperty("发票明细子表")
    private List<InvoiceDetail> invoiceDetails;
}
