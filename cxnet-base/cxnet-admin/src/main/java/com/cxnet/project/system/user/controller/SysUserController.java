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
 * ????????????
 *
 * @author cxnet
 */
@Slf4j
@RestController
@Api(tags = "????????????")
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
     * ??????????????????
     */
    @ApiOperation("??????????????????")
    @GetMapping
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * ??????????????????
     */
    @ApiOperation("??????????????????")
    @GetMapping("/bdPersonnel")
    public TableDataInfo userList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectBdUserList(user);
        return getDataTable(list);
    }

    @ApiOperation("??????????????????")
    @Log(title = "????????????", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "????????????");
    }

    /**
     * ????????????????????????????????????
     */
    @ApiOperation("????????????????????????????????????")
    @GetMapping(value = "/detail/{userId}")
    public AjaxResult getInfo(@PathVariable String userId) {
        AjaxResult ajax = success(userService.selectUserById(userId));
        ajax.put("postIds", postService.selectPostListByUserId(userId));
        ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        return ajax;
    }

    /**
     * ?????????????????????????????????
     */
    @ApiOperation("?????????????????????????????????")
    @GetMapping(value = "/getUserMenuIds/{userId}")
    public AjaxResult getUserMenuIds(@PathVariable("userId") String userId) {
        return success(userService.selectMenuIdsByUserId(userId));
    }

    /**
     * ????????????
     */
    @ApiOperation("????????????")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "????????????", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return error("????????????'" + user.getUserName() + "'??????????????????????????????");
        } /*else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return error("????????????'" + user.getUserName() + "'??????????????????????????????");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return error("????????????'" + user.getUserName() + "'??????????????????????????????");
        }*/
        user.setCreateBy(SecurityUtils.getUsername());
        return toAjax(userService.insertUser(user));
    }

    /**
     * ????????????
     */
    @ApiOperation("????????????")
    @PreAuthorize("@ss.hasPermi('system:user:update')")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
//        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
//            return AjaxResult.error("????????????'" + user.getUserName() + "'??????????????????????????????");
//        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
//            return AjaxResult.error("????????????'" + user.getUserName() + "'??????????????????????????????");
//        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * ??????????????????
     */
    @ApiOperation("??????????????????")
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
     * ????????????
     */
    @ApiOperation("????????????")
    @PreAuthorize("@ss.hasPermi('system:user:delete')")
    @Log(title = "????????????", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult delete(@PathVariable String[] userIds) {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * ????????????
     */
    @ApiOperation("????????????")
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * ????????????
     */
    @ApiOperation("????????????")
    @PreAuthorize("@ss.hasPermi('system:user:update')")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
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
     * ????????????????????????????????????
     */
    @ApiOperation("????????????????????????????????????")
    @GetMapping(value = "/selectUserListByRoleId/{roleId}")
    public AjaxResult selectUserListByRoleId(@PathVariable String roleId) {
        return success(userService.selectUserListByRoleId(roleId));
    }

    /**
     * ????????????????????????????????????
     */
    @ApiOperation("????????????????????????????????????")
    @GetMapping(value = "/selectUserListByPostId/{postId}")
    public AjaxResult selectUserListByPostId(@PathVariable String postId) {
        return success(userService.selectUserListByPostId(postId));
    }

    /**
     * ????????????????????????????????????
     */
    @ApiOperation("????????????????????????????????????")
    @GetMapping(value = "/selectUserByDept/{deptCode}")
    public AjaxResult selectUserByDept(@PathVariable String deptCode) {
        return success(userService.selectUserByDept(deptCode));
    }


    /**
     * ??????Excel??????
     *
     * @param users ??????????????????
     */
    @ApiOperation("??????Excel??????")
    @PreAuthorize("@ss.hasPermi('system:user:importExcelData')")
    @Log(title = "????????????", businessType = BusinessType.IMPORT)
    @PostMapping("/importExcelData")
    public AjaxResult importExcelData(@RequestBody List<SysUser> users) throws Exception {

        if (CollectionUtils.isEmpty(users)) {
            throw new CustomException("????????????");
        }

        int size = users.size();
        List<String> nameList = new ArrayList<>(size);
        SysUser sysUser = null;
        for (int index = 0; index < size; index++) {
            sysUser = users.get(index);

            int row = index + 1;
            if (null == sysUser) {
                throw new CustomException("???" + row + "???????????????");
            }
            sysUser.setUserName(StringUtils.trim(sysUser.getUserName()));
            if (nameList.contains(sysUser.getUserName())) {
                throw new CustomException("????????????" + row + "?????????????????????????????????");
            }
            if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(sysUser.getUserName()))) {
                throw new CustomException("????????????" + row + "?????????????????????????????????");
            }
            SysDept sysDept = deptService.selectDeptName(sysUser.getDeptCode());
            if (sysDept == null) {
                return AjaxResult.error("???" + row + "????????????????????????????????????");
            }
            sysUser.setDeptId(sysDept.getDeptId());
            sysUser.setUserType("0");
            sysUser.setSex("???".equals(sysUser.getSex().trim()) ? "0" : ("???".equals(sysUser.getSex().trim()) ? "1" : "2"));

            nameList.add(sysUser.getUserName());
        }

        //??????????????????
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
        return util.importTemplateExcel("????????????");
    }

    /**
     * ??????????????????
     */
    @ApiOperation("?????????????????????")
    @Log(title = "??????????????????", businessType = BusinessType.INSERT)
    @PostMapping("/addUser")
    public AjaxResult addUser(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return error("????????????'" + user.getUserName() + "'??????????????????????????????");
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