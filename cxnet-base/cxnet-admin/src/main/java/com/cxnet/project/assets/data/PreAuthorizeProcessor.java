package com.cxnet.project.assets.data;

import com.cxnet.common.utils.spring.SpringUtils;
import com.cxnet.project.system.menu.mapper.SysMenuMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * 权限标签解析
 */
@Component
public class PreAuthorizeProcessor implements BeanPostProcessor {

    public static SysMenuMapper sysMenuMapper = SpringUtils.getBean(SysMenuMapper.class);

    public static List<String> perms = sysMenuMapper.selectMenuPerms();

    public static HashMap<String, Object> preAuthorizeMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods != null) {
            for (Method method : methods) {
                PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(method, PreAuthorize.class);
                if (null != preAuthorize) {
                    String str = preAuthorize.value();
                    String val = str.substring(str.indexOf('\'') + 1, str.lastIndexOf('\''));
                    if (!perms.contains(val)) {
                        preAuthorizeMap.put(val, val);
                    }
                }
            }
        }
        return bean;
    }
}