package com.cxnet.asset.astcardstyletype.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.astcardstyletype.domain.AstCardStyleType;

import java.util.List;

/**
 * 卡片样式管理资产类别表(AstCardStyleType)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-26 15:31:37
 */
public interface AstCardStyleTypeMapper extends BaseMapper<AstCardStyleType> {

    List<AstCardStyleType> selectAll(AstCardStyleType astCardStyleType);
}