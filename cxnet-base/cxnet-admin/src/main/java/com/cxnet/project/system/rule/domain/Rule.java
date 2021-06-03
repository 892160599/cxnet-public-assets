package com.cxnet.project.system.rule.domain;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;

import java.util.List;

/**
 * 编号器实体 sys_rule
 *
 * @author caixx
 * @date 2020-07-31
 */
@Data
@ApiModel("编号器")
public class Rule extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String ruleId;
    /**
     * 编号器编码
     */
    @Excel(name = "编号器编码")
    @ApiModelProperty("编号器编码")
    private String ruleCode;
    /**
     * 编号器名称
     */
    @Excel(name = "编号器名称")
    @ApiModelProperty("编号器名称")
    private String ruleName;
    /**
     * 编号前缀
     */
    @Excel(name = "编号前缀")
    @ApiModelProperty("编号前缀")
    private String rulePrefix;
    /**
     * 编码字段长度
     */
    @Excel(name = "编码字段长度")
    @ApiModelProperty("编码字段长度")
    private Long ruleLen;
    /**
     * 是否有字段参与（ 0是  2否）
     */
    @Excel(name = "是否有字段参与", readConverterExp = "0=是,2=否")
    @ApiModelProperty("是否有字段参与（ 0是  2否）")
    private Long isJoin;
    /**
     * 编号器子表
     */
    @Excel(name = "编号器子表")
    @ApiModelProperty("编号器子表")
    private List<RuleCol> ruleCols;

}
