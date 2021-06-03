package com.cxnet.asset.card.mapper;

import com.cxnet.asset.card.domain.AstBillList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产操作明细表(AstBillList)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-06 16:47:04
 */
public interface AstBillListMapper extends BaseMapper<AstBillList> {

    List<AstBillList> selectAll(AstBillList astBillList);
}

