package com.cxnet.asset.inventory.mapper;

import com.cxnet.asset.check.domain.AstCheckBill;
import com.cxnet.asset.inventory.domain.AstSurplusBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产盘盈主表(AstSurplusBill)表数据库访问层
 *
 * @author zhangyl
 * @since 2021-04-19 14:14:44
 */
public interface AstSurplusBillMapper extends BaseMapper<AstSurplusBill> {

    List<AstSurplusBill> selectAll(AstSurplusBill astSurplusBill);

    List<AstSurplusBill> selectAstSurplusList(AstSurplusBill astSurplusBill);
}

