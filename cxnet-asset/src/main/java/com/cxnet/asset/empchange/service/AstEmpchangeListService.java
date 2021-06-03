package com.cxnet.asset.empchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.cxnet.asset.empchange.domain.AstEmpchangeList;

import java.util.List;

/**
 * 资产使用人变动明细表(AstEmpchangeList)表服务接口
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:30
 */
public interface AstEmpchangeListService extends IService<AstEmpchangeList> {

    List<AstEmpchangeList> getThisMoAstchange(String date, List<String> astCardIds);
}

