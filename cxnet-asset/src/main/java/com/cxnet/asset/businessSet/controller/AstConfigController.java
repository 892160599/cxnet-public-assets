package com.cxnet.asset.businessSet.controller;

import cn.hutool.core.util.ObjectUtil;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.businessSet.domain.AstConfig;
import com.cxnet.asset.businessSet.service.AstConfigService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产参数设置(AstConfig)表控制层
 *
 * @author caixx
 * @since 2021-04-06 09:41:42
 */
@Slf4j
@Api(tags = "资产参数设置")
@RestController
@RequestMapping("/astConfig")
public class AstConfigController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstConfigService astConfigService;

    /**
     * 分页查询所有数据
     *
     * @param astConfig 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @GetMapping
    public AjaxResult selectAll(AstConfig astConfig) {
        startPage();
        QueryWrapper<AstConfig> qw = new QueryWrapper<>(astConfig);
        List<AstConfig> list = astConfigService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 通过单位id查询
     *
     * @param unitId 查询实体
     * @return 通过单位id查询
     */
    @ApiOperation("通过单位id查询")
    @GetMapping("/selectOne/{unitId}")
    public AjaxResult selectByUnitId(@PathVariable String unitId) {
        return success(astConfigService.selectByUnitId(unitId));
    }

    /**
     * 修改数据
     *
     * @param astConfig 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @Log(title = "资产参数设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstConfig astConfig) {
        return success(astConfigService.update(astConfig));
    }


}