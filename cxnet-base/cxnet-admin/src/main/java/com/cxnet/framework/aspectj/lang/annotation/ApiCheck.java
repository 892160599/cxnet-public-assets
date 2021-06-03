package com.cxnet.framework.aspectj.lang.annotation;

import com.cxnet.framework.aspectj.lang.enums.ServiceName;

import java.lang.annotation.*;

/**
 * api接口校验
 *
 * @author cxnet
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiCheck {

    /**
     * 系统标识
     */
    public ServiceName serviceName() default ServiceName.JSY;

}
