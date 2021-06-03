package com.cxnet.flow.model.domain;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cxnet.common.utils.poi.annotation.Excel;
import com.cxnet.framework.web.domain.BaseEntity;

/**
 * 系统模块流程表实体 sys_model_deployment
 *
 * @author caixx
 * @date 2020-08-12
 */
@Data
@ApiModel("系统模块流程表")
public class SysModelDeployment extends BaseEntity {

    /**
     * 模块id
     */
    @Excel(name = "模块id")
    @ApiModelProperty("模块id")
    private String modelId;
    /**
     * 模型标识
     */
    @Excel(name = "模型标识")
    @ApiModelProperty("模型标识")
    private String key;
    /**
     * 单位id
     */
    @Excel(name = "单位id")
    @ApiModelProperty("单位id")
    private String unitId;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;
    /**
     * 状态（0代表启用 2代表停用）
     */
    @Excel(name = "状态", readConverterExp = "0=代表启用,2=代表停用")
    @ApiModelProperty("状态（0代表启用 2代表停用）")
    private String status;
    /**
     * 模型id
     */
    @Excel(name = "模型id")
    @ApiModelProperty("模型id")
    private String deploymentId;

    @ApiModelProperty("模型名称")
    private String deploymentName;
    @ApiModelProperty("系统模块名称")
    private String modelName;
    @ApiModelProperty("流程所属单位名称")
    private String unitName;
    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("是否选中,0选中，2未选中")
    private Integer isBind = 2;

    @ApiModelProperty("绑定单据id")
    private String bindModelId;

    @ApiModelProperty("备注")
    private String remark;

}
