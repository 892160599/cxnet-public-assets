package com.cxnet.baseData.assetType.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.baseData.assetType.domain.BdAnnex;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.domain.SelectionBdAssetType;
import com.cxnet.baseData.assetType.domain.vo.BdAssetTypeVo;
import com.cxnet.common.utils.tree.Zone;

import java.util.List;

/**
 * 资产类别Service接口
 *
 * @author caixx
 * @date 2020-07-20
 */
public interface BdAssetTypeService extends IService<BdAssetType> {
    /**
     * 查询资产类别
     *
     * @param assetId 资产类别ID
     * @return 资产类别
     */
    public BdAssetType selectAssetTypeById(String assetId);

    BdAssetTypeVo selectOne(String assetId);

    String update(BdAssetTypeVo assetTypeVo);

    /**
     * 查询资产类别集合
     *
     * @param assetType 资产类别
     * @return 资产类别集合
     */
    public List<BdAssetType> selectAssetTypeList(BdAssetType assetType);

    /**
     * 查询资产类别集合tree
     *
     * @return 资产类别集合
     */
    public List<Zone> selectAssetTypeListTree(BdAssetType bdAssetType);

    /**
     * 单位选用资产类别
     *
     * @param assetType
     * @return
     */
    List<Zone> insertBatchSelectAssetType(SelectionBdAssetType assetType);

    /**
     * 新增资产类别
     *
     * @param assetType 资产类别
     * @return 结果
     */
    public int insertAssetType(BdAssetType assetType);

    /**
     * 批量新增资产类别
     *
     * @param assetTypes 资产类别
     * @return 结果
     */
    public int insertBatchAssetType(List<BdAssetType> assetTypes);

    /**
     * 修改资产类别
     *
     * @param assetType 资产类别
     * @return 结果
     */
    public int updateAssetType(BdAssetType assetType);

    public int updateAssetUnid(BdAssetType assetType);

    /**
     * 批量修改资产类别
     *
     * @param assetTypes 资产类别
     * @return 结果
     */
    public int updateBatchAssetType(List<BdAssetType> assetTypes);

    /**
     * 批量删除资产类别
     *
     * @param assetIds 需要删除的资产类别ID
     * @return 结果
     */
    public int deleteAssetTypeByIds(String[] assetIds);

    /**
     * 删除资产类别信息
     *
     * @param assetId 资产类别ID
     * @return 结果
     */
    public int deleteAssetTypeById(String assetId);

    /**
     * 校验编码是否存在
     *
     * @param assetCode
     * @return
     */
    boolean checkAssetCode(String assetCode);

    void checkAssetTypes(List<BdAssetType> assetTypes);
}
