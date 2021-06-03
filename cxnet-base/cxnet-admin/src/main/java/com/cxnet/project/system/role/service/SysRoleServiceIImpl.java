package com.cxnet.project.system.role.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cxnet.framework.security.SecurityUtils;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.aspectj.lang.annotation.DataScope;
import com.cxnet.project.system.role.domain.MobRoleMenu;
import com.cxnet.project.system.role.domain.SysRole;
import com.cxnet.project.system.role.domain.SysRoleDept;
import com.cxnet.project.system.role.domain.SysRoleMenu;
import com.cxnet.project.system.role.mapper.MobRoleMenuMapper;
import com.cxnet.project.system.role.mapper.SysRoleDeptMapper;
import com.cxnet.project.system.role.mapper.SysRoleMapper;
import com.cxnet.project.system.role.mapper.SysRoleMenuMapper;
import com.cxnet.project.system.user.mapper.SysUserRoleMapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;

/**
 * 角色 业务层处理
 *
 * @author cxnet
 */
@Service
public class SysRoleServiceIImpl implements SysRoleServiceI {
    @Autowired(required = false)
    private SysRoleMapper roleMapper;

    @Autowired(required = false)
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired(required = false)
    private SysUserRoleMapper userRoleMapper;

    @Autowired(required = false)
    private SysRoleDeptMapper roleDeptMapper;

    @Autowired(required = false)
    private MobRoleMenuService mobRoleMenuService;

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
//    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(String userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRole> selectRoleAll() {
        return roleMapper.selectRoleAll();
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<String> selectRoleListByUserId(String userId) {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(String roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRole role) {
        String roleId = StringUtils.isNull(role.getRoleId()) ? "-1" : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && !info.getRoleId().equals(roleId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role) {
        String roleId = StringUtils.isNull(role.getRoleId()) ? "-1" : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && !info.getRoleId().equals(roleId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId())) {
            SysRole sysRole = roleMapper.selectRoleById(role.getRoleId());
            if ("1".equals(sysRole.getIsFinal())) {
                throw new CustomException("不允许修改系统内置角色");
            }
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public Long countUserRoleByRoleId(String roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRole role) {
        // 新增角色信息
        roleMapper.insertRole(role);
        //新增角色与app应用信息对应关系
        insertRoleAppMenu(role);
        //新增角色菜单信息
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRole role) {
        // 修改角色信息
        roleMapper.updateRole(role);
        Map<String, Object> columnMap = new HashedMap(1);
        columnMap.put("ROLE_ID", role.getRoleId());
        // 删除角色与app应用关联
        mobRoleMenuService.removeByMap(columnMap);
        //新增角色与app应用信息对应关系
        insertRoleAppMenu(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role) {
        return roleMapper.updateRole(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authDataScope(SysRole role) {
        String menuId = role.getMenuId();
        String roleId = role.getRoleId();
        if (StringUtils.isEmpty(roleId)) {
            throw new CustomException("请选择授权角色！");
        }
        if (StringUtils.isEmpty(menuId)) {
            throw new CustomException("请选择授权菜单！");
        }
        // 修改授权信息
        roleMenuMapper.updateByRoleIdAndMenuId(role);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDeptByRoleIdAndMenuId(role.getRoleId(), role.getMenuId());
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<>();
        if (ArrayUtil.isNotEmpty(role.getMenuIds())) {
            for (String menuId : role.getMenuIds()) {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(role.getRoleId());
                rm.setMenuId(menuId);
                list.add(rm);
            }
            if (list.size() > 0) {
                rows = roleMenuMapper.batchRoleMenu(list);
            }
        }
        return rows;
    }


    /**
     * 新增角色APP菜单信息
     *
     * @param role 角色对象
     */
    private void insertRoleAppMenu(SysRole role) {

        String[] appMenuIds = role.getAppMenuIds();
        //判断应用菜单是否为空
        if (ArrayUtil.isNotEmpty(appMenuIds)) {
            List<MobRoleMenu> mobRoleMenus = new ArrayList<>();
            for (String appMenuId : appMenuIds) {
                MobRoleMenu mobRoleMenu = new MobRoleMenu();
                mobRoleMenu.setRoleId(role.getRoleId());
                mobRoleMenu.setMenuId(appMenuId);
                ;
                mobRoleMenu.setDataScope("2");
                mobRoleMenus.add(mobRoleMenu);
            }
            mobRoleMenuService.saveBatch(mobRoleMenus);
        }
    }


    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(SysRole role) {
        int rows = 1;
        String menuId = role.getMenuId();
        String roleId = role.getRoleId();
        if (StringUtils.isEmpty(roleId)) {
            throw new CustomException("请选择授权角色！");
        }
        if (StringUtils.isEmpty(menuId)) {
            throw new CustomException("请选择授权菜单！");
        }
        // 新增角色菜单与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList<>();
        for (String deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setMenuId(menuId);
            rd.setRoleId(roleId);
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int deleteRoleById(String roleId) {
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    public int deleteRoleByIds(String[] roleIds) {
        for (String roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        return roleMapper.deleteRoleByIds(roleIds);
    }


    /**
     * 批量插入角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int insertBatchRole(List<SysRole> role) {
        return roleMapper.insertBatchRole(role);
    }
}
