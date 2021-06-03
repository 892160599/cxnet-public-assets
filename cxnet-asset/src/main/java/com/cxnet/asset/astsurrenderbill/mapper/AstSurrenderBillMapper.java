package com.cxnet.asset.astsurrenderbill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.astsurrenderbill.domain.AstSurrenderBill;

import java.util.List;

/**
 * 资产交回单主表(AstSurrenderBill)表数据库访问层
 *
 * @author guks
 * @since 2021-04-15 11:57:53
 */
public interface AstSurrenderBillMapper extends BaseMapper<AstSurrenderBill> {

    List<AstSurrenderBill> selectAll(AstSurrenderBill astSurrenderBill);
}