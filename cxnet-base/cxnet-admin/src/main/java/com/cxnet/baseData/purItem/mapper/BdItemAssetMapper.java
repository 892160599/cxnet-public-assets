package com.cxnet.baseData.purItem.mapper;

import com.cxnet.baseData.purItem.domain.BdItemAsset;

import java.util.List;

/**
 * 采购品目与资产类别关联Mapper接口
 *
 * @author cxnet
 * @date 2020-07-22
 */
public interface BdItemAssetMapper {
    /**
     * 查询采购品目与资产类别关联
     *
     * @param itemId 采购品目与资产类别关联ID
     * @return 采购品目与资产类别关联
     */
    public BdItemAsset selectBdItemAssetById(String itemId);

    /**
     * 查询采购品目与资产类别关联集合
     *
     * @param bdItemAsset 采购品目与资产类别关联
     * @return 采购品目与资产类别关联集合
     */
    public List<BdItemAsset> selectBdItemAssetList(BdItemAsset bdItemAsset);

    /**
     * 新增采购品目与资产类别关联
     *
     * @param bdItemAsset 采购品目与资产类别关联
     * @return 结果
     */
    public int insertBdItemAsset(BdItemAsset bdItemAsset);

    /**
     * 批量新增采购品目与资产类别关联
     *
     * @param bdItemAssets 采购品目与资产类别关联
     * @return 结果
     */
    public int insertBatchBdItemAsset(List<BdItemAsset> bdItemAssets);

    /**
     * 修改采购品目与资产类别关联
     *
     * @param bdItemAsset 采购品目与资产类别关联
     * @return 结果
     */
    public int updateBdItemAsset(BdItemAsset bdItemAsset);

    /**
     * 批量修改采购品目与资产类别关联
     *
     * @param bdItemAssets 采购品目与资产类别关联
     * @return 结果
     */
    public int updateBatchBdItemAsset(List<BdItemAsset> bdItemAssets);

    /**
     * 删除采购品目与资产类别关联
     *
     * @param itemId 采购品目与资产类别关联ID
     * @return 结果
     */
    public int deleteBdItemAssetById(String itemId);

    /**
     * 删除采购品目与资产类别关联
     *
     * @param itemCode 采购品目与资产类别关联code
     * @return 结果
     */
    public int deleteBdItemAssetByCode(String itemCode);

    /**
     * 批量删除采购品目与资产类别关联
     *
     * @param itemIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBdItemAssetByIds(String[] itemIds);


}
