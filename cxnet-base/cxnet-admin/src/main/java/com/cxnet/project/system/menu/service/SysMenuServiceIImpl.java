package com.cxnet.project.system.menu.service;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cxnet.common.exception.CustomException;
import com.cxnet.framework.aspectj.DataScopeAspect;
import com.cxnet.project.system.role.domain.SysRole;
import com.cxnet.project.system.role.domain.SysRoleDept;
import com.cxnet.project.system.role.mapper.SysRoleDeptMapper;
import com.cxnet.project.system.role.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.project.system.menu.domain.SysMenu;
import com.cxnet.project.system.menu.domain.vo.MetaVo;
import com.cxnet.project.system.menu.domain.vo.RouterVo;
import com.cxnet.project.system.menu.mapper.SysMenuMapper;
import com.cxnet.project.system.role.mapper.SysRoleMenuMapper;

/**
 * 菜单 业务层处理
 *
 * @author cxnet
 */
@Service
public class SysMenuServiceIImpl implements SysMenuServiceI {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired(required = false)
    private SysMenuMapper menuMapper;

    @Autowired(required = false)
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired(required = false)
    private SysRoleMapper sysRoleMapper;

    @Autowired(required = false)
    private SysRoleDeptMapper sysRoleDeptMapper;

    @Autowired(required = false)
    private SysMenuMapper sysMenuMapper;

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu) {
        List<SysMenu> menuList = menuMapper.selectMenuList(menu);
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(String userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询快捷菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectQuickMenuByUserId(String userId) {
        return menuMapper.selectQuickMenuByUserId(userId);
    }

    /**
     * 根据用户名称查询菜单
     *
     * @param userId 用户Id
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(String userId) {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId)) {
            menus = menuMapper.selectMenuTreeAll();
        } else {
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, "0");
    }

    /**
     * 根据用户ID查询菜单ID集合排除隐藏菜单
     *
     * @param userId 用户Id
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserIdExVisible(String userId) {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId)) {
            menus = menuMapper.selectMenuTreeAll();
        } else {
            menus = menuMapper.selectMenuTreeByUserIdExVisible(userId);
        }
        return getChildPerms(menus, "0");
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<String> selectMenuListByRoleId(String roleId) {
        return menuMapper.selectMenuListByRoleId(roleId);
    }


    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setName(menu.getMenuName());
            router.setPath(getRouterPath(menu));
            router.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
            router.setName(menu.getMenuName());
            router.setVisible(menu.getVisible());
            router.setModelCode(menu.getModelCode());
            router.setIsFrame(menu.getIsFrame());
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0) {
                //目录
                if ("M".equals(menu.getMenuType())) {
                    router.setAlwaysShow(true);
                    router.setRedirect("noRedirect");
                    router.setChildren(buildMenus(cMenus));
                    //菜单
                } else if ("C".equals(menu.getMenuType())) {
                    router.setAlwaysShow(true);
                    router.setChildren(buildMenus(cMenus));
                }
            }
            routers.add(router);
        }
        return routers;
    }


    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildAnaMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setName(menu.getMenuName());
            router.setPath(getRouterPath(menu));
            router.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
            router.setName(menu.getMenuName());
            router.setVisible(menu.getVisible());
            router.setModelCode(menu.getModelCode());
            router.setIsFrame(menu.getIsFrame());
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildAnaMenus(cMenus));
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(String menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuId(String menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean checkMenuExistRole(String menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(String menuId) {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        String menuId = StringUtils.isNull(menu.getMenuId()) ? "-1" : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && !info.getMenuId().equals(menuId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录
        if ("0".equals(menu.getParentId()) && "1".equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        return routerPath;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, String parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            // 判断是否有子节点
            if (hasChild(list, tChild)) {
                Iterator<SysMenu> it = childList.iterator();
                while (it.hasNext()) {
                    SysMenu n = (SysMenu) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().equals(t.getMenuId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    /**
     * 根据权限标识查询菜单
     *
     * @param perms 菜单信息
     * @return 结果
     */
    @Override
    public SysMenu selectMenuByPerms(String perms) {

        return menuMapper.selectMenuByPerms(perms);
    }

    /**
     * 根据角色菜单查询数据权限
     *
     * @param roleId
     * @param menuId
     * @return
     */
    @Override
    public SysRole roleIdMenuIdSelect(String roleId, String menuId) {
        SysMenu sysMenu = sysMenuMapper.selectMenuById(menuId);
        SysRole sysRole = sysRoleMapper.selectRoleById(roleId);
        if (ObjectUtil.isNull(sysMenu) || ObjectUtil.isNull(sysRole)) {
            throw new CustomException("未查询到绑定信息，请刷新后重试！");
        }
        sysRole.setMenuId(menuId);
        sysRole.setIsDataScope(sysMenu.getIsDataScope());
        String dataScope = roleMenuMapper.roleIdMenuIdSelect(roleId, menuId);
        sysRole.setDataScope(dataScope);
        if (StringUtils.isNotEmpty(dataScope) && DataScopeAspect.DATA_SCOPE_CUSTOM.equals(dataScope)) {
            List<SysRoleDept> sysRoleDepts = sysRoleDeptMapper.selectDeptRoleDeptByRoleIdAndMenuId(roleId, menuId);
            if (CollectionUtil.isNotEmpty(sysRoleDepts)) {
                String[] deptIds = {};
                deptIds = sysRoleDepts.stream().map(SysRoleDept::getDeptId).collect(Collectors.toList()).toArray(deptIds);
                sysRole.setDeptIds(deptIds);
            }
        }
        return sysRole;
    }
}
