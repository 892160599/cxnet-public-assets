package com.cxnet.asset.astcollectbill.controller;

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

import com.cxnet.asset.astcollectbill.domain.AstCollectBill;
import com.cxnet.asset.astcollectbill.domain.vo.AstCollectBillVO;
import com.cxnet.asset.astcollectbill.service.AstCollectBillService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 资产领用单主表(AstCollectBill)表控制层
 *
 * @author guks
 * @since 2021-04-12 10:52:13
 */
@Api(tags = "资产领用单主表")
@RestController
@RequestMapping("/astCollectBill")
public class AstCollectBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstCollectBillService astCollectBillService;


    /**
     * 分页查询所有数据
     *
     * @param astCollectBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astCollectBill:astCollectBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstCollectBill astCollectBill) {
        startPage();
        List<AstCollectBill> list = astCollectBillService.selectAll(astCollectBill);
        return success(getDataTable(list));
    }

    /**
     * 查询领用人
     *
     * @return 所有数据
     */
    @ApiOperation("查询领用人")
    @PreAuthorize("@ss.hasPermi('astCollectBill:astCollectBill:query')")
    @GetMapping("/emp")
    public AjaxResult selectEmpNameAll() {
        return success(astCollectBillService.selectEmpNameAll());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astCollectBill:astCollectBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable String id) {
        return success(astCollectBillService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param astCollectBillVO 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astCollectBill:astCollectBill:insert')")
    @Log(title = "资产领用单主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstCollectBillVO astCollectBillVO) {
        return success(astCollectBillService.insert(astCollectBillVO));
    }

    /**
     * 修改数据
     *
     * @param astCollectBillVO 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astCollectBill:astCollectBill:update')")
    @Log(title = "资产领用单主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstCollectBillVO astCollectBillVO) {
        return success(astCollectBillService.update(astCollectBillVO));
    }

    /**
     * 删除数据
     *
     * @param ids 主键集合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astCollectBill:astCollectBill:delete')")
    @Log(title = "资产领用单主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(astCollectBillService.delete(ids));
    }

    /**
     * 送审
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("送审")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:submit')")
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("1");
        return success(astCollectBillService.submit(commonProcessRequest));
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("审核")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:audit')")
    @PostMapping("/purAudit")
    public AjaxResult purAudit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("2");
        return success(astCollectBillService.audit(commonProcessRequest));
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("退回")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:back')")
    @PostMapping("/purBack")
    public AjaxResult purBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return success(astCollectBillService.back(commonProcessRequest));
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("收回")
    @PostMapping("/taskBack")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:taskBack')")
    public AjaxResult taskBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("4");
        return success(astCollectBillService.taskBack(commonProcessRequest));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astCollectBill:astCollectBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }


    /**
     * 获取资产卡片列表
     *
     * @param unitId 单位id
     * @return 结果
     */
    @ApiOperation("获取资产卡片列表")
    @PostMapping("{unitId}/astcard")
    public AjaxResult getAstCardList(@PathVariable("unitId") String unitId) {
        return success(astCollectBillService.getAstCardList(unitId, "4"));
    }
}