package com.cxnet.asset.astsurrenderbill.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.astsurrenderbill.domain.AstSurrenderBill;
import com.cxnet.asset.astsurrenderbill.domain.vo.AstSurrenderBillVO;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.flow.domain.CommonProcessRequest;

/**
 * 资产交回单主表(AstSurrenderBill)表服务接口
 *
 * @author guks
 * @since 2021-04-15 11:57:50
 */
public interface AstSurrenderBillService extends IService<AstSurrenderBill> {

    /**
     * 分页查询
     *
     * @param astSurrenderBill
     * @return
     */
    List<AstSurrenderBill> selectAll(AstSurrenderBill astSurrenderBill);

    /**
     * 查询单条记录
     *
     * @param id
     * @return
     */
    AstSurrenderBillVO selectOne(String id);

    /**
     * 插入资产交回数据
     *
     * @param astSurrenderBillVO
     * @return
     */
    AstSurrenderBillVO insert(AstSurrenderBillVO astSurrenderBillVO);

    /**
     * 更新资产交回数据
     *
     * @param astSurrenderBillVO
     * @return
     */
    AstSurrenderBillVO update(AstSurrenderBillVO astSurrenderBillVO);

    /**
     * 批量删除资产交回数据
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
     * 查询经办人
     *
     * @return
     */
    List<Map<String, String>> selectAgentAll();


    /**
     * 获取资产卡片列表
     *
     * @param utitId      单位id
     * @param usageStatus 使用状态
     * @return
     */
    List<AstCard> getAstCardList(String utitId, String usageStatus);
}