package com.cxnet.baseData.govexpeco.bdGovexpeco.service;

import com.cxnet.baseData.govexpeco.bdGovexpeco.domain.BdGovexpeco;
import com.cxnet.common.utils.tree.Zone;

import java.util.List;
import java.util.Map;

/**
 * 政府预算经济分类Service接口
 *
 * @author cxnet
 * @date 2020-08-17
 */
public interface BdGovexpecoServiceI {
    /**
     * 查询政府预算经济分类
     *
     * @param govId 政府预算经济分类ID
     * @return 政府预算经济分类
     */
    public BdGovexpeco selectBdGovexpecoById(String govId);

    /**
     * 查询政府预算经济分类集合
     *
     * @param bdGovexpeco 政府预算经济分类
     * @return 政府预算经济分类集合
     */
    public List<BdGovexpeco> selectBdGovexpecoList(BdGovexpeco bdGovexpeco);

    /**
     * 新增政府预算经济分类
     *
     * @param bdGovexpeco 政府预算经济分类
     * @return 结果
     */
    public int insertBdGovexpeco(BdGovexpeco bdGovexpeco);

    /**
     * 批量新增政府预算经济分类
     *
     * @param bdGovexpecos 政府预算经济分类
     * @return 结果
     */
    public int insertBatchBdGovexpeco(List<BdGovexpeco> bdGovexpecos, Long year);

    /**
     * 修改政府预算经济分类
     *
     * @param bdGovexpeco 政府预算经济分类
     * @return 结果
     */
    public int updateBdGovexpeco(BdGovexpeco bdGovexpeco);

    /**
     * 批量修改政府预算经济分类
     *
     * @param bdGovexpecos 政府预算经济分类
     * @return 结果
     */
    public int updateBatchBdGovexpeco(List<BdGovexpeco> bdGovexpecos);

    /**
     * 批量删除政府预算经济分类
     *
     * @param govIds 需要删除的政府预算经济分类ID
     * @return 结果
     */
    public int deleteBdGovexpecoByIds(String[] govIds);

    /**
     * 删除政府预算经济分类信息
     *
     * @param govId 政府预算经济分类ID
     * @return 结果
     */
    public int deleteBdGovexpecoById(String govId);

    /**
     * 查询政府预算经济分类Tree
     *
     * @param bdGovexpeco
     * @return
     */
    List<Zone> selectBdGovexpecoListTree(BdGovexpeco bdGovexpeco);

    Map<String, Integer> carryForwardYear();

    void carryForwardData(String year);
}
