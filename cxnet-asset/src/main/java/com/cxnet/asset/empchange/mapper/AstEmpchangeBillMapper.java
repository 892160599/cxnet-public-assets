package com.cxnet.asset.empchange.mapper;

import com.cxnet.asset.empchange.domain.AstEmpchangeBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产使用人变动主表(AstEmpchangeBill)表数据库访问层
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:10
 */
public interface AstEmpchangeBillMapper extends BaseMapper<AstEmpchangeBill> {

    List<AstEmpchangeBill> selectAll(AstEmpchangeBill astEmpchangeBill);
}

