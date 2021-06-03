package com.cxnet.project.system.dept.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cxnet.common.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.constant.UserConstants;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.TreeUtil;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.post.domain.SysPost;
import com.cxnet.project.system.post.service.SysPostServiceI;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 部门信息
 *
 * @author cxnet
 */
@RestController
@Api(tags = "部门信息")
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    @Autowired(required = false)
    private SysDeptServiceI deptService;

    @Autowired(required = false)
    private SysPostServiceI postService;

    /**
     * 获取部门列表
     */
    @ApiOperation("获取部门列表")
    @GetMapping
    public AjaxResult list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        JSONArray objects = new TreeUtil().toTree(JSONArray.parseArray(JSON.toJSONString(depts)), "deptId", "parentId");
        return AjaxResult.success(JSONObject.parseArray(objects.toJSONString(), SysDept.class));
    }

    /**
     * 根据部门编号获取详细信息
     */
    @ApiOperation("根据部门编号获取详细信息")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable String deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptIds 部门ID
     * @return 部门信息
     */
    @ApiOperation("根据部门ID查询信息")
    @PostMapping("/selectDeptListByIds")
    public AjaxResult selectDeptListByIds(@RequestBody String[] deptIds) {
        return AjaxResult.success(deptService.selectDeptListByIds(deptIds));
    }

    /**
     * 获取部门下拉树列表
     */
    @ApiOperation("获取部门下拉树列表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept sysDept) {
        List<SysDept> list = deptService.selectDeptList(sysDept);

        List<Zone> zoneList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list)) {
            zoneList = list.stream().map(obj -> {
                Zone zone = new Zone();
                if (null != obj) {
                    zone.setId(null == obj.getDeptId() ? "" : obj.getDeptId());
                    zone.setPid(null == obj.getParentId() ? "" : obj.getParentId());
                    zone.setLabel("[" + obj.getDeptCode() + "]" + obj.getDeptName());
                    zone.setType(obj.getDeptType());
                    zone.setCode(obj.getDeptCode());
                    zone.setPCode(obj.getParentCode());
                    zone.setName(obj.getDeptName());
                }
                return zone;
            }).collect(Collectors.toList());
        }

        return AjaxResult.success(ZoneUtils.buildTree(zoneList));
    }

    /**
     * 获取组织机构下拉树列表
     */
    @ApiOperation("获取组织机构下拉树列表")
    @GetMapping("/orgTreeSelect")
    public AjaxResult orgTreeSelect() {
        List<Map> list = deptService.orgTreeSelectList();

        List<Zone> zoneList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list)) {
            zoneList = list.stream().map(obj -> {
                Zone zone = new Zone();
                if (null != obj) {
                    zone.setId(null == obj.get("id") ? "" : obj.get("id").toString());
                    zone.setPid(null == obj.get("pid") ? "" : obj.get("pid").toString());
                    zone.setLabel(null == obj.get("label") ? "" : obj.get("label").toString());
                    zone.setType(null == obj.get("type") ? "" : obj.get("type").toString());
                    zone.setCode(null == obj.get("code") ? "" : obj.get("code").toString());
                    zone.setPCode(null == obj.get("pCode") ? "" : obj.get("pCode").toString());

                }
                return zone;
            }).collect(Collectors.toList());
        }
        return AjaxResult.success(ZoneUtils.buildTree(zoneList));
    }

    /**
     * 获取组织机构用户下拉树列表
     */
    @ApiOperation("获取组织机构用户下拉树列表")
    @GetMapping("/orgUserTreeSelect")
    public AjaxResult orgUserTreeSelect() {
        List<Map> list = deptService.orgUserTreeSelectList();
        List<Zone> zoneList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list)) {
            zoneList = list.stream().map(obj -> {
                Zone zone = new Zone();
                zone.setId(obj.get("id") + "");
                zone.setPid(obj.get("pid") + "");
                zone.setLabel(obj.get("label") + "");
                zone.setType(obj.get("type") + "");
                zone.setCode(obj.get("code") + "");
                zone.setPCode(obj.get("pCode") + "");
                return zone;
            }).collect(Collectors.toList());
        }

        return AjaxResult.success(ZoneUtils.buildTree(zoneList));
    }

    /**
     * 加载对应角色菜单部门列表树
     */
    @ApiOperation("加载对应角色菜单部门列表树")
    @GetMapping(value = "/roleDeptTreeselect/{roleId}/{menuId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") String roleId, @PathVariable String menuId) {
        return AjaxResult.success(deptService.selectDeptListByRoleId(roleId, menuId));
    }

    /**
     * 新增部门
     */
    @ApiOperation("新增部门")
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        String msg = "0".equals(dept.getDeptType()) ? "新增单位'" : "新增部门'";

        // 判断单位编码是否唯一
        if ("0".equals(dept.getDeptType())) {
            if (UserConstants.NOT_UNIQUE.equals(deptService.checkUnitCodeUnique(dept))) {
                return AjaxResult.error(msg + dept.getDeptName() + "'失败，编码已存在");
            }

        } else {
            if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptCodeOfUnitUnique(dept))) {
                return AjaxResult.error(msg + dept.getDeptName() + "'失败，编码已存在");
            }
        }

        dept.setCreateBy(SecurityUtils.getUsername());


        return AjaxResult.success("操作成功", deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @ApiOperation("修改部门")
    @PreAuthorize("@ss.hasPermi('system:dept:update')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated @RequestBody SysDept dept) {
        String msg = "0".equals(dept.getDeptType()) ? "修改单位'" : "修改部门'";

        if ("0".equals(dept.getDeptType())) {
            if (UserConstants.NOT_UNIQUE.equals(deptService.checkUnitCodeUnique(dept))) {
                return AjaxResult.error(msg + dept.getDeptName() + "'失败，编码已存在");
            }
        } else if ("1".equals(dept.getDeptType())) {
            if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptCodeOfUnitUnique(dept))) {
                return AjaxResult.error(msg + dept.getDeptName() + "'失败，编码已存在");
            }
        }

        if (dept.getDeptId().equals(dept.getParentId()) || dept.getDeptCode().equals(dept.getParentCode())) {
            return AjaxResult.error(msg + dept.getDeptName() + "'失败，上级机构不能是自己");
        }

        if ("0".equals(dept.getDeptType())) {
            dept.setAffilUnitId(dept.getParentId());
            dept.setAffilUnitCode(StringUtils.isEmpty(dept.getParentCode()) ? dept.getDeptCode() : dept.getParentCode());
            dept.setAffilUnitName(StringUtils.isEmpty(dept.getParentName()) ? dept.getDeptName() : dept.getParentName());
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @ApiOperation("删除部门")
    @PreAuthorize("@ss.hasPermi('system:dept:delete')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptCode}/{affilUnitCode}")
    public AjaxResult delete(@PathVariable String deptCode, @PathVariable String affilUnitCode) {

        if ("null".equals(affilUnitCode) || deptCode.equals(affilUnitCode)) {
            affilUnitCode = "";
        }

        if (deptService.hasChildByDeptCode(deptCode, affilUnitCode)) {
            return AjaxResult.error("存在下级部门或单位,不允许删除");
        }
        if (deptService.hasPostByDeptCode(deptCode, affilUnitCode)) {
            return AjaxResult.error("存在下级岗位,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptCode)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }


        return toAjax(deptService.deleteDeptByCode(deptCode, affilUnitCode));
    }

    @ApiOperation("导出部门信息")
    @Log(title = "部门管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dept:export')")
    @GetMapping("/export")
    public AjaxResult export(SysDept dept) {
        List<SysDept> list = deptService.selectDeptList(dept);
        ExcelUtil<SysDept> util = new ExcelUtil<>(SysDept.class);
        return util.exportExcel(list, "部门数据");
    }

    @ApiOperation("系统单位切换")
    @GetMapping(value = "/changeDept")
    public AjaxResult changeDept(@RequestParam String userName) {
        List<SysDept> list = deptService.changeDept(userName);
        return AjaxResult.success(list);
    }

    @ApiOperation("系统单位切换tree")
    @GetMapping(value = "/changeDeptTree")
    public AjaxResult changeDeptTree() {
        String userId = SecurityUtils.getLoginUser().getUser().getUserId();
        List<Zone> zoneList = deptService.changeDeptTree(userId);
        return AjaxResult.success(ZoneUtils.buildTree(zoneList));
    }

    /**
     * 获取单位下部门列表
     */
    @ApiOperation("获取单位下部门列表")
    @GetMapping(value = "/depListSelect/{deptCode}")
    public AjaxResult depDeptListSelect(@PathVariable("deptCode") String deptCode) {
        List<Zone> zoneList = deptService.depDeptListSelect(deptCode);
        return AjaxResult.success(ZoneUtils.buildTreeByTopVal(zoneList, deptCode));
    }

    /**
     * 获取单位下部门列表
     */
    @ApiOperation("获取当前用户所在岗位的部门列表，以及部门人员里面所在的部门")
    @GetMapping(value = "/depListSelectByUserId/{userId}/{unitId}")
    public AjaxResult depListSelectByUserId(@PathVariable("userId") String userId, @PathVariable("unitId") String unitId) {
        List<SysDept> zoneList = deptService.depListSelectByUserId(userId, unitId);
        return AjaxResult.success(zoneList);
    }

    /**
     * 导入Excel数据
     *
     * @param dept 部门信息
     */
    @ApiOperation("导入Excel数据")
    @PreAuthorize("@ss.hasPermi('system:dept:importExcelData')")
    @PostMapping("/importExcelData")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult importExcelData(@RequestBody List<SysDept> dept) {
        int i = 2;
        for (SysDept v : dept) {
            if (StringUtils.isEmpty(v.getDeptName()) || StringUtils.isEmpty(v.getDeptCode()) || StringUtils.isEmpty(v.getDeptType())) {
                throw new CustomException("第" + i + "行,缺少机构名称/机构编码/机构类型");
            }
            if (!"2".equals(v.getDeptType())) {
                // 判断单位是否拥有上级
                if ("0".equals(v.getDeptType()) && StringUtils.isEmpty(v.getParentId()) && StringUtils.isEmpty(v.getAffilUnitCode())) {
                    v.setParentId("0");
                    v.setAffilUnitCode(v.getDeptCode());
                    v.setAffilUnitName(v.getDeptName());
                } else if ("1".equals(v.getDeptType())) {
                    if (StringUtils.isEmpty(v.getAffilUnitCode()) && StringUtils.isEmpty(v.getParentId())) {
                        throw new CustomException("第" + i + "行,[" + v.getDeptCode() + "]" + v.getDeptName() + "缺少父机构ID或所属单位编码");
                    }
                }
                SysDept exists = deptService.selectOneDept(v.getDeptCode(), v.getParentId(), v.getAffilUnitCode());
                if (exists != null) {
                    throw new CustomException("第" + i + "行,已存在[" + v.getDeptCode() + "]" + v.getDeptName() + "部门/单位");
                }
                // 部门/非顶级单位
                v.setParentCode(v.getParentId());
                if (!"0".equals(v.getParentId())) {
                    SysDept sysDept = deptService.selectOneDept(v.getParentId(), null, v.getAffilUnitCode());
                    if (sysDept == null) {
                        throw new CustomException("第" + i + "行,不存在[" + v.getParentId() + "]父机构或[" + v.getAffilUnitCode() + "]所属单位");
                    }
                    // 子部门设置上级部门
                    if ("1".equals(sysDept.getDeptType())) {
                        v.setParentDeptId(sysDept.getDeptId());
                        v.setParentDeptCode(sysDept.getDeptCode());
                        v.setParentDeptName(sysDept.getDeptName());
                    }
                    // 查询顶级单位
                    String deptId = deptService.selectUnitId(v.getAffilUnitCode());
                    if (StringUtils.isEmpty(deptId)) {
                        throw new CustomException("第" + i + "行,不存在[" + v.getAffilUnitCode() + "]所属单位");
                    }
                    // 设置所属单位信息
                    v.setParentId(sysDept.getDeptId());
                    v.setAffilUnitId(deptId);
                    v.setAffilUnitName(sysDept.getAffilUnitName());
                }
                // 设置基础值
                v.setStatus("0");
                v.setCreateBy(SecurityUtils.getUsername());
                deptService.insertDept(v);
            } else {
                if (StringUtils.isEmpty(v.getAffilUnitCode()) || StringUtils.isEmpty(v.getParentId())) {
                    throw new CustomException("第" + i + "行,[" + v.getDeptCode() + "]" + v.getDeptName() + "岗位缺少父机构id/所属单位编码");
                }
                SysPost exists = postService.selectOnePost(v.getDeptCode(), v.getParentId(), v.getAffilUnitCode());
                if (exists != null) {
                    throw new CustomException("第" + i + "行,同部门下已存在[" + exists.getPostCode() + "]" + exists.getPostName() + "岗位");
                }
                SysDept sysDept = deptService.selectOneDept(v.getParentId(), null, v.getAffilUnitCode());
                if (sysDept == null) {
                    throw new CustomException("第" + i + "行,不存在[" + v.getParentId() + "]父机构或[" + v.getAffilUnitCode() + "]所属单位");
                }
                String deptId = deptService.selectUnitId(v.getAffilUnitCode());
                if (StringUtils.isEmpty(deptId)) {
                    throw new CustomException("第" + i + "行,不存在[" + v.getAffilUnitCode() + "]所属单位");
                }
                SysPost post = new SysPost();
                post.setCreateBy(SecurityUtils.getUsername());
                post.setPostName(v.getDeptName());
                post.setPostCode(v.getDeptCode());
                post.setDeptCode(v.getParentId());
                post.setAffilUnitCode(v.getAffilUnitCode());
                post.setPostSort("0");
                post.setStatus("0");
                post.setDeptId(sysDept.getDeptId());
                post.setAffilUnitId(deptId);
                post.setAffilUnitName(sysDept.getAffilUnitName());
                postService.insertPost(post);
            }
            i++;
        }
        return AjaxResult.success();
    }


    /**
     * 获取部门下拉树列表
     */
    @ApiOperation("获取可见单位下拉树列表")
    @GetMapping("/treeselectUnit")
    public AjaxResult treeselectUnit(SysDept sysDept) {
        List<SysDept> list = deptService.selectDeptListUnit(sysDept);

        List<Zone> zoneList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list)) {
            zoneList = list.stream().map(obj -> {
                Zone zone = new Zone();
                if (null != obj) {
                    zone.setId(null == obj.getDeptId() ? "" : obj.getDeptId().toString());
                    zone.setPid(null == obj.getParentId() ? "" : obj.getParentId().toString());
                    zone.setLabel("[" + obj.getDeptCode() + "]" + obj.getDeptName());
                    zone.setType(obj.getDeptType());
                    zone.setCode(obj.getDeptCode());
                    zone.setPCode(obj.getParentCode());
                }
                return zone;
            }).collect(Collectors.toList());
        }

        return AjaxResult.success(ZoneUtils.buildTree(zoneList));
    }

    /**
     * 获取单位下的部门，及部门人员
     */
    @ApiOperation("获取单位下的部门，及部门人员")
    @GetMapping("/personbyunitid")
    public AjaxResult selectBdPerSonByUnitid(String unitId) {
        return AjaxResult.success(deptService.selectBdPersonByUnitId(unitId));
    }


    /**
     * 获取当前用户所属的部门
     *
     * @param userId 用户id
     * @return
     */
    @ApiOperation("获取当前用户所属的部门")
    @GetMapping("/{userId}/dept")
    public AjaxResult getDeptsByUserId(@PathVariable String userId) {
        return AjaxResult.success(deptService.getDeptsByUserId(userId));
    }

    /**
     * 获取当前用户单位下所有部门
     *
     * @return
     */
    @ApiOperation("获取当前用户单位下所有部门")
    @GetMapping("/getThisDept")
    public AjaxResult getThisDept() {
        return AjaxResult.success(deptService.getThisDept(SecurityUtils.getLoginUser().getUser().getDeptId()));
    }

    /**
     * 获取除了本单位的其他单位
     *
     * @return
     */
    @ApiOperation("获取除了本单位的其他单位")
    @GetMapping("/getNoThisUnit")
    public AjaxResult getNoThisUnit() {
        return AjaxResult.success(deptService.getNoThisUnit(SecurityUtils.getLoginUser().getUser().getDeptId()));
    }

    /**
     * 获取单位tree
     *
     * @return
     */
    @ApiOperation("获取单位tree")
    @GetMapping("/getUnitTree")
    public AjaxResult getUnitTree() {
        List<Zone> unitTree = deptService.getUnitTree();
        return AjaxResult.success(ZoneUtils.buildTree(unitTree));
    }


    /**
     * 获取公务之家单位
     *
     * @return 结果
     */
    @ApiOperation("获取公务之家单位")
    @GetMapping("/getReim")
    public AjaxResult getReimUnit() {
        return AjaxResult.success(deptService.getReimUnit());
    }

    /**
     * 获取公务之家单位
     *
     * @return 结果
     */
    @ApiOperation("获取公务之家单位")
    @PostMapping("/updateReim")
    public AjaxResult updateReim(@RequestBody SysDept sysDept) {
        return AjaxResult.success(deptService.updateReimUnit(sysDept));
    }

}
