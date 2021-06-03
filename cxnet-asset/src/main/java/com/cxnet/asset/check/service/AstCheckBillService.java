package com.cxnet.asset.check.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.check.domain.AstCheckBill;
import com.cxnet.asset.check.domain.vo.AstCheckVo;
import com.cxnet.flow.domain.CommonProcessRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 资产验收主表(AstCheckBill)表服务接口
 *
 * @author caixx
 * @since 2021-03-25 09:39:40
 */
public interface AstCheckBillService extends IService<AstCheckBill> {

    /**
     * 分页查询
     *
     * @param astCheckBill
     * @return
     */
    List<AstCheckBill> selectAll(AstCheckBill astCheckBill);

    AstCheckVo selectOne(String id);

    AstCheckVo insert(AstCheckVo astCheckVo);

    AstCheckVo update(AstCheckVo astCheckVo);

    String delete(List<String> ids);

    /**
     * 生成卡片
     *
     * @param ids
     * @return
     */
    String produce(List<String> ids);

    /**
     * 送审
     *
     * @param commonProcessRequest 流程提交参数对象
     * @return 送审结果
     */
    String submit(CommonProcessRequest commonProcessRequest);

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     * @return 审核结果
     */
    String audit(CommonProcessRequest commonProcessRequest);

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     * @return 退回结果
     */
    String back(CommonProcessRequest commonProcessRequest);

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     * @return 收回结果
     */
    String taskBack(CommonProcessRequest commonProcessRequest);

    /**
     * 查询验收人
     *
     * @return
     */
    List<Map<String, String>> selectCheckPersonAll();

}