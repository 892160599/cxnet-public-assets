package com.cxnet.common.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 计算年度结转
 *
 * @author cxnet
 * @date 2021/1/22
 */
public class CarryForwardUtils {

    /**
     * oldYear：当前最新数据年度
     * newYear：需要结转至XX年份
     * info：
     * 1  是否将xx年度数据结转至xx年度
     * 2  是否将xx年度数据重新结转至xx年度
     * 0  提示 结转功能仅在12月和1月可用
     *
     * @param year 原数据最新年度
     * @return 结果
     */
    public static Map<String, Integer> carryForward(Integer year) {
        Map<String, Integer> info = new HashMap<>(3);
        Calendar date = Calendar.getInstance();
        int now = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        // 12月进行结转操作
        if (month == Calendar.DECEMBER) {
            // 当前最新数据年度等于当前年，向下一年结转数据
            if (year == now) {
                info.put("oldYear", year);
                info.put("newYear", now + 1);
                info.put("info", 1);
                // 当前最新数据年度等于下一年，重新结转下一年数据
            } else if (year == (now + 1)) {
                info.put("oldYear", year - 1);
                info.put("newYear", now + 1);
                info.put("info", 2);
            }
            // 1月进行结转操作
        } else if (month == Calendar.JANUARY) {
            // 当前数据最新年度等于当前年，重新结转当年数据
            if (year == now) {
                info.put("oldYear", year - 1);
                info.put("newYear", now);
                info.put("info", 2);
                // 当前最新数据年度等于前一年，结转至当前年数据
            } else if (year == (now - 1)) {
                info.put("oldYear", year);
                info.put("newYear", now);
                info.put("info", 1);
            }
            // 其他月份不进行结转
        } else {
            info.put("oldYear", 0);
            info.put("newYear", 0);
            info.put("info", 0);
        }
        return info;
    }

}
