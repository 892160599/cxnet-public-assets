package com.cxnet.asset.astchange.mapper;

import com.cxnet.asset.astchange.domain.AstAstchangeBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产变动主表(AstAstchangeBill)表数据库访问层
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:14
 */
public interface AstAstchangeBillMapper extends BaseMapper<AstAstchangeBill> {

    List<AstAstchangeBill> selectAll(AstAstchangeBill astAstchangeBill);
}

