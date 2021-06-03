package com.cxnet.asset.astcardacsequip.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.astcardacsequip.domain.AstCardAcsequip;
import com.cxnet.asset.astcardacsequip.service.AstCardAcsequipService;
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
import java.io.Serializable;
import java.util.List;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 附属设备(AstCardAcsequip)表控制层
 *
 * @author makejava
 * @since 2021-04-08 10:35:05
 */
@Slf4j
@Api(tags = "附属设备")
@RestController
@RequestMapping("/astCardAcsequip")
public class AstCardAcsequipController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstCardAcsequipService astCardAcsequipService;

    /**
     * 分页查询所有数据
     *
     * @param astCardAcsequip 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astCardAcsequip:astCardAcsequip:query')")
    @GetMapping
    public AjaxResult selectAll(AstCardAcsequip astCardAcsequip) {
        startPage();
        QueryWrapper<AstCardAcsequip> qw = new QueryWrapper<>(astCardAcsequip);
        List<AstCardAcsequip> list = astCardAcsequipService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astCardAcsequip:astCardAcsequip:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(astCardAcsequipService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param astCardAcsequip 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astCardAcsequip:astCardAcsequip:insert')")
    @Log(title = "附属设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstCardAcsequip astCardAcsequip) {
        return success(astCardAcsequipService.save(astCardAcsequip));
    }

    /**
     * 修改数据
     *
     * @param astCardAcsequip 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astCardAcsequip:astCardAcsequip:update')")
    @Log(title = "附属设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstCardAcsequip astCardAcsequip) {
        return success(astCardAcsequipService.updateById(astCardAcsequip));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astCardAcsequip:astCardAcsequip:delete')")
    @Log(title = "附属设备", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(astCardAcsequipService.removeByIds(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astCardAcsequip:astCardAcsequip:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }
}