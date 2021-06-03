package com.cxnet.rpc.domain.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 发票附件关联表
 *
 * @author renwei
 * @since 2021-05-19 11:56:24
 */
@Data
@ApiModel(description = "发票附件关联表")
public class InvoiceAnnex implements Serializable {
    private static final long serialVersionUID = -36068735789730221L;

    @ApiModelProperty("附件id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String annexId;

    @ApiModelProperty("报销单据id")
    private String invoiceId;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("附件类型 ")
    private String annexType;

    @ApiModelProperty("序号,标记")
    private String annexOrder;

    @ApiModelProperty("字典值集")
    private String dictValue;

    @ApiModelProperty("备注")
    private String bz;

    @ApiModelProperty("真实名字")
    @TableField(exist = false)
    private String realName;

    @ApiModelProperty("文件路径")
    @TableField(exist = false)
    private String fileName;

    @ApiModelProperty("创建时间")
    @TableField(exist = false)
    private Date createTime;

    @ApiModelProperty("上传人")
    @TableField(exist = false)
    private String createName;

}