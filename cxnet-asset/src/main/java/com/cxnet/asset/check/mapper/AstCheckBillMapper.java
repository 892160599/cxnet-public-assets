package com.cxnet.asset.check.mapper;

import com.cxnet.asset.check.domain.AstCheckBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产验收主表(AstCheckBill)表数据库访问层
 *
 * @author caixx
 * @since 2021-03-25 09:39:41
 */
public interface AstCheckBillMapper extends BaseMapper<AstCheckBill> {

    List<AstCheckBill> selectAstCheckBillsList(AstCheckBill astCheckBill);
}