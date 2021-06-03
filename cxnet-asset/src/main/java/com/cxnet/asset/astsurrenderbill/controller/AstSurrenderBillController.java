package com.cxnet.asset.astsurrenderbill.controller;

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

import com.cxnet.asset.astsurrenderbill.domain.AstSurrenderBill;
import com.cxnet.asset.astsurrenderbill.domain.vo.AstSurrenderBillVO;
import com.cxnet.asset.astsurrenderbill.service.AstSurrenderBillService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 资产交回单主表(AstSurrenderBill)表控制层
 *
 * @author guks
 * @since 2021-04-15 11:57:52
 */
@Api(tags = "资产交回单主表")
@RestController
@RequestMapping("/astSurrenderBill")
public class AstSurrenderBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstSurrenderBillService astSurrenderBillService;

    /**
     * 分页查询所有数据
     *
     * @param astSurrenderBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstSurrenderBill astSurrenderBill) {
        startPage();
        List<AstSurrenderBill> list = astSurrenderBillService.selectAll(astSurrenderBill);
        return success(getDataTable(list));
    }


    /**
     * 查询经办人
     *
     * @return 所有数据
     */
    @ApiOperation("查询领用人")
    @PreAuthorize("@ss.hasPermi('astCollectBill:astCollectBill:query')")
    @GetMapping("/agent")
    public AjaxResult selectAgentAll() {
        return success(astSurrenderBillService.selectAgentAll());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable String id) {
        return success(astSurrenderBillService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param astSurrenderBillVO 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:insert')")
    @Log(title = "资产交回单主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstSurrenderBillVO astSurrenderBillVO) {
        return success(astSurrenderBillService.insert(astSurrenderBillVO));
    }

    /**
     * 修改数据
     *
     * @param astSurrenderBillVO 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:update')")
    @Log(title = "资产交回单主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstSurrenderBillVO astSurrenderBillVO) {
        return success(astSurrenderBillService.update(astSurrenderBillVO));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:delete')")
    @Log(title = "资产交回单主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(astSurrenderBillService.delete(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:print')")
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
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:submit')")
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("1");
        return success(astSurrenderBillService.submit(commonProcessRequest));
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("审核")
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:audit')")
    @PostMapping("/purAudit")
    public AjaxResult purAudit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("2");
        return success(astSurrenderBillService.audit(commonProcessRequest));
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("退回")
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:back')")
    @PostMapping("/purBack")
    public AjaxResult purBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return success(astSurrenderBillService.back(commonProcessRequest));
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("收回")
    @PostMapping("/taskBack")
    @PreAuthorize("@ss.hasPermi('astSurrenderBill:astSurrenderBill:taskBack')")
    public AjaxResult taskBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("4");
        return success(astSurrenderBillService.taskBack(commonProcessRequest));
    }


    /**
     * 获取资产卡片列表
     *
     * @return 结果
     */
    @ApiOperation("获取资产卡片列表")
    @PostMapping("{unitId}/astcard")
    public AjaxResult getAstCardList(@PathVariable("unitId") String unitId) {
        return success(astSurrenderBillService.getAstCardList(unitId, "1"));
    }
}