package com.cxnet.baseData.purItem.service;

import com.cxnet.baseData.purItem.domain.BdPurItem;
import com.cxnet.baseData.purItem.domain.SelectionPurItem;
import com.cxnet.baseData.purItem.domain.vo.BdPurItemVo;
import com.cxnet.common.utils.tree.Zone;

import java.util.List;
import java.util.Map;

/**
 * 采购品目Service接口
 *
 * @author caixx
 * @date 2020-07-21
 */
public interface BdPurItemServiceI {
    /**
     * 查询采购品目
     *
     * @param itemId 采购品目ID
     * @return 采购品目
     */
    public BdPurItem selectPurItemById(String itemId);

    /**
     * 查询采购品目vo
     *
     * @param itemId
     * @return
     */
    public BdPurItemVo selectPurItemAndAssetTypeById(String itemId);

    /**
     * 查询采购品目集合
     *
     * @param purItem 采购品目
     * @return 采购品目集合
     */
    public List<BdPurItem> selectPurItemList(BdPurItem purItem);

    /**
     * 新增采购品目
     *
     * @param purItem 采购品目
     * @return 结果
     */
    public int insertPurItem(BdPurItem purItem);

    /**
     * 批量新增采购品目
     *
     * @param purItems 采购品目
     * @return 结果
     */
    public int insertBatchPurItem(List<BdPurItem> purItems);

    /**
     * 修改采购品目
     *
     * @param purItem 采购品目
     * @return 结果
     */
    public int updatePurItem(BdPurItem purItem);

    /**
     * 批量修改采购品目
     *
     * @param purItems 采购品目
     * @return 结果
     */
    public int updateBatchPurItem(List<BdPurItem> purItems);

    /**
     * 批量删除采购品目
     *
     * @param itemIds 需要删除的采购品目ID
     * @return 结果
     */
    public int deletePurItemByIds(String[] itemIds);

    /**
     * 删除采购品目信息
     *
     * @param itemId 采购品目ID
     * @return 结果
     */
    public int deletePurItemById(String itemId);

    /**
     * 获取采购品目下拉树列表
     */
    public List<Map> treeselectList();

    /**
     * 获取采购品目下拉树列表
     *
     * @param bdPurItem
     * @return
     */
    List<Zone> selectPurItemListTree(BdPurItem bdPurItem);

    /**
     * 校验编码是否存在
     *
     * @param itemCode
     * @return
     */
    boolean checkItemCode(String itemCode);

    /**
     * 校验编码修改是否存在
     *
     * @param purItemVo
     * @return
     */
    boolean checkItemCodeUpdate(BdPurItemVo purItemVo);

    /**
     * 新增购品目信息和子表信息
     *
     * @param purItemVo
     * @return
     */
    int insertPurItemAndAssetType(BdPurItemVo purItemVo);

    /**
     * 修改购品目信息和子表信息
     *
     * @param purItemVo
     * @return
     */
    int updatePurItemAndAssetType(BdPurItemVo purItemVo);

    /**
     * 是否存在相同品目编码
     *
     * @param purItem 品目编码
     * @return 结果
     */
    void checkImportItemCode(List<BdPurItem> purItem);

    /**
     * 校验是否有下级采购品目
     *
     * @param itemId
     * @return
     */
    boolean hasChildByItemCode(String itemId);

    /**
     * 单位选用采购品目
     */
    List<Zone> insertBatchSelectionPurItem(SelectionPurItem selectionPurItem);

    Map<String, Integer> carryForwardYear();

    void carryForwardData(String year);
}
