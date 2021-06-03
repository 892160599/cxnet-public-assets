package com.cxnet.asset.businessSet.controller;

import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.businessSet.domain.AstMapConfig;
import com.cxnet.asset.businessSet.service.AstMapConfigService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产地图配置(AstMapConfig)表控制层
 *
 * @author caixx
 * @since 2021-04-25 14:06:24
 */
@Slf4j
@Api(tags = "资产地图配置")
@RestController
@RequestMapping("/astMapConfig")
public class AstMapConfigController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstMapConfigService astMapConfigService;

    /**
     * 查询当前单位地图配置信息
     *
     * @return 单条数据
     */
    @ApiOperation("查询当前单位地图配置信息")
    @GetMapping
    public AjaxResult getThisAstMapConfig() {
        return success(astMapConfigService.getThisAstMapConfig());
    }

    /**
     * 修改数据
     *
     * @param astMapConfig 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @Log(title = "资产地图配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstMapConfig astMapConfig) {
        return success(astMapConfigService.updateById(astMapConfig));
    }

}