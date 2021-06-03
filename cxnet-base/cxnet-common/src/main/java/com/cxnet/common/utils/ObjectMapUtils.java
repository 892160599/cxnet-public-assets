package com.cxnet.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectMapUtils {
    /**
     * 将map转换为一个对象
     *
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        // 取得目标实体中的所有字段
        Field[] fields = obj.getClass().getDeclaredFields();
        outer:
        for (Field f : fields) {
            // 获取字段修饰符，如果为final直接跳过
            String mod = Modifier.toString(f.getModifiers());
            if (mod.contains("final")) {
                continue outer;
            }
            // 获取字段名称
            String fieldname = f.getName();
            String value = "";
            if (map.get(fieldname) != null) {
                value = map.get(fieldname).toString();
            } else {
                value = null;
            }

            // 拼接set方法名
            String setMethodStr = parSetName(f.getName());
            // 获取实体类的set方法
            Method setEntity = obj.getClass().getMethod(setMethodStr, f.getType());
            // 获取当前字段的类型
            String fieldType = f.getType().getSimpleName();
            // 给实体赋值
            setField(fieldType, setEntity, obj, value);
        }

        return obj;
    }

    /**
     * 将对象转换为map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) {

        Map<String, Object> map = new HashMap<String, Object>();
        // System.out.println(obj.getClass());
        // 获取f对象对应类中的所有属性域
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
//			varName = varName.toLowerCase();// 将key置为小写，默认为对象的属性
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fields[i].get(obj);
                if (o != null)
                    map.put(varName, o.toString());
                // System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                log.error("错误原因:{}", ex.getMessage(), ex);
            } catch (IllegalAccessException ex) {
                log.error("错误原因:{}", ex.getMessage(), ex);
            }
        }
        return map;
    }

    /**
     * 拼接某属性的set方法
     *
     * @param fieldName
     * @return
     */
    private static String parSetName(String fieldName) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        int startIndex = 0;
        return "set" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
                + fieldName.substring(startIndex + 1);
    }

    /**
     * 根据字段类型不同set实体类中的值
     *
     * @param fieldType
     * @param set
     * @param entity
     * @param value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static <T> void setField(String fieldType, Method set, T entity, String value)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (value == null || "".equals(value)) {
            return;
        } else {
            if ("String".equals(fieldType)) {
                set.invoke(entity, value);
            } else if ("Date".equals(fieldType)) {
                Date temp = new Date();
                set.invoke(entity, temp);
            } else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
                Integer intval = Integer.parseInt(value);
                set.invoke(entity, intval);
            } else if ("Long".equalsIgnoreCase(fieldType)) {
                Long temp = Long.parseLong(value);
                set.invoke(entity, temp);
            } else if ("Double".equalsIgnoreCase(fieldType)) {
                Double temp = Double.parseDouble(value);
                set.invoke(entity, temp);
            } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                Boolean temp = Boolean.parseBoolean(value);
                set.invoke(entity, temp);
            } else {
                return;
            }
        }

    }
}
