package com.cxnet.basic.expModuleConfig.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报销单据模块配置
 *
 * @author caixx
 * @since 2021-01-14 18:13:34
 */
@Data
@ApiModel(description = "报销单据模块配置")
public class ExpModuleConfig implements Serializable {
    private static final long serialVersionUID = 630814226578694896L;

    @ApiModelProperty("主键id")
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("年度")
    private Integer fiscal;

    @ApiModelProperty("单据编码")
    private String modelCode;

    @ApiModelProperty("模块编码")
    private String moduleCode;

    @ApiModelProperty("是否显示")
    private String isDisplay;

    @ApiModelProperty("顺序")
    private Integer orders;

    @ApiModelProperty("模块名称")
    private String title;

    @ApiModelProperty("区域left or right")
    private String area;

    @ApiModelProperty("扩展参数")
    private String properties;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("标签")
    private String label;

    @ApiModelProperty("是否允许为空")
    private String isEmpty;

    @ApiModelProperty("扩展字段1")
    private String expExtend1;

    @ApiModelProperty("扩展字段2")
    private String expExtend2;

    @ApiModelProperty("扩展字段3")
    private String expExtend3;

    @ApiModelProperty("扩展字段4")
    private String expExtend4;

    @ApiModelProperty("扩展字段5")
    private String expExtend5;

    @ApiModelProperty("任务节点id")
    private String actId;

    @ApiModelProperty("主表名")
    private String tableName;

    @ApiModelProperty("附属表名")
    private String subsidiaryTableName;

    @ApiModelProperty("样式")
    private String style;

    @ApiModelProperty("字段KEY，不为空的话根据字典配置字段属性")
    private String dictKey;

    @ApiModelProperty("是否主模块")
    private String isMain;

    @ApiModelProperty("字典配置json")
    private String dictJson;

    @ApiModelProperty("配置字段")
    @TableField(exist = false)
    private Object details;


    @ApiModelProperty("单据样式id")
    private String cardStyleId;


    @ApiModelProperty("模块名称")
    private String modulName;


}