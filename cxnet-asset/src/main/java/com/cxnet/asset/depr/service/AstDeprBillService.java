package com.cxnet.asset.depr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.depr.domain.AstDeprBill;
import com.cxnet.asset.depr.domain.vo.AstDeprMoVo;

import java.util.List;
import java.util.Map;

/**
 * 资产折旧主表(AstDeprBill)表服务接口
 *
 * @author caixx
 * @since 2021-04-08 16:03:11
 */
public interface AstDeprBillService extends IService<AstDeprBill> {

    /**
     * 查询年度
     *
     * @return
     */
    List<Integer> getDeprFiscal();

    /**
     * 根据年度查询月份折旧状态
     *
     * @param deprFiscal
     * @return
     */
    List<AstDeprMoVo> getDeprMoByDeprFiscal(Integer deprFiscal);

    /**
     * 查询主表
     *
     * @param astDeprBill
     * @return
     */
    AstDeprBill selectAstDeprByAstDeprBill(AstDeprBill astDeprBill);

    /**
     * 计算
     *
     * @param astDeprBill
     * @return
     */
    String calculation(AstDeprBill astDeprBill);

    /**
     * 确认
     *
     * @param id
     * @return
     */
    String confirm(String id);

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
     * 查询资产折旧记录
     *
     * @param id
     * @return
     */
    List<Map<String, Object>> record(String id);
}