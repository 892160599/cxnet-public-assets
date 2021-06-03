package com.cxnet.baseData.assetType.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("选用资产类别实体")
public class SelectionBdAssetType {
    /**
     * 资产类别分类code
     */
    @ApiModelProperty("资产类别code")
    private String[] assetCodes;
    /**
     * 单位id
     */
    @ApiModelProperty("单位id")
    private String unitId;

}
