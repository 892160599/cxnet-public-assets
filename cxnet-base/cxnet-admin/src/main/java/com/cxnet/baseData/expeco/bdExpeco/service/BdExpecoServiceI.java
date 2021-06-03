package com.cxnet.baseData.expeco.bdExpeco.service;

import com.cxnet.baseData.expeco.bdExpeco.domain.BdExpeco;
import com.cxnet.baseData.expeco.bdExpeco.domain.SelectionBdExpeco;
import com.cxnet.common.utils.tree.Zone;

import java.util.List;
import java.util.Map;

/**
 * 部门预算经济分类Service接口
 *
 * @author cxnet
 * @date 2020-08-17
 */
public interface BdExpecoServiceI {
    /**
     * 查询部门预算经济分类
     *
     * @param expecoId 部门预算经济分类ID
     * @return 部门预算经济分类
     */
    public BdExpeco selectBdExpecoById(String expecoId);

    /**
     * 查询部门预算经济分类集合
     *
     * @param bdExpeco 部门预算经济分类
     * @return 部门预算经济分类集合
     */
    public List<BdExpeco> selectBdExpecoList(BdExpeco bdExpeco);

    /**
     * 新增部门预算经济分类
     *
     * @param bdExpeco 部门预算经济分类
     * @return 结果
     */
    public int insertBdExpeco(BdExpeco bdExpeco);

    /**
     * 批量新增部门预算经济分类
     *
     * @param bdExpecos 部门预算经济分类
     * @return 结果
     */
    public int insertBatchBdExpeco(List<BdExpeco> bdExpecos, Long year);

    /**
     * 修改部门预算经济分类
     *
     * @param bdExpeco 部门预算经济分类
     * @return 结果
     */
    public int updateBdExpeco(BdExpeco bdExpeco);

    /**
     * 批量修改部门预算经济分类
     *
     * @param bdExpecos 部门预算经济分类
     * @return 结果
     */
    public int updateBatchBdExpeco(List<BdExpeco> bdExpecos);

    /**
     * 批量删除部门预算经济分类
     *
     * @param expecoIds 需要删除的部门预算经济分类ID
     * @return 结果
     */
    public int deleteBdExpecoByIds(String[] expecoIds);

    /**
     * 删除部门预算经济分类信息
     *
     * @param expecoId 部门预算经济分类ID
     * @return 结果
     */
    public int deleteBdExpecoById(String expecoId);

    /**
     * 查询部门预算经济分类tree
     *
     * @param bdExpeco
     * @return
     */
    List<Zone> selectBdExpecoListTree(BdExpeco bdExpeco);

    /**
     * 单位选用预算经济分类
     *
     * @param selectionBdExpeco
     * @return
     */
    List<Zone> insertBatchSelectionBdExpeco(SelectionBdExpeco selectionBdExpeco);

    Map<String, Integer> carryForwardYear();

    void carryForwardData(String year);
}
