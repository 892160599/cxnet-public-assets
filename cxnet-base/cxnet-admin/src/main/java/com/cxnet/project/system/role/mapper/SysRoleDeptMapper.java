package com.cxnet.project.system.role.mapper;

import java.util.List;

import com.cxnet.project.system.role.domain.SysRoleDept;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 角色与部门关联表 数据层
 *
 * @author cxnet
 */
public interface SysRoleDeptMapper {
    /**
     * 通过角色ID删除角色和部门关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleDeptByRoleId(String roleId);

    /**
     * 通过角色ID和菜单id删除角色菜单和部门关联
     *
     * @param roleId
     * @param menuId
     * @return
     */
    public int deleteRoleDeptByRoleIdAndMenuId(@Param("roleId") String roleId, @Param("menuId") String menuId);

    /**
     * 批量删除角色部门关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleDept(String[] ids);

    /**
     * 查询部门使用数量
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int selectCountRoleDeptByDeptId(String deptId);

    /**
     * 根据角色菜单查询部门
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     * @return
     */
    @Select("select * from SYS_ROLE_DEPT where role_id = #{roleId} and menu_id = #{menuId}")
    List<SysRoleDept> selectDeptRoleDeptByRoleIdAndMenuId(@Param("roleId") String roleId, @Param("menuId") String menuId);

    /**
     * 批量新增角色部门信息
     *
     * @param roleDeptList 角色部门列表
     * @return 结果
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
