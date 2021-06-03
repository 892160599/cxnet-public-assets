package com.cxnet.framework.quartz.service.impl;

import com.cxnet.framework.quartz.domain.ScheduleJobLogBean;
import com.cxnet.framework.quartz.mapper.ScheduleJobLogMapper;
import com.cxnet.framework.quartz.service.ScheduleJobLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {

    @Resource
    private ScheduleJobLogMapper scheduleJobLogMapper;

    @Override
    public int insert(ScheduleJobLogBean record) {
        return scheduleJobLogMapper.insert(record);
    }

    @Override
    public PageInfo<ScheduleJobLogBean> selectAll(ScheduleJobLogBean scheduleJobLogBean) {
        PageHelper.startPage(scheduleJobLogBean.getPageNum(), scheduleJobLogBean.getPageSize());
        return new PageInfo<>(scheduleJobLogMapper.selectAll(scheduleJobLogBean));
    }

    @Override
    public int delete(List<Long> logIds) {
        return scheduleJobLogMapper.deleteLogs(logIds);
    }

    @Override
    public List<ScheduleJobLogBean> select(ScheduleJobLogBean scheduleJobLogBean) {
        return scheduleJobLogMapper.selectAll(scheduleJobLogBean);
    }
}
