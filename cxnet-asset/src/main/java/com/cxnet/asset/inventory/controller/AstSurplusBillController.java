package com.cxnet.asset.inventory.controller;

import com.cxnet.asset.inventory.domain.vo.AstSurplusBillVo;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.domain.CommonProcessRequest;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.inventory.domain.AstSurplusBill;
import com.cxnet.asset.inventory.service.AstSurplusBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产盘盈主表(AstSurplusBill)表控制层
 *
 * @author zhangyl
 * @since 2021-04-19 14:14:44
 */
@Slf4j
@Api(tags = "资产盘盈")
@RestController
@RequestMapping("/astSurplusBill")
public class AstSurplusBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstSurplusBillService astSurplusBillService;

    /**
     * 分页查询所有数据
     *
     * @param astSurplusBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstSurplusBill astSurplusBill) {
        startPage();
        List<AstSurplusBill> list = astSurplusBillService.selectAll(astSurplusBill);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable String id) {
        return success(astSurplusBillService.getList(id));
    }

    /**
     * 新增数据
     *
     * @param astSurplusBill 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:insert')")
    @Log(title = "资产盘盈主表", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    public AjaxResult insert(@RequestBody AstSurplusBillVo astSurplusBillVo) {
        return success(astSurplusBillService.insertSurplus(astSurplusBillVo));
    }

    /**
     * 修改数据
     *
     * @param astSurplusBill 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:update')")
    @Log(title = "资产盘盈主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstSurplusBillVo astSurplusBillVo) {
        return success(astSurplusBillService.updateSurplus(astSurplusBillVo));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:delete')")
    @Log(title = "资产盘盈主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(astSurplusBillService.deleteSurplus(ids));
    }

    /**
     * 生成资产卡片
     *
     * @param astSurplusBill 实体对象
     * @return 新增结果
     */
    @ApiOperation("生成资产卡片")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:insert')")
    @Log(title = "资产盘盈主表", businessType = BusinessType.INSERT)
    @PostMapping("/insertCard")
    public AjaxResult insertCard(@RequestBody List<String> ids) {
        ids.forEach(v -> {
            astSurplusBillService.insertCard(v);
        });
        return success("生成成功");
    }


    /**
     * 送审
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("送审")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:submit')")
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("1");
        return success(astSurplusBillService.submit(commonProcessRequest));
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("审核")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:audit')")
    @PostMapping("/purAudit")
    public AjaxResult purAudit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("2");
        return success(astSurplusBillService.audit(commonProcessRequest));
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("退回")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:back')")
    @PostMapping("/purBack")
    public AjaxResult purBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return success(astSurplusBillService.back(commonProcessRequest));
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("收回")
    @PostMapping("/taskBack")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:taskBack')")
    public AjaxResult taskBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("4");
        return success(astSurplusBillService.taskBack(commonProcessRequest));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astSurplusBill:astSurplusBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

}

