package com.cxnet.framework.quartz.mapper;

import com.cxnet.framework.quartz.domain.ScheduleJobBean;
import com.cxnet.framework.quartz.domain.ScheduleJobExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleJobMapper {
    int countByExample(ScheduleJobExample example);

    int deleteByExample(ScheduleJobExample example);

    int deleteByPrimaryKey(Long jobId);

    int insert(ScheduleJobBean record);

    Long insertSelective(ScheduleJobBean record);

    List<ScheduleJobBean> selectByExample(ScheduleJobExample example);

    ScheduleJobBean selectByPrimaryKey(Long jobId);

    int updateByExampleSelective(@Param("record") ScheduleJobBean record, @Param("example") ScheduleJobExample example);

    int updateByExample(@Param("record") ScheduleJobBean record, @Param("example") ScheduleJobExample example);

    int updateByPrimaryKeySelective(ScheduleJobBean record);

    int updateByPrimaryKey(ScheduleJobBean record);

    List<ScheduleJobBean> selectAll(ScheduleJobBean scheduleJobBean);

    ScheduleJobBean selectByJobId(Long jobId);
}