package com.cxnet.asset.dispose.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.dispose.domain.AstDisposeBill;
import com.cxnet.asset.dispose.domain.vo.AstDisposeVo;
import com.cxnet.asset.dispose.domain.vo.SelectAstDisposeBillVo;
import com.cxnet.flow.domain.CommonProcessRequest;

import java.util.List;

/**
 * 资产处置主表(AstDisposeBill)表服务接口
 *
 * @author zhaoyi
 * @since 2021-03-25 10:13:22
 */
public interface AstDisposeBillService extends IService<AstDisposeBill> {

    String saveAstDisposeVo(AstDisposeVo astDisposeVo);

    String updateAstDisposeVo(AstDisposeVo astDisposeVo);

    String delete(List<String> ids);

    /**
     * 送审
     *
     * @param commonProcessRequest
     * @return
     */
    int disSubmit(CommonProcessRequest commonProcessRequest);

    /**
     * 审核
     *
     * @param commonProcessRequest
     * @return
     */
    int disAudit(CommonProcessRequest commonProcessRequest);

    /**
     * 退回
     *
     * @param commonProcessRequest
     * @return
     */
    int disBack(CommonProcessRequest commonProcessRequest);

    /**
     * 收回
     *
     * @param commonProcessRequest
     * @return
     */
    int taskBack(CommonProcessRequest commonProcessRequest);

    /**
     * 分页查询
     *
     * @param astDisposeBill
     * @return
     */
    List<AstDisposeBill> selectAll(AstDisposeBill astDisposeBill);

    List<SelectAstDisposeBillVo> selectAstDisposeBillList(String astId);
}

