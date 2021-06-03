package com.cxnet.asset.depr.mapper;

import com.cxnet.asset.depr.domain.AstDeprRepairList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 资产补计提折旧明细表(AstDeprRepairList)表数据库访问层
 *
 * @author caixx
 * @since 2021-04-16 18:07:52
 */
public interface AstDeprRepairListMapper extends BaseMapper<AstDeprRepairList> {

    List<AstDeprRepairList> selectAll(AstDeprRepairList astDeprRepairList);
}