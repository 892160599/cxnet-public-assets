package com.cxnet.rpc.service;

import com.cxnet.rpc.domain.PurPurchaseIndex;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PurPurchaseIndexServiceRpc {
    /**
     * 根据采购单id查询经费来源
     *
     * @param purId
     * @return
     */
    List<PurPurchaseIndex> selectPurPurchaseIndexById(String purId);


    /**
     * 根据采购单id查询经费来源
     *
     * @param purIds
     * @return
     */
    List<PurPurchaseIndex> selectPurPurchaseIndexById(String[] purIds);


}
