package com.cxnet.framework.quartz.controller;

import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.framework.quartz.domain.ScheduleJobBean;
import com.cxnet.framework.quartz.service.ScheduleJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 定时器接口控制层
 *
 * @author cxnet
 * @since 2021-04-22 15:05:42
 */
@Slf4j
@Api(description = "定时器接口控制层")
@RestController
@RequestMapping("/job")
public class ScheduleJobController {

    @Resource
    private ScheduleJobService scheduleJobService;

    /**
     * 查询任务列表
     */
    @ApiOperation("查询任务列表")
    @GetMapping
    public AjaxResult selectAll(ScheduleJobBean scheduleJobBean) {
        return AjaxResult.success(scheduleJobService.selectAll(scheduleJobBean));
    }

    /**
     * 查询任务详情根据id
     */
    @ApiOperation("查询任务详情根据id")
    @GetMapping("/{jobId}")
    public AjaxResult selectByJobId(@PathVariable Long jobId) {
        return AjaxResult.success(scheduleJobService.selectByJobId(jobId));
    }

    /**
     * 添加定时器
     */
    @ApiOperation("添加定时器")
    @PostMapping("/insertJob")
    public AjaxResult insertJob(@RequestBody ScheduleJobBean scheduleJobBean) {
        scheduleJobBean.setCreateTime(new Date());
        scheduleJobService.insert(scheduleJobBean);
        return AjaxResult.success();
    }

    /**
     * 执行一次定时器
     */
    @ApiOperation("执行一次定时器")
    @GetMapping("/runJob")
    public AjaxResult runJob(Long jobId) {
        scheduleJobService.run(jobId);
        return AjaxResult.success();
    }

    /**
     * 更新定时器
     */
    @ApiOperation("更新定时器")
    @PutMapping("/updateJob")
    public AjaxResult updateJob(@RequestBody ScheduleJobBean scheduleJobBean) {
        scheduleJobService.updateByPrimaryKeySelective(scheduleJobBean);
        return AjaxResult.success();
    }

    /**
     * 停止定时器
     */
    @ApiOperation("停止定时器")
    @GetMapping("/pauseJob")
    public AjaxResult pauseJob(Long jobId) {
        scheduleJobService.pauseJob(jobId);
        return AjaxResult.success();
    }

    /**
     * 恢复定时器
     */
    @ApiOperation("恢复定时器")
    @GetMapping("/resumeJob")
    public AjaxResult resumeJob(Long jobId) {
        scheduleJobService.resumeJob(jobId);
        return AjaxResult.success();
    }

    /**
     * 删除定时器
     */
    @ApiOperation("删除定时器")
    @DeleteMapping("/deleteJob")
    public AjaxResult deleteJob(@RequestParam("jobIds") List<Long> jobIds) {
        scheduleJobService.delete(jobIds);
        return AjaxResult.success();
    }

    /**
     * 导出定时器
     */
    @ApiOperation("导出定时器")
    @PutMapping("/export")
    public AjaxResult export(@RequestBody ScheduleJobBean scheduleJobBean) {
        List<ScheduleJobBean> scheduleJobBeans = scheduleJobService.export(scheduleJobBean);
        ExcelUtil<ScheduleJobBean> util = new ExcelUtil<>(ScheduleJobBean.class);
        return util.exportExcel(scheduleJobBeans, "定时器列表");
    }

}
