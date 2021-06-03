package com.cxnet.asset.astchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.astchange.domain.AstAstchangeList;

import java.util.List;

/**
 * 资产变动明细表(AstAstchangeList)表服务接口
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:55
 */
public interface AstAstchangeListService extends IService<AstAstchangeList> {

    List<AstAstchangeList> getThisMoAstchange(String date, List<String> astCardIds);

}

