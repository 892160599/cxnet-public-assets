package com.cxnet.rpc.service.expense;

import com.cxnet.rpc.domain.expense.ExpApplyBorrowListVo;

import java.util.List;

public interface ExpApplyBorrowRpcService {
    /**
     * 根据id查询借款单
     *
     * @param 对应借款的fromId
     * @return
     */
    List<ExpApplyBorrowListVo> selectExpApplyBorrowListVoByPurId(String fromId);
}
