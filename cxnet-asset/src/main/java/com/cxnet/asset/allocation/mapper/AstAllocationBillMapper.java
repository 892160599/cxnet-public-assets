package com.cxnet.asset.allocation.mapper;

import com.cxnet.asset.allocation.domain.AstAllocationBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产部门调剂主表(AstAllocationBill)表数据库访问层
 *
 * @author zhaoyi
 * @since 2021-04-02 09:55:49
 */
public interface AstAllocationBillMapper extends BaseMapper<AstAllocationBill> {

    List<AstAllocationBill> selectAll(AstAllocationBill astAllocationBill);

}

