package com.cxnet.baseData.assetType.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.baseData.assetType.domain.BdAnnex;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.common.utils.tree.Zone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资产类别Mapper接口
 *
 * @author caixx
 * @date 2020-07-20
 */
public interface BdAssetTypeMapper extends BaseMapper<BdAssetType> {
    /**
     * 查询资产类别
     *
     * @param assetId 资产类别ID
     * @return 资产类别
     */
    public BdAssetType selectAssetTypeById(String assetId);

    /**
     * 根据id查询
     *
     * @param expecoCodes
     * @return
     */
    List<BdAssetType> selectBdAssetType(String[] expecoCodes);

    /**
     * 查询所有上级集合
     *
     * @param assetCode
     * @return
     */
    List<BdAssetType> selectBdAssetTypeList(@Param("expecoCode") String assetCode);

    /**
     * 删除数据
     *
     * @param unitId
     * @return
     */
    int deleteBdAssetType(@Param("unitId") String unitId);

    /**
     * 查询资产类别集合
     *
     * @param DbAssetType 资产类别
     * @return 资产类别集合
     */
    public List<BdAssetType> selectAssetTypeList(BdAssetType DbAssetType);

    /**
     * 查询资产类别集合tree
     *
     * @return 资产类别集合
     */
    public List<Zone> selectAssetTypeListTree(BdAssetType bdAssetType);

    /**
     * 新增资产类别
     *
     * @param DbAssetType 资产类别
     * @return 结果
     */
    public int insertAssetType(BdAssetType DbAssetType);

    /**
     * 批量新增资产类别
     *
     * @param DbAssetTypes 资产类别
     * @return 结果
     */
    public int insertBatchAssetType(List<BdAssetType> DbAssetTypes);

    /**
     * 修改资产类别
     *
     * @param DbAssetType 资产类别
     * @return 结果
     */
    public int updateAssetType(BdAssetType DbAssetType);

    /**
     * 批量修改资产类别
     *
     * @param DbAssetTypes 资产类别
     * @return 结果
     */
    public int updateBatchAssetType(List<BdAssetType> DbAssetTypes);

    /**
     * 删除资产类别
     *
     * @param assetId 资产类别ID
     * @return 结果
     */
    public int deleteAssetTypeById(String assetId);

    /**
     * 批量删除资产类别
     *
     * @param assetIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteAssetTypeByIds(String[] assetIds);

    /**
     * 根据品目id查询资产类别列表
     *
     * @param itemCode
     * @return
     */
    List<BdAssetType> selectAssetTypeListById(String itemCode);

    List<BdAssetType> selectAssetTypeByCode(@Param("list") List<String> codes);

    /**
     * 校验编码是否存在
     *
     * @param assetCode
     * @return
     */
    int checkAssetCode(@Param("assetCode") String assetCode, @Param("assetId") String assetId);

    int updateAssetUnid(@Param("assetCode") String assetCode, @Param("assetId") String assetId, @Param("unitId") String unitId);

    /**
     * 根据id查询是否有子节点
     *
     * @param assetIds
     * @return
     */
    int existChildren(String[] assetIds);
}
