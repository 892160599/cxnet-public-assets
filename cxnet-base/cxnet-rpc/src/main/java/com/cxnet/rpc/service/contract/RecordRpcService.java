package com.cxnet.rpc.service.contract;

import com.cxnet.rpc.domain.contract.RecordRpc;

import java.math.BigDecimal;

public interface RecordRpcService {
    /**
     * 根据合同编号查找备案合同信息
     *
     * @param conId 合同编号
     * @return 备案合同信息
     */
    RecordRpc getRecordRpcByConId(String conId);

    /**
     * 修改合同备案余额
     *
     * @param conId     合同编号
     * @param usedMoney 本次使用金额
     */
    void updateRecordBalancer(String conId, BigDecimal usedMoney);

}
