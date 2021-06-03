package com.cxnet.rpc.service;

import com.cxnet.rpc.domain.PurPurchaseItem;

import java.util.List;

/**
 * 采购明细服务接口
 *
 * @since 2021-03-17 17:17:20
 */
public interface PurPurchaseItemServiceRpc {
    /**
     * 批量添加采购明细
     *
     * @param purchaseItemList
     */
    void batchInsertPurPurchaseItem(List<PurPurchaseItem> purchaseItemList);

    /**
     * 根据purId删除采购明细
     *
     * @param purId
     */
    void batchDelPurPurchaseItem(String purId);

    /**
     * 根据purId查询采购明细
     *
     * @param purId
     * @return
     */
    List<PurPurchaseItem> selectPurPurchaseItemByPurId(String purId);
}
