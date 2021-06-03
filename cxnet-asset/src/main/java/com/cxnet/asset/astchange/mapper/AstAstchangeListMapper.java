package com.cxnet.asset.astchange.mapper;

import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资产变动明细表(AstAstchangeList)表数据库访问层
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:55
 */
public interface AstAstchangeListMapper extends BaseMapper<AstAstchangeList> {

    List<AstAstchangeList> selectAll(AstAstchangeList astAstchangeList);

    List<AstAstchangeList> getThisMoAstchange(@Param("date") String date, @Param("astCardIds") List<String> astCardIds);
}

