package com.cxnet.project.system.dept.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cxnet.common.utils.tree.Zone;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.domain.SysDeptVO;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.rpc.domain.system.deptrpc.SysDeptRpc;

/**
 * 部门管理 数据层
 *
 * @author cxnet
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 条件查询部门
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptListBySysDept(SysDept dept);

    /**
     * 根据id查询部门
     *
     * @param deptId 部门id
     * @return
     */
    SysDeptRpc selectRpcDeptById(@Param("deptId") String deptId);

    /**
     * 根据code查询部门
     *
     * @param deptCode 部门code
     * @return
     */
    SysDeptRpc selectRpcDeptByCode(@Param("deptCode") String deptCode, @Param("parentId") String parentId);

    /**
     * 根据角色ID菜单ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    public List<Integer> selectDeptListByRoleId(@Param("roleId") String roleId, @Param("menuId") String menuId);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(String deptId);

    /**
     * 根据部门ID查询信息
     *
     * @param deptIds 部门ID
     * @return 部门信息
     */
    List<SysDept> selectDeptListByIds(String[] deptIds);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(String deptId);

    /**
     * 是否存在子节点
     *
     * @param deptCode 部门编码
     * @return 结果
     */
    public int hasChildByDeptCode(@Param("deptCode") String deptCode, @Param("affilUnitCode") String affilUnitCode);

    /**
     * 是否存在岗位子节点
     *
     * @param deptCode 部门编码
     * @return 结果
     */
    public int hasPostByDeptCode(@Param("deptCode") String deptCode, @Param("affilUnitCode") String affilUnitCode);

    /**
     * 查询部门是否存在用户
     *
     * @param deptCode 部门编码
     * @return 结果
     */
    public int checkDeptExistUser(String deptCode);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") String parentId, @Param("parentCode") String parentCode);

    /**
     * 校验部门编码是否唯一
     *
     * @param deptCode 部门编码
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysDept checkDeptCodeUnique(@Param("deptCode") String deptCode, @Param("parentId") String parentId, @Param("parentCode") String parentCode);


    /**
     * 校验单位编码是否唯一
     *
     * @param deptCode 部门编码
     * @param deptId   部门ID
     * @return 结果
     */
    public Integer checkUnitCodeUnique(@Param("deptCode") String deptCode, @Param("deptId") String deptId);


    /**
     * 校验部门编码是否唯一
     *
     * @param deptCode      部门编码
     * @param deptId        部门ID
     * @param affilUnitCode 所属单位
     * @return 结果
     */
    public Integer checkDeptCodeOfUnitUnique(@Param("deptCode") String deptCode, @Param("deptId") String deptId, @Param("affilUnitCode") String affilUnitCode);

    /**
     * 校验部门编码是否唯一
     *
     * @param deptCode 部门编码
     * @return 结果
     */
    public Integer checkDeptCodeUniqueByDeptCode(@Param("deptCode") String deptCode, @Param("deptId") String deptId, @Param("parentId") String parentId);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 修改所在部门的父级部门状态
     *
     * @param dept 部门
     */
    public void updateDeptStatus(SysDept dept);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);


    /**
     * 删除部门管理信息
     *
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位
     * @return 结果
     */
    public int deleteDeptByCode(@Param("deptCode") String deptCode, @Param("affilUnitCode") String affilUnitCode);


    /**
     * 查询组织机构树集合
     *
     * @return 结果
     */
    public List<Map> orgTreeSelectList();

    /**
     * 查询组织机构用户树集合
     *
     * @return 结果
     */
    public List<Map> orgUserTreeSelectList();

    /**
     * 批量插入部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertBatchDept(List<SysDept> dept);

    /**
     * 系统单位切换
     *
     * @param userName 用户
     * @return 用户所在得部门
     */
    public List<SysDept> changeDept(String userName);

    /**
     * 查询单位下部门列表
     *
     * @param deptCode 单位id
     * @return 部门列表
     */
    List<Zone> depDeptListSelect(String deptCode);

    /**
     * 查询单位tree
     *
     * @return 查询单位tree
     */
    List<Zone> getUnitTree();

    /**
     * 获取当前用户所在岗位的部门列表，以及部门人员里面所在的部门
     *
     * @param userId
     * @return 部门列表
     */
    List<SysDept> depListSelectByUserId(@Param("userId") String userId, @Param("unitId") String unitId);

    /**
     * 查询单位切换tree
     *
     * @param userId
     * @return
     */
    List<Zone> changeDeptTree(String userId);

    List<Zone> changeDeptTreeNew(String userId);

    /**
     * 查找部门名称
     *
     * @param deptCode 部门code
     * @return 部门名称
     */
    public SysDept selectDeptName(String deptCode);

    List<SysDeptVO> getDeptList(SysUser sysUser);

    @Select("select * from sys_dept\n" +
            "where dept_id = (\n" +
            "select dept_id from sys_user\n" +
            "where user_id = #{userId})")
    SysDept getUnit(SysUser user);

    /**
     * 查找部门id
     *
     * @param deptCode      部门编码
     * @param parentCode    上级编码
     * @param affilUnitCode 所属部门编码
     * @return SysDept 部门单位
     */
    SysDept selectOneDept(@Param("deptCode") String deptCode, @Param("parentCode") String parentCode, @Param("affilUnitCode") String affilUnitCode);

    List<String> selectUnit(String unitId);

    List<SysDept> selectDeptListUnit(String deptId);

    String selectUnitId(String code);

    /**
     * 根据单位id查询下面的部门
     *
     * @param unitId 单位id
     * @return
     */
    List<SysDeptRpc> selectDeptListByUnitId(@Param("unitId") String unitId);

    int hasExistUnitCode(String unitCode);

    List<SysDept> selectSendData();

    /**
     * 查询单位下所有部门
     *
     * @param unitId
     * @return
     */
    List<Zone> getDeptListByUnitId(@Param("unitId") String unitId);

    /**
     * 根据用户id查询所属部门
     *
     * @param userId 用户id
     * @return 用户所属部门
     */
    List<SysDeptVO> getDeptsByUserId(@Param("userId") String userId);

    /**
     * 获取当前用户单位下所有部门
     *
     * @param unitId 单位id
     * @return 当前用户单位下所有部门
     */
    List<SysDeptVO> getThisDept(@Param("unitId") String unitId);

    /**
     * 根据用户id查询所属部门及下级部门
     *
     * @param userId 单位id
     * @return 用户所属部门及下级部门
     */
    List<SysDeptVO> getDeptsAndSubsetsByUserId(@Param("userId") String userId);

    /**
     * 查询当前单位及所有下级单位
     *
     * @param unitId 单位id
     * @return 当前单位及所有下级单位
     */
    List<SysDeptVO> getThisUnitAndSubsets(@Param("unitId") String unitId);

    /**
     * 根据所属科室查询单位
     *
     * @param businessNames
     * @return
     */
    @Select("select * from sys_dept where del_flag = '0' and status = '0' and dept_type = '0' and business_name in ('${businessNames}')")
    List<SysDept> selectDeptListByBusinessName(@Param("businessNames") String businessNames);

    /**
     * 查询预算科管辖单位
     *
     * @return
     */
    @Select("select * from sys_dept where del_flag = '0' and status = '0' and dept_type = '0' and unit_type in ('0','2','3')")
    List<SysDept> selectDeptListByBudget();

    /**
     * 获取除了本单位的其他单位
     *
     * @param unitId
     * @return
     */
    List<SysDeptVO> getNoThisUnit(@Param("unitId") String unitId);

    List<SysDept> getReimUnit();

    void updateReimById(SysDept dept);

    /**
     * 查询指定单位及所有下级单位ids
     *
     * @param unitId
     * @return
     */
    @Select("select dept_id from (\n" +
            "\tselect * from SYS_DEPT sd\n" +
            "\tstart with sd.dept_id = #{unitId} and sd.dept_type = '0' and sd.status = '0' and sd.del_flag = '0'\n" +
            "\tconnect by sd.parent_id  = prior sd.dept_id\n" +
            ")\n" +
            "where dept_type = '0' and status = '0' and del_flag = '0'")
    List<String> selectUnitIdsByUnitId(@Param("unitId") String unitId);
}
