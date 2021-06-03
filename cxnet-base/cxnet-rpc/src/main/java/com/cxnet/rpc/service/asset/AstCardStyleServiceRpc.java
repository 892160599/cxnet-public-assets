package com.cxnet.rpc.service.asset;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.rpc.domain.asset.AstCardStyle;

/**
 * 单据样式配置表(AstCardStyle)表服务接口
 *
 * @author
 * @since
 */
public interface AstCardStyleServiceRpc extends IService<AstCardStyle> {

    /**
     * 根据单位id和资产编码查询卡片样式
     *
     * @param unitId    单位id
     * @param assetCode 资产编码
     * @param assetType 资产类型
     * @return 卡片样式
     */
    AstCardStyle getAstCardStyleByUnitIdAndAssetCode(String unitId, String assetCode, String assetType);
}