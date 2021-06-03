package com.cxnet.project.system.menu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.TreeUtil;
import com.cxnet.project.assets.data.cache.CacheKey;
import com.cxnet.project.assets.data.cache.CacheMgr;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.ehcache.search.Result;
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
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.project.system.menu.domain.SysMenu;
import com.cxnet.project.system.menu.service.SysMenuServiceI;

/**
 * 菜单信息
 *
 * @author cxnet
 */
@RestController
@Api(tags = "菜单信息")
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
    @Autowired(required = false)
    private SysMenuServiceI menuService;

    /**
     * 获取帆软菜单信息
     */
    @ApiOperation("获取帆软菜单信息")
    @GetMapping("/getFinereport")
    public AjaxResult getFinereport() {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setVisible("0");
        List<SysMenu> menus = menuService.selectMenuList(sysMenu);
        List<SysMenu> collect = menus.stream()
                .filter(v -> StringUtils.isNotEmpty(v.getPath()) && v.getPath().endsWith("Ifr"))
                .collect(Collectors.toList());
        return AjaxResult.success(collect);
    }

    /**
     * 获取菜单列表
     */
    @ApiOperation("获取菜单列表")
    // @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping
    public AjaxResult list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu);
        JSONArray objects = new TreeUtil().toTree(JSONArray.parseArray(JSON.toJSONString(menus)), "menuId", "parentId");
        return AjaxResult.success(objects);
    }


    /**
     * 获取所有权限
     */
    @ApiOperation("获取所有权限")
    @GetMapping("/getPreAuthorize")
    public AjaxResult getPreAuthorize() {
        List<Object> resLst = new ArrayList<>();
        List<Result> results = new CacheMgr().getCache(CacheKey.CachePreAuthorizeProcessor.name());
        for (Result result : results) {
            resLst.add(result.getValue());
        }
        return AjaxResult.success(resLst);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @ApiOperation("根据菜单编号获取详细信息")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable String menuId) {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation("获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu);
        JSONArray objects = new TreeUtil().toTree(JSONArray.parseArray(JSON.toJSONString(menus)), "menuId", "parentId");
        return AjaxResult.success(new TreeUtil().toTreeSelect(objects, "icon", "menuId", "parentId", "menuName", "isDataScope"));
    }

    /**
     * 加载对应用户菜单列表树
     */
    @ApiOperation("加载对应用户菜单列表树")
    @GetMapping("/userMenuTreeselect/{userId}")
    public AjaxResult userMenuTreeselect(@PathVariable String userId) {
        List<SysMenu> menus = menuService.selectMenuTreeByUserIdExVisible(userId);
        JSONArray objects = new TreeUtil().toTreeSelect(JSONArray.parseArray(JSON.toJSONString(menus)), "icon", "menuId", "parentId", "menuName", "isDataScope");
        return AjaxResult.success(objects);
    }

    /**
     * 加载对应角色菜单列表树
     */
    @ApiOperation("加载对应角色菜单列表树")
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") String roleId) {
        return AjaxResult.success(menuService.selectMenuListByRoleId(roleId));
    }


    /**
     * 根据角色菜单查询数据权限
     */
    @ApiOperation("根据角色菜单查询数据权限")
    @GetMapping(value = "/{roleId}/{menuId}")
    public AjaxResult roleIdMenuIdSelect(@PathVariable("roleId") String roleId, @PathVariable("menuId") String menuId) {
        return AjaxResult.success(menuService.roleIdMenuIdSelect(roleId, menuId));
    }

    /**
     * 查询用户快捷菜单
     */
    @ApiOperation("查询用户快捷菜单")
    @GetMapping("/quickMenuByUserId/{userId}")
    public AjaxResult quickMenuByUserId(@PathVariable String userId) {
        return AjaxResult.success(menuService.selectQuickMenuByUserId(userId));
    }

    /**
     * 新增菜单
     */
    @ApiOperation("新增菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @ApiOperation("修改菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:update')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @ApiOperation("删除菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:delete')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult delete(@PathVariable("menuId") String menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}