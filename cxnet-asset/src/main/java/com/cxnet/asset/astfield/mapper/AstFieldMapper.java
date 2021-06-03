package com.cxnet.asset.astfield.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.astfield.domain.AstField;

/**
 * 卡片字段表(AstField)表数据库访问层
 *
 * @author guks
 * @since 2021-04-25 14:31:59
 */
public interface AstFieldMapper extends BaseMapper<AstField> {

    List<AstField> selectAll(AstField astField);
}