package com.cxnet.asset.businessSet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.businessSet.domain.AstDeprMethod;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.common.utils.tree.Zone;

import java.util.List;

/**
 * 资产折旧方法表(AstDeprMethod)表服务接口
 *
 * @author zhangyl
 * @since 2021-03-25 10:00:09
 */
public interface AstDeprMethodService extends IService<AstDeprMethod> {

    /**
     * 查询折旧方法树结构
     * @param deprMethodName
     * @return
     */
//    public List<Zone> selectAssetTypeListTree(String  deprMethodName);
}

