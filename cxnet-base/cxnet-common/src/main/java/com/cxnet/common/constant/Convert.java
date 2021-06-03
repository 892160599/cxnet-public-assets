package com.cxnet.common.constant;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 自定义转换注解
 *
 * @author gks
 */
@Documented
@Target({FIELD})
@Retention(RUNTIME)
public @interface Convert {


    String key() default "";

    boolean dictValue() default false;
}
