package com.cxnet.common.constant;

/**
 * @program: cxnet-internal-control
 * @description: 系统控制常量信息
 * @author: Mr.Cai
 * @create: 2020-12-18 16:42
 **/
public class SysControlConstants {

    /**
     * 是否允许无申请报销 0:是 2:否
     */
    public static final String IS_ALLOWED_APPLY = "is_allowed_apply";

    /**
     * 经费来源控制规则 1:指标控制 2:用款计划控制
     */
    public static final String FUNDING_SOURCES = "funding_sources";

    /**
     * 前置申请控制来源 1:预算指标 2:用款计划
     */
    public static final String IS_CONTROL_PLAN = "is_control_plan";

    /**
     * 单据退回方式 1:直接退回至经办人 2:逐级退回
     */
    public static final String DOC_BACK_METHOD = "doc_back_method";
    /**
     * 借款单前置申请控制
     * 1：无前置申请 2：有前置申请
     */
    public static final String BORROW_CONTROL_RULE = "borrow_control_rule";
    /**
     * 前置申请占用指标 0:是 2:否
     */
    public static final String BORROW_IS_USE = "borrow_is_use";

}
