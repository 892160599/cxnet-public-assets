package com.cxnet.project.system.dept.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.TreeUtil;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.domain.SysDeptVO;
import com.cxnet.project.system.dept.mapper.SysDeptMapper;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.mapper.SysUserMapper;
import com.cxnet.rpc.domain.basedata.BdPersonRpc;
import com.cxnet.rpc.domain.system.deptrpc.SysDeptRpc;
import com.cxnet.rpc.service.basedata.BdPersonServiceRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 * @author cxnet
 */
@Service
public class SysDeptServiceIImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptServiceI {

    @Autowired(required = false)
    private SysDeptMapper deptMapper;
    @Autowired(required = false)
    private SysUserMapper sysUserMapper;
    @Autowired(required = false)
    private BdPersonServiceRpc bdPersonServiceRpc;

    /**
     * 预算科代码
     */
    private static final String BUDGET_CODE = "0003";

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
//    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 根据角色ID菜单ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Integer> selectDeptListByRoleId(String roleId, String menuId) {
        return deptMapper.selectDeptListByRoleId(roleId, menuId);
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(String deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptIds 部门ID
     * @return 部门信息
     */
    @Override
    public List<SysDept> selectDeptListByIds(String[] deptIds) {
        return deptMapper.selectDeptListByIds(deptIds);
    }

    /**
     * 是否存在子节点
     *
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位编码
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptCode(String deptCode, String affilUnitCode) {
        int result = deptMapper.hasChildByDeptCode(deptCode, affilUnitCode);
        return result > 0 ? true : false;
    }

    /**
     * 是否存在岗位子节点
     *
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位编码
     * @return 结果
     */
    @Override
    public boolean hasPostByDeptCode(String deptCode, String affilUnitCode) {
        int result = deptMapper.hasPostByDeptCode(deptCode, affilUnitCode);
        return result > 0 ? true : false;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptCode 部门编码
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(String deptCode) {
        int result = deptMapper.checkDeptExistUser(deptCode);
        return result > 0 ? true : false;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        String deptId = StringUtils.isNull(dept.getDeptId()) ? "-1" : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId(), dept.getParentCode());
        if (StringUtils.isNotNull(info) && !info.getDeptId().equals(deptId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门编码是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptCodeUnique(SysDept dept) {
        String deptId = StringUtils.isNull(dept.getDeptId()) ? "-1" : dept.getDeptId();
        Integer count = deptMapper.checkDeptCodeUniqueByDeptCode(dept.getDeptCode(), deptId, dept.getParentId());
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验单位编码是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkUnitCodeUnique(SysDept dept) {
        String deptId = StringUtils.isNull(dept.getDeptId()) ? "-1" : dept.getDeptId();
        Integer count = deptMapper.checkUnitCodeUnique(dept.getDeptCode(), deptId);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;

    }


    /**
     * 校验所属单位下部门编码是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptCodeOfUnitUnique(SysDept dept) {
        String deptId = StringUtils.isNull(dept.getDeptId()) ? "-1" : dept.getDeptId();

        Integer count = deptMapper.checkDeptCodeOfUnitUnique(dept.getDeptCode(), deptId, dept.getAffilUnitCode());

        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;

    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String insertDept(SysDept dept) {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        if (info == null) {
            dept.setAncestors("'0'");
            deptMapper.insertDept(dept);
            return dept.getDeptId();
        }
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new CustomException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + "'" + dept.getParentId() + "'");
        deptMapper.insertDept(dept);
        return dept.getDeptId();
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + "'" + newParentDept.getDeptId() + "'";
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        } else {
            dept.setAncestors("0");
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatus(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(SysDept dept) {
        String updateBy = dept.getUpdateBy();
        dept = deptMapper.selectDeptById(dept.getDeptId());
        dept.setUpdateBy(updateBy);
        deptMapper.updateDeptStatus(dept);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(String deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(String deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 查询组织机构树集合
     *
     * @return 结果
     */
    @Override
    public List<Map> orgTreeSelectList() {
        return deptMapper.orgTreeSelectList();
    }

    /**
     * 查询组织机构用户树集合
     *
     * @return 结果
     */
    @Override
    public List<Map> orgUserTreeSelectList() {
        return deptMapper.orgUserTreeSelectList();
    }

    /**
     * 批量插入部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertBatchDept(List<SysDept> dept) {
        return deptMapper.insertBatchDept(dept);
    }


    /**
     * 删除部门管理信息
     *
     * @param deptCode      部门编码
     * @param affilUnitCode 所属单位编码
     * @return 结果
     */
    @Override
    public int deleteDeptByCode(String deptCode, String affilUnitCode) {
        return deptMapper.deleteDeptByCode(deptCode, affilUnitCode);
    }

    /**
     * 系统单位切换
     *
     * @param userName 用户
     * @return 用户所在得部门
     */
    @Override
    public List<SysDept> changeDept(String userName) {
        return deptMapper.changeDept(userName);
    }

    /**
     * 查询单位下部门列表
     *
     * @param deptCode 单位deptCode
     * @return 部门列表
     */
    @Override
    public List<Zone> depDeptListSelect(String deptCode) {
        return deptMapper.depDeptListSelect(deptCode);
    }

    /**
     * 查询单位下部门tree
     *
     * @param unitId
     * @return
     */
    @Override
    public List<Zone> selectDeptListByUnitId(String unitId) {
        return deptMapper.getDeptListByUnitId(unitId);
    }

    /**
     * 获取当前用户所在岗位的部门列表，以及部门人员里面所在的部门
     *
     * @param userId
     * @return 部门列表
     */
    @Override
    public List<SysDept> depListSelectByUserId(String userId, String unitId) {
        return deptMapper.depListSelectByUserId(userId, unitId);
    }

    /**
     * 查询系统单位切换tree
     *
     * @param userId
     * @return
     */
    @Override
    public List<Zone> changeDeptTree(String userId) {
        List<Zone> zoneList = deptMapper.changeDeptTreeNew(userId);
        List<String> collect = zoneList.stream().map(Zone::getId).collect(Collectors.toList());
        for (Zone z : zoneList) {
            if (!collect.contains(z.getPid())) {
                z.setPid("0");
            }
        }
        return zoneList;
//        return deptMapper.changeDeptTree(userId);
    }

    /**
     * 修改默认单位
     *
     * @param unitId 单位id
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDeptId(String unitId, String userId) {
        return sysUserMapper.updateDeptId(unitId, userId);
    }

    /**
     * 查找部门名称
     *
     * @param deptCode 部门code
     * @return 部门名称
     */
    @Override
    public SysDept selectDeptName(String deptCode) {
        return deptMapper.selectDeptName(deptCode);
    }

    /**
     * 查找部门id
     *
     * @param deptCode      部门编码
     * @param affilUnitCode 所属部门编码
     */
    @Override
    public SysDept selectOneDept(String deptCode, String parentCode, String affilUnitCode) {
        return deptMapper.selectOneDept(deptCode, parentCode, affilUnitCode);
    }

    @Override
    public List<SysDept> selectDeptListUnit(SysDept sysDept) {
        String deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        return deptMapper.selectDeptListUnit(deptId);
    }

    @Override
    public List<SysDeptRpc> selectBdPersonByUnitId(String unitId) {
        if (StringUtils.isEmpty(unitId)) {
            throw new CustomException("单位编号不能为空");
        }
        List<SysDeptRpc> sysDeptRpcs = deptMapper.selectDeptListByUnitId(unitId);
        if (CollectionUtil.isNotEmpty(sysDeptRpcs)) {
            sysDeptRpcs.forEach(v -> {
                List<BdPersonRpc> bdPersonRpcs = bdPersonServiceRpc.selectPersonByDeptId(v.getParentId(), v.getDeptId());
                v.setBdPersonRpcs(bdPersonRpcs);
            });
        }

        return sysDeptRpcs;
    }

    /**
     * 查询id
     *
     * @param deptCode 部门编码
     */
    @Override
    public String selectUnitId(String deptCode) {
        return deptMapper.selectUnitId(deptCode);
    }

    @Override
    public int hasExistUnitCode(String unitCode) {
        return deptMapper.hasExistUnitCode(unitCode);
    }

    /**
     * 根据用户id查询所属部门
     *
     * @param userId 用户id
     * @return 用户所属部门
     */
    @Override
    public List<SysDeptVO> getDeptsByUserId(String userId) {
        return deptMapper.getDeptsByUserId(userId);
    }

    /**
     * 根据用户id查询所属部门及下级部门
     *
     * @param userId 用户id
     * @return 用户所属部门及下级部门
     */
    @Override
    public List<SysDeptVO> getDeptsAndSubsetsByUserId(String userId) {
        return deptMapper.getDeptsAndSubsetsByUserId(userId);
    }

    /**
     * 根据当前登录用户查询所属部门
     *
     * @return 当前登录用户所属部门ids
     */
    @Override
    public String getDeptsAndSubsetsIdsByUserId() {
        String userId = SecurityUtils.getLoginUser().getUser().getUserId();
        return SysDeptVOToStr(getDeptsAndSubsetsByUserId(userId));
    }

    /**
     * 根据当前登录用户查询所属部门及下级部门
     *
     * @return 当前登录用户所属部门及下级部门ids
     */
    @Override
    public String getDeptsIdsByUserId() {
        String userId = SecurityUtils.getLoginUser().getUser().getUserId();
        return SysDeptVOToStr(getDeptsByUserId(userId));
    }

    /**
     * SysDeptVO中deptId转换为in字符串
     *
     * @param deptsByUserId
     * @return str
     */
    private String SysDeptVOToStr(List<SysDeptVO> deptsByUserId) {
        String deptIds = "";
        if (CollectionUtil.isNotEmpty(deptsByUserId)) {
            return deptsByUserId.stream().map(SysDeptVO::getDeptId).collect(Collectors.joining("','"));
        }
        return deptIds;
    }

    /**
     * 获取单位下所有部门
     *
     * @param unitId 单位id
     * @return 当前用户单位下所有部门
     */
    @Override
    public List<SysDeptVO> getThisDept(String unitId) {
        List<SysDeptVO> sysDeptVOS = new ArrayList<>();
        if (StringUtils.isEmpty(unitId)) {
            return sysDeptVOS;
        }
        return deptMapper.getThisDept(unitId);
    }

    /**
     * 获取当前单位及所有下级单位
     *
     * @return 当前单位及所有下级单位
     */
    @Override
    public List<SysDeptVO> getThisUnitAndSubsets() {
        String unitId = SecurityUtils.getLoginUser().getUser().getDeptId();
        List<SysDeptVO> sysDeptVOS = new ArrayList<>();
        if (StringUtils.isEmpty(unitId)) {
            return sysDeptVOS;
        }
        return deptMapper.getThisUnitAndSubsets(unitId);
    }

    /**
     * 获取当前单位及所有下级单位tree
     *
     * @return 当前单位及所有下级单位tree
     */
    @Override
    public List<Zone> getThisUnitAndSubsetTree() {
        List<SysDeptVO> thisUnitAndSubsets = getThisUnitAndSubsets();
        if (CollectionUtil.isNotEmpty(thisUnitAndSubsets)) {
            return ZoneUtils.buildZone(thisUnitAndSubsets, "deptId", "deptCode", "deptName", "parentId", "parentCode", "fullName", "");
        }
        return new ArrayList<>();
    }

    /**
     * 获取当前单位及所有下级单位ids
     *
     * @return 单位及所有下级单位ids
     */
    @Override
    public String getThisUnitAndSubsetsIds() {
        return SysDeptVOToStr(getThisUnitAndSubsets());
    }

    /**
     * 查询当前用户管辖单位（预算业务）
     *
     * @return
     */
    @Override
    public List<String> getUnitIdsByThisUser() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 所属单位
        SysDept unit = user.getDept();
        // 所属部门
        List<SysDeptVO> dept = this.getDeptsByUserId(user.getUserId());
        // 单位类型
        String unitType = unit.getUnitType();
        // 所属单位id
        String unitId = unit.getDeptId();
        // 所属部门code
        List<String> deptCodes = dept.stream().map(SysDeptVO::getDeptCode).collect(Collectors.toList());
        switch (unitType) {
            // 财政单位
            case "0":
                // 预算科 查看财政单位 主管单位 基层单位
                if (deptCodes.contains(BUDGET_CODE)) {
                    List<SysDept> sysDepts = deptMapper.selectDeptListByBudget();
                    return sysDepts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
                } else {
                    // 其他科室 查看本科室单位
                    return selectDeptListByBusinessName(String.join("','", deptCodes));
                }
                // 主管 本单位
            case "2":
                // todo 街道
            default:

        }
        return CollUtil.newArrayList(unitId);
    }

    /**
     * 根据所属科室查询单位
     *
     * @param departmentCode
     * @return unitIds
     */
    @Override
    public List<String> selectDeptListByBusinessName(String departmentCode) {
        List<SysDept> sysDepts = deptMapper.selectDeptListByBusinessName(departmentCode);
        List<String> unitIds = sysDepts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(unitIds)) {
            unitIds.add("");
        }
        return unitIds;
    }

    /**
     * 获取除了本单位的其他单位
     *
     * @param unitId
     * @return
     */
    @Override
    public List<SysDeptVO> getNoThisUnit(String unitId) {
        List<SysDeptVO> sysDeptVOS = new ArrayList<>();
        if (StringUtils.isEmpty(unitId)) {
            return sysDeptVOS;
        }
        return deptMapper.getNoThisUnit(unitId);
    }

    /**
     * 查询单位tree
     *
     * @return
     */
    @Override
    public List<Zone> getUnitTree() {
        return deptMapper.getUnitTree();
    }

    @Override
    public List<SysDept> getReimUnit() {
        return deptMapper.getReimUnit();
    }


    @Override
    public int updateReimUnit(SysDept sysDept) {

        return 1;
    }

}
