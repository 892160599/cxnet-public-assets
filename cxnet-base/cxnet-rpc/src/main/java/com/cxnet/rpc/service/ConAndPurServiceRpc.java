package com.cxnet.rpc.service;

import com.cxnet.rpc.domain.PurPurchase;

import java.util.List;

/**
 * 合同选择采购计划备案服务接口
 *
 * @author rw
 * @since 2020-10-28 10:41:41
 */
public interface ConAndPurServiceRpc {

    List<PurPurchase> conSelectPur(PurPurchase purPurchase);

    /**
     * 根据采购编号获取采购单
     *
     * @param purId
     * @return
     */
    PurPurchase conSelectPurByPurId(String purId);


    PurPurchase selectPurByPurId(String purId);

    /**
     * 获取采购单据
     *
     * @param purPurchase
     * @param type        contract:合同查看
     * @return
     */
    List<PurPurchase> getNoContractPur(PurPurchase purPurchase, String type);
}