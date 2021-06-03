package com.cxnet.framework.security.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.domain.SysDeptVO;
import com.cxnet.project.system.dept.mapper.SysDeptMapper;
import com.cxnet.project.system.user.service.SysUserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.menu.service.SysMenuServiceI;
import com.cxnet.project.system.role.service.SysRoleServiceI;

/**
 * 用户权限处理
 *
 * @author cxnet
 */
@Component
public class SysPermissionService {
    @Autowired(required = false)
    private SysRoleServiceI roleService;

    @Autowired(required = false)
    private SysMenuServiceI menuService;

    @Autowired(required = false)
    private SysUserServiceI serviceI;

    @Autowired(required = false)
    private SysDeptMapper sysDeptMapper;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }


    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("*:*:*");
        } else {
            roles.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取组织机构
     *
     * @param user 用户信息
     * @return 组织机构代码
     */
    public String getDeptPermission(SysUser user) {
        // 管理员拥有所有权限
        String dept = serviceI.getDeptPermission(user.getUserId());

        return dept;
    }

    public SysDept getUnit(SysUser user) {
        return sysDeptMapper.getUnit(user);
    }
}
