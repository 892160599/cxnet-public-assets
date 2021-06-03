package com.cxnet.asset.astfield.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.framework.web.domain.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 卡片字段表
 *
 * @author guks
 * @since 2021-04-25 14:32:00
 */
@Data
@ApiModel(description = "卡片字段表")
public class AstField extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -63956195010657852L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("字段英文名")
    private String fieldCode;

    @ApiModelProperty("显示名称")
    private String fieldName;

    @ApiModelProperty("导入excel的列名称")
    private String importTitle;

    @ApiModelProperty("导出excel的列名称")
    private String exportTitle;

    @ApiModelProperty("是否启用(0 是 2否)")
    private String isEnable;

    @ApiModelProperty("是否必填(0是 2否)")
    private String isRequired;

    @ApiModelProperty("是否是excel导入导出列(1仅导出 2仅导入 3 导入导出)")
    private String isImport;

    @ApiModelProperty("顺序)")
    private Integer orders;

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

    @ApiModelProperty("日期格式")
    private String dateFormat;

    @ApiModelProperty("导出类型（0数字 1字符串）")
    private String cellType;

    @ApiModelProperty("高度")
    private BigDecimal height;

    @ApiModelProperty("宽度")
    private BigDecimal width;

    @ApiModelProperty("值为空的时候默认值")
    private String defaultValue;

    @ApiModelProperty("必填提示信息")
    private String requiredInformation;

    @ApiModelProperty("字典key")
    private String dictKey;

    @ApiModelProperty("长度")
    private String maxLen;
}