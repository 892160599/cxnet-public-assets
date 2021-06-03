package com.cxnet.asset.depr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.depr.domain.AstDeprRepairBill;
import com.cxnet.asset.depr.domain.AstDeprRepairList;
import com.cxnet.asset.depr.domain.vo.AstDeprRepairVo;

import java.util.List;

/**
 * 资产补计提折旧主表(AstDeprRepairBill)表服务接口
 *
 * @author caixx
 * @since 2021-04-16 18:07:51
 */
public interface AstDeprRepairBillService extends IService<AstDeprRepairBill> {

    /**
     * 查询主表详情
     *
     * @param fiscal
     * @param deprMo
     * @return
     */
    AstDeprRepairBill getAstDeprRepairBill(Integer fiscal, Integer deprMo);

    /**
     * 选择资产明细
     *
     * @param astDeprRepairBill
     * @return
     */
    List<AstDeprRepairList> selectAstDetails(AstDeprRepairBill astDeprRepairBill);

    /**
     * 取消确认
     *
     * @param id
     * @return
     */
    String cancelConfirm(String id);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    String deleteById(String id);

    /**
     * 确认
     *
     * @param astDeprRepairVo
     * @return
     */
    String confirm(AstDeprRepairVo astDeprRepairVo);
}