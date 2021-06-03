package com.cxnet.asset.astcardstyletype.domain;

import java.io.Serializable;

import com.cxnet.framework.web.domain.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 卡片样式管理资产类别表
 *
 * @author guks
 * @since 2021-04-26 15:31:38
 */
@Data
@ApiModel(description = "卡片样式管理资产类别表")
public class AstCardStyleType extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -47928937869394418L;


    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("卡片样式id")
    private String cardStyleId;

    @ApiModelProperty("资产类别编码")
    private String assetCode;

    @ApiModelProperty("资产类别名称")
    private String assetName;

    @ApiModelProperty("年度")
    private String fiscal;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

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

}