package com.cxnet.project.system.notice.controller;

import java.util.List;

import com.cxnet.project.system.notice.domain.vo.SysNoticeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.framework.web.page.TableDataInfo;
import com.cxnet.project.system.notice.domain.SysNotice;
import com.cxnet.project.system.notice.service.SysNoticeServiceI;

/**
 * 通知公告操作处理
 *
 * @author cxnet
 */
@RestController
@Api(tags = "通知公告")
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {
    @Autowired(required = false)
    private SysNoticeServiceI noticeService;

    /**
     * 获取通知公告列表
     */
    @ApiOperation("获取通知公告列表")
    @GetMapping
    public TableDataInfo list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @ApiOperation("根据通知公告编号获取详细信息")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable String noticeId) {
        return AjaxResult.success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @ApiOperation("新增通知公告")
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNoticeVo noticeVo) {
        return toAjax(noticeService.insertNotice(noticeVo));
    }

    /**
     * 修改通知公告
     */
    @ApiOperation("修改通知公告")
    @PreAuthorize("@ss.hasPermi('system:notice:update')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated @RequestBody SysNoticeVo noticeVo) {
        return toAjax(noticeService.updateNotice(noticeVo));
    }

    /**
     * 删除通知公告
     */
    @ApiOperation("删除通知公告")
    @PreAuthorize("@ss.hasPermi('system:notice:delete')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping()
    public AjaxResult delete(String[] noticeId) {
        return toAjax(noticeService.deleteNoticeByIds(noticeId));
    }


    /**
     * 新增知识库
     */
    @ApiOperation("新增知识库")
    @PreAuthorize("@ss.hasPermi('system:knowledge:add')")
    @Log(title = "知识库", businessType = BusinessType.INSERT)
    @PostMapping("/knowledge")
    public AjaxResult addKnowledge(@Validated @RequestBody SysNoticeVo noticeVo) {
        return toAjax(noticeService.insertNotice(noticeVo));
    }

    /**
     * 修改知识库
     */
    @ApiOperation("修改知识库")
    @PreAuthorize("@ss.hasPermi('system:knowledge:update')")
    @Log(title = "知识库", businessType = BusinessType.UPDATE)
    @PutMapping("/knowledge")
    public AjaxResult updateKnowledge(@Validated @RequestBody SysNoticeVo noticeVo) {
        return toAjax(noticeService.updateNotice(noticeVo));
    }

    /**
     * 删除知识库
     */
    @ApiOperation("删除知识库")
    @PreAuthorize("@ss.hasPermi('system:knowledge:delete')")
    @Log(title = "知识库", businessType = BusinessType.DELETE)
    @DeleteMapping("/knowledge")
    public AjaxResult deleteKnowledge(String[] noticeId) {
        return toAjax(noticeService.deleteNoticeByIds(noticeId));
    }
}
