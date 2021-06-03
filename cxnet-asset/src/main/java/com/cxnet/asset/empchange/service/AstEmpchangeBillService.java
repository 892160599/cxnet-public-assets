package com.cxnet.asset.empchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.empchange.domain.AstEmpchangeBill;
import com.cxnet.asset.empchange.domain.vo.AstEmpchangeVo;
import com.cxnet.flow.domain.CommonProcessRequest;

import java.util.List;

/**
 * 资产使用人变动主表(AstEmpchangeBill)表服务接口
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:09
 */
public interface AstEmpchangeBillService extends IService<AstEmpchangeBill> {

    String saveAstEmpchangeVo(AstEmpchangeVo astEmpchangeVo);

    String updateAstEmpchangeVo(AstEmpchangeVo astEmpchangeVo);

    String delete(List<String> ids);

    String empSubmit(CommonProcessRequest commonProcessRequest);

    String empAudit(CommonProcessRequest commonProcessRequest);

    String empBack(CommonProcessRequest commonProcessRequest);

    String taskBack(CommonProcessRequest commonProcessRequest);

    List<AstEmpchangeBill> selectAll(AstEmpchangeBill astEmpchangeBill);
}

