package com.cxnet.companyBaseDate.place.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.companyBaseDate.place.domain.AstPlace;

import java.util.List;

/**
 * 存放地点(AstPlace)表服务接口
 *
 * @author zhangyl
 * @since 2021-03-30 09:55:26
 */
public interface AstPlaceService extends IService<AstPlace> {

    List<Zone> selectAstPlaceTree(AstPlace astPlace);
}

