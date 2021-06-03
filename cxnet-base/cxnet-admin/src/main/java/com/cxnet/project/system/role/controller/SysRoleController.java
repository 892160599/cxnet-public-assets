package com.cxnet.project.system.role.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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

import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.web.page.TableDataInfo;
import com.cxnet.project.system.role.domain.SysRole;
import com.cxnet.project.system.role.service.SysRoleServiceI;
import com.cxnet.project.system.user.domain.SysUserRole;
import com.cxnet.project.system.user.service.SysUserServiceI;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 角色信息
 *
 * @author cxnet
 */
@RestController
@Api(tags = "角色信息")
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Autowired(required = false)
    private SysRoleServiceI roleService;

    @Autowired(required = false)
    private SysUserServiceI userService;

    @ApiOperation("获取角色列表")
    @GetMapping
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @ApiOperation("导出角色信息")
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @PutMapping("/export")
    public AjaxResult export(@RequestBody SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @ApiOperation("根据角色编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable String roleId) {
        return AjaxResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @ApiOperation("新增角色")
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色编码已存在");
        }
        role.setCreateBy(SecurityUtils.getUsername());
        roleService.insertRole(role);

        return AjaxResult.success();
    }

    /**
     * 修改角色
     */
    @ApiOperation("修改角色")
    @PreAuthorize("@ss.hasPermi('system:role:update')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated @RequestBody SysRole role) {

        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(SecurityUtils.getUsername());
        roleService.updateRole(role);
        return AjaxResult.success();
    }

    /**
     * 修改保存数据权限
     */
    @ApiOperation("修改保存数据权限")
    @PreAuthorize("@ss.hasPermi('system:role:update')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.authDataScope(role);
        return AjaxResult.success();
    }

    /**
     * 角色状态修改
     */
    @ApiOperation("角色状态修改")
    @PreAuthorize("@ss.hasPermi('system:role:update')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @PreAuthorize("@ss.hasPermi('system:role:delete')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult delete(@PathVariable String[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 删除角色用户
     */
    @ApiOperation("删除角色用户")
    @PreAuthorize("@ss.hasPermi('system:role:delete')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/delUserRole")
    public AjaxResult delUserRole(@RequestBody SysUserRole userRole) {
        return toAjax(userService.deleteUserRoleInfo(userRole));
    }

    /**
     * 获取角色选择框列表
     */
    @ApiOperation("获取角色选择框列表")
    //  @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        return AjaxResult.success(roleService.selectRoleAll());
    }

    /**
     * 导入Excel数据
     *
     * @param role 角色信息
     */
    @ApiOperation("导入Excel数据")
    @PreAuthorize("@ss.hasPermi('system:role:importExcelData')")
    @PostMapping("/importExcelData")
    public AjaxResult importExcelData(@RequestBody List<SysRole> role) {
        if (CollectionUtils.isEmpty(role)) {
            throw new CustomException("报表为空");
        }

        int size = role.size();
        SysRole sysRole = null;
        for (int index = 0; index < size; index++) {
            sysRole = role.get(index);
            int row = index + 3;
            if (null == sysRole) {
                throw new CustomException("第" + row + "行数据为空");
            }

            if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(sysRole))) {
                throw new CustomException("表格中第" + row + "行数据出现角色名称号重复");
            } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(sysRole))) {
                throw new CustomException("表格中第" + row + "行数据出现角色权限重复");
            }
        }
        role.forEach(v -> {
            v.setRoleSort("0");
            v.setStatus("0");
            v.setCreateBy(SecurityUtils.getUsername());
        });
        return AjaxResult.success(roleService.insertBatchRole(role));
    }
}