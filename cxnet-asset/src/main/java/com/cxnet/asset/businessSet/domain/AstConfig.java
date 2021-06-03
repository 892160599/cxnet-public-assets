package com.cxnet.asset.businessSet.domain;

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
 * 资产参数设置
 *
 * @author caixx
 * @since 2021-04-06 09:41:42
 */
@Data
@ApiModel(description = "资产参数设置")
public class AstConfig extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 584020103021315205L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单位id ")
    private String unitId;

    @ApiModelProperty("折旧启用期间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deprStartDate;

    @ApiModelProperty("最新折旧期间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deprNewestDate;

    @ApiModelProperty("折旧方法变更当期生效 （默认 0是）")
    private String deprMethodChange;

    @ApiModelProperty("预计使用月限变更当期生效 （默认 0是）")
    private String useLifeChange;

    @ApiModelProperty("使用状态变更当期生效 （默认 0是）")
    private String usageStatusChange;

    @ApiModelProperty("尾差处理，使用期限的最后一期折旧提足（默认 0是）")
    private String tailDifference;

    @ApiModelProperty("资产类别变更当期折旧归属 （默认 1调整前的资产类别 2调整后的资产类别）")
    private String assetTypeChange;

    @ApiModelProperty("使用部门变更当期折旧归属 （默认 1调整前的资产类别 2调整后的资产类别）")
    private String departmentChange;

    @ApiModelProperty("管理部门变更当期折旧归属 （默认 1调整前的资产类别 2调整后的资产类别）")
    private String deptChange;

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