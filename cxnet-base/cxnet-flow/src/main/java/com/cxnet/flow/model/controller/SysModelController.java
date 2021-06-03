package com.cxnet.flow.model.controller;

import java.math.BigDecimal;
import java.util.*;

import com.cxnet.common.constant.UserConstants;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.common.utils.tree.ZoneUtils;
import com.cxnet.flow.model.domain.*;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.config.domain.SysConfig;
import com.cxnet.project.system.config.service.SysConfigServiceI;
import com.cxnet.rpc.domain.SysDbYb;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.flow.model.service.SysModelServiceI;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 系统模块管理控制层
 *
 * @author caixx
 * @date 2020-08-12
 */
@Slf4j
@RestController
@Api(tags = "系统模块管理，流程定义")
@RequestMapping("/system/model")
public class SysModelController extends BaseController {

    @Autowired(required = false)
    private SysModelServiceI sysModelService;
    @Autowired(required = false)
    private SysConfigServiceI sysConfigServiceI;

    /**
     * 查询系统模块管理tree
     *
     * @param sysModel 查询系统模块管理tree
     */
    @ApiOperation("查询系统模块管理tree")
    @GetMapping("/tree")
    public AjaxResult treeSysModel(SysModel sysModel) {
        List<Zone> list = sysModelService.selectSysModelTree(sysModel);
        return success(ZoneUtils.buildTree(list));
    }

    /**
     * 查询系统模块业务单据集合
     *
     * @param modelId 查询系统模块业务单据集合
     */
    @ApiOperation("查询系统模块业务单据集合")
    @GetMapping("/modelBill/{modelId}")
    public AjaxResult selectModelBill(@PathVariable String modelId) {
        return success(sysModelService.selectModelBill(modelId));
    }


    /**
     * 查询系统模块管理
     *
     * @param modelId 查询系统模块管理
     */
    @ApiOperation("查询系统模块业务单据集合")
    @GetMapping("/detail/{modelId}")
    public AjaxResult selectSysModelById(@PathVariable String modelId) {
        return success(sysModelService.selectSysModelById(modelId));
    }

    /**
     * 修改系统模块管理
     *
     * @param sysModel 系统模块管理
     */
    @ApiOperation("修改系统模块管理")
    @PreAuthorize("@ss.hasPermi('system:model:update')")
    @Log(title = "系统模块管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateSysModel(@Validated @RequestBody SysModel sysModel) {
        return toAjax(sysModelService.updateSysModel(sysModel));
    }

    /**
     * 查询流程定义列表
     *
     * @param sysModelDeployment 查询流程定义列表
     */
    @ApiOperation("查询流程定义列表")
    @GetMapping()
    public AjaxResult selectSysModel(SysModelDeployment sysModelDeployment) {
        return success(sysModelService.selectModelDeploymentList(sysModelDeployment));
    }

    /**
     * 查询单据绑定的流程列表
     *
     * @param sysModelDeployment 查询单据绑定的流程列表
     */
    @ApiOperation("查询单据绑定的流程列表")
    @GetMapping("/bindList")
    public AjaxResult selectDeploymentListByModelId(SysModelDeployment sysModelDeployment) {
        return success(sysModelService.selectDeploymentListByModelId(sysModelDeployment));
    }

    /**
     * 修改绑定流程
     *
     * @return
     */
    @ApiOperation("修改绑定流程")
    @PreAuthorize("@ss.hasPermi('system:model:update')")
    @PostMapping("/bindBill")
    public AjaxResult bindBill(@RequestBody SysModelDeployment sysModelDeployment) {
        return success(sysModelService.bindBill(sysModelDeployment));
    }

    /**
     * 新增流程
     *
     * @param sysModelDeployment 新增流程
     */
    @ApiOperation("新增流程")
    @PreAuthorize("@ss.hasPermi('system:model:add')")
    @Log(title = "新增流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addSysModelDeployment(@RequestBody SysModelDeployment sysModelDeployment) {
        return toAjax(sysModelService.insertSysModelDeployment(sysModelDeployment));
    }

    /**
     * 删除流程
     *
     * @param deploymentId 部署id
     */
    @ApiOperation("删除流程")
    @PreAuthorize("@ss.hasPermi('system:model:delete')")
    @Log(title = "删除流程", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deploymentId}")
    public AjaxResult delSysModelDeployment(@PathVariable String deploymentId) {
        return toAjax(sysModelService.delSysModelDeployment(deploymentId));
    }

