package com.cxnet.project.system.rule.domain;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;

/**
 * 编号器配置表实体 sys_rule_col
 *
 * @author caixx
 * @date 2020-07-31
 */
@Data
@ApiModel("编号器配置表")
public class RuleCol extends BaseEntity {

    /**
     * 编号器ID
     */
    @Excel(name = "编号器ID")
    @ApiModelProperty("编号器ID")
    private String ruleId;
    /**
     * 参与顺序
     */
    @Excel(name = "参与顺序")
    @ApiModelProperty("参与顺序")
    private Long joinOrder;
    /**
     * 参与字段名
     */
    @Excel(name = "参与字段名")
    @ApiModelProperty("参与字段名")
    private String joinCol;
    /**
     * 本段分割符
     */
    @Excel(name = "本段分割符")
    @ApiModelProperty("本段分割符")
    private String cutChar;
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 本段长度
     */
    @Excel(name = "本段长度")
    @ApiModelProperty("本段长度")
    private Long colLen;
    /**
     * 填充位置    1：前补空     2：后补空
     */
    @Excel(name = "填充位置    1：前补空     2：后补空")
    @ApiModelProperty("填充位置    1：前补空     2：后补空")
    private Long fillPosition;
    /**
     * 补空位字符
     */
    @Excel(name = "补空位字符")
    @ApiModelProperty("补空位字符")
    private String fillChar;
    /**
     * 格式
     */
    @Excel(name = "格式")
    @ApiModelProperty("格式")
    private String colFormat;

}