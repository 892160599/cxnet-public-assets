package com.cxnet.flow.model.domain;

import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统模块管理实体 sys_model
 *
 * @author caixx
 * @date 2020-08-12
 */
@Data
@ApiModel("系统模块管理")
public class SysModelBill extends BaseEntity {

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
     * 系统模块
     */
    private String modelNameParent;

    private String ruleName;
    private String dictName;
}
