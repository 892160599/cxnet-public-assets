package com.cxnet.asset.depr.mapper;

import com.cxnet.asset.depr.domain.AstDeprRepairBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产补计提折旧主表(AstDeprRepairBill)表数据库访问层
 *
 * @author caixx
 * @since 2021-04-16 18:07:51
 */
public interface AstDeprRepairBillMapper extends BaseMapper<AstDeprRepairBill> {

    List<AstDeprRepairBill> selectAll(AstDeprRepairBill astDeprRepairBill);
}