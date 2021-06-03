package com.cxnet.basic.expBasicConfig.domain;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cxnet.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报销单据配置
 *
 * @author caixx
 * @since 2020-09-11 15:13:55
 */
@Data
@ApiModel(description = "报销单据配置")
public class ExpBasicConfig implements Serializable {
    private static final long serialVersionUID = -88844330610187379L;

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("单据编码")
    private String modelCode;
    @ApiModelProperty("数据库字段")
    private String value;
    @ApiModelProperty("驼峰字段")
    @TableField(exist = false)
    private String humpValue;
    @ApiModelProperty("显示名称")
    private String label;
    @ApiModelProperty("字段注释")
    private String fieldNote;
    @ApiModelProperty("显示类型")
    private String editor;
    @ApiModelProperty("字段KEY")
    private String dictKey;
    @ApiModelProperty("提示")
    private String ruleText;
    @ApiModelProperty("请求参数")
    private String param;
    @ApiModelProperty("顺序")
    private String orders;
    @ApiModelProperty("说明")
    private String explain;
    @ApiModelProperty("是否字典")
    private String isDict;
    @ApiModelProperty("是否显示")
    private String isDisplay;
    @ApiModelProperty("是否允许为空")
    private String isEmpty;
    @ApiModelProperty("是否只读")
    private String isReadOnly;
    @ApiModelProperty("单位id")
    private String unitId;
    @ApiModelProperty("模块id")
    private String moduleId;
    @ApiModelProperty("扩展参数")
    private String properties;
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
    @ApiModelProperty("样式")
    private String style;
    @ApiModelProperty("模块字典值")
    private String moduleDictKey;
    @ApiModelProperty("是否在列表页显示")
    private String isTableDisplay;
    @ApiModelProperty("字段长度")
    private Integer len;
    @ApiModelProperty("字段最大长度")
    private Integer maxLen;
    @ApiModelProperty("是否可配")
    private String isConfig;
    @ApiModelProperty("宽度")
    private String width;

    @ApiModelProperty("单据样式id")
    private String cardStyleId;

    @ApiModelProperty("提示内容")
    private String placeholder;


    public String getHumpValue() {
        return StringUtils.isNotEmpty(this.value) ? StrUtil.toCamelCase(this.value.toLowerCase()) : "";
    }
}