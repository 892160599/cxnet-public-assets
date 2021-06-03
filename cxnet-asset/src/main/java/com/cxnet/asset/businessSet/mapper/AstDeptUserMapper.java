package com.cxnet.asset.businessSet.mapper;

import com.cxnet.asset.businessSet.domain.AstDeptUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.baseData.expfunc.bdExpfunc.domain.BdExpfunc;
import com.cxnet.common.utils.tree.Zone;

import java.util.List;

/**
 * (AstDeptUser)表数据库访问层
 *
 * @author zhangyl
 * @since 2021-03-29 10:39:24
 */
public interface AstDeptUserMapper extends BaseMapper<AstDeptUser> {

    List<Zone> selectAstDeptTree(AstDeptUser astDeptUser);
}

