package com.cxnet.project.system.dept.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.domain.SysDeptVO;
import com.cxnet.rpc.domain.system.deptrpc.SysDeptRpc;

import java.util.List;
import java.util.Map;

/**
 * 部门管理 服务层
 *
 * @author cxnet
 */
public interface SysDeptServiceI extends IService<SysDept> {
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 系统单位切换
     *
     * @param userName 用户
     * @return 用户所在得部门
     */
    public List<SysDept> changeDept(String userName);


    /**
     * 根据角色ID菜单ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    public List<Integer> selectDeptListByRoleId(String roleId, String menuId);

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
     * 是否存在部门子节点
     *
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位编码
     * @return 结果
     */
    public boolean hasChildByDeptCode(String deptCode, String affilUnitCode);

    /**
     * 是否存在岗位子节点
     *
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位编码
     * @return 结果
     */
    public boolean hasPostByDeptCode(String deptCode, String affilUnitCode);

    /**
     * 查询部门是否存在用户
     *
     * @param deptCode 部门编码
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(String deptCode);

    /**
     * 删除部门管理信息
     *
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位编码
     * @return 结果
     */
    public int deleteDeptByCode(String deptCode, String affilUnitCode);

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptNameUnique(SysDept dept);

    /**
     * 校验部门编码是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptCodeUnique(SysDept dept);


    /**
     * 校验单位编码是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkUnitCodeUnique(SysDept dept);

    /**
     * 校验所属单位下部门编码是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptCodeOfUnitUnique(SysDept dept);


    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

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
     * 查询单位下部门列表
     *
     * @param deptCode 单位deptCode
     * @return 部门列表
     */
    public List<Zone> depDeptListSelect(String deptCode);

    /**
     * 查询单位下部门tree
     *
     * @return 部门tree
     */
    public List<Zone> selectDeptListByUnitId(String unitId);

    /**
     * 获取当前用户所在岗位的部门列表，以及部门人员里面所在的部门
     *
     * @param userId
     * @return 部门列表
     */
    public List<SysDept> depListSelectByUserId(String userId, String unitId);

    /**
     * 切换单位树
     *
     * @param userId
     * @return
     */
    List<Zone> changeDeptTree(String userId);

    /**
     * 修改默认单位
     *
     * @param unitId 单位id
     * @return 结果
     */
    public int updateDeptId(String unitId, String userId);

    /**
     * 查找部门名称
     *
     * @param deptCode 部门code
     * @return 部门名称
     */
    public SysDept selectDeptName(String deptCode);

    /**
     * 查找部门id
     *
     * @param deptCode      部门编码
     * @param affilUnitCode 所属部门编码
     */
    SysDept selectOneDept(String deptCode, String parentCode, String affilUnitCode);

    List<SysDept> selectDeptListUnit(SysDept sysDept);

    /**
     * 根据单位id查下属部门
     *
     * @param unitId
     * @return
     */
    List<SysDeptRpc> selectBdPersonByUnitId(String unitId);

    int hasExistUnitCode(String unitCode);

    /**
     * 查询id
     *
     * @param deptCode 部门编码
     */
    String selectUnitId(String deptCode);

    /**
     * 根据用户id查询所属部门
     *
     * @param userId 用户id
     * @return 用户所属部门
     */
    List<SysDeptVO> getDeptsByUserId(String userId);

    /**
     * 根据用户id查询所属部门及下级部门
     *
     * @param userId 用户id
     * @return 用户所属部门及下级部门
     */
    List<SysDeptVO> getDeptsAndSubsetsByUserId(String userId);

    /**
     * 根据当前登录用户查询所属部门
     *
     * @return 当前登录用户所属部门ids
     */
    String getDeptsIdsByUserId();

    /**
     * 根据当前登录用户查询所属部门及下级部门
     *
     * @return 当前登录用户所属部门及下级部门ids
     */
    String getDeptsAndSubsetsIdsByUserId();

    /**
     * 获取单位下所有部门
     *
     * @param unitId 单位id
     * @return 当前用户单位下所有部门
     */
    List<SysDeptVO> getThisDept(String unitId);

    /**
     * 获取当前单位及所有下级单位
     *
     * @return 当前单位及所有下级单位
     */
    List<SysDeptVO> getThisUnitAndSubsets();

    /**
     * 获取当前单位及所有下级单位tree
     *
     * @return 当前单位及所有下级单位tree
     */
    List<Zone> getThisUnitAndSubsetTree();

    /**
     * 获取当前单位及所有下级单位ids
     *
     * @return
     */
    String getThisUnitAndSubsetsIds();

    /**
     * 查询当前用户管辖单位（预算业务）
     *
     * @return
     */
    List<String> getUnitIdsByThisUser();

    /**
     * 根据所属科室查询单位
     *
     * @param departmentCode
     * @return
     */
    List<String> selectDeptListByBusinessName(String departmentCode);

    /**
     * 获取除了本单位的其他单位
     *
     * @param unitId
     * @return
     */
    List<SysDeptVO> getNoThisUnit(String unitId);

    List<SysDept> getReimUnit();

    /**
     * 查询单位tree
     *
     * @return
     */
    List<Zone> getUnitTree();

    int updateReimUnit(SysDept sysDept);
}
