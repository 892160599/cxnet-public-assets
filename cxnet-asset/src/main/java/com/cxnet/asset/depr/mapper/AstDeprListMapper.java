package com.cxnet.asset.depr.mapper;

import com.cxnet.asset.depr.domain.AstDeprList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产折旧明细表(AstDeprList)表数据库访问层
 *
 * @author caixx
 * @since 2021-04-08 16:03:13
 */
public interface AstDeprListMapper extends BaseMapper<AstDeprList> {

    List<AstDeprList> selectAll(AstDeprList astDeprList);
}