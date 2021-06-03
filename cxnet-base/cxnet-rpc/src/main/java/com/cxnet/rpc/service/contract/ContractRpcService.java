package com.cxnet.rpc.service.contract;

public interface ContractRpcService {
    /**
     * 根据采购单id获取合同单据
     *
     * @param procureCode
     * @return
     */
    String selectContractByPurId(String procureCode);

    boolean selectContract(String conId);


}
