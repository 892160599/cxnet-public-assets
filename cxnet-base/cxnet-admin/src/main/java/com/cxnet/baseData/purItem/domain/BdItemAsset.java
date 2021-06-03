package com.cxnet.baseData.purItem.domain;

import com.alibaba.fastjson.JSON;
import com.cxnet.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 采购品目与资产类别关联实体 bd_item_asset
 *
 * @author cxnet
 * @date 2020-07-22
 */
@Data
@ApiModel("采购品目与资产类别关联")
public class BdItemAsset extends BaseEntity {

    /**
     * 采购品目代码
     */
    @ApiModelProperty("采购品目代码")
    private String itemCode;
    /**
     * 资产类别id
     */
    @ApiModelProperty("资产类别id")
    private String assetId;

    public BdItemAsset() {

    }

    public BdItemAsset(String itemCode, String assetId) {
        this.itemCode = itemCode;
        this.assetId = assetId;
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

}
