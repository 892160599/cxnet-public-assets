package com.cxnet.asset.astcollectbill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.astcollectbill.domain.AstCollectBill;

import java.util.List;

/**
 * 资产领用单主表(AstCollectBill)表数据库访问层
 *
 * @author guks
 * @since 2021-04-12 10:52:14
 */
public interface AstCollectBillMapper extends BaseMapper<AstCollectBill> {

    List<AstCollectBill> selectAstCollectBillsList(AstCollectBill astCollectBill);
}