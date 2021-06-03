package com.cxnet.asset.depr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.asset.depr.domain.AstDeprBill;
import com.cxnet.asset.depr.domain.AstDeprList;
import com.cxnet.asset.depr.domain.vo.AstDeprListVo;

import java.util.List;

/**
 * 资产折旧明细表(AstDeprList)表服务接口
 *
 * @author caixx
 * @since 2021-04-08 16:03:13
 */
public interface AstDeprListService extends IService<AstDeprList> {

    /**
     * 查询汇总明细
     *
     * @param id
     * @return
     */
    AstDeprListVo astSummaryList(String id);

    /**
     * 根据主表id查询
     *
     * @param id
     * @return
     */
    List<AstDeprList> getByDeptId(String id);

    /**
     * 分页查询折旧明细
     *
     * @param astDeprBill
     * @return
     */
    List<AstDeprList> selectAll(AstDeprBill astDeprBill);

}