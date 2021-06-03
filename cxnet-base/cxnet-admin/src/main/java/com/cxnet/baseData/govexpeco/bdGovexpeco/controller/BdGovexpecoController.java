package com.cxnet.baseData.govexpeco.bdGovexpeco.controller;

import com.cxnet.baseData.govexpeco.bdGovexpeco.domain.BdGovexpeco;
import com.cxnet.baseData.govexpeco.bdGovexpeco.service.BdGovexpecoServiceI;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
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

import java.util.List;
import java.util.Map;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 政府预算经济分类控制层
 *
 * @author cxnet
 * @date 2020-08-17
 */
@Slf4j
@RestController
@Api(tags = "政府预算经济分类")
@RequestMapping("/baseData/govexpeco")
public class BdGovexpecoController extends BaseController {

    @Autowired(required = false)
    private BdGovexpecoServiceI bdGovexpecoService;


    /**
     * 查询政府预算经济分类集合
     *
     * @param bdGovexpeco 政府预算经济分类
     */
    @ApiOperation("查询政府预算经济分类集合")
    //@PreAuthorize("@ss.hasPermi('baseData:govexpeco:query')")
    @GetMapping
    public AjaxResult listBdGovexpeco(BdGovexpeco bdGovexpeco) {
        startPage();
        List<BdGovexpeco> list = bdGovexpecoService.selectBdGovexpecoList(bdGovexpeco);
        return success(getDataTable(list));
    }

    /**
     * 查询政府预算经济分类集tree
     *
     * @param bdGovexpeco 政府预算经济分类
     */
    @ApiOperation("查询政府预算经济分类集tree")
//    @PreAuthorize("@ss.hasPermi('baseData:govexpeco:query')")
    @GetMapping("tree")
    public AjaxResult treeBdGovexpeco(BdGovexpeco bdGovexpeco) {
        List<Zone> zones = bdGovexpecoService.selectBdGovexpecoListTree(bdGovexpeco);
        return success(ZoneUtils.buildTreeBypcode(zones));
    }


    /**
     * 查询政府预算经济分类明细
     *
     * @param id 政府预算经济分类ID
     */
    @ApiOperation("查询政府预算经济分类明细")
    // @PreAuthorize("@ss.hasPermi('baseData:govexpeco:query')")
    @GetMapping("/{id}")
    public AjaxResult getBdGovexpeco(@PathVariable String id) {
        return success(bdGovexpecoService.selectBdGovexpecoById(id));
    }

    /**
     * 新增政府预算经济分类
     *
     * @param bdGovexpeco 政府预算经济分类
     */
    @ApiOperation("新增政府预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:govexpeco:add')")
    @Log(title = "政府预算经济分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addBdGovexpeco(@RequestBody BdGovexpeco bdGovexpeco) {
        bdGovexpecoService.insertBdGovexpeco(bdGovexpeco);
        return success("操作成功", bdGovexpeco.getGovId());
    }

    /**
     * 批量新增政府预算经济分类
     *
     * @param bdGovexpeco 政府预算经济分类
     */
    @ApiOperation("批量新增政府预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:govexpeco:add')")
    @Log(title = "政府预算经济分类", businessType = BusinessType.INSERT)
    @PostMapping("/batch/{year}")
    public AjaxResult addBdGovexpeco(@RequestBody List<BdGovexpeco> bdGovexpeco, @PathVariable Long year) {
        return success(bdGovexpecoService.insertBatchBdGovexpeco(bdGovexpeco, year));
    }

    /**
     * 修改政府预算经济分类
     *
     * @param bdGovexpeco 政府预算经济分类
     */
    @ApiOperation("修改政府预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:govexpeco:update')")
    @Log(title = "政府预算经济分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateBdGovexpeco(@Validated @RequestBody BdGovexpeco bdGovexpeco) {
        return toAjax(bdGovexpecoService.updateBdGovexpeco(bdGovexpeco));
    }

    /**
     * 删除政府预算经济分类
     *
     * @param id 政府预算经济分类ID
     */
    @ApiOperation("删除政府预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:govexpeco:delete')")
    @Log(title = "政府预算经济分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult deleteBdGovexpeco(@PathVariable String id) {
        return toAjax(bdGovexpecoService.deleteBdGovexpecoById(id));
    }

    /**
     * 导出政府预算经济分类
     *
     * @param bdGovexpeco BdGovexpeco
     */
    @ApiOperation("导出政府预算经济分类")
    @PreAuthorize("@ss.hasPermi('baseData:govexpeco:export')")
    @GetMapping("/export")
    public AjaxResult exportExcel(BdGovexpeco bdGovexpeco) {
        List<BdGovexpeco> list = bdGovexpecoService.selectBdGovexpecoList(bdGovexpeco);
        return success(list);
    }

    /**
     * 政府预算经济分类结转年度计算
     *
     * @return 结果
     */
    @ApiOperation("政府预算经济分类结转年度计算")
    @PreAuthorize("@ss.hasPermi('baseData:govexpeco:inherit')")
    @PostMapping("/inheritYear")
    public AjaxResult carryForwardYear() {
        Map<String, Integer> year = bdGovexpecoService.carryForwardYear();
        return success(year);
    }

    /**
     * 政府预算经济分类结转
     *
     * @return 结果
     */
    @ApiOperation("政府预算经济分类结转")
    @PostMapping("/carryForward/{year}")
    public AjaxResult carryForwardData(@PathVariable("year") String year) {
        bdGovexpecoService.carryForwardData(year);
        return success();
    }

}
