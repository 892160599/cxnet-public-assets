package com.cxnet.baseData.expfunc.bdExpfunc.controller;

import com.cxnet.baseData.expfunc.bdExpfunc.domain.BdExpfunc;
import com.cxnet.baseData.expfunc.bdExpfunc.domain.SelectionBdExpfunc;
import com.cxnet.baseData.expfunc.bdExpfunc.service.BdExpfuncServiceI;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 支出功能分类控制层
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Slf4j
@RestController
@Api(tags = "支出功能分类")
@RequestMapping("/baseData/expfunc")
@Validated
public class BdExpfuncController extends BaseController {

    @Autowired(required = false)
    private BdExpfuncServiceI bdExpfuncService;


    /**
     * 查询支出功能分类集合
     *
     * @param bdExpfunc 支出功能分类
     */
    @ApiOperation("查询支出功能分类集合")
    @GetMapping
    public AjaxResult listBdExpfunc(BdExpfunc bdExpfunc) {
        startPage();
        List<BdExpfunc> list = bdExpfuncService.selectBdExpfuncList(bdExpfunc);
        return success(getDataTable(list));
    }

    /**
     * 查询支出功能分类tree
     *
     * @param bdExpfunc 支出功能分类
     */
    @ApiOperation("查询支出功能分类集合tree")
    @GetMapping("/tree")
    public AjaxResult treeBdExpfunc(BdExpfunc bdExpfunc) {
        return success(bdExpfuncService.selectBdExpfuncTree(bdExpfunc));
    }

    /**
     * 选用功能分类
     *
     * @param selectionBdExpfunc 支出功能分类
     */
    @ApiOperation("选用功能分类")
    @PreAuthorize("@ss.hasPermi('baseData:expfunc:selection')")
    @Log(title = "选用功能分类", businessType = BusinessType.INSERT)
    @PostMapping("/selection")
    public AjaxResult addBdExpfunc(@RequestBody SelectionBdExpfunc selectionBdExpfunc) {
        return success(bdExpfuncService.insertBatchSelectionBdExpfunc(selectionBdExpfunc));
    }

    /**
     * 查询支出功能分类明细
     *
     * @param id 支出功能分类ID
     */
    @ApiOperation("查询支出功能分类明细")
    //  @PreAuthorize("@ss.hasPermi('baseData:expfunc:query')")
    @GetMapping("/{id}")
    public AjaxResult getBdExpfunc(@NotBlank(message = "支出功能分类编号不能为空") @PathVariable String id) {
        return success(bdExpfuncService.selectBdExpfuncById(id));
    }

    /**
     * 新增支出功能分类
     *
     * @param bdExpfunc 支出功能分类
     */
    @ApiOperation("新增支出功能分类")
    @PreAuthorize("@ss.hasPermi('baseData:expfunc:add')")
    @Log(title = "支出功能分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addBdExpfunc(@RequestBody BdExpfunc bdExpfunc) {
        bdExpfuncService.insertBdExpfunc(bdExpfunc);
        return success("操作成功", bdExpfunc.getFuncId());
    }

    /**
     * 批量新增支出功能分类
     *
     * @param bdExpfuncs 支出功能分类
     */
    @ApiOperation("批量新增支出功能分类")
    @PreAuthorize("@ss.hasPermi('baseData:expfunc:add')")
    @Log(title = "支出功能分类", businessType = BusinessType.INSERT)
    @PostMapping("/batch/{year}")
    public AjaxResult addBdExpfunc(@RequestBody List<BdExpfunc> bdExpfuncs, @PathVariable Long year) {
        return success(bdExpfuncService.insertBatchAssetType(bdExpfuncs, year));
    }

    /**
     * 修改支出功能分类
     *
     * @param bdExpfunc 支出功能分类
     */
    @ApiOperation("修改支出功能分类")
    @PreAuthorize("@ss.hasPermi('baseData:expfunc:update')")
    @Log(title = "支出功能分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateBdExpfunc(@Validated @RequestBody BdExpfunc bdExpfunc) {
        return toAjax(bdExpfuncService.updateBdExpfunc(bdExpfunc));
    }

    /**
     * 删除支出功能分类
     *
     * @param id 支出功能分类ID
     */
    @ApiOperation("删除支出功能分类")
    @PreAuthorize("@ss.hasPermi('baseData:expfunc:delete')")
    @Log(title = "支出功能分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult deleteBdExpfunc(@NotBlank(message = "支出功能分类编号不能为空") @PathVariable String id) {
        return toAjax(bdExpfuncService.deleteBdExpfuncById(id));
    }

    /**
     * 导出支出功能分类
     *
     * @param bdExpfunc BdExpfunc
     */
    @ApiOperation("导出支出功能分类")
    @PreAuthorize("@ss.hasPermi('baseData:expfunc:export')")
    @GetMapping("/export")
    public AjaxResult exportExcel(BdExpfunc bdExpfunc) {
        List<BdExpfunc> list = bdExpfuncService.selectBdExpfuncList(bdExpfunc);
        return success(list);
    }

    /**
     * 支出功能分类结转年度计算
     *
     * @return 结果
     */
    @ApiOperation("支出功能分类结转年度计算")
    @PreAuthorize("@ss.hasPermi('baseData:expfunc:inherit')")
    @PostMapping("/inheritYear")
    public AjaxResult carryForwardYear() {
        Map<String, Integer> year = bdExpfuncService.carryForwardYear();
        return success(year);
    }

    /**
     * 支出功能分类结转
     *
     * @return 结果
     */
    @ApiOperation("支出功能分类结转")
    @PostMapping("/carryForward/{year}")
    public AjaxResult carryForwardData(@PathVariable("year") String year) {
        bdExpfuncService.carryForwardData(year);
        return success();
    }

}
