package com.cxnet.asset.businessSet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.businessSet.domain.AstConfig;

/**
 * 资产参数设置(AstConfig)表服务接口
 *
 * @author caixx
 * @since 2021-04-06 09:41:42
 */
public interface AstConfigService extends IService<AstConfig> {

    AstConfig selectByUnitId(String unitId);

    AstConfig update(AstConfig astConfig);
}