package com.cxnet.baseData.purItem.controller;

import com.cxnet.baseData.purItem.domain.BdPurItem;
import com.cxnet.baseData.purItem.domain.SelectionPurItem;
import com.cxnet.baseData.purItem.domain.vo.BdPurItemVo;
import com.cxnet.baseData.purItem.service.BdPurItemServiceI;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.utils.poi.ExcelUtil;
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

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 采购品目控制层
 *
 * @author caixx
 * @date 2020-07-21
 */
@Slf4j
@RestController
@Api(tags = "采购品目")
@RequestMapping("/bd/purItem")
@Validated
public class BdPurItemController extends BaseController {

    @Autowired(required = false)
    private BdPurItemServiceI purItemService;


    /**
     * 查询采购品目集合
     *
     * @param purItem 采购品目
     */
    @ApiOperation("查询采购品目集合")
    //  @PreAuthorize("@ss.hasPermi('baseData:purItem:query')")
    @GetMapping
    public AjaxResult listPurItem(BdPurItem purItem) {
        startPage();
        List<BdPurItem> list = purItemService.selectPurItemList(purItem);
        return success(getDataTable(list));
    }

    /**
     * 获取采购品目下拉树列表
     */
    @ApiOperation("获取采购品目下拉树列表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(@Validated BdPurItem purItem) {
        List<Zone> zones = purItemService.selectPurItemListTree(purItem);
        return success(ZoneUtils.buildTreeBypcode(zones));
    }

    /**
     * 校验编码是否存在
     *
     * @return
     */
    @ApiOperation("校验编码是否存在")
    //  @PreAuthorize("@ss.hasPermi('baseData:purItem:query')")
    @GetMapping("/checkItemCode/{itemCode}")
    public AjaxResult checkItemCode(@NotBlank(message = "编码不能为空") @PathVariable String itemCode) {
        boolean flag = purItemService.checkItemCode(itemCode);
        return success(flag);
    }

    /**
     * 查询采购品目明细及子表
     *
     * @param id 采购品目ID
     */
    @ApiOperation("查询采购品目明细及子表")
    //@PreAuthorize("@ss.hasPermi('baseData:purItem:query')")
    @GetMapping("/{id}")
    public AjaxResult getPurItem(@NotBlank(message = "采购品目编号不能为空") @PathVariable String id) {
        return success(purItemService.selectPurItemAndAssetTypeById(id));
    }


    /**
     * 新增采购品目及子表
     *
     * @param purItemVo 采购品目
     */
    @ApiOperation("新增采购品目")
    @PreAuthorize("@ss.hasPermi('baseData:purItem:add')")
    @Log(title = "采购品目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addPurItem(@Validated @RequestBody BdPurItemVo purItemVo) {
        if (purItemService.checkItemCode(purItemVo.getPurItem().getItemCode())) {
            return AjaxResult.error("采购品目编码相同,请修改采购品目编码！！！");
        }
        purItemService.insertPurItemAndAssetType(purItemVo);
        return success("操作成功", purItemVo.getPurItem().getItemId());
    }

    /**
     * 修改采购品目
     *
     * @param purItemVo 采购品目vo
     */
    @ApiOperation("修改采购品目")
    @PreAuthorize("@ss.hasPermi('baseData:purItem:update')")
    @Log(title = "采购品目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updatePurItem(@Validated @RequestBody BdPurItemVo purItemVo) {
        if (purItemService.checkItemCodeUpdate(purItemVo)) {
            return AjaxResult.error("采购品目编码相同,请修改采购品目编码！！！");
        }
        return toAjax(purItemService.updatePurItemAndAssetType(purItemVo));
    }

    /**
     * 删除采购品目
     *
     * @param itemId 采购品目ID
     */
    @ApiOperation("删除采购品目")
    @PreAuthorize("@ss.hasPermi('baseData:purItem:delete')")
    @Log(title = "采购品目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemId}")
    public AjaxResult deletePurItem(@NotBlank(message = "采购品目编号不能为空") @PathVariable String itemId) {
        if (purItemService.hasChildByItemCode(itemId)) {
            return AjaxResult.error("存在下级单位,不允许删除");
        }
        return toAjax(purItemService.deletePurItemById(itemId));
    }

    /**
     * 导出采购品目
     *
     * @param purItem 采购品目
     */
    @ApiOperation("导出采购品目")
    @PreAuthorize("@ss.hasPermi('baseData:purItem:export')")
    @Log(title = "采购品目", businessType = BusinessType.EXPORT)
    @PutMapping("/export")
    public AjaxResult exportPurItem(BdPurItem purItem) {
        List<BdPurItem> list = purItemService.selectPurItemList(purItem);
        ExcelUtil<BdPurItem> util = new ExcelUtil<>(BdPurItem.class);
        return util.exportExcel(list, "采购品目数据");
    }

    /**
     * 批量新增采购品目
     *
     * @param purItem 采购品目
     */
    @ApiOperation("导入采购品目")
    @PreAuthorize("@ss.hasPermi('baseData:purItem:add')")
    @Log(title = "采购品目", businessType = BusinessType.INSERT)
    @PostMapping("/importData")
    public AjaxResult importData(@RequestBody List<BdPurItem> purItem) throws Exception {
        purItemService.checkImportItemCode(purItem);
        return AjaxResult.success(purItemService.insertBatchPurItem(purItem));
    }

    /**
     * 选用采购品目分类
     *
     * @param selectionPurItem 采购品目分类
     */
    @ApiOperation("选用采购品目分类")
    @PreAuthorize("@ss.hasPermi('baseData:purItem:selection')")
    @Log(title = "选用采购品目分类", businessType = BusinessType.INSERT)
    @PostMapping("/selection")
    public AjaxResult addBdExpfunc(@RequestBody SelectionPurItem selectionPurItem) {
        return success(purItemService.insertBatchSelectionPurItem(selectionPurItem));
    }

    /**
     * 导出采购品目分类
     *
     * @param purItem BdPurItem
     */
    @ApiOperation("导出采购品目分类")
    @PreAuthorize("@ss.hasPermi('baseData:purItem:export')")
    @GetMapping("/export")
    public AjaxResult exportExcel(BdPurItem purItem) {
        List<BdPurItem> list = purItemService.selectPurItemList(purItem);
        return success(list);
    }

    /**
     * 采购品目分类结转年度计算
     *
     * @return 结果
     */
    @ApiOperation("采购品目分类结转年度计算")
    @PreAuthorize("@ss.hasPermi('baseData:purItem:inherit')")
    @PostMapping("/inheritYear")
    public AjaxResult carryForwardYear() {
        Map<String, Integer> year = purItemService.carryForwardYear();
        return success(year);
    }

    /**
     * 采购品目分类结转
     *
     * @return 结果
     */
    @ApiOperation("支出功能分类结转")
    @PostMapping("/carryForward/{year}")
    public AjaxResult carryForwardData(@PathVariable("year") String year) {
        purItemService.carryForwardData(year);
        return success();
    }

}
