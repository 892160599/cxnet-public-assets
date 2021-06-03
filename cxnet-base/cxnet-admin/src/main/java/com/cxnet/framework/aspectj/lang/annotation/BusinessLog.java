package com.cxnet.framework.aspectj.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.aspectj.lang.enums.OperatorType;

/**
 * 具体业务操作日志
 *
 * @author cxnet
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessLog {


    /**
     * 模块名称
     */
    String module() default "";

    /**
     * 操作内容
     */
    String value() default "";

    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 操作主表
     */
    String operatorTable() default "";

    /**
     * 是否记录
     */
    boolean isRecordKey() default false;

    /**
     * 被修改的实体的唯一标识,例如:用户实体的唯一标识为"id"
     */
    String key() default "id";

    /**
     * 是否批量修改
     */
    boolean isBatch() default false;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default false;


}
