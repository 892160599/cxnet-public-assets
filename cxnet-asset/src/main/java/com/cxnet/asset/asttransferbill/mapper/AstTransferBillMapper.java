package com.cxnet.asset.asttransferbill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.asttransferbill.domain.AstTransferBill;

import java.util.List;

/**
 * 资产移交单主表(AstTransferBill)表数据库访问层
 *
 * @author guks
 * @since 2021-04-15 11:58:23
 */
public interface AstTransferBillMapper extends BaseMapper<AstTransferBill> {

    List<AstTransferBill> selectAll(AstTransferBill astTransferBill);
}