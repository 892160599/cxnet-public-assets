package com.cxnet.asset.allocation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.allocation.domain.AstAllocationBill;
import com.cxnet.asset.allocation.domain.vo.AstAllocationVo;
import com.cxnet.flow.domain.CommonProcessRequest;

import java.util.List;

/**
 * 资产部门调剂主表(AstAllocationBill)表服务接口
 *
 * @author zhaoyi
 * @since 2021-04-02 09:55:49
 */
public interface AstAllocationBillService extends IService<AstAllocationBill> {

    String saveAstAllocationVo(AstAllocationVo astAllocationVo);

    String updateAstAllocationVo(AstAllocationVo astAllocationVo);

    String delete(List<String> ids);

    String allocationSubmit(CommonProcessRequest commonProcessRequest);

    String allocationAudit(CommonProcessRequest commonProcessRequest);

    String allocationBack(CommonProcessRequest commonProcessRequest);

    String taskBack(CommonProcessRequest commonProcessRequest);

    List<AstAllocationBill> selectAll(AstAllocationBill astAllocationBill);
}

