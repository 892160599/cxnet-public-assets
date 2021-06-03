package com.cxnet.framework.quartz.service;


import com.cxnet.framework.quartz.domain.ScheduleJobBean;
import com.cxnet.framework.quartz.domain.ScheduleJobExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ScheduleJobService {
    /**
     * 主键查询
     */
    ScheduleJobBean selectByPrimaryKey(Long jobId);

    /**
     * 列表查询
     */
    List<ScheduleJobBean> selectByExample(ScheduleJobExample example);

    /**
     * 保存
     */
    int insert(ScheduleJobBean record);

    /**
     * 更新
     */
    int updateByPrimaryKeySelective(ScheduleJobBean record);

    /**
     * 停止
     */
    void pauseJob(Long jobId);

    /**
     * 恢复
     */
    void resumeJob(Long jobId);

    /**
     * 执行
     */
    void run(Long jobId);

    /**
     * 删除
     */
    void delete(List<Long> jobIds);

    /**
     * 查询所有
     *
     * @param scheduleJobBean scheduleJobBean
     * @return 分页结果集
     */
    PageInfo<ScheduleJobBean> selectAll(ScheduleJobBean scheduleJobBean);

    ScheduleJobBean selectByJobId(Long jobId);

    List<ScheduleJobBean> export(ScheduleJobBean scheduleJobBean);
}
