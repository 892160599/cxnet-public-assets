package com.cxnet.rpc.service.budget;

import com.cxnet.rpc.domain.BugBillBalance;
import com.cxnet.rpc.domain.budget.BugPlanRpc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface BugPlanRpcService {
    /**
     * 生成用款计划
     *
     * @param balance   指标
     * @param planMoney 计划金额
     * @return
     */
    HashMap<String, String> createBugPlan(BugBillBalance balance, BigDecimal planMoney);

    /**
     * 新增占用金额
     *
     * @param modelCode 单据类型
     * @param addAmt    占用金额
     * @param planId    用款计划编号
     */
    void addPlanAmt(String modelCode, BigDecimal addAmt, String planId);

    /**
     * 增加已用金额
     *
     * @param modelCode 单据类型
     * @param addAmt    占用金额
     * @param planId    用款计划编号
     */
    void addUsedPlanAmt(String modelCode, BigDecimal addAmt, String planId);

    /**
     * 修改占用金额
     *
     * @param modelCode 单据类型
     * @param oldAmt    原占有金额
     * @param newAmt    现占有金额
     * @param planId    用款计划编号
     */
    void updatePlanAmt(String modelCode, BigDecimal oldAmt, BigDecimal newAmt, String planId);

    /**
     * 删除占用金额
     *
     * @param modelCode 单据类型
     * @param delAmt    删除金额
     * @param planId    用款计划编号
     */
    void deletePlanAmt(String modelCode, BigDecimal delAmt, String planId);

    /**
     * 删除已用金额
     *
     * @param modelCode 单据类型
     * @param delAmt    删除金额
     * @param planId    用款计划编号
     */
    void deleteUsedPlanAmt(String modelCode, BigDecimal delAmt, String planId);

    /**
     * 终审占用金额
     *
     * @param modelCode 单据类型
     * @param auditAmt  审核金额
     * @param planId    用款计划编号
     */
    void auditPlanAmt(String modelCode, BigDecimal auditAmt, String planId);

    /**
     * 办结还原金额
     *
     * @param modelCode         单据类型
     * @param fundSettlementAmt 办结金额
     * @param planId            用款计划编号
     */
    void fundSettlementAmt(String modelCode, BigDecimal fundSettlementAmt, String planId);


    /**
     * 直接修改用款计划金额
     *
     * @param useAmt 本次使用金额
     * @param planId 用款计划Id
     * @param type   类型
     */
    void updatePlanAmt(BigDecimal useAmt, String planId, String type);


    /**
     * 占用用款计划
     *
     * @param oldAmt 原余额
     * @param newAmt 申请金额
     * @param planId 用款计划ID
     * @return
     */
    void updateBugPlanBalance(BigDecimal oldAmt, BigDecimal newAmt, String planId);

    /**
     * 根据id查找用款计划
     *
     * @param planId
     * @return
     */
    BugPlanRpc selectBugPlanByPlanId(String planId);

    /**
     * 修改用款计划
     *
     * @param bugPlanRpc
     */
    void updateBugPlan(BugPlanRpc bugPlanRpc);


    /**
     * 查询用款计划集合
     *
     * @param bugPlanRpc
     * @return
     */
    List<BugPlanRpc> listBugPlanRpc(BugPlanRpc bugPlanRpc);

}
