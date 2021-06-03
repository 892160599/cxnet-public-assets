package com.cxnet.asset.card.mapper;

import com.cxnet.asset.card.domain.AstOpeningJournal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 期初卡片导入日志表(AstOpeningJournal)表数据库访问层
 *
 * @author zhangyl
 * @since 2021-04-22 16:29:33
 */
public interface AstOpeningJournalMapper extends BaseMapper<AstOpeningJournal> {

    List<AstOpeningJournal> selectAll(AstOpeningJournal astOpeningJournal);
}

