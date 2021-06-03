package com.cxnet.rpc.service.contract;

import com.cxnet.rpc.domain.contract.ConFunds;

import java.util.List;

public interface ConFundsServiceRpc {

    List<ConFunds> seletConfundsByConId(String conId);

    List<ConFunds> seletConfundsByConIds(List<String> conIds);
}
