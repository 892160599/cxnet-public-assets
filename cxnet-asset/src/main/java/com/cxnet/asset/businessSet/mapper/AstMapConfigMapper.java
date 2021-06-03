package com.cxnet.asset.businessSet.mapper;

import com.cxnet.asset.businessSet.domain.AstMapConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产地图配置(AstMapConfig)表数据库访问层
 *
 * @author caixx
 * @since 2021-04-25 14:06:24
 */
public interface AstMapConfigMapper extends BaseMapper<AstMapConfig> {

    List<AstMapConfig> selectAll(AstMapConfig astMapConfig);
}