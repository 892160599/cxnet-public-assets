package com.cxnet.rpc.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 发票类型关联表
 *
 * @author renwei
 * @since 2021-05-18 10:44:29
 */
@Data
@ApiModel(description = "发票类型关联表")
public class InvoiceType implements Serializable {
    private static final long serialVersionUID = 149314753158159108L;

    @ApiModelProperty("发票类型id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String typeId;
    @ApiModelProperty("服务配置id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String serviceId;

    @ApiModelProperty("本地发票类型编码")
    private String invoiceType;

    @ApiModelProperty("本地发票类型名称")
    private String invoiceTypeName;

    @ApiModelProperty("服务商发票类型编码")
    private String serviceType;

    @ApiModelProperty("服务商发票类型名称")
    private String serviceTypeName;

}