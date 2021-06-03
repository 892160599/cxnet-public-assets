package com.cxnet.project.system.noticeannex.controller;

import com.cxnet.project.system.noticeannex.domain.SysNoticeAnnex;
import com.cxnet.project.system.noticeannex.service.SysNoticeAnnexService;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 通知公告附件表(SysNoticeAnnex)表控制层
 *
 * @author ssw
 * @since 2020-10-26 12:26:40
 */
@Slf4j
@Api(tags = "通知公告附件表")
@RestController
@RequestMapping("/sysNoticeAnnex")
public class SysNoticeAnnexController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private SysNoticeAnnexService sysNoticeAnnexService;

    /**
     * 分页查询所有数据
     *
     * @param sysNoticeAnnex 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    //   @PreAuthorize("@ss.hasPermi('sysNoticeAnnex:sysNoticeAnnex:query')")
    @GetMapping
    public AjaxResult selectAll(SysNoticeAnnex sysNoticeAnnex) {
        startPage();
        QueryWrapper<SysNoticeAnnex> qw = new QueryWrapper<>(sysNoticeAnnex);
        List<SysNoticeAnnex> list = sysNoticeAnnexService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    //   @PreAuthorize("@ss.hasPermi('sysNoticeAnnex:sysNoticeAnnex:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(sysNoticeAnnexService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysNoticeAnnex 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('sysNoticeAnnex:sysNoticeAnnex:insert')")
    @Log(title = "通知公告附件表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody SysNoticeAnnex sysNoticeAnnex) {
        return success(sysNoticeAnnexService.save(sysNoticeAnnex));
    }

    /**
     * 修改数据
     *
     * @param sysNoticeAnnex 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('sysNoticeAnnex:sysNoticeAnnex:update')")
    @Log(title = "通知公告附件表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody SysNoticeAnnex sysNoticeAnnex) {
        return success(sysNoticeAnnexService.updateById(sysNoticeAnnex));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('sysNoticeAnnex:sysNoticeAnnex:delete')")
    @Log(title = "通知公告附件表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestParam List<String> ids) {
        return success(sysNoticeAnnexService.removeByIds(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('sysNoticeAnnex:sysNoticeAnnex:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }
}