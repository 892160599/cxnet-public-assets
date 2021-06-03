package com.cxnet.companyBaseDate.place.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cxnet.framework.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 存放地点
 *
 * @author zhangyl
 * @since 2021-03-30 09:55:31
 */
@Data
@ApiModel(description = "存放地点")
public class AstPlace extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 313592530939814537L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("年度")
    private String fiscal;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("存放地点编码")
    private String placeCode;

    @ApiModelProperty("存放地点名称")
    private String placeName;

    @ApiModelProperty("父级编码")
    private String parentCode;

    @ApiModelProperty("面积")
    private String area;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("是否公物仓（0 是 2 否）")
    private String isAssets;

    @ApiModelProperty("备注")
    private String remart;

    @ApiModelProperty("0正常     2停用")
    private String status;

    @ApiModelProperty("0正常     2停用")
    private String delFlag;

    @ApiModelProperty("地图范围")
    private String mapRange;

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

    @TableField(exist = false)
    private List<MapRange> list;

}

