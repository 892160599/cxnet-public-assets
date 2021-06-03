package com.cxnet.framework.quartz.service.impl;

import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.pager.PagerUtils;
import com.cxnet.framework.quartz.domain.ScheduleJobBean;
import com.cxnet.framework.quartz.domain.ScheduleJobExample;
import com.cxnet.framework.quartz.mapper.ScheduleJobMapper;
import com.cxnet.framework.quartz.service.ScheduleJobService;
import com.cxnet.framework.quartz.utils.ScheduleUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Resource
    private Scheduler scheduler;
    @Resource
    private ScheduleJobMapper scheduleJobMapper;

    private PagerUtils pagerUtils = new PagerUtils();

    /**
     * 定时器初始化
     */
    @PostConstruct
    public void init() {
        ScheduleJobExample example = new ScheduleJobExample();
        List<ScheduleJobBean> scheduleJobBeanList = scheduleJobMapper.selectByExample(example);
        for (ScheduleJobBean scheduleJobBean : scheduleJobBeanList) {
            CronTrigger cronTrigger = ScheduleUtil.getCronTrigger(scheduler, scheduleJobBean.getJobId());
            if (cronTrigger == null) {
                ScheduleUtil.createJob(scheduler, scheduleJobBean);
            } else {
                ScheduleUtil.updateJob(scheduler, scheduleJobBean);
            }
        }
    }

    /**
     * 查询主键
     *
     * @param jobId id
     * @return 结果
     */
    @Override
    public ScheduleJobBean selectByPrimaryKey(Long jobId) {
        return scheduleJobMapper.selectByPrimaryKey(jobId);
    }

    /**
     * 查询列表
     *
     * @param example example
     * @return 结果
     */
    @Override
    public List<ScheduleJobBean> selectByExample(ScheduleJobExample example) {
        return scheduleJobMapper.selectByExample(example);
    }

    /**
     * 新增
     *
     * @param record record
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ScheduleJobBean record) {
        scheduleJobMapper.insertSelective(record);
        ScheduleUtil.createJob(scheduler, record);
        return 1;
    }

    /**
     * 更新
     *
     * @param record record
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(ScheduleJobBean record) {
        ScheduleUtil.updateJob(scheduler, record);
        return scheduleJobMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 停止
     *
     * @param jobId id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseJob(Long jobId) {
        ScheduleJobBean scheduleJobBean = scheduleJobMapper.selectByPrimaryKey(jobId);
        ScheduleUtil.pauseJob(scheduler, jobId);
        scheduleJobBean.setStatus(1);
        scheduleJobMapper.updateByPrimaryKeySelective(scheduleJobBean);
    }

    /**
     * 恢复
     *
     * @param jobId id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resumeJob(Long jobId) {
        ScheduleJobBean scheduleJobBean = scheduleJobMapper.selectByPrimaryKey(jobId);
        ScheduleUtil.resumeJob(scheduler, jobId);
        scheduleJobBean.setStatus(0);
        scheduleJobMapper.updateByPrimaryKeySelective(scheduleJobBean);
    }

    /**
     * 执行定时器
     *
     * @param jobId id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(Long jobId) {
        ScheduleJobBean scheduleJobBean = scheduleJobMapper.selectByPrimaryKey(jobId);
        ScheduleUtil.run(scheduler, scheduleJobBean);
    }

    /**
     * 删除定时器
     *
     * @param jobIds ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> jobIds) {
        jobIds.forEach(jobId -> {
            if (jobId == 1L) {
                throw new CustomException("本次操作含有初始数据，无法删除！");
            }
            ScheduleUtil.deleteJob(scheduler, jobId);
            scheduleJobMapper.deleteByPrimaryKey(jobId);
        });
    }

    /**
     * 查询所有
     *
     * @param scheduleJobBean scheduleJobBean
     * @return 分页结果集
     */
    @Override
    public PageInfo<ScheduleJobBean> selectAll(ScheduleJobBean scheduleJobBean) {
        PageHelper.startPage(scheduleJobBean.getPageNum(), scheduleJobBean.getPageSize());
        return new PageInfo<>(scheduleJobMapper.selectAll(scheduleJobBean));
    }

    @Override
    public ScheduleJobBean selectByJobId(Long jobId) {
        return scheduleJobMapper.selectByJobId(jobId);
    }

    @Override
    public List<ScheduleJobBean> export(ScheduleJobBean scheduleJobBean) {
        return scheduleJobMapper.selectAll(scheduleJobBean);
    }
}
