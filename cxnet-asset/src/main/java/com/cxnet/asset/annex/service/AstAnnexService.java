package com.cxnet.asset.annex.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.annex.domain.AstAnnex;

import java.util.List;

/**
 * 资产附件关联表(AstAnnex)表服务接口
 *
 * @author makejava
 * @since 2021-03-25 10:55:11
 */
public interface AstAnnexService extends IService<AstAnnex> {

    List<AstAnnex> listByAstIdAndAnnexType(String astId, String annexType);

}

