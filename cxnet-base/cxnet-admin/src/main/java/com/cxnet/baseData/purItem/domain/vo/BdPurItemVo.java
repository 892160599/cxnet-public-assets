package com.cxnet.baseData.purItem.domain.vo;

import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.purItem.domain.BdPurItem;
import lombok.Data;

import java.util.List;

/**
 * 采购品目vo PurItemVo
 *
 * @author caixx
 * @date 2020-07-21
 */
@Data
public class BdPurItemVo {

    private BdPurItem purItem;
    private List<BdAssetType> assetTypeList;
}
