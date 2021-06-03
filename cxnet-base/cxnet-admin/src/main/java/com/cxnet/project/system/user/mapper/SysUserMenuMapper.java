package com.cxnet.project.system.user.mapper;

import java.util.List;

import com.cxnet.project.system.user.domain.SysUserMenu;

/**
 * 用户与菜单关联表 数据层
 *
 * @author cxnet
 */
public interface SysUserMenuMapper {
    /**
     * 通过用户ID删除用户和菜单关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserMenuByUserId(String userId);

    /**
     * 通过菜单ID查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int countUserMenuById(String menuId);

    /**
     * 批量删除用户和菜单关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserMenu(String[] ids);

    /**
     * 批量新增用户菜单信息
     *
     * @param userMenuList 用户角色列表
     * @return 结果
     */
    public int batchUserMenu(List<SysUserMenu> userMenuList);

    /**
     * 根据用户ID查询菜单ID集合
     *
     * @param userId 用户ID
     * @return 选中菜单列表
     */
    public List<Long> selectMenuIdsByUserId(String userId);
}
