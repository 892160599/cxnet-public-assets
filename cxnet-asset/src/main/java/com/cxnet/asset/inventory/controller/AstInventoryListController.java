package com.cxnet.asset.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.inventory.domain.AstInventoryBill;
import com.cxnet.asset.inventory.domain.AstInventoryList;
import com.cxnet.asset.inventory.service.AstInventoryListService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.poi.ExcelUtil;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * (AstInventoryList)表控制层
 *
 * @author zhangyl
 * @since 2021-04-06 17:39:43
 */
@Slf4j
@Api(tags = "资产盘点明细表")
@RestController
@RequestMapping("/astInventoryList")
public class AstInventoryListController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstInventoryListService astInventoryListService;

    /**
     * 查询个人盘点进度
     *
     * @param astInventoryList 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询个人盘点进度")
    @PreAuthorize("@ss.hasPermi('astInventoryList:astInventoryList:query')")
    @GetMapping("/selectAll")
    public AjaxResult selectAll(String applyDeptCode, String empCode, String billNo) {
        QueryWrapper<AstInventoryList> qw = new QueryWrapper<>();
        qw.select("DISTINCT EMP_CODE,EMP_NAME,APPLY_DEPT_CODE,APPLY_DEPT_NAME,IS_CHECK");
        qw.lambda().eq(StringUtils.isNotEmpty(billNo), AstInventoryList::getBillNo, billNo)
                .eq(StringUtils.isNotEmpty(applyDeptCode), AstInventoryList::getApplyDeptCode, applyDeptCode)
                .eq(StringUtils.isNotEmpty(empCode), AstInventoryList::getEmpCode, empCode);
        startPage();
        List<AstInventoryList> list = astInventoryListService.list(qw);
        return success(getDataTable(list));
    }


    /**
     * 查询盘点清单
     *
     * @param astInventoryList 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询盘点清单")
    @PreAuthorize("@ss.hasPermi('astInventoryList:astInventoryList:query')")
    @GetMapping("/selectPan")
    public AjaxResult selectPan(String billNo, String unitId, String applyDeptCode, String empCode) {
        startPage();
        List<AstInventoryList> list = astInventoryListService.selectPan(billNo, unitId, applyDeptCode, empCode);
        return success(getDataTable(list));
    }

    /**
     * 查询录入信息
     *
     * @param astInventoryList 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询录入信息")
    @PreAuthorize("@ss.hasPermi('astInventoryList:astInventoryList:query')")
    @GetMapping("/selectTory")
    public AjaxResult selectTory(String billNo, String userId) {
        startPage();
        List<AstInventoryList> list = astInventoryListService.selectTory(billNo, userId);
        return success(getDataTable(list));
    }


    /**
     * 修改录入数据
     *
     * @param astInventoryList 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改录入数据")
    @PreAuthorize("@ss.hasPermi('astInventoryList:astInventoryList:update')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PostMapping("/updateTory")
    public AjaxResult updateTory(@RequestBody List<AstInventoryList> astInventoryLists) {
        List<AstInventoryList> inventoryLists = astInventoryListService.updateTory(astInventoryLists);
        return success(inventoryLists);
    }


    /**
     * 查询盘点汇总
     *
     * @param astInventoryList 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询盘点汇总")
    @PreAuthorize("@ss.hasPermi('astInventoryList:astInventoryList:query')")
    @GetMapping("/selectList")
    public AjaxResult selectList(String billNo, String empCode, String applyDeptCode) {
        QueryWrapper<AstInventoryList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstInventoryList::getBillNo, billNo).eq(AstInventoryList::getDelFlag, "0")
                .eq(StringUtils.isNotEmpty(applyDeptCode), AstInventoryList::getApplyDeptCode, applyDeptCode)
                .eq(StringUtils.isNotEmpty(empCode), AstInventoryList::getEmpCode, empCode);
        startPage();
        List<AstInventoryList> list = astInventoryListService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 查询部门盘点进度
     *
     * @param astInventoryList 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询部门盘点进度")
    @PreAuthorize("@ss.hasPermi('astInventoryList:astInventoryList:query')")
    @PostMapping("/dept")
    public AjaxResult selectDept(String billNo) {
        startPage();
        List<AstInventoryList> astInventoryLists = astInventoryListService.selectDept(billNo);
        return success(getDataTable(astInventoryLists));
    }

    /**
     * 查询盘点信息
     *
     * @param astInventoryList 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询盘点信息")
    @PreAuthorize("@ss.hasPermi('astInventoryList:astInventoryList:query')")
    @PostMapping("/selectCheck")
    public AjaxResult selectCheck(@RequestBody AstInventoryList astInventoryList) {
        QueryWrapper<AstInventoryList> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstInventoryList::getBillNo, astInventoryList.getBillNo())
                .eq(AstInventoryList::getApplyDeptCode, astInventoryList.getApplyDeptCode())
                .eq(AstInventoryList::getEmpCode, astInventoryList.getEmpCode());
        List<AstInventoryList> list = astInventoryListService.list(wrapper);
        return success(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astInventoryList:astInventoryList:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(astInventoryListService.getById(id));
    }

    /**
     * 资产盘点导入
     *
     * @param astInventoryLists
     * @return
     */
    @ApiOperation("资产盘点导入")
    @PreAuthorize("@ss.hasPermi('bugProjectLibrary:bugProjectLibrary:import')")
    @PostMapping("/import")
    public AjaxResult projectLibraryImp(@RequestBody List<AstInventoryList> astInventoryLists) {
        return success(astInventoryListService.insertBatchList(astInventoryLists));
    }

    /**
     * 资产盘点导出
     *
     * @param astInventoryLists
     * @return
     */
    @ApiOperation("资产盘点导出")
    @PreAuthorize("@ss.hasPermi('bugProjectLibrary:bugProjectLibrary:export')")
    @GetMapping("/export")
    public AjaxResult projectLibraryExport(AstInventoryBill astInventoryBill) {
        QueryWrapper<AstInventoryList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstInventoryList::getDelFlag, "0").eq(AstInventoryList::getStatus, "3")
                .eq(AstInventoryList::getBillNo, astInventoryBill.getBillNo());
        List<AstInventoryList> list = astInventoryListService.list(qw);
        if (CollectionUtils.isEmpty(list)) {
            throw new CustomException("未能查询到该资产下的盘点信息");
        }
        ExcelUtil<AstInventoryList> util = new ExcelUtil<>(AstInventoryList.class);
        return util.exportExcel(list, "资产盘点明细表数据");
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astInventoryList:astInventoryList:delete')")
    @Log(title = "", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        ids.forEach(v -> {
            AstInventoryList byId = astInventoryListService.getById(v);
            byId.setDelFlag("2");
            astInventoryListService.updateById(byId);
        });
        return success("删除成功");
    }

    /**
     * 插入资产处置数据
     *
     * @param astInventoryLists 实体对象
     * @return 修改结果
     */
    @ApiOperation("插入资产处置数据")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping("/insertDispose")
    public AjaxResult insertDispose(List<AstInventoryList> astInventoryLists) {
        String dispose = astInventoryListService.insertDispose(astInventoryLists);
        return success(dispose);
    }

}

