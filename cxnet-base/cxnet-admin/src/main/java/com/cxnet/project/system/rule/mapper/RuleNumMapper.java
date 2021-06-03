package com.cxnet.project.system.rule.mapper;

import com.cxnet.project.system.rule.domain.RuleNum;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 当前编号Mapper接口
 *
 * @author caixx
 * @date 2020-07-31
 */
public interface RuleNumMapper {
    /**
     * 查询当前编号
     *
     * @param id 当前编号ID
     * @return 当前编号
     */
    public RuleNum selectRuleNumById(String id);

    /**
     * 查询当前编号集合
     *
     * @param ruleNum 当前编号
     * @return 当前编号集合
     */
    public List<RuleNum> selectRuleNumList(RuleNum ruleNum);

    /**
     * 新增当前编号
     *
     * @param ruleNum 当前编号
     * @return 结果
     */
    public int insertRuleNum(RuleNum ruleNum);

    /**
     * 批量新增当前编号
     *
     * @param ruleNums 当前编号
     * @return 结果
     */
    public int insertBatchRuleNum(List<RuleNum> ruleNums);

    /**
     * 修改当前编号
     *
     * @param ruleNum 当前编号
     * @return 结果
     */
    public int updateRuleNum(RuleNum ruleNum);

    /**
     * 批量修改当前编号
     *
     * @param ruleNums 当前编号
     * @return 结果
     */
    public int updateBatchRuleNum(List<RuleNum> ruleNums);

    /**
     * 删除当前编号
     *
     * @param id 当前编号ID
     * @return 结果
     */
    public int deleteRuleNumById(String id);

    /**
     * 批量删除当前编号
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRuleNumByIds(String[] ids);

    /**
     * 根据编码和标记字段查询
     *
     * @param ruleCode 编号器编码
     * @param mark
     * @return 结果
     */
    RuleNum findByRuleCodeAndMark(@Param("ruleCode") String ruleCode, @Param("mark") String mark);

}
