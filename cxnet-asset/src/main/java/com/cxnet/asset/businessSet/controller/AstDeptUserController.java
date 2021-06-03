package com.cxnet.asset.businessSet.controller;

import cn.hutool.core.util.ObjectUtil;
import com.cxnet.asset.businessSet.domain.AstDeprMethod;
import com.cxnet.asset.businessSet.domain.AstDeptUnder;
import com.cxnet.asset.businessSet.service.AstDeptUnderService;
import com.cxnet.baseData.expfunc.bdExpfunc.domain.BdExpfunc;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.businessSet.domain.AstDeptUser;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * (AstDeptUser)表控制层
 *
 * @author zhangyl
 * @since 2021-03-29 10:39:24
 */
@Slf4j
@Api(tags = "资产管理员")
@RestController
@RequestMapping("/astDeptUser")
public class AstDeptUserController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstDeptUserService astDeptUserService;

    @Resource
    private AstDeptUnderService astDeptUnderService;

    @Resource
    private SysDeptServiceI sysDeptServiceI;

    /**
     * 查询资产管理员tree
     *
     * @param bdExpfunc 资产管理员
     */
    @ApiOperation("查询资产管理员集合tree")
    @GetMapping("/tree")
    public AjaxResult treeAstDeptUser(AstDeptUser astDeptUser) {
        return success(astDeptUserService.selectAstDeptTree(astDeptUser));
    }

    /**
     * 根据登录账号查询当前登录人是什么管理员
     *
     * @param bdExpfunc 资产管理员
     */
    @ApiOperation("根据登录账号查询当前登录人是什么管理员")
    @GetMapping("/getAdmin")
    public AjaxResult getAdmin() {
        String name = "";
        Map<String, List<String>> map = astDeptUserService.getMap();
        if (map.get("unit") != null) {
            name = "adminUser";
        } else if (map.get("dept") != null) {
            name = "deptUser";
        }
        return success(name);
    }

    /**
     * 根据用户账号查询当前登录人是什么管理员
     */
    @ApiOperation("根据用户账号查询当前登录人是什么管理员")
    @GetMapping("/getPerson/{personnelId}")
    public AjaxResult getPerson(@PathVariable String personnelId) {
        String name = "";
        Map<String, List<String>> map = astDeptUserService.getPerMap(personnelId);
        if (map.get("unit") != null) {
            name = "adminUser";
        } else if (map.get("dept") != null) {
            name = "deptUser";
        }
        return success(name);
    }

    /**
     * 查询资产管理员
     *
     * @param bdExpfunc 资产管理员
     */
    @ApiOperation("查询资产管理员集合")
    @GetMapping("/selectUser")
    public AjaxResult selectUser() {
        QueryWrapper<AstDeptUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstDeptUser::getDelFlag, "0");
        List<AstDeptUser> list = astDeptUserService.list(wrapper);
        return success(list);
    }

    /**
     * 查询部门归口管理
     *
     * @param bdExpfunc 部门归口管理
     */
    @ApiOperation("查询部门归口管理集合")
    @GetMapping("/selectUnder")
    public AjaxResult selectUnder() {
        QueryWrapper<AstDeptUnder> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstDeptUnder::getDelFlag, "0");
        List<AstDeptUnder> list = astDeptUnderService.list(wrapper);
        return success(list);
    }


    /**
     * 新增部门归口管理
     *
     * @param astDeptUnder 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增部门归口管理")
    @PreAuthorize("@ss.hasPermi('astDeptUser:astDeptUser:insert')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping("/insertUnder")
    public AjaxResult insertUnder(@RequestBody AstDeptUnder astDeptUnder) {
        return success(astDeptUnderService.save(astDeptUnder));
    }

    /**
     * 资产管理员设置查询
     *
     * @param astDeptUser 实体对象
     * @return 新增结果
     */
    @ApiOperation("资产管理员设置查询")
    @PreAuthorize("@ss.hasPermi('astDeptUser:astDeptUser:insert')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping("/selUser")
    public AjaxResult selUser(@RequestBody AstDeptUser astDeptUser) {
        QueryWrapper<AstDeptUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstDeptUser::getUserName, astDeptUser.getUserName())
                .eq(AstDeptUser::getDeptId, astDeptUser.getDeptId())
                .eq(AstDeptUser::getDelFlag, "0").eq(AstDeptUser::getStatus, "0");
        AstDeptUser one = astDeptUserService.getOne(wrapper);
        if (ObjectUtil.isNotNull(one)) {
            return success(one);
        } else {
            AstDeptUser user = new AstDeptUser();
            user.setUserCode(astDeptUser.getUserCode());
            user.setUserName(astDeptUser.getUserName());
            user.setDeptId(astDeptUser.getDeptId());
            user.setIsDeptAdmin("2");
            user.setIsUnitAdmin("2");
            user.setDelFlag("0");
            user.setStatus("0");
            return success(user);
        }
    }

    /**
     * 部门归口管理设置查询
     *
     * @param astDeptUser 实体对象
     * @return 新增结果
     */
    @ApiOperation("部门归口管理设置查询")
    @PreAuthorize("@ss.hasPermi('astDeptUser:astDeptUser:insert')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping("/selUnder")
    public AjaxResult selUnder(@RequestBody AstDeptUnder astDeptUnder) {
        QueryWrapper<AstDeptUnder> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstDeptUnder::getDeptCode, astDeptUnder.getDeptCode())
                .eq(AstDeptUnder::getDeptName, astDeptUnder.getDeptName())
                .eq(AstDeptUnder::getDelFlag, "0").eq(AstDeptUnder::getStatus, "0");
        AstDeptUnder one = astDeptUnderService.getOne(wrapper);
        if (ObjectUtil.isNotNull(one)) {
            return success(one);
        } else {
            AstDeptUnder user = new AstDeptUnder();
            user.setDeptCode(astDeptUnder.getDeptCode());
            user.setDeptName(astDeptUnder.getDeptName());
            user.setDelFlag("0");
            user.setStatus("0");
            return success(user);
        }
    }

    /**
     * 新增资产管理员
     *
     * @param astDeptUser 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增资产管理员")
    @PreAuthorize("@ss.hasPermi('astDeptUser:astDeptUser:insert')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping("/insertUser")
    public AjaxResult insertUser(@RequestBody AstDeptUser astDeptUser) {
        String status = "1";
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDept::getDeptId, astDeptUser.getDeptId())
                .eq(SysDept::getStatus, "0").eq(SysDept::getDelFlag, "0");
        List<SysDept> sysDeptList = sysDeptServiceI.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(sysDeptList)) {
            astDeptUser.setDeptCode(sysDeptList.get(0).getDeptCode());
            astDeptUser.setDeptName(sysDeptList.get(0).getDeptName());
        }
        QueryWrapper<AstDeptUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstDeptUser::getDelFlag, "0").eq(AstDeptUser::getStatus, "0")
                .eq(AstDeptUser::getUserName, astDeptUser.getUserName())
                .eq(AstDeptUser::getDeptId, astDeptUser.getDeptId());
        List<AstDeptUser> list = astDeptUserService.list(wrapper);
        if (list.size() > 0) {
            throw new CustomException("不可重复新增");
        } else {
            astDeptUserService.save(astDeptUser);
        }
        return success(status);
    }

    /**
     * 修改资产管理员
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("修改资产管理员")
    @PreAuthorize("@ss.hasPermi('astDeptUser:astDeptUser:update')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PostMapping("/updateDept")
    public AjaxResult updateDept(@RequestBody AstDeptUser astDeptUser) {
        astDeptUserService.updateById(astDeptUser);
        QueryWrapper<AstDeptUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstDeptUser::getDelFlag, "0");
        List<AstDeptUser> list = astDeptUserService.list(wrapper);
        return success(list);
    }

    /**
     * 修改部门归口管理
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("修改部门归口管理")
    @PreAuthorize("@ss.hasPermi('astDeptUser:astDeptUser:update')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PostMapping("/updateUnder")
    public AjaxResult updateUnder(@RequestBody AstDeptUnder astDeptUnder) {
        astDeptUnderService.updateById(astDeptUnder);
        QueryWrapper<AstDeptUnder> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstDeptUnder::getDelFlag, "0");
        List<AstDeptUnder> list = astDeptUnderService.list(wrapper);
        return success(list);
    }

    /**
     * 删除部门归口管理
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除部门归口管理")
    @PreAuthorize("@ss.hasPermi('astDeptUser:astDeptUser:delete')")
    @Log(title = "删除部门归口管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteUnder/{id}")
    public AjaxResult delete(@PathVariable String id) {
        AstDeptUnder byId = astDeptUnderService.getById(id);
        byId.setDelFlag("2");
        astDeptUnderService.updateById(byId);
        QueryWrapper<AstDeptUnder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AstDeptUnder::getDelFlag, "0");
        List<AstDeptUnder> list = astDeptUnderService.list(queryWrapper);
        return success(list);
    }

    /**
     * 删除资产管理员
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除资产管理员")
    @PreAuthorize("@ss.hasPermi('astDeptUser:astDeptUser:delete')")
    @Log(title = "删除资产管理员", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteUser/{id}")
    public AjaxResult deleteUser(@PathVariable String id) {
        AstDeptUser byId = astDeptUserService.getById(id);
        byId.setDelFlag("2");
        astDeptUserService.updateById(byId);
        QueryWrapper<AstDeptUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AstDeptUser::getDelFlag, "0");
        List<AstDeptUser> list = astDeptUserService.list(queryWrapper);
        return success(list);
    }
}

