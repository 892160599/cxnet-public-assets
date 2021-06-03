package com.cxnet.asset.astcardacsequip.domain;

import com.cxnet.framework.web.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 附属设备
 *
 * @author makejava
 * @since 2021-04-08 10:35:07
 */
@Data
@ApiModel(description = "附属设备")
public class AstCardAcsequip implements Serializable {
    private static final long serialVersionUID = -92946163329998068L;


    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("卡片ID")
    private String astId;

    @ApiModelProperty("记录排序号")
    private Integer ordSeq;

    @ApiModelProperty("设备编码")
    private String equipCode;

    @ApiModelProperty("设备名称")
    private String equipName;

    @ApiModelProperty("数量")
    private Integer qty;

    @ApiModelProperty("价值")
    private Integer cost;

    @ApiModelProperty("计量单位")
    private String unit;

    @ApiModelProperty("规格型号")
    private String spec;

    @ApiModelProperty("出厂日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date manufacturedDate;

    @ApiModelProperty("出厂编号")
    private String manufacturedCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("扩展字段1")
    private String extend1;

    @ApiModelProperty("扩展字段2")
    private String extend2;

    @ApiModelProperty("扩展字段3")
    private String extend3;

    @ApiModelProperty("扩展字段4")
    private String extend4;

    @ApiModelProperty("扩展字段5")
    private String extend5;

    @ApiModelProperty("扩展字段6")
    private String extend6;

    @ApiModelProperty("扩展字段7")
    private String extend7;

    @ApiModelProperty("扩展字段8")
    private String extend8;

    @ApiModelProperty("扩展字段9")
    private String extend9;

    @ApiModelProperty("扩展字段10")
    private String extend10;

}