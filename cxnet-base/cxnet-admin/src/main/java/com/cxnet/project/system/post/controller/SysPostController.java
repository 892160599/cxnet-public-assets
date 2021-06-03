package com.cxnet.project.system.post.controller;

import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.cxnet.common.core.text.Convert;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.service.SysUserServiceI;
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
import com.cxnet.common.constant.UserConstants;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.framework.web.page.TableDataInfo;
import com.cxnet.project.system.post.domain.SysPost;
import com.cxnet.project.system.post.service.SysPostServiceI;

/**
 * 岗位信息操作处理
 *
 * @author cxnet
 */
@RestController
@Api(tags = "岗位信息")
@RequestMapping("/system/post")
public class SysPostController extends BaseController {
    @Autowired(required = false)
    private SysPostServiceI postService;
    @Autowired(required = false)
    private SysUserServiceI userService;

    /**
     * 获取岗位列表
     */
    @ApiOperation("获取岗位列表")
    // @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping
    public TableDataInfo list(SysPost post) {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    @ApiOperation("导出岗位信息")
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dept:export')")
    @GetMapping("/export")
    public AjaxResult export(SysPost post) {
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }

    /**
     * 根据岗位编号获取详细信息
     */
    @ApiOperation("根据岗位编号获取详细信息")
    //  @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable String postId) {
        return AjaxResult.success(postService.selectPostById(postId));
    }

    /**
     * 新增岗位
     */
    @ApiOperation("新增岗位")
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return AjaxResult.error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(SecurityUtils.getUsername());

        return AjaxResult.success("操作成功", postService.insertPost(post));
    }

    /**
     * 修改岗位
     */
    @ApiOperation("修改岗位")
    @PreAuthorize("@ss.hasPermi('system:dept:update')")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return AjaxResult.error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(postService.updatePost(post));
    }

    /**
     * 删除岗位
     */
    @ApiOperation("删除岗位")
    @PreAuthorize("@ss.hasPermi('system:dept:delete')")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{postIds}/{affilUnitCode}")
    public AjaxResult delete(@PathVariable String postIds, @PathVariable String affilUnitCode) {
        List<SysUser> sysUsers = userService.selectUserListByPostId(postIds);
        if (CollectionUtil.isNotEmpty(sysUsers)) {
            return AjaxResult.error("该岗位下存在用户,不允许删除");
        }
        return toAjax(postService.deletePostById(postIds, affilUnitCode));
    }

    /**
     * 获取岗位选择框列表
     */
    @ApiOperation("获取岗位选择框列表")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<SysPost> posts = postService.selectPostAll();
        return AjaxResult.success(posts);
    }
}
