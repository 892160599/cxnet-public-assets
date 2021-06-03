package com.cxnet.project.system.menu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.cxnet.project.system.menu.domain.SysMenu;
import org.apache.ibatis.annotations.Select;

/**
 * 菜单表 数据层
 *
 * @author cxnet
 */
public interface SysMenuMapper {
    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 根据用户所有权限
     *
     * @return 权限列表
     */
    public List<String> selectMenuPerms();

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> selectMenuPermsByUserId(String userId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeByUserId(String userId);

    /**
     * 根据用户ID查询菜单ID集合排除隐藏菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuTreeByUserIdExVisible(String userId);

    /**
     * 根据用户ID查询快捷菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectQuickMenuByUserId(String userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    public List<String> selectMenuListByRoleId(String roleId);


    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    public SysMenu selectMenuById(String menuId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int hasChildByMenuId(String menuId);

    /**
     * 新增菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int insertMenu(SysMenu menu);

    /**
     * 修改菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int updateMenu(SysMenu menu);

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int deleteMenuById(String menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") String parentId);

    /**
     * 根据权限标识查询菜单
     *
     * @param perms 菜单信息
     * @return 结果
     */
    public SysMenu selectMenuByPerms(@Param("perms") String perms);

    /**
     * 根据path和顶级path查询菜单id
     *
     * @param path
     * @return
     */
    @Select("SELECT * FROM SYS_MENU\n" +
            "where path = #{path} " +
            "and rownum = 1 ")
    SysMenu selectMenuByPathAndTopPath(@Param("path") String path);
}
