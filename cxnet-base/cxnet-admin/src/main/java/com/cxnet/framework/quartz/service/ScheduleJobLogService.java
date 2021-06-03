package com.cxnet.framework.quartz.service;

import com.cxnet.framework.quartz.domain.ScheduleJobLogBean;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ScheduleJobLogService {
    /**
     * 保存
     */
    int insert(ScheduleJobLogBean record);

    PageInfo<ScheduleJobLogBean> selectAll(ScheduleJobLogBean scheduleJobLogBean);

    int delete(List<Long> logIds);

    List<ScheduleJobLogBean> select(ScheduleJobLogBean scheduleJobLogBean);
}
