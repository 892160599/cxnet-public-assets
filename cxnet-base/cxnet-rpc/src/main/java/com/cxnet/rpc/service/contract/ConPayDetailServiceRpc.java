package com.cxnet.rpc.service.contract;

import com.cxnet.rpc.domain.contract.ConPayDetailRpc;

import java.util.List;

public interface ConPayDetailServiceRpc {
    /**
     * 根据合同id获取合同支付详情
     *
     * @param conId 合同id
     * @return 合同支付详情集合
     */
    List<ConPayDetailRpc> getConPayByConId(String conId);

    /**
     * 批量保存合同支付详情
     *
     * @param conPayDetailRpcs 合同支付详情集合
     */
    void saveConPayDetailBatch(List<ConPayDetailRpc> conPayDetailRpcs);
}
