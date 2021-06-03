package com.cxnet.companyBaseDate.place.mapper;

import com.cxnet.asset.businessSet.domain.AstDeptUser;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.companyBaseDate.place.domain.AstPlace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 存放地点(AstPlace)表数据库访问层
 *
 * @author zhangyl
 * @since 2021-03-30 09:55:30
 */
public interface AstPlaceMapper extends BaseMapper<AstPlace> {

    List<Zone> selectAstPlaceTree(AstPlace astPlace);
}

