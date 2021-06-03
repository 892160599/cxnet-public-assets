package com.cxnet.flow.model.domain;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;

/**
 * 系统模块管理实体 sys_model
 *
 * @author caixx
 * @date 2020-08-12
 */
@Data
@ApiModel("系统模块管理")
public class SysModel extends BaseEntity {

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private String modelId;
    /**
     * 编码
     */
    @Excel(name = "编码")
    @ApiModelProperty("编码")
    private String modelCode;
    /**
     * 名称
     */
    @Excel(name = "名称")
    @ApiModelProperty("名称")
    private String modelName;
    /**
     * 父级ID
     */
    @Excel(name = "父级ID")
    @ApiModelProperty("父级ID")
    private String parentId;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    @ApiModelProperty("显示顺序")
    private Long orderNum;
    /**
     * 类型（1模块 2单据）
     */
    @Excel(name = "类型", readConverterExp = "1=模块,2=单据")
    @ApiModelProperty("类型（1模块 2单据）")
    private Long modelType;
    /**
     * 编号器ID
     */
    @Excel(name = "编号器ID")
    @ApiModelProperty("编号器ID")
    private String ruleId;
    /**
     * 字典id
     */
    @Excel(name = "字典id")
    @ApiModelProperty("字典id")
    private String dictId;
    @ApiModelProperty("主表名")
    private String tableMain;
    @ApiModelProperty("费用明细表名")
    private String tableExpName;
    @ApiModelProperty("制度说明")
    private String regimeExplain;
    @ApiModelProperty("是否允许无申请报销 0:是 2:否")
    private String isAllowedApply;
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
}
