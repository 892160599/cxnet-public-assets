package com.cxnet.asset.empchange.mapper;

import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.cxnet.asset.empchange.domain.AstEmpchangeList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产使用人变动明细表(AstEmpchangeList)表数据库访问层
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:31
 */
public interface AstEmpchangeListMapper extends BaseMapper<AstEmpchangeList> {

    List<AstEmpchangeList> selectAll(AstEmpchangeList astEmpchangeList);

    List<AstEmpchangeList> getThisMoAstchange(String date, List<String> astCardIds);
}

