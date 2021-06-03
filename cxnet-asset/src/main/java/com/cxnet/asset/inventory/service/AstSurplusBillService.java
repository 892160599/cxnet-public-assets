package com.cxnet.asset.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.check.domain.AstCheckBill;
import com.cxnet.asset.inventory.domain.AstSurplusBill;
import com.cxnet.asset.inventory.domain.vo.AstSurplusBillVo;
import com.cxnet.flow.domain.CommonProcessRequest;

import java.util.List;

/**
 * 资产盘盈主表(AstSurplusBill)表服务接口
 *
 * @author zhangyl
 * @since 2021-04-19 14:14:43
 */
public interface AstSurplusBillService extends IService<AstSurplusBill> {

    AstSurplusBillVo insertSurplus(AstSurplusBillVo astSurplusBillVo);

    AstSurplusBillVo getList(String id);

    AstSurplusBillVo updateSurplus(AstSurplusBillVo astSurplusBillVo);

    int deleteSurplus(List<String> ids);

    int insertCard(String id);

    /**
     * 分页查询
     *
     * @param astSurplusBill
     * @return
     */
    List<AstSurplusBill> selectAll(AstSurplusBill astSurplusBill);

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

}

