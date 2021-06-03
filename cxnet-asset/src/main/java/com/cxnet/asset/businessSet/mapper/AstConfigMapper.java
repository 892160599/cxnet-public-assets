package com.cxnet.asset.businessSet.mapper;

import com.cxnet.asset.businessSet.domain.AstConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产参数设置(AstConfig)表数据库访问层
 *
 * @author caixx
 * @since 2021-04-06 09:41:42
 */
public interface AstConfigMapper extends BaseMapper<AstConfig> {

    List<AstConfig> selectAll(AstConfig astConfig);
}