package com.cxnet.basic.expModuleConfig.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.basic.expModuleConfig.domain.ExpModuleConfig;
import com.cxnet.basic.expModuleConfig.service.ExpModuleConfigService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 报销单据模块配置(ExpModuleConfig)表控制层
 *
 * @author caixx
 * @since 2021-01-14 18:13:34
 */
@Slf4j
@Api(tags = "报销单据模块配置")
@RestController
@RequestMapping("/expModuleConfig")
public class ExpModuleConfigController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private ExpModuleConfigService expModuleConfigService;

    /**
     * 分页查询所有数据
     *
     * @param expModuleConfig 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('expModuleConfig:expModuleConfig:query')")
    @GetMapping
    public AjaxResult selectAll(ExpModuleConfig expModuleConfig) {
        startPage();
        QueryWrapper<ExpModuleConfig> qw = new QueryWrapper<>(expModuleConfig);
        List<ExpModuleConfig> list = expModuleConfigService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('expModuleConfig:expModuleConfig:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(expModuleConfigService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param expModuleConfig 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('expModuleConfig:expModuleConfig:insert')")
    @Log(title = "报销单据模块配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody ExpModuleConfig expModuleConfig) {
        return success(expModuleConfigService.save(expModuleConfig));
    }

    /**
     * 修改数据
     *
     * @param expModuleConfig 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('expModuleConfig:expModuleConfig:update')")
    @Log(title = "报销单据模块配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody ExpModuleConfig expModuleConfig) {

        return success(expModuleConfigService.updateById(expModuleConfig));
    }


    /**
     * 批量修改数据
     *
     * @param expModuleConfig 实体对象
     * @return 修改结果
     */
    @ApiOperation("批量修改数据")
    @Log(title = "报销单据模块配置", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult updateBatch(@RequestBody List<ExpModuleConfig> expModuleConfigList) {

        return success(expModuleConfigService.updateBatchById(expModuleConfigList));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('expModuleConfig:expModuleConfig:delete')")
    @Log(title = "报销单据模块配置", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@NotEmpty(message = "至少选中一条要删除的数据！") @RequestBody List<String> ids) {
        return success(expModuleConfigService.removeByIds(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('expModuleConfig:expModuleConfig:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }
}