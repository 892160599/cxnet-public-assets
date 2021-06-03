package com.cxnet.companyBaseDate.place.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.companyBaseDate.place.mapper.AstPlaceMapper;
import com.cxnet.companyBaseDate.place.domain.AstPlace;
import com.cxnet.companyBaseDate.place.service.AstPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 存放地点(AstPlace)表服务实现类
 *
 * @author zhangyl
 * @since 2021-03-30 09:55:27
 */
@Service
public class AstPlaceServiceImpl extends ServiceImpl<AstPlaceMapper, AstPlace> implements AstPlaceService {

    @Autowired(required = false)
    private AstPlaceMapper astPlaceMapper;

    @Override
    public List<Zone> selectAstPlaceTree(AstPlace astPlace) {
        astPlace.setDelFlag("0");
        List<Zone> zones = astPlaceMapper.selectAstPlaceTree(astPlace);
        List<Zone> zoneList = ZoneUtils.buildTreeBypcode(zones);
        return zoneList;
    }
}

