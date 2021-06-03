package com.cxnet.baseData.expfunc.bdExpfunc.service;

import com.cxnet.baseData.expfunc.bdExpfunc.domain.BdExpfunc;
import com.cxnet.baseData.expfunc.bdExpfunc.domain.SelectionBdExpfunc;
import com.cxnet.common.utils.tree.Zone;

import java.util.List;
import java.util.Map;

/**
 * 支出功能分类Service接口
 *
 * @author cxnet
 * @date 2020-08-17
 */
public interface BdExpfuncServiceI {
    /**
     * 查询支出功能分类
     *
     * @param funcId 支出功能分类ID
     * @return 支出功能分类
     */
    public BdExpfunc selectBdExpfuncById(String funcId);

    /**
     * 查询支出功能分类集合
     *
     * @param bdExpfunc 支出功能分类
     * @return 支出功能分类集合
     */
    public List<BdExpfunc> selectBdExpfuncList(BdExpfunc bdExpfunc);

    /**
     * 查询支出功能分类tree
     *
     * @param bdExpfunc 支出功能分类
     * @return 支出功能分类tree
     */
    public List<Zone> selectBdExpfuncTree(BdExpfunc bdExpfunc);

    /**
     * 新增支出功能分类
     *
     * @param bdExpfunc 支出功能分类
     * @return 结果
     */
    public int insertBdExpfunc(BdExpfunc bdExpfunc);

    /**
     * 批量新增支出功能分类
     *
     * @param bdExpfuncs 支出功能分类
     * @return 结果
     */
    public int insertBatchBdExpfunc(List<BdExpfunc> bdExpfuncs);

    /**
     * 修改支出功能分类
     *
     * @param bdExpfunc 支出功能分类
     * @return 结果
     */
    public int updateBdExpfunc(BdExpfunc bdExpfunc);

    /**
     * 批量修改支出功能分类
     *
     * @param bdExpfuncs 支出功能分类
     * @return 结果
     */
    public int updateBatchBdExpfunc(List<BdExpfunc> bdExpfuncs);

    /**
     * 批量删除支出功能分类
     *
     * @param funcIds 需要删除的支出功能分类ID
     * @return 结果
     */
    public int deleteBdExpfuncByIds(String[] funcIds);

    /**
     * 删除支出功能分类信息
     *
     * @param funcId 支出功能分类ID
     * @return 结果
     */
    public int deleteBdExpfuncById(String funcId);

    /**
     * 单位选用功能分类
     *
     * @param selectionBdExpfunc
     * @return
     */
    List<Zone> insertBatchSelectionBdExpfunc(SelectionBdExpfunc selectionBdExpfunc);

    int insertBatchAssetType(List<BdExpfunc> bdExpfuncs, Long year);

    /**
     * 支出功能分类结转年度计算
     *
     * @return 结果
     */
    Map<String, Integer> carryForwardYear();

    /**
     * 支出功能分类年度结转
     *
     * @return 结果
     */
    void carryForwardData(String year);
}
