package com.cxnet.project.system.user.controller;

import static com.cxnet.common.constant.AjaxResult.error;
import static com.cxnet.common.constant.AjaxResult.success;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.cxnet.framework.redis.RedisCache;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.web.page.TableDataInfo;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.dict.domain.SysDictData;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import com.cxnet.project.system.post.service.SysPostServiceI;
import com.cxnet.project.system.role.service.SysRoleServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.service.SysUserServiceI;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户信息
 *
 * @author cxnet
 */
@Slf4j
@RestController
@Api(tags = "用户信息")
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired(required = false)
    private SysUserServiceI userService;

    @Autowired(required = false)
    private SysRoleServiceI roleService;

    @Autowired(required = false)
    private SysPostServiceI postService;
    @Autowired(required = false)
    private SysDeptServiceI deptService;

    @Autowired(required = false)
    private SysDictDataServiceI sysDictDataServiceI;

    @Autowired(required = false)
    private RedisCache redisCache;

    /**
     * 获取用户列表
     */
    @ApiOperation("获取用户列表")
    @GetMapping
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 获取用户列表
     */
    @ApiOperation("获取用户列表")
    @GetMapping("/bdPersonnel")
    public TableDataInfo userList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectBdUserList(user);
        return getDataTable(list);
    }

    @ApiOperation("导出用户信息")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @ApiOperation("根据用户编号获取详细信息")
    @GetMapping(value = "/detail/{userId}")
    public AjaxResult getInfo(@PathVariable String userId) {
        AjaxResult ajax = success(userService.selectUserById(userId));
        ajax.put("postIds", postService.selectPostListByUserId(userId));
        ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        return ajax;
    }

    /**
     * 加载对应用户菜单列表树
     */
    @ApiOperation("加载对应用户菜单列表树")
    @GetMapping(value = "/getUserMenuIds/{userId}")
    public AjaxResult getUserMenuIds(@PathVariable("userId") String userId) {
        return success(userService.selectMenuIdsByUserId(userId));
    }

    /**
     * 新增用户
     */
    @ApiOperation("新增用户")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } /*else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }*/
        user.setCreateBy(SecurityUtils.getUsername());
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @ApiOperation("修改用户")
    @PreAuthorize("@ss.hasPermi('system:user:update')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
//        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
//            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
//        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
//            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
//        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 用户菜单修改
     */
    @ApiOperation("用户菜单修改")
    @PutMapping("/updateUserMenuIds")
    public AjaxResult updateUserMenuIds(@Validated @RequestBody SysUser user) {
        try {
            return success(userService.updateUserMenuIds(user));
        } catch (Exception e) {
            log.error("Catch exception updateUserMenuIds error:" + e.toString());
        }
        return error();
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult delete(@PathVariable String[] userIds) {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @ApiOperation("重置密码")
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @ApiOperation("状态修改")
    @PreAuthorize("@ss.hasPermi('system:user:update')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        userService.updateUserStatus(user);
        if ("0".equals(user.getStatus())) {
            SysUser sysUser = userService.selectUserById(user.getUserId());
            if (sysUser != null && StringUtils.isNotEmpty(sysUser.getUserName())) {
                redisCache.deleteObject(sysUser.getUserName());
            }
        }
        return toAjax(1);
    }

    /**
     * 根据角色编号获取用户集合
     */
    @ApiOperation("根据角色编号获取用户集合")
    @GetMapping(value = "/selectUserListByRoleId/{roleId}")
    public AjaxResult selectUserListByRoleId(@PathVariable String roleId) {
        return success(userService.selectUserListByRoleId(roleId));
    }

    /**
     * 根据岗位编号获取用户集合
     */
    @ApiOperation("根据岗位编号获取用户集合")
    @GetMapping(value = "/selectUserListByPostId/{postId}")
    public AjaxResult selectUserListByPostId(@PathVariable String postId) {
        return success(userService.selectUserListByPostId(postId));
    }

    /**
     * 根据单位编码获取用户集合
     */
    @ApiOperation("根据单位编码获取用户集合")
    @GetMapping(value = "/selectUserByDept/{deptCode}")
    public AjaxResult selectUserByDept(@PathVariable String deptCode) {
        return success(userService.selectUserByDept(deptCode));
    }


    /**
     * 导入Excel数据
     *
     * @param users 用户信息集合
     */
    @ApiOperation("导入Excel数据")
    @PreAuthorize("@ss.hasPermi('system:user:importExcelData')")
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importExcelData")
    public AjaxResult importExcelData(@RequestBody List<SysUser> users) throws Exception {

        if (CollectionUtils.isEmpty(users)) {
            throw new CustomException("报表为空");
        }

        int size = users.size();
        List<String> nameList = new ArrayList<>(size);
        SysUser sysUser = null;
        for (int index = 0; index < size; index++) {
            sysUser = users.get(index);

            int row = index + 1;
            if (null == sysUser) {
                throw new CustomException("第" + row + "行数据为空");
            }
            sysUser.setUserName(StringUtils.trim(sysUser.getUserName()));
            if (nameList.contains(sysUser.getUserName())) {
                throw new CustomException("表格中第" + row + "行数据出现登录账号重复");
            }
            if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(sysUser.getUserName()))) {
                throw new CustomException("表格中第" + row + "行数据出现登录账号重复");
            }
            SysDept sysDept = deptService.selectDeptName(sysUser.getDeptCode());
            if (sysDept == null) {
                return AjaxResult.error("第" + row + "行，所属部门错误或不存在");
            }
            sysUser.setDeptId(sysDept.getDeptId());
            sysUser.setUserType("0");
            sysUser.setSex("男".equals(sysUser.getSex().trim()) ? "0" : ("女".equals(sysUser.getSex().trim()) ? "1" : "2"));

            nameList.add(sysUser.getUserName());
        }

        //获取数据字典
        List<SysDictData> sysDictDataList = sysDictDataServiceI.selectDictDataByType("sys_data_scope");

        Map<String, String> sysDictMap = null;
        if (CollectionUtils.isNotEmpty(sysDictDataList)) {
            sysDictMap = sysDictDataList.stream().collect(Collectors.toMap(SysDictData::getDictLabel, SysDictData::getDictValue));
        }

        for (SysUser user : users) {
            user.setStatus("0");
            user.setCreateBy(SecurityUtils.getUsername());
            if (null != sysDictMap) {
                String value = sysDictMap.get(user.getDataScope());
                user.setDataScope(StringUtils.isBlank(value) ? user.getDataScope() : value);
            }
            user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        }

        return AjaxResult.success(userService.insertBatchSysUser(users));
    }


    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 新增岗位用户
     */
    @ApiOperation("新增岗位下用户")
    @Log(title = "岗位用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/addUser")
    public AjaxResult addUser(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        SysDept sysDept = deptService.selectDeptById(user.getDeptId());
        Map<String, Object> map = new HashMap<>(2);
        map.put("sysDept", sysDept);
        map.put("user", user);
        return toAjaxResult(userService.insertUser(user), map);
    }
}