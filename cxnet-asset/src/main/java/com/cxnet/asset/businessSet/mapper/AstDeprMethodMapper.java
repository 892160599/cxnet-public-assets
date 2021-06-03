package com.cxnet.asset.businessSet.mapper;

import com.cxnet.asset.businessSet.domain.AstDeprMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.baseData.expeco.bdExpeco.domain.BdExpeco;
import com.cxnet.common.utils.tree.Zone;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 资产折旧方法表(AstDeprMethod)表数据库访问层
 *
 * @author zhangyl
 * @since 2021-03-25 10:00:11
 */
public interface AstDeprMethodMapper extends BaseMapper<AstDeprMethod> {

    List<Zone> selectAstDeprMethodTree(AstDeprMethod astDeprMethod);
}

