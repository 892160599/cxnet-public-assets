package com.cxnet.rpc.service.expense;

import com.cxnet.rpc.domain.SysDbYb;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2020-12-26 10:57
 * @Description:
 */
public interface ExpenseServiceRpc {

    /**
     * 查询报销分摊待办
     *
     * @return
     */
    List<SysDbYb> getExpShareDb();

    /**
     * 查询报销分摊已办
     *
     * @return
     */
    List<SysDbYb> getExpShareYb();
}
