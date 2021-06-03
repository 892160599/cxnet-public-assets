package com.cxnet.project.system.rule.service;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.cxnet.common.exception.CustomException;
import com.cxnet.project.system.rule.domain.RuleCol;
import com.cxnet.project.system.rule.domain.RuleNum;
import com.cxnet.project.system.rule.mapper.RuleColMapper;
import com.cxnet.project.system.rule.mapper.RuleNumMapper;
import com.cxnet.rpc.service.sysModel.SysModelServiceIRpc;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cxnet.project.system.rule.domain.Rule;
import com.cxnet.project.system.rule.mapper.RuleMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 编号器Service业务层处理
 *
 * @author caixx
 * @date 2020-07-31
 */
@Service
@Slf4j
public class RuleServiceImpl implements RuleServiceI {
    @Autowired(required = false)
    private RuleMapper ruleMapper;

    @Autowired(required = false)
    private RuleColMapper ruleColMapper;

    @Autowired(required = false)
    private RuleNumMapper ruleNumMapper;

    @Autowired(required = false)
    private SysModelServiceIRpc sysModelServiceIRpc;

    /**
     * 查询编号器
     *
     * @param ruleId 编号器ID
     * @return 编号器
     */
    @Override
    public Rule selectRuleById(String ruleId) {
        List<RuleCol> ruleCols = ruleColMapper.selectRuleColByRuleId(ruleId);
        Rule rule = ruleMapper.selectRuleById(ruleId);
        rule.setRuleCols(ruleCols);
        return rule;
    }

    /**
     * 查询编号器集合
     *
     * @param rule 编号器
     * @return 编号器
     */
    @Override
    public List<Rule> selectRuleList(Rule rule) {
        return ruleMapper.selectRuleList(rule);
    }

    /**
     * 新增编号器
     *
     * @param rule 编号器
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRule(Rule rule) {
        Rule oldRule = ruleMapper.selectRuleByRuleCode(rule.getRuleCode());
        if (oldRule != null) {
            throw new CustomException("编码重复！");
        }
        int i = ruleMapper.insertRule(rule);
        List<RuleCol> ruleCols = rule.getRuleCols();
        if (CollectionUtils.isNotEmpty(ruleCols)) {
            ruleCols.forEach(v -> v.setRuleId(rule.getRuleId()));
            ruleColMapper.insertBatchRuleCol(ruleCols);
        }
        return i;
    }

    /**
     * 批量新增编号器
     *
     * @param rules 编号器
     * @return 结果
     */
    @Override
    public int insertBatchRule(List<Rule> rules) {
        return ruleMapper.insertBatchRule(rules);
    }

    /**
     * 修改编号器
     *
     * @param rule 编号器
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRule(Rule rule) {
        List<RuleCol> ruleCols = rule.getRuleCols();
        int i = ruleMapper.updateRule(rule);
        ruleColMapper.deleteRuleColByRuleIds(new String[]{rule.getRuleId()});
        if (CollectionUtils.isNotEmpty(ruleCols)) {
            ruleCols.forEach(v -> v.setRuleId(rule.getRuleId()));
            ruleColMapper.insertBatchRuleCol(ruleCols);
        }
        return i;
    }

    /**
     * 批量修改编号器
     *
     * @param rules 编号器
     * @return 结果
     */
    @Override
    public int updateBatchRule(List<Rule> rules) {
        return ruleMapper.updateBatchRule(rules);
    }

    /**
     * 批量删除编号器
     *
     * @param ruleIds 需要删除的编号器ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRuleByIds(String[] ruleIds) {
        ruleColMapper.deleteRuleColByRuleIds(ruleIds);
        return ruleMapper.deleteRuleByIds(ruleIds);
    }

    /**
     * 删除编号器信息
     *
     * @param ruleId 编号器ID
     * @return 结果
     */
    @Override
    public int deleteRuleById(String ruleId) {
        return ruleMapper.deleteRuleById(ruleId);
    }

    /**
     * 根据单据编码查询最新编号
     *
     * @param modelCode 单据编码
     * @param map       参与字段map
     * @return 最新编号
     */
    @Override
    public String nextNumberByModelCode(String modelCode, Map<String, Object> map) {
        return nextNumber(sysModelServiceIRpc.getRuleCode(modelCode), map);
    }

    /**
     * 根据单据编码查询最新编号
     *
     * @param modelCode 单据编码
     * @param object    参与字段obj
     * @return 最新编号
     */
    @Override
    public String nextNumberByModelCode(String modelCode, Object object) {
        return nextNumber(sysModelServiceIRpc.getRuleCode(modelCode), object);
    }

    /**
     * 根据单据编码查询最新编号
     *
     * @param modelCode 单据编码
     * @return 最新编号
     */
    @Override
    public String nextNumberByModelCode(String modelCode) {
        return nextNumber(sysModelServiceIRpc.getRuleCode(modelCode));
    }

    /**
     * 根据编号器编码查询最新编号
     *
     * @param ruleCode 编号器编号
     * @return 最新编号
     */
    @Override
    public String nextNumber(String ruleCode) {
        return nextNumber(ruleCode, new HashMap<>());
    }

