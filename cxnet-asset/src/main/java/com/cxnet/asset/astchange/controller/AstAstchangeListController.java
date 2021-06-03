package com.cxnet.asset.astchange.controller;

import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.cxnet.asset.astchange.service.AstAstchangeListService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产变动明细表(AstAstchangeList)表控制层
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:55
 */
@Slf4j
@Api(tags = "资产变动明细表")
@RestController
@RequestMapping("/astAstchangeList")
public class AstAstchangeListController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstAstchangeListService astAstchangeListService;

    /**
     * 分页查询所有数据
     *
     * @param astAstchangeList 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeList:astAstchangeList:query')")
    @GetMapping
    public AjaxResult selectAll(AstAstchangeList astAstchangeList) {
        startPage();
        QueryWrapper<AstAstchangeList> qw = new QueryWrapper<>(astAstchangeList);
        List<AstAstchangeList> list = astAstchangeListService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeList:astAstchangeList:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(astAstchangeListService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param astAstchangeList 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeList:astAstchangeList:insert')")
    @Log(title = "资产变动明细表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstAstchangeList astAstchangeList) {
        return success(astAstchangeListService.save(astAstchangeList));
    }

    /**
     * 修改数据
     *
     * @param astAstchangeList 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeList:astAstchangeList:update')")
    @Log(title = "资产变动明细表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstAstchangeList astAstchangeList) {
        return success(astAstchangeListService.updateById(astAstchangeList));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeList:astAstchangeList:delete')")
    @Log(title = "资产变动明细表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(astAstchangeListService.removeByIds(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astAstchangeList:astAstchangeList:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }
}

