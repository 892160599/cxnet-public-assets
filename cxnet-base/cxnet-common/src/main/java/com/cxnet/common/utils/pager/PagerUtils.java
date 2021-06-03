package com.cxnet.common.utils.pager;

import cn.hutool.core.collection.CollectionUtil;
import com.cxnet.common.exception.CustomException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cxnet
 * @date 2021/4/26
 */
public class PagerUtils<T> {

    /**
     * 分页
     *
     * @param list     集合
     * @param pageNum  第几页
     * @param pageSize 每页显示多少条
     * @return 结果集
     */
    public Map<String, Object> dataPager(List<T> list, Integer pageNum, Integer pageSize) {
        if (pageNum == 0 || pageSize == 0) {
            throw new CustomException("分页数据异常");
        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("code", "200");
        map.put("msg", "success");
        if (CollectionUtil.isNotEmpty(list)) {
            // 总条数
            int total = list.size();
            //获取总页数
            int totalPage = total / pageSize;
            if (total % pageSize != 0) {
                totalPage = totalPage + 1;
            }
            // 当前第几页数据
            int currentPage = totalPage < pageNum ? totalPage : pageNum;
            // 起始索引
            int fromIndex = pageSize * (currentPage - 1);
            // 结束索引
            int toIndex = pageSize * currentPage > total ? total : pageSize * currentPage;
            // 分页数据
            list = list.subList(fromIndex, toIndex);
            map.put("rows", list);
            map.put("total", total);
            return map;
        }
        map.put("rows", Collections.EMPTY_LIST);
        map.put("total", 0);
        return map;
    }

    public Map<String, Object> pagerList(List<T> list, Integer pageNum, Integer pageSize) {
        if (pageNum <= 0 || pageSize <= 0) {
            throw new CustomException("分页数据异常");
        }
        Map<String, Object> map = new HashMap<>(5);
        map.put("code", "200");
        map.put("msg", "success");
        if (CollectionUtil.isNotEmpty(list)) {
            map.put("total", list.size());
            // 分页数据
            list = list.stream().skip((pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
            map.put("rows", list);
            return map;
        }
        map.put("rows", Collections.EMPTY_LIST);
        map.put("total", 0);
        return map;
    }

}