    /**
     * 停用流程
     *
     * @param deploymentId 部署id
     */
    @ApiOperation("停用流程")
    @PreAuthorize("@ss.hasPermi('system:model:stop')")
    @Log(title = "停用流程", businessType = BusinessType.UPDATE)
    @GetMapping("/stop/{deploymentId}")
    public AjaxResult stopSysModelDeployment(@PathVariable String deploymentId) {
        return toAjax(sysModelService.stopSysModelDeployment(deploymentId));
    }

    /**
     * 查询单据配置信息
     *
     * @param billTypeCode 部署id
     */
    @ApiOperation("查询单据配置信息")
    @GetMapping("/conf/{billTypeCode}")
    public AjaxResult selectBillConf(@PathVariable String billTypeCode) {
        SysBillConf sysBillConf = sysModelService.selectBillConf(billTypeCode);
        return success(sysBillConf);
    }

    /**
     * 判断当前人是否是审核人
     *
     * @param processinstId 流程id
     * @return 结果
     */
    @ApiOperation("判断当前人是否是审核人")
    @GetMapping("/checkProcess/{processinstId}")
    public AjaxResult checkProcess(@PathVariable String processinstId) {
        return success(TaskUtils.isToDo(processinstId));
    }

    /**
     * 判断当前人是否是可编辑单据
     *
     * @param processinstId 流程id
     * @return 结果
     */
    @ApiOperation("判断当前人是否是可编辑单据")
    @GetMapping("/checkEditPermis/{processinstId}")
    public AjaxResult checkEditPermis(@PathVariable String processinstId) {
        SysConfig sysConfig = sysConfigServiceI.selectConfigById("3");
        if (!UserConstants.YES.equals(sysConfig.getConfigValue())) {
            return success(new AjaxResult());
        }
        return success(TaskUtils.checkEditPermis(processinstId));
    }

    /**
     * 判断当前人是否可收回
     *
     * @param processinstId 流程id
     * @return 结果
     */
    @ApiOperation("判断当前人是否可收回")
    @GetMapping("/checkBackTaskBack/{processinstId}")
    public AjaxResult checkBackTaskBack(@PathVariable String processinstId) {
        return success(TaskUtils.checkBackTaskBack(processinstId));
    }

    /**
     * 查询流程历史记录及节点信息
     *
     * @param
     */
    @ApiOperation("查询流程历史记录及节点信息")
    @GetMapping("/history")
    public AjaxResult history(String processInstanceId, String modelCode) {
        if (StringUtils.isNotEmpty(processInstanceId)) {
            return success(TaskUtils.getHistoryByPiid(processInstanceId));
        }
        if (StringUtils.isNotEmpty(modelCode)) {
            return success(TaskUtils.getDefinitionByModelCode(modelCode));
        }
        return success();
    }

    /**
     * 查询当前人的所有待办任务
     *
     * @param
     */
    @ApiOperation("查询当前人的所有待办任务")
    @GetMapping("/selectTodo")
    public AjaxResult selectTodo(SearchValueEntity searchData) {
        startPage();
        List<SysDbYb> list = sysModelService.selectAllAcivitiDb(SecurityUtils.getUsername(), searchData.getStartTime(),
                searchData.getEndTime(), searchData.getModelName(), searchData.getSearchValue(), searchData.getDeptCode(),
                searchData.getBillNo(), searchData.getMinAmt(), searchData.getMaxAmt(), searchData.getStatus(), searchData.getParentPath());
        return success(getDataTable(list));
    }

    /**
     * 查询当前人的所有已办任务
     *
     * @param
     */
    @ApiOperation("查询当前人的所有已办任务")
    @GetMapping("/selectDone")
    public AjaxResult selectDone(SearchValueEntity searchData) {
        startPage();
        List<SysDbYb> list = sysModelService.selectAllAcivitiYb(SecurityUtils.getUsername(), searchData.getStartTime(),
                searchData.getEndTime(), searchData.getModelName(), searchData.getSearchValue(), searchData.getDeptCode(),
                searchData.getBillNo(), searchData.getMinAmt(), searchData.getMaxAmt(), searchData.getStatus(), searchData.getParentPath());
        return success(getDataTable(list));
    }

    /**
     * 移动端查看我的发起
     *
     * @param
     * @return
     */
    @ApiOperation("移动端查看我的发起")
    @GetMapping("/selectMySelfStart")
    public AjaxResult mobileSelectMySelfStart(SearchValueEntity searchData) {
        startPage();
        List<SysDbYb> list = sysModelService.mobileSelectMySelfStart(SecurityUtils.getLoginUser().getUser().getNickName(), searchData.getStartTime(),
                searchData.getEndTime(), searchData.getModelName(), searchData.getDeptCode(),
                searchData.getBillNo(), searchData.getMinAmt(), searchData.getMaxAmt(), searchData.getStatus(), searchData.getParentPath());
        return success(getDataTable(list));
    }

}
