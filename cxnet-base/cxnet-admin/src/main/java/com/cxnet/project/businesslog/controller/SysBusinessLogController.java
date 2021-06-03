package com.cxnet.project.businesslog.controller;

import static com.cxnet.common.constant.AjaxResult.success;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.project.businesslog.domain.SysBusinessLog;
import com.cxnet.project.businesslog.service.SysBusinessLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务日志表(SysBusinessLog)表控制层
 *
 * @author guks
 * @since 2021-04-21 18:01:45
 */
@Api(tags = "业务日志表")
@RestController
@RequestMapping("/sysBusinessLog")
public class SysBusinessLogController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private SysBusinessLogService sysBusinessLogService;

    /**
     * 分页查询所有数据
     *
     * @param sysBusinessLog 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @GetMapping
    public AjaxResult selectAll(SysBusinessLog sysBusinessLog) {
        //如果标识为空的，那么就不做查询
        if (StringUtils.isBlank(sysBusinessLog.getOperateKey())) {
            return success(new ArrayList<>());
        }

        startPage();
        QueryWrapper<SysBusinessLog> qw = new QueryWrapper<>(sysBusinessLog);
        List<SysBusinessLog> list = sysBusinessLogService.list(qw);
        return success(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(sysBusinessLogService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysBusinessLog 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @Log(title = "业务日志表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody SysBusinessLog sysBusinessLog) {
        return success(sysBusinessLogService.save(sysBusinessLog));
    }

    /**
     * 修改数据
     *
     * @param sysBusinessLog 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @Log(title = "业务日志表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody SysBusinessLog sysBusinessLog) {
        return success(sysBusinessLogService.updateById(sysBusinessLog));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @Log(title = "业务日志表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(sysBusinessLogService.removeByIds(ids));
    }
}