package com.cxnet.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/***概述：驼峰keyMap
 * 功能：将带"_"的key转化为驼峰命名的key
 * 作者：shl
 * 创建时间：2020/7/21 13:37
 */
public class CamelKeyMap<K, V> extends HashMap<K, V> {

    //重写put方法，方便注入的时候转化key值
    @Override
    public V put(K key, V value) {

        //将带"_"的key转化为驼峰命名的key
        if (key instanceof String) {
            String name = String.valueOf(key);
            if (null != name && !name.isEmpty() && name.contains("_")) {
                //包含下划线，下划线转驼峰
                key = (K) underscoreToCamel(name);
            }
        }
        //统一校验特殊的value,不能为""
        if (value instanceof String) {
            String tempValue = String.valueOf(value);
            if (StringUtils.isBlank(tempValue) || "null".equals(tempValue)) {
                //包含下划线，下划线转驼峰
                value = null;
            }
        }

        return super.put(key, value);
    }

    //存不需要转化的key
    public V putNomalKey(K key, V value) {
        return super.put(key, value);
    }

    //驼峰转化下划线，并将字母大写
    private String underscoreToCamel(String key) {
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String camels[] = key.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();

    }
}