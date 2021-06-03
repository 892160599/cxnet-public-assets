package com.cxnet.asset.astrepairbill.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.astrepairbill.domain.AstRepairBill;
import com.cxnet.asset.astrepairbill.domain.vo.AstRepairBillVO;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.flow.domain.CommonProcessRequest;

/**
 * 资产报修单主表(AstRepairBill)表服务接口
 *
 * @author guks
 * @since
 */
public interface AstRepairBillService extends IService<AstRepairBill> {
    /**
     * 分页查询
     *
     * @param astRepairBill
     * @return
     */
    List<AstRepairBill> selectAll(AstRepairBill astRepairBill);

    /**
     * 查询单条记录
     *
     * @param id
     * @return
     */
    AstRepairBillVO selectOne(String id);

    /**
     * 插入资产领用数据
     *
     * @param astRepairBillVO
     * @return
     */
    AstRepairBillVO insert(AstRepairBillVO astRepairBillVO);

    /**
     * 更新资产领用数据
     *
     * @param astRepairBillVO
     * @return
     */
    AstRepairBillVO update(AstRepairBillVO astRepairBillVO);

    /**
     * 批量删除资产领用数据
     *
     * @param ids
     * @return
     */
    String delete(List<String> ids);


    /**
     * 送审
     *
     * @param commonProcessRequest
     * @return
     */
    String submit(CommonProcessRequest commonProcessRequest);

    /**
     * 审核
     *
     * @param commonProcessRequest
     * @return
     */
    String audit(CommonProcessRequest commonProcessRequest);

    /***
     * 退回
     * @param commonProcessRequest
     * @return
     */
    String back(CommonProcessRequest commonProcessRequest);

    /**
     * 收回
     *
     * @param commonProcessRequest
     * @return
     */
    String taskBack(CommonProcessRequest commonProcessRequest);

    /**
     * 查询领用人
     *
     * @return
     */
    List<Map<String, String>> selectRepairNameAll();


    /**
     * 获取资产卡片列表
     *
     * @param utitId      单位id
     * @param usageStatus 使用状态
     * @return
     */
    List<AstCard> getAstCardList(String utitId, String usageStatus);

}