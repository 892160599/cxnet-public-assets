package com.cxnet.asset.check.domain;

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
 * 资产验收明细表
 *
 * @author caixx
 * @since 2021-03-25 14:21:28
 */
@Data
@ApiModel(description = "资产验收明细表")
public class AstCheckList implements Serializable {
    private static final long serialVersionUID = -23881928150478313L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("验收主表id")
    private String checkId;

    @ApiModelProperty("资产卡片id")
    private String astId;

    @ApiModelProperty("资产卡片编号")
    private String astCode;

    @ApiModelProperty("资产名称")
    private String astName;

    @ApiModelProperty("资产类型代码")
    private String typeCode;

    @ApiModelProperty("资产类型名称")
    private String typeName;

    @ApiModelProperty("资产大类编码，存放一级1,2,3,4,5,6")
    private String classCode;

    @ApiModelProperty("资产大类名称，存放通用设备、专用设备")
    private String className;

    @ApiModelProperty("资产类别编码")
    private String categoryCode;

    @ApiModelProperty("资产类别名称")
    private String categoryName;

    @ApiModelProperty("折旧方法")
    private String depMethod;

    @ApiModelProperty("价值类型")
    private String costType;

    @ApiModelProperty("价值")
    private BigDecimal cost;

    @ApiModelProperty("数量")
    private Integer qty;

    @ApiModelProperty("单价")
    private BigDecimal pricing;

    @ApiModelProperty("购置日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date acquisitionDate;

    @ApiModelProperty("开始使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startUsedate;

    @ApiModelProperty("预计使用年限")
    private Integer assetUselife;

    @ApiModelProperty("入账日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date vouDate;

    @ApiModelProperty("顺序")
    private Integer sort;

    @ApiModelProperty("经费来源代码")
    private String fundsourceCode;

    @ApiModelProperty("经费来源名称")
    private String fundsourceName;

    @ApiModelProperty("使用部门id")
    private String departmentId;

    @ApiModelProperty("使用部门编码")
    private String departmentCode;

    @ApiModelProperty("使用部门名称")
    private String departmentName;

    @ApiModelProperty("使用人id")
    private String empId;

    @ApiModelProperty("使用人代码")
    private String empCode;

    @ApiModelProperty("使用人名称")
    private String empName;

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