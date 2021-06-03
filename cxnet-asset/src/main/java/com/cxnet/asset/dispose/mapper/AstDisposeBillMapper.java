package com.cxnet.asset.dispose.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.dispose.domain.AstDisposeBill;

import java.util.List;

/**
 * 资产处置主表(AstDisposeBill)表数据库访问层
 *
 * @author zhaoyi
 * @since 2021-03-25 10:13:22
 */
public interface AstDisposeBillMapper extends BaseMapper<AstDisposeBill> {

    List<AstDisposeBill> selectAstDisposeBillList(AstDisposeBill astDisposeBill);
}

