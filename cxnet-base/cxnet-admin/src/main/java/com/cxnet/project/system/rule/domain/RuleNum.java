package com.cxnet.project.system.rule.domain;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;

/**
 * 当前编号实体 sys_rule_num
 *
 * @author caixx
 * @date 2020-07-31
 */
@Data
@ApiModel("当前编号")
public class RuleNum extends BaseEntity {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;
    /**
     * 编号器编码
     */
    @Excel(name = "编号器编码")
    @ApiModelProperty("编号器编码")
    private String ruleCode;
    /**
     * 编号标识标记
     */
    @Excel(name = "编号标识标记")
    @ApiModelProperty("编号标识标记")
    private String mark;
    /**
     * 当前编号
     */
    @Excel(name = "当前编号")
    @ApiModelProperty("当前编号")
    private Long currentNumber;

}