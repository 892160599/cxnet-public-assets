package com.cxnet.baseData.assetType.controller;

import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.domain.SelectionBdAssetType;
import com.cxnet.baseData.assetType.domain.vo.BdAssetTypeVo;
import com.cxnet.baseData.assetType.service.BdAssetTypeService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产类别控制层
 *
 * @author caixx
 * @date 2020-07-20
 */
@Slf4j
@RestController
@Api(tags = "资产类别")
@RequestMapping("/bd/assetType")
@Validated
public class BdAssetTypeController extends BaseController {

    @Autowired(required = false)
    private BdAssetTypeService assetTypeService;

    /**
     * 查询资产类别树
     */
    @ApiOperation("查询资产类别树")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(BdAssetType assetType) {
        List<Zone> zones = assetTypeService.selectAssetTypeListTree(assetType);
        return success(zones);
    }

    /**
     * 选用资产类别
     *
     * @param selectionBdAssetType 资产类别
     */
    @ApiOperation("选用资产类别")
    @Log(title = "选资产类别", businessType = BusinessType.INSERT)
    @PostMapping("/selection")
    public AjaxResult addBdExpfunc(@RequestBody SelectionBdAssetType selectionBdAssetType) {
        return success(assetTypeService.insertBatchSelectAssetType(selectionBdAssetType));
    }

    /**
     * 查询资产类别明细
     *
     * @param id 资产类别ID
     */
    @ApiOperation("查询资产类别明细")
    @GetMapping("/{assetId}")
    public AjaxResult getAsset(@PathVariable String assetId) {
        BdAssetType bdAssetType = assetTypeService.selectAssetTypeById(assetId);
        return success(bdAssetType);
    }

    /**
     * 查询单位资产类别明细
     *
     * @param id 资产类别ID
     */
    @ApiOperation("查询单位资产类别明细")
    @GetMapping("/getAssetType")
    public AjaxResult getAssetType(String assetId) {
        BdAssetTypeVo bdAssetTypeVo = assetTypeService.selectOne(assetId);
        return success(bdAssetTypeVo);
    }

    /**
     * 新增资产类别
     *
     * @param assetType 资产类别
     */
    @ApiOperation("新增资产类别")
    @Log(title = "资产类别", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult insertType(@RequestBody BdAssetType assetType) {
        int i = assetTypeService.insertAssetType(assetType);
        return success(i);
    }


    /**
     * 修改资产类别
     *
     * @param assetType 资产类别
     */
    @ApiOperation("修改资产类别")
    @Log(title = "资产类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateType(@RequestBody BdAssetType assetType) {
        assetType.setUnitId("*");
        return success(assetTypeService.updateById(assetType));
    }

    /**
     * 修改单位资产类别
     *
     * @param assetType 资产类别
     */
    @ApiOperation("修改单位资产类别")
//    @Log(title = "资产类别", businessType = BusinessType.UPDATE)
    @PutMapping("/updateAssetType")
    public AjaxResult updateAssetType(@RequestBody BdAssetTypeVo assetTypeVo) {
        String assId = assetTypeService.update(assetTypeVo);
        BdAssetTypeVo bdAssetTypeVo = assetTypeService.selectOne(assId);
        return success(bdAssetTypeVo);
    }

    /**
     * 删除资产类别
     *
     * @param id 资产类别ID
     */
    @ApiOperation("删除资产类别")
    @Log(title = "资产类别", businessType = BusinessType.DELETE)
    @DeleteMapping()
    public AjaxResult deleteAssetType(@NotEmpty(message = "资产类别ID不能为空") String[] id) {
        return toAjax(assetTypeService.deleteAssetTypeByIds(id));
    }

    /**
     * 导出资产类别
     *
     * @param assetType BdAssetType
     */
    @ApiOperation("导出资产类别")
    @GetMapping("/export")
    public AjaxResult exportExcel(BdAssetType assetType) {
        List<BdAssetType> list = assetTypeService.selectAssetTypeList(assetType);
        return success(list);
    }

    /**
     * 校验编码是否存在
     *
     * @return
     */
    @ApiOperation("校验编码是否存在")
    //  @PreAuthorize("@ss.hasPermi('baseData:assetType:query')")
    @GetMapping("/checkAssetCode/{assetCode}")
    public AjaxResult checkAssetCode(@NotBlank(message = "编码不能为空") @PathVariable String assetCode) {
        boolean flag = assetTypeService.checkAssetCode(assetCode);
        return success(flag);
    }
}
