package com.cxnet.rpc.service.expense;

import java.math.BigDecimal;

public interface ExpExpensePurchaseServiceRpc {

    BigDecimal selectExpensePurchase(String purId);
}
