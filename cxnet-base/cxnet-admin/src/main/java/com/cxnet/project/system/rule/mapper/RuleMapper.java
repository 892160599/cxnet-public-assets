package com.cxnet.project.system.rule.mapper;

import com.cxnet.project.system.rule.domain.Rule;

import java.util.List;
import java.util.Map;

/**
 * 编号器Mapper接口
 *
 * @author caixx
 * @date 2020-07-31
 */
public interface RuleMapper {
    /**
     * 查询编号器
     *
     * @param ruleId 编号器ID
     * @return 编号器
     */
    public Rule selectRuleById(String ruleId);

    /**
     * 查询编号器集合
     *
     * @param rule 编号器
     * @return 编号器集合
     */
    public List<Rule> selectRuleList(Rule rule);

    /**
     * 新增编号器
     *
     * @param rule 编号器
     * @return 结果
     */
    public int insertRule(Rule rule);

    /**
     * 批量新增编号器
     *
     * @param rules 编号器
     * @return 结果
     */
    public int insertBatchRule(List<Rule> rules);

    /**
     * 修改编号器
     *
     * @param rule 编号器
     * @return 结果
     */
    public int updateRule(Rule rule);

    /**
     * 批量修改编号器
     *
     * @param rules 编号器
     * @return 结果
     */
    public int updateBatchRule(List<Rule> rules);

    /**
     * 删除编号器
     *
     * @param ruleId 编号器ID
     * @return 结果
     */
    public int deleteRuleById(String ruleId);

    /**
     * 批量删除编号器
     *
     * @param ruleIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteRuleByIds(String[] ruleIds);

    /**
     * 根据编号查询编号器
     *
     * @param ruleCode
     * @return
     */
    Rule selectRuleByRuleCode(String ruleCode);
}
