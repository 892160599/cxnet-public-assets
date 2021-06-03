package com.cxnet.rpc.service.expense;

import com.cxnet.rpc.domain.expense.ExpBasicControl;

import java.util.List;

public interface ExpBasicControlServiceRpc {
    /**
     * 获取全部控制规则
     *
     * @return
     */
    List<ExpBasicControl> getExpBasicControl();

    /**
     * 根据控制规则编码获取控制规则
     *
     * @param code 控制规则编码
     * @return
     */
    ExpBasicControl getExpBasicControlByCode(String code);
}
