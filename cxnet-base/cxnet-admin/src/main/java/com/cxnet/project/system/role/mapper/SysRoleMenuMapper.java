package com.cxnet.project.system.role.mapper;

import java.util.List;

import com.cxnet.project.system.role.domain.SysRole;
import com.cxnet.project.system.role.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 角色与菜单关联表 数据层
 *
 * @author cxnet
 */
public interface SysRoleMenuMapper {
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int checkMenuExistRole(String menuId);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleMenuByRoleId(String roleId);

    /**
     * 批量新增角色菜单信息
     *
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);

    /**
     * 根据角色菜单查询数据权限
     *
     * @param roleId
     * @param menuId
     * @return
     */
    String roleIdMenuIdSelect(@Param("roleId") String roleId, @Param("menuId") String menuId);

    /**
     * 根据用户id和菜单id查询权限列表
     *
     * @param userId
     * @param menuId
     * @return
     */
    @Select("select distinct * from SYS_ROLE_MENU\n" +
            "where MENU_ID = #{menuId} and role_id in( SELECT role_id from SYS_USER_ROLE\n" +
            "where user_id = #{userId})")
    List<SysRoleMenu> selectByUserIdAndMenuId(@Param("userId") String userId, @Param("menuId") String menuId);

    /**
     * 根据角色菜单修改数据权限
     *
     * @param role
     * @return
     */
    @Update("update SYS_ROLE_MENU set data_scope = #{dataScope} where role_id = #{roleId} and MENU_ID = #{menuId}")
    int updateByRoleIdAndMenuId(SysRole role);
}
