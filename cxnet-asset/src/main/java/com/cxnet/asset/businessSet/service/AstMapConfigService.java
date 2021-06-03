package com.cxnet.asset.businessSet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.businessSet.domain.AstMapConfig;

/**
 * 资产地图配置(AstMapConfig)表服务接口
 *
 * @author caixx
 * @since 2021-04-25 14:06:24
 */
public interface AstMapConfigService extends IService<AstMapConfig> {

    /**
     * 查询当前单位地图配置信息
     *
     * @return
     */
    AstMapConfig getThisAstMapConfig();


}