package com.cxnet.asset.businessSet.controller;

import com.cxnet.asset.assetcardstyle.service.AstCardStyleService;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.businessSet.domain.AstDeprMethod;
import com.cxnet.asset.businessSet.service.AstDeprMethodService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产折旧方法表(AstDeprMethod)表控制层
 *
 * @author zhangyl
 * @since 2021-03-25 10:00:10
 */
@Slf4j
@Api(tags = "资产折旧方法表")
@RestController
@RequestMapping("/astDeprMethod")
@Validated
public class AstDeprMethodController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstDeprMethodService astDeprMethodService;

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @ApiOperation("查询所有数据")
    @GetMapping("/selectAll")
    public AjaxResult selectAll(String deprMethodName, String unitId, String status) {
        QueryWrapper<AstDeprMethod> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstDeprMethod::getDelFlag, "0").eq(AstDeprMethod::getStatus, status)
                .like(deprMethodName != null, AstDeprMethod::getDeprMethodName, deprMethodName);
        List<AstDeprMethod> list = astDeprMethodService.list(wrapper);
        list.sort(Comparator.comparing(AstDeprMethod::getDeprMethodCode));
        return success(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astDeprMethod:astDeprMethod:query')")
    @GetMapping("/selectOne/{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(astDeprMethodService.getById(id));
    }

    /**
     * 通过单位id查询单条数据
     *
     * @param unitId 主键
     * @return 单条数据
     */
    @ApiOperation("通过单位id查询单条数据")
    @PreAuthorize("@ss.hasPermi('astDeprMethod:astDeprMethod:query')")
    @GetMapping("/selectUid/{unitId}")
    public AjaxResult selectUid(@PathVariable String unitId) {
        QueryWrapper<AstDeprMethod> wrapper = new QueryWrapper();
        wrapper.eq("UNIT_ID", unitId).eq("DEL_FLAG", "0").eq("status", "0");
        List<AstDeprMethod> list = astDeprMethodService.list(wrapper);
        return success(list);
    }

    /**
     * 新增数据
     *
     * @param astDeprMethod 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astDeprMethod:astDeprMethod:insert')")
    @Log(title = "资产折旧方法表", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    public AjaxResult insert(@RequestBody AstDeprMethod astDeprMethod) {
        astDeprMethod.setDelFlag("0");
        return success(astDeprMethodService.save(astDeprMethod));
    }

    /**
     * 修改数据
     *
     * @param astDeprMethod 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astDeprMethod:astDeprMethod:update')")
    @Log(title = "资产折旧方法表", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult update(@RequestBody AstDeprMethod astDeprMethod) {
        return success(astDeprMethodService.updateById(astDeprMethod));
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astDeprMethod:astDeprMethod:delete')")
    @Log(title = "资产折旧方法表", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(String id) {
        AstDeprMethod byId = astDeprMethodService.getById(id);
        byId.setDelFlag("2");
        boolean b = astDeprMethodService.updateById(byId);
        return success(b);
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astDeprMethod:astDeprMethod:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }
}

