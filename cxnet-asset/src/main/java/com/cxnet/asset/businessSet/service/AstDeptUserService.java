package com.cxnet.asset.businessSet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.businessSet.domain.AstDeptUser;
import com.cxnet.common.utils.tree.Zone;

import java.util.List;
import java.util.Map;

/**
 * (AstDeptUser)表服务接口
 *
 * @author zhangyl
 * @since 2021-03-29 10:39:24
 */
public interface AstDeptUserService extends IService<AstDeptUser> {

    List<Zone> selectAstDeptTree(AstDeptUser astDeptUser);

    Map<String, List<String>> getMap();

    Map<String, List<String>> getPerMap(String personnelId);
}