    /**
     * 根据编号器编码查询最新编号
     *
     * @param ruleCode 编号器编码
     * @param beanMap  自定义编号字段
     * @return 最新编号
     */
    @Override
    public String nextNumber(String ruleCode, Map<String, Object> beanMap) {
        return nextNumber(ruleCode, new HashMap<>(), beanMap);
    }

    /**
     * 根据编号器编码查询最新编号
     *
     * @param ruleCode 编号器编号
     * @param object   参与字段obj（没有传null）
     * @return 最新编号
     */
    @Override
    public synchronized String nextNumber(String ruleCode, Object object) {
        Map<String, String> colType = getColType(object);
        Map<String, Object> beanMap = BeanUtil.beanToMap(object, true, false);
        return nextNumber(ruleCode, colType, beanMap);
    }

    /**
     * 根据编号器编码查询最新编号
     *
     * @param ruleCode 编号器编号
     * @return 最新编号
     */
    private String nextNumber(String ruleCode, Map<String, String> colType, Map<String, Object> beanMap) {
        // 根据编号器编码查询编号器
        Rule rule = ruleMapper.selectRuleByRuleCode(ruleCode);
        if (rule == null) {
            throw new CustomException("未找到编号器:" + ruleCode + "，请联系系统管理员！");
        }
        // 前缀
        StringBuilder prefix = new StringBuilder(StringUtils.isEmpty(rule.getRulePrefix()) ? "" : rule.getRulePrefix());
        // 编号长度
        Long ruleLen = rule.getRuleLen();
        // 有参与字段
        if (rule.getIsJoin() == 0) {
            List<RuleCol> list = ruleColMapper.selectRuleColByRuleId(rule.getRuleId());
            if (CollectionUtils.isEmpty(list)) {
                throw new CustomException("编号器:" + ruleCode + "异常，请联系系统管理员！");
            }
            SimpleDateFormat tueSdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (RuleCol v : list) {
                // 获取拼接字段值
                Object obj = beanMap.get(v.getJoinCol().toLowerCase());
                if (ObjectUtils.isEmpty(obj)) {
                    log.info("编号器参与字段：" + v.getJoinCol() + "未找到！");
                    throw new CustomException("编号器:" + ruleCode + "异常，请联系系统管理员！");
                }
                String value = String.valueOf(obj);
                // 日期格式转换
                String type = colType.get(v.getJoinCol().toLowerCase());
                if ("Date".equals(type) && StringUtils.isNotEmpty(v.getColFormat())) {
                    try {
                        Date parse;
                        if (value.length() > 20) {
                            parse = tueSdf.parse(value);
                        } else {
                            parse = sdf.parse(value);
                        }
                        value = DateUtil.format(parse, v.getColFormat());
                    } catch (ParseException e) {
                        log.info("编号器:" + ruleCode + "异常，请联系系统管理员！");
                        log.error("错误原因:{}", e.getMessage(), e);
                        throw new CustomException("编号器:" + ruleCode + "异常，请联系系统管理员！");
                    }
                }
                // 获取补空位字符
                String bkChar = v.getFillChar();
                // 比较长度 补位
                Long colLen = v.getColLen();
                if (StringUtils.isNotBlank(value) && colLen != null && value.length() < colLen && StringUtils.isNotBlank(bkChar)) {
                    long bkLen = colLen - (long) value.length();
                    StringBuilder bkStr = new StringBuilder();
                    for (int i = 0; i < bkLen; i++) {
                        bkStr.append(bkChar);
                    }
                    if (v.getFillPosition() == 1) { // 前补
                        value = bkStr + value;
                    } else { // 后补
                        value = value + bkStr;
                    }
                }
                prefix.append(value).append(StringUtils.isEmpty(v.getCutChar()) ? "" : v.getCutChar());
            }
        }
        // 查询当前编号
        RuleNum sysCurrentNumber = ruleNumMapper.findByRuleCodeAndMark(ruleCode, prefix.toString());
        if (sysCurrentNumber != null) { // 存在编号+1
            Long currentNumber = sysCurrentNumber.getCurrentNumber() + 1;
            sysCurrentNumber.setCurrentNumber(currentNumber);
            ruleNumMapper.updateRuleNum(sysCurrentNumber);
            return prefix.append(String.format("%0" + ruleLen + "d", currentNumber)).toString();
        } else { //未查询到新增
            RuleNum newRuleNum = new RuleNum();
            newRuleNum.setRuleCode(ruleCode);
            newRuleNum.setMark(prefix.toString());
            newRuleNum.setCurrentNumber(1L);
            ruleNumMapper.insertRuleNum(newRuleNum);
            return prefix.append(String.format("%0" + ruleLen + "d", 1)).toString();
        }
    }

    /**
     * 获取bean 值,类型
     *
     * @param object
     */
    public Map<String, String> getColType(Object object) {
        Method[] methods = object.getClass().getMethods();//取所有的方法
        Map<String, String> map = new HashMap<>(methods.length);
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                String lsName = method.getName().substring(3); // 属性
                String type = method.getReturnType().getName(); //类型
                map.put(StrUtil.toUnderlineCase(lsName), type.substring(type.lastIndexOf(".") + 1));
            }
        }
        return map;
    }
}
