package com.cxnet.framework.quartz.controller;

import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.framework.quartz.domain.ScheduleJobLogBean;
import com.cxnet.framework.quartz.service.ScheduleJobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cxnet
 * @date 2021/4/27
 */
@Slf4j
@Api(description = "定时器日志接口控制层")
@RestController
@RequestMapping("/jobLog")
public class ScheduleJobLogController {

    @Resource
    private ScheduleJobLogService scheduleJobLogService;

    /**
     * 查询任务日志列表
     */
    @ApiOperation("查询任务列表")
    @GetMapping
    public AjaxResult selectAll(ScheduleJobLogBean scheduleJobLogBean) {
        return AjaxResult.success(scheduleJobLogService.selectAll(scheduleJobLogBean));
    }

    /**
     * 删除任务日志列表
     */
    @ApiOperation("删除任务日志列表")
    @DeleteMapping
    public AjaxResult delete(@RequestParam("logIds") List<Long> logIds) {
        return AjaxResult.success(scheduleJobLogService.delete(logIds));
    }

    /**
     * 导出任务日志列表
     */
    @ApiOperation("导出任务日志列表")
    @GetMapping("/export")
    public AjaxResult export(@RequestBody ScheduleJobLogBean scheduleJobLogBean) {
        List<ScheduleJobLogBean> scheduleJobLogBeanPageInfo = scheduleJobLogService.select(scheduleJobLogBean);
        ExcelUtil<ScheduleJobLogBean> util = new ExcelUtil<>(ScheduleJobLogBean.class);
        return util.exportExcel(scheduleJobLogBeanPageInfo, "任务日志列表");
    }

}
