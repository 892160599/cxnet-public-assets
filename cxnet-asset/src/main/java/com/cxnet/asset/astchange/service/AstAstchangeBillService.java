package com.cxnet.asset.astchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.astchange.domain.AstAstchangeBill;
import com.cxnet.asset.astchange.domain.vo.AstAstchangeVo;
import com.cxnet.asset.astchange.domain.vo.SelectAstAstChangeListVo;
import com.cxnet.flow.domain.CommonProcessRequest;

import java.util.List;

/**
 * 资产变动主表(AstAstchangeBill)表服务接口
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:14
 */
public interface AstAstchangeBillService extends IService<AstAstchangeBill> {

    String saveAstAstchangeVo(AstAstchangeVo astAstchangeVo);

    String updateAstAstchangeVo(AstAstchangeVo astAstchangeVo);

    String delete(List<String> ids);

    String astSubmit(CommonProcessRequest commonProcessRequest);

    String astAudit(CommonProcessRequest commonProcessRequest);

    String astBack(CommonProcessRequest commonProcessRequest);

    String taskBack(CommonProcessRequest commonProcessRequest);

    List<AstAstchangeBill> selectAll(AstAstchangeBill astAstchangeBill);

    List<SelectAstAstChangeListVo> selectAstAstChangeList(String astId);
}

