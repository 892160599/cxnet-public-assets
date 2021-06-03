package com.cxnet.project.system.rule.mapper;

import com.cxnet.project.system.rule.domain.RuleCol;

import java.util.List;
import java.util.Map;

/**
 * 编号器子Mapper接口
 *
 * @author caixx
 * @date 2020-07-31
 */
public interface RuleColMapper {
    /**
     * 查询编号器子
     *
     * @param id 编号器子ID
     * @return 编号器子
     */
    public RuleCol selectRuleColById(String id);

    /**
     * 查询编号器子集合
     *
     * @param ruleCol 编号器子
     * @return 编号器子集合
     */
    public List<RuleCol> selectRuleColList(RuleCol ruleCol);

    /**
     * 新增编号器子
     *
     * @param ruleCol 编号器子
     * @return 结果
     */
    public int insertRuleCol(RuleCol ruleCol);

    /**
     * 批量新增编号器子
     *
     * @param ruleCols 编号器子
     * @return 结果
     */
    public int insertBatchRuleCol(List<RuleCol> ruleCols);

    /**
     * 修改编号器子
     *
     * @param ruleCol 编号器子
     * @return 结果
     */
    public int updateRuleCol(RuleCol ruleCol);

    /**
     * 批量修改编号器子
     *
     * @param ruleCols 编号器子
     * @return 结果
     */
    public int updateBatchRuleCol(List<RuleCol> ruleCols);

    /**
     * 删除编号器子
     *
     * @param id 编号器子ID
     * @return 结果
     */
    public int deleteRuleColById(String id);

    /**
     * 批量删除编号器子
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRuleColByIds(String[] ids);

    /**
     * 根据编号器id查询
     *
     * @param ruleId
     * @return
     */
    List<RuleCol> selectRuleColByRuleId(String ruleId);

    /**
     * 根据编号器id删除
     *
     * @param ruleIds
     * @return
     */
    int deleteRuleColByRuleIds(String[] ruleIds);

}