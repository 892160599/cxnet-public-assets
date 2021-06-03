package com.cxnet.common.utils.tree;

import com.cxnet.common.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @program: cxnet
 * @description:
 * @author: Mr.Cai
 * @create: 2020-07-21 11:49
 **/
@Slf4j
public class ZoneUtils {

    /**
     * 根据pid构建tree结构
     *
     * @param zoneList
     * @return
     */
    public static List<Zone> buildTree(List<Zone> zoneList) {
        zoneList = zoneList.stream().filter(v -> Objects.nonNull(v) && StringUtils.isNotEmpty(v.getId()) && !v.getId().equals(v.getPid())).collect(Collectors.toList());
        Map<String, List<Zone>> zoneByParentIdMap = zoneList.stream().collect(Collectors.groupingBy(Zone::getPid));
        zoneList.forEach(zone -> zone.setChildren(zoneByParentIdMap.get(zone.getId())));
        return zoneList.stream().filter(v -> "0".equals(v.getPid()) || StringUtils.isEmpty(v.getPid())).collect(Collectors.toList());
    }

    /**
     * 根据pcode构建tree结构
     *
     * @param zoneList
     * @return
     */
    public static List<Zone> buildTreeBypcode(List<Zone> zoneList) {
        zoneList = zoneList.stream().filter(v -> Objects.nonNull(v) && StringUtils.isNotEmpty(v.getCode()) && !v.getCode().equals(v.getPCode())).collect(Collectors.toList());
        Map<String, List<Zone>> zoneByParentIdMap = zoneList.stream().collect(Collectors.groupingBy(Zone::getPCode));
        zoneList.forEach(zone -> zone.setChildren(zoneByParentIdMap.get(zone.getCode())));
        return zoneList.stream().filter(v -> "0".equals(v.getPCode()) || StringUtils.isEmpty(v.getPCode())).collect(Collectors.toList());

    }

    /**
     * 指定父级元素根据pid构建tree结构
     *
     * @return
     */
    public static List<Zone> buildTreeByTopVal(List<Zone> zoneList, String topVal) {
        if (StringUtils.isEmpty(topVal)) {
            return zoneList;
        }
        zoneList = zoneList.stream().filter(v -> Objects.nonNull(v) && StringUtils.isNotEmpty(v.getId()) && !v.getId().equals(v.getPid())).collect(Collectors.toList());
        Map<String, List<Zone>> zoneByParentIdMap = zoneList.stream().collect(Collectors.groupingBy(Zone::getPid));
        zoneList.forEach(zone -> zone.setChildren(zoneByParentIdMap.get(zone.getId())));
        return zoneList.stream().filter(v -> topVal.equals(v.getPid())).collect(Collectors.toList());
    }


    /**
     * 指定元素根据id构建tree结构(包含父节点)
     *
     * @return
     */
    public static List<Zone> buildTreeById(List<Zone> zoneList, String id) {
        if (StringUtils.isEmpty(id)) {
            return zoneList;
        }
        zoneList = zoneList.stream().filter(v -> Objects.nonNull(v) && StringUtils.isNotEmpty(v.getId()) && !v.getId().equals(v.getPid())).collect(Collectors.toList());
        Map<String, List<Zone>> zoneByParentIdMap = zoneList.stream().collect(Collectors.groupingBy(Zone::getPid));
        zoneList.forEach(zone -> zone.setChildren(zoneByParentIdMap.get(zone.getId())));
        return zoneList.stream().filter(v -> id.equals(v.getId())).collect(Collectors.toList());
    }

    /**
     * List<T> ==> List<Zone>
     *
     * @param list
     * @param id    指定字段名称
     * @param code
     * @param name
     * @param pid
     * @param pCode
     * @param label
     * @param type
     * @return List<Zone>
     */
    public static List<Zone> buildZone(List list, String id, String code, String name, String pid, String pCode, String label, String type) {
        List<Zone> zoneList = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            Object v = list.get(j);
            Zone zone = new Zone();
            Field[] fields = v.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    String fieldsName = fields[i].getName();
                    String s = String.valueOf(fields[i].get(v));
                    if ("null".equals(s)) {
                        s = "";
                    }
                    if (fieldsName.equals(id)) {
                        zone.setId(s);
                    }
                    if (fieldsName.equals(code)) {
                        zone.setCode(s);
                    }
                    if (fieldsName.equals(name)) {
                        zone.setName(s);
                    }
                    if (fieldsName.equals(pid)) {
                        zone.setPid(s);
                    }
                    if (fieldsName.equals(pCode)) {
                        zone.setPCode(s);
                    }
                    if (fieldsName.equals(label)) {
                        zone.setLabel(s);
                    }
                    if (fieldsName.equals(type)) {
                        zone.setType(s);
                    }
                } catch (IllegalAccessException e) {
                    log.error("错误原因:{}", e.getMessage(), e);
                }
            }
            zoneList.add(zone);
        }
        return zoneList;
    }


}
