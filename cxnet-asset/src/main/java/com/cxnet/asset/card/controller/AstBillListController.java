package com.cxnet.asset.card.controller;

import static com.cxnet.common.constant.AjaxResult.success;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 资产操作明细表(AstBillList)表控制层
 *
 * @author makejava
 * @since 2021-04-06 16:47:03
 */
@Api(tags = "资产操作明细表")
@RestController
@RequestMapping("/astBillList")
public class AstBillListController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstBillListService astBillListService;

    /**
     * 分页查询所有数据
     *
     * @param astBillList 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astBillList:astBillList:query')")
    @GetMapping
    public AjaxResult selectAll(AstBillList astBillList) {
        startPage();
        QueryWrapper<AstBillList> qw = new QueryWrapper<>(astBillList);
        List<AstBillList> list = astBillListService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astBillList:astBillList:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(astBillListService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param astBillList 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astBillList:astBillList:insert')")
    @Log(title = "资产操作明细表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstBillList astBillList) {
        return success(astBillListService.save(astBillList));
    }

    /**
     * 修改数据
     *
     * @param astBillList 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astBillList:astBillList:update')")
    @Log(title = "资产操作明细表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstBillList astBillList) {
        return success(astBillListService.updateById(astBillList));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astBillList:astBillList:delete')")
    @Log(title = "资产操作明细表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(astBillListService.removeByIds(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astBillList:astBillList:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }
}

