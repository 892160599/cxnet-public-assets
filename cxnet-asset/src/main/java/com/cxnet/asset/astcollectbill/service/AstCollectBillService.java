package com.cxnet.asset.astcollectbill.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.astcollectbill.domain.AstCollectBill;
import com.cxnet.asset.astcollectbill.domain.vo.AstCollectBillVO;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.flow.domain.CommonProcessRequest;

/**
 * 资产领用单主表(AstCollectBill)表服务接口
 *
 * @author guks
 * @since 2021-04-12 10:52:09
 */
public interface AstCollectBillService extends IService<AstCollectBill> {

    /**
     * 分页查询
     *
     * @param astCollectBill
     * @return
     */
    List<AstCollectBill> selectAll(AstCollectBill astCollectBill);

    /**
     * 查询单条记录
     *
     * @param id 主键
     * @return
     */
    AstCollectBillVO selectOne(String id);

    /**
     * 插入资产领用数据
     *
     * @param astCollectBillVO 资产领用显示对象
     * @return
     */
    AstCollectBillVO insert(AstCollectBillVO astCollectBillVO);

    /**
     * 更新资产领用数据
     *
     * @param astCollectBillVO 资产领用显示对象
     * @return
     */
    AstCollectBillVO update(AstCollectBillVO astCollectBillVO);

    /**
     * 批量删除资产领用数据
     *
     * @param ids id集合
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
    List<Map<String, String>> selectEmpNameAll();


    /**
     * 获取资产卡片列表
     *
     * @param utitId      单位id
     * @param usageStatus 使用状态
     * @return
     */
    List<AstCard> getAstCardList(String utitId, String usageStatus);

}