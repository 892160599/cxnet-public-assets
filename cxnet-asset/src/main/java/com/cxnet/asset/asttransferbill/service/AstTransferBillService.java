package com.cxnet.asset.asttransferbill.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.asttransferbill.domain.AstTransferBill;
import com.cxnet.asset.asttransferbill.domain.vo.AstTransferBillVO;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.flow.domain.CommonProcessRequest;

/**
 * 资产移交单主表(AstTransferBill)表服务接口
 *
 * @author guks
 * @since 2021-04-15 11:58:21
 */
public interface AstTransferBillService extends IService<AstTransferBill> {

    /**
     * 分页查询
     *
     * @param astTransferBill
     * @return
     */
    List<AstTransferBill> selectAll(AstTransferBill astTransferBill);

    /**
     * 查询单条记录
     *
     * @param id
     * @return
     */
    AstTransferBillVO selectOne(String id);

    /**
     * 插入资产领用数据
     *
     * @param astTransferBillVO
     * @return
     */
    AstTransferBillVO insert(AstTransferBillVO astTransferBillVO);

    /**
     * 更新资产领用数据
     *
     * @param astTransferBillVO
     * @return
     */
    AstTransferBillVO update(AstTransferBillVO astTransferBillVO);

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
    List<Map<String, String>> selecteTransferNameAll();


    /**
     * 获取资产卡片列表
     *
     * @param utitId      单位id
     * @param usageStatus 使用状态
     * @return
     */
    List<AstCard> getAstCardList(String utitId, String usageStatus);
}