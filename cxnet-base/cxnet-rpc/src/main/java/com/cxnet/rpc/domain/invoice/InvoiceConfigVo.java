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
public class InvoiceConfigVo implements Serializable {
    private static final long serialVersionUID = 6779209643841292765L;
    @ApiModelProperty("发票配置主表")
    @Valid
    private InvoiceConfig invoiceConfig;
    @ApiModelProperty("发票类型子表")
    private List<InvoiceType> typeList;
}
