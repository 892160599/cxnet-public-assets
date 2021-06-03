package com.cxnet.asset.check.controller;

import com.cxnet.asset.check.domain.AstCheckBill;
import com.cxnet.asset.check.domain.vo.AstCheckVo;
import com.cxnet.asset.check.service.AstCheckBillService;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产验收(AstCheckBill)表控制层
 *
 * @author caixx
 * @since 2021-03-25 09:39:41
 */
@Slf4j
@Api(tags = "资产验收")
@RestController
@RequestMapping("/astCheckBill")
public class AstCheckBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstCheckBillService astCheckBillService;

    /**
     * 分页查询所有数据
     *
     * @param astCheckBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstCheckBill astCheckBill) {
        startPage();
        List<AstCheckBill> list = astCheckBillService.selectAll(astCheckBill);
        return success(getDataTable(list));
    }

    /**
     * 查询验收人
     *
     * @return 所有数据
     */
    @ApiOperation("查询验收人")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:query')")
    @GetMapping("selectCheckPersonAll")
    public AjaxResult selectCheckPersonAll() {
        return success(astCheckBillService.selectCheckPersonAll());
    }


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable String id) {
        return success(astCheckBillService.selectOne(id));
    }

    /**
     * 新增数据
     *
     * @param astCheckVo 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:insert')")
    @Log(title = "资产验收主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstCheckVo astCheckVo) {
        return success(astCheckBillService.insert(astCheckVo));
    }

    /**
     * 修改数据
     *
     * @param astCheckVo 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:update')")
    @Log(title = "资产验收主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstCheckVo astCheckVo) {
        return success(astCheckBillService.update(astCheckVo));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:delete')")
    @Log(title = "资产验收主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success(astCheckBillService.delete(ids));
    }

    /**
     * 生成卡片
     *
     * @param ids 主键结合
     * @return 结果
     */
    @ApiOperation("生成卡片")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:produce')")
    @Log(title = "生成卡片", businessType = BusinessType.DELETE)
    @PostMapping("/produce")
    public AjaxResult produce(@RequestBody List<String> ids) {
        return success(astCheckBillService.produce(ids));
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
        return success(astCheckBillService.submit(commonProcessRequest));
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
        return success(astCheckBillService.audit(commonProcessRequest));
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
        return success(astCheckBillService.back(commonProcessRequest));
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
        return success(astCheckBillService.taskBack(commonProcessRequest));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astCheckBill:astCheckBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

}