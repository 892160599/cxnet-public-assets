package com.cxnet.asset.astrepairbill.controller;

import static com.cxnet.common.constant.AjaxResult.success;

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

import com.cxnet.asset.astrepairbill.domain.AstRepairBill;
import com.cxnet.asset.astrepairbill.domain.vo.AstRepairBillVO;
import com.cxnet.asset.astrepairbill.service.AstRepairBillService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 资产报修单主表(AstRepairBill)表控制层
 *
 * @author guks
 * @since 2021-04-15 11:57:19
 */
@Api(tags = "资产报修单主表")
@RestController
@RequestMapping("/astRepairBill")
public class AstRepairBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstRepairBillService astRepairBillService;

    /**
     * 分页查询所有数据
     *
     * @param astRepairBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstRepairBill astRepairBill) {
        startPage();
        List<AstRepairBill> list = astRepairBillService.selectAll(astRepairBill);
        return success(getDataTable(list));
    }


    /**
     * 查询报修人
     *
     * @return 所有数据
     */
    @ApiOperation("查询报修人")
    @GetMapping("/repair")
    public AjaxResult selectRepairNameAll() {
        return success(astRepairBillService.selectRepairNameAll());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable String id) {
        return success(astRepairBillService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param astRepairBill 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:insert')")
    @Log(title = "资产报修单主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstRepairBillVO astRepairBillVO) {
        return success(astRepairBillService.insert(astRepairBillVO));
    }

    /**
     * 修改数据
     *
     * @param astRepairBill 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:update')")
    @Log(title = "资产报修单主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstRepairBillVO astRepairBillVO) {
        return success(astRepairBillService.update(astRepairBillVO));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:delete')")
    @Log(title = "资产报修单主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(astRepairBillService.delete(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }


    /**
     * 送审
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("送审")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:submit')")
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("1");
        return success(astRepairBillService.submit(commonProcessRequest));
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("审核")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:audit')")
    @PostMapping("/purAudit")
    public AjaxResult purAudit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("2");
        return success(astRepairBillService.audit(commonProcessRequest));
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("退回")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:back')")
    @PostMapping("/purBack")
    public AjaxResult purBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return success(astRepairBillService.back(commonProcessRequest));
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("收回")
    @PostMapping("/taskBack")
    @PreAuthorize("@ss.hasPermi('astRepairBill:astRepairBill:taskBack')")
    public AjaxResult taskBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("4");
        return success(astRepairBillService.taskBack(commonProcessRequest));
    }


    /**
     * 获取资产卡片列表
     *
     * @return 结果
     */
    @ApiOperation("获取资产卡片列表")
    @PostMapping("{unitId}/astcard")
    public AjaxResult getAstCardList(@PathVariable("unitId") String unitId) {
        return success(astRepairBillService.getAstCardList(unitId, "1"));
    }
}