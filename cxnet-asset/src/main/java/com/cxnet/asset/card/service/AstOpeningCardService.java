package com.cxnet.asset.card.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.domain.AstOpeningCard;
import com.cxnet.asset.card.domain.AstOpeningJournal;

import java.util.List;

/**
 * 期初卡片导入记录表(AstOpeningCard)表服务接口
 *
 * @author zhangyl
 * @since 2021-04-22 16:29:00
 */
public interface AstOpeningCardService extends IService<AstOpeningCard> {

    /**
     * 期初卡片导入
     *
     * @param astCardList
     * @return
     */
    AstOpeningCard insertCardList(List<AstCard> astCardList);

    /**
     * 查询期初卡片导入日志信息
     *
     * @param id
     * @return
     */
    List<AstOpeningJournal> getList(String id);
}

