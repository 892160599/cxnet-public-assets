package com.cxnet.baseData.assetType.controller;

import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.baseData.assetType.domain.BdAnnex;
import com.cxnet.baseData.assetType.service.BdAnnexService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 基础资料附件表(BdAnnex)表控制层
 *
 * @author zhangyl
 * @since 2021-03-26 13:56:47
 */
@Slf4j
@Api(tags = "基础资料附件表")
@RestController
@RequestMapping("/bdAnnex")
public class BdAnnexController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private BdAnnexService bdAnnexService;

    /**
     * 分页查询所有数据
     *
     * @param bdAnnex 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('bdAnnex:bdAnnex:query')")
    @GetMapping
    public AjaxResult selectAll(BdAnnex bdAnnex) {
        startPage();
        QueryWrapper<BdAnnex> qw = new QueryWrapper<>(bdAnnex);
        List<BdAnnex> list = bdAnnexService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('bdAnnex:bdAnnex:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(bdAnnexService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param bdAnnex 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('bdAnnex:bdAnnex:insert')")
    @Log(title = "基础资料附件表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody BdAnnex bdAnnex) {
        return success(bdAnnexService.save(bdAnnex));
    }

    /**
     * 修改数据
     *
     * @param bdAnnex 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('bdAnnex:bdAnnex:update')")
    @Log(title = "基础资料附件表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody BdAnnex bdAnnex) {
        return success(bdAnnexService.updateById(bdAnnex));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('bdAnnex:bdAnnex:delete')")
    @Log(title = "基础资料附件表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@NotEmpty(message = "至少选中一条要删除的数据！") @RequestBody List<String> ids) {
        return success(bdAnnexService.removeByIds(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('bdAnnex:bdAnnex:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }
}

