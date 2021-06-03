package com.cxnet.rpc.service;


import com.cxnet.rpc.domain.BugBillBalance;

import java.math.BigDecimal;
import java.util.List;

/**
 * 指标rpc
 *
 * @Auther: Administrator
 * @Date: 2020-10-28 15:47
 * @Description:
 */
public interface BugBillBalanceServiceRpcI {


    /**
     * 新增占用金额
     *
     * @param modelCode 单据类型
     * @param addAmt    占用金额
     * @param balId     指标余额id
     * @return BugBillBalance
     */
    public void addAmt(String modelCode, BigDecimal addAmt, String balId);


    /**
     * 增加已用金额 并减去余额
     *
     * @param modelCode 单据类型
     * @param addAmt    占用金额
     * @param balId     指标余额id
     * @return BugBillBalance
     */
    void addUsedAmt(String modelCode, BigDecimal addAmt, String balId);

    /**
     * 修改占用金额
     *
     * @param modelCode 单据类型
     * @param oldAmt    原占用金额
     * @param newAmt    新占用金额
     * @param balId     指标余额id
     * @return BugBillBalance
     */
    public void updateAmt(String modelCode, BigDecimal oldAmt, BigDecimal newAmt, String balId);

    /**
     * 删除操作，删除占用金额
     *
     * @param modelCode 单据类型
     * @param delAmt    占用金额
     * @param balId     指标余额id
     * @return
     */
    public void delAmt(String modelCode, BigDecimal delAmt, String balId);

    /**
     * 删除已用金额
     *
     * @param modelCode 单据类型
     * @param delAmt    使用金额
     * @param balId     指标余额id
     */
    void delUsedAmt(String modelCode, BigDecimal delAmt, String balId);


    /**
     * 终审操作
     *
     * @param modelCode 单据类型
     * @param auditAmt  终审金额
     * @param balId     指标余额id
     */
    public void auditAmt(String modelCode, BigDecimal auditAmt, String balId);

    /**
     * 销审操作
     *
     * @param modelCode 单据类型
     * @param cancelAmt 销审金额
     * @param balId     指标余额id
     */
    public void cancelAmt(String modelCode, BigDecimal cancelAmt, String balId);

    /**
     * 办结操作
     *
     * @param modelCode         单据类型
     * @param fundSettlementAmt 办结金额
     * @param balId             指标余额id
     */
    public void fundSettlementAmt(String modelCode, BigDecimal fundSettlementAmt, String balId);

    /**
     * 根据id获取预算指标余额
     *
     * @param balId 指标余额主键
     * @return
     */
    BugBillBalance getBugBillBalanceById(String balId);

    /**
     * 直接修改预算指标可用余额
     *
     * @param useAmt 本次使用金额
     * @param balId  指标余额id
     * @param type   类型
     */
    void updateBugBillBalAmt(BigDecimal useAmt, String balId, String type);


    /**
     * 查询可用指标
     *
     * @param bugBillBalance
     * @return
     */
    List<BugBillBalance> billBalanceList(BugBillBalance bugBillBalance);

}
