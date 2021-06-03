package com.cxnet.baseData.expeco.bdExpeco.controller;

import com.cxnet.baseData.expeco.bdExpeco.domain.BdExpeco;
import com.cxnet.baseData.expeco.bdExpeco.domain.SelectionBdExpeco;
import com.cxnet.baseData.expeco.bdExpeco.service.BdExpecoServiceI;
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
 * 部门预算经济分类控制层
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Slf4j
@RestController
@Api(tags = "部门预算经济分类")
@RequestMapping("/baseData/expeco")
@Validated
public class BdExpecoController extends BaseController {

    @Autowired(required = false)
    private BdExpecoServiceI bdExpecoService;


    /**
     * 查询部门预算经济分类集合
     *
     * @param bdExpeco 部门预算经济分类
     */
    @ApiOperation("查询部门预算经济分类集合")
    @GetMapping
    public AjaxResult listBdExpeco(BdExpeco bdExpeco) {
        startPage();
        List<BdExpeco> list = bdExpecoService.selectBdExpecoList(bdExpeco);
        return success(getDataTable(list));
    }

    /**
     * 查询部门预算经济分类tree
     *
     * @param bdExpeco 部门预算经济分类
     */
    @ApiOperation("查询部门预算经济分类tree")
    @GetMapping("/tree")
    public AjaxResult treeBdExpeco(BdExpeco bdExpeco) {
        return success(bdExpecoService.selectBdExpecoListTree(bdExpeco));
    }

    /**
     * 选用部门预算经济分类
     *
     * @param selectionBdExpeco 部门预算经济分类
     */
    @ApiOperation("选用部门预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:expeco:selection')")
    @Log(title = "选用部门预算经济分类", businessType = BusinessType.INSERT)
    @PostMapping("/selection")
    public AjaxResult addBdExpfunc(@RequestBody SelectionBdExpeco selectionBdExpeco) {
        return success(bdExpecoService.insertBatchSelectionBdExpeco(selectionBdExpeco));
    }

    /**
     * 查询部门预算经济分类明细
     *
     * @param id 部门预算经济分类ID
     */
    @ApiOperation("查询部门预算经济分类明细")
    @GetMapping("/{id}")
    public AjaxResult getBdExpeco(@NotBlank(message = "部门预算经济分类编号不能为空") @PathVariable String id) {
        return success(bdExpecoService.selectBdExpecoById(id));
    }

    /**
     * 新增部门预算经济分类
     *
     * @param bdExpeco 部门预算经济分类
     */
    @ApiOperation("新增部门预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:expeco:add')")
    @Log(title = "部门预算经济分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addBdExpeco(@RequestBody BdExpeco bdExpeco) {
        bdExpecoService.insertBdExpeco(bdExpeco);
        return success("操作成功", bdExpeco.getExpecoId());
    }

    /**
     * 批量新增部门预算经济分类
     *
     * @param bdExpecos 部门预算经济分类
     */
    @ApiOperation("新增部门预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:expeco:add')")
    @Log(title = "批量新增部门预算经济分类", businessType = BusinessType.INSERT)
    @PostMapping("/batch/{year}")
    public AjaxResult addBdExpeco(@RequestBody List<BdExpeco> bdExpecos, @PathVariable Long year) {
        return success(bdExpecoService.insertBatchBdExpeco(bdExpecos, year));
    }

    /**
     * 修改部门预算经济分类
     *
     * @param bdExpeco 部门预算经济分类
     */
    @ApiOperation("修改部门预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:expeco:update')")
    @Log(title = "部门预算经济分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateBdExpeco(@Validated @RequestBody BdExpeco bdExpeco) {
        return toAjax(bdExpecoService.updateBdExpeco(bdExpeco));
    }

    /**
     * 删除部门预算经济分类
     *
     * @param id 部门预算经济分类ID
     */
    @ApiOperation("删除部门预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:expeco:delete')")
    @Log(title = "部门预算经济分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult deleteBdExpeco(@NotBlank(message = "部门预算经济分类编号不能为空") @PathVariable String id) {
        return toAjax(bdExpecoService.deleteBdExpecoById(id));
    }

    /**
     * 导出部门经济分类
     *
     * @param bdExpeco BdExpeco
     */
    @ApiOperation("导出部门经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:expeco:export')")
    @GetMapping("/export")
    public AjaxResult exportExcel(BdExpeco bdExpeco) {
        List<BdExpeco> list = bdExpecoService.selectBdExpecoList(bdExpeco);
        return success(list);
    }

    /**
     * 部门经济分类结转年度计算
     *
     * @return 结果
     */
    @ApiOperation("部门经济分类结转年度计算")
    @PreAuthorize("@ss.hasPermi('baseData:expeco:inherit')")
    @PostMapping("/inheritYear")
    public AjaxResult carryForwardYear() {
        Map<String, Integer> year = bdExpecoService.carryForwardYear();
        return success(year);
    }

    /**
     * 部门经济分类结转
     *
     * @return 结果
     */
    @ApiOperation("部门经济分类结转")
    @PostMapping("/carryForward/{year}")
    public AjaxResult carryForwardData(@PathVariable("year") String year) {
        bdExpecoService.carryForwardData(year);
        return success();
    }

}
