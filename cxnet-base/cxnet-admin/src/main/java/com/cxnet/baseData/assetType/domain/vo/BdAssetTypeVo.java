package com.cxnet.baseData.assetType.domain.vo;

import com.cxnet.baseData.assetType.domain.BdAnnex;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "单位资产vo")
public class BdAssetTypeVo {

    @ApiModelProperty(value = "单位资产主表")
    private BdAssetType bdAssetType;

    @ApiModelProperty(value = "单位资产附件表")
    private List<BdAnnex> bdAnnexes;

    @ApiModelProperty(value = "计量单位")
    private List<String> measurement;

}
