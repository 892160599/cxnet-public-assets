package com.cxnet.asset.businessSet.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.cxnet.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 资产地图配置
 *
 * @author caixx
 * @since 2021-04-25 14:06:24
 */
@Data
@ApiModel(description = "资产地图配置")
public class AstMapConfig extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 155451125465294051L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单位id ")
    private String unitId;

    @ApiModelProperty("默认层级")
    private Integer defaultLevel;

    @ApiModelProperty("最大层级")
    private Integer maxLevel;

    @ApiModelProperty("默认经度")
    private BigDecimal defaultLong;

    @ApiModelProperty("默认维度")
    private BigDecimal defaultLat;

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