package com.cxnet.asset.astrepairbill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.astrepairbill.domain.AstRepairBill;

import java.util.List;

/**
 * 资产报修单主表(AstRepairBill)表数据库访问层
 *
 * @author guks
 * @since 2021-04-15 11:57:19
 */
public interface AstRepairBillMapper extends BaseMapper<AstRepairBill> {

    List<AstRepairBill> selectAll(AstRepairBill astRepairBill);
}