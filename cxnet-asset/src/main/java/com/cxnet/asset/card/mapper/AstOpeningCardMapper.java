package com.cxnet.asset.card.mapper;

import com.cxnet.asset.card.domain.AstOpeningCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 期初卡片导入记录表(AstOpeningCard)表数据库访问层
 *
 * @author zhangyl
 * @since 2021-04-22 16:29:01
 */
public interface AstOpeningCardMapper extends BaseMapper<AstOpeningCard> {

    List<AstOpeningCard> selectAll(AstOpeningCard astOpeningCard);
}

