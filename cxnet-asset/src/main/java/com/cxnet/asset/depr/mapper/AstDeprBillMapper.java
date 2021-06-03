package com.cxnet.asset.depr.mapper;

import com.cxnet.asset.depr.domain.AstDeprBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资产折旧主表(AstDeprBill)表数据库访问层
 *
 * @author caixx
 * @since 2021-04-08 16:03:12
 */
public interface AstDeprBillMapper extends BaseMapper<AstDeprBill> {

    List<AstDeprBill> selectAll(AstDeprBill astDeprBill);

    List<Map<String, Object>> record(@Param("id") String id);
}