package com.cxnet.project.system.parameter.controller;

import com.cxnet.common.constant.AjaxResult;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.service.SysParameterServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置处理
 *
 * @author ssw
 */
@RestController
@Api(tags = "系统配置")
@RequestMapping("/system/parameter")
public class SysParameterController {
    @Autowired(required = false)
    private SysParameterServiceI sysParameterServiceI;

    /**
     * 首页获取系统配置
     * 不需要验证
     */
    @ApiOperation("获取系统配置,不需要验证")
    @GetMapping("/no")
    public AjaxResult selectSysParameter() {
        return AjaxResult.success(sysParameterServiceI.selectSysParameter());
    }

    /**
     * 获取系统配置
     * 需要token验证
     *
     * @return
     */
    @ApiOperation("获取系统配置,需要验证")
    @GetMapping
    public AjaxResult selectSysParameterByToken() {
        return AjaxResult.success(sysParameterServiceI.selectSysParameter());
    }


    /**
     * 保存系统配置
     *
     * @param sysParameter
     * @return
     */
    @ApiOperation("保存系统配置")
    @PostMapping("/insertSysParameter")
    @PreAuthorize("@ss.hasPermi('system:parameter:update')")
    public AjaxResult insertSysParameter(@RequestBody @ApiParam("SysParameter") SysParameter sysParameter) {
        sysParameterServiceI.insertSysParameter(sysParameter);
        return AjaxResult.success("success");
    }


}
