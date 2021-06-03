package com.cxnet.project.system.rule.service;

import com.cxnet.project.system.rule.domain.Rule;

import java.util.List;
import java.util.Map;

/**
 * 编号器Service接口
 *
 * @author caixx
 * @date 2020-07-31
 */
public interface RuleServiceI {
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
     * 批量删除编号器
     *
     * @param ruleIds 需要删除的编号器ID
     * @return 结果
     */
    public int deleteRuleByIds(String[] ruleIds);

    /**
     * 删除编号器信息
     *
     * @param ruleId 编号器ID
     * @return 结果
     */
    public int deleteRuleById(String ruleId);

    /**
     * 根据编号器编码查询最新编号
     *
     * @param ruleCode 编号器编号
     * @return 最新编号
     */
    public String nextNumber(String ruleCode);

    /**
     * 根据编号器编码查询最新编号
     *
     * @param ruleCode 编号器编号
     * @param object   参与字段obj
     * @return 最新编号
     */
    public String nextNumber(String ruleCode, Object object);

    /**
     * 根据编号器编码查询最新编号
     *
     * @param ruleCode 编号器编号
     * @param map      参与字段map
     * @return 最新编号
     */
    public String nextNumber(String ruleCode, Map<String, Object> map);

    /**
     * 根据单据编码查询最新编号
     *
     * @param modelCode 单据编码
     * @param map       参与字段map
     * @return 最新编号
     */
    public String nextNumberByModelCode(String modelCode, Map<String, Object> map);

    /**
     * 根据单据编码查询最新编号
     *
     * @param modelCode 单据编码
     * @param object    参与字段obj
     * @return 最新编号
     */
    public String nextNumberByModelCode(String modelCode, Object object);

    /**
     * 根据单据编码查询最新编号
     *
     * @param modelCode 单据编码
     * @return 最新编号
     */
    public String nextNumberByModelCode(String modelCode);

}
