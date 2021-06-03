package com.cxnet.baseData.purItem.mapper;

import com.cxnet.baseData.purItem.domain.BdPurItem;
import com.cxnet.common.utils.tree.Zone;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 采购品目Mapper接口
 *
 * @author caixx
 * @date 2020-07-21
 */
public interface BdPurItemMapper {
    /**
     * 查询采购品目
     *
     * @param itemId 采购品目ID
     * @return 采购品目
     */
    public BdPurItem selectPurItemById(String itemId);

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
     * 删除采购品目
     *
     * @param itemId 采购品目ID
     * @return 结果
     */
    public int deletePurItemById(String itemId);

    /**
     * 批量删除采购品目 假删
     *
     * @param itemIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePurItemByIds(String[] itemIds);

    /**
     * 批量删除采购品目 真删
     *
     * @param itemIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePurItemByIdList(List<String> itemIds);

    /**
     * 查询采购品目
     *
     * @param itemIds
     * @return 结果
     */
    public List<BdPurItem> selectPurItemByIds(String[] itemIds);

    /**
     * 获取采购品目下拉树列表
     */
    public List<Map> treeselectList();

    /**
     * 获取采购品目下拉树列表
     *
     * @param bdPurItem 业务年度
     * @return Zone
     */
    List<Zone> selectPurItemListTree(BdPurItem bdPurItem);

    /**
     * 校验编码是否存在
     *
     * @param itemCode
     * @return
     */
    int checkItemCode(String itemCode);

    List<BdPurItem> selectItemByCode(@Param("list") List<String> codes);

    public int hasChildByItemCode(String itemId);

    /**
     * 查询所有上级集合
     *
     * @param purYear
     * @param itemCode
     * @return
     */
    List<BdPurItem> selectBdItemUpList(@Param("purYear") Long purYear, @Param("itemCode") String itemCode);

    int deletePurItemByYear(@Param("unitId") String unitId, @Param("year") Long year);

    int selectLatestYear();

    void carryForwardData(@Param("newYear") Integer newYear, @Param("oldYear") Integer oldYear);

}
