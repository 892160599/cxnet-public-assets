package com.cxnet.asset.card.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.asset.card.domain.AstCard;

/**
 * 资产主表(AstCard)表数据库访问层
 *
 * @author guks
 * @since 2021-03-29 10:23:02
 */
public interface AstCardMapper extends BaseMapper<AstCard> {


    List<AstCard> selectAll(AstCard astCard);
}

