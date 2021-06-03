package com.cxnet.asset.depr.controller;

import com.cxnet.asset.businessSet.domain.AstDeprMethod;
import com.cxnet.asset.businessSet.service.AstDeprMethodService;
import com.cxnet.asset.depr.domain.AstDeprList;
import com.cxnet.asset.depr.service.AstDeprListService;
import com.cxnet.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.depr.domain.AstDeprBill;
import com.cxnet.asset.depr.service.AstDeprBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产折旧主表(AstDeprBill)表控制层
 *
 * @author caixx
 * @since 2021-04-08 16:03:11
 */
@Slf4j
@Api(tags = "资产折旧")
@RestController
@RequestMapping("/astDeprBill")
public class AstDeprBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstDeprBillService astDeprBillService;

    @Resource
    private AstDeprListService astDeprListService;


    /**
     * 查询折旧年限下拉
     *
     * @return 集合
     */
    @ApiOperation("查询折旧年限下拉")
    @GetMapping("/getDeprFiscal")
    public AjaxResult getDeprFiscal() {
        return success(astDeprBillService.getDeprFiscal());
    }

    /**
     * 根据折旧年限查询折旧月份列表
     *
     * @return 集合
     */
    @ApiOperation("根据折旧年限查询折旧月份列表")
    @GetMapping("/getDeprMo/{deprFiscal}")
    public AjaxResult getDeprMo(@PathVariable("deprFiscal") Integer deprFiscal) {
        return success(astDeprBillService.getDeprMoByDeprFiscal(deprFiscal));
    }

    /**
     * 查询主表详情
     *
     * @param astDeprBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询主表详情")
    @PreAuthorize("@ss.hasPermi('astDeprBill:astDeprBill:query')")
    @GetMapping("/astDeprBill")
    public AjaxResult selectAstDeprBill(AstDeprBill astDeprBill) {
        astDeprBill = astDeprBillService.selectAstDeprByAstDeprBill(astDeprBill);
        return success(astDeprBill);
    }

    /**
     * 分页查询折旧明细
     *
     * @param astDeprBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询折旧明细")
    @PreAuthorize("@ss.hasPermi('astDeprBill:astDeprBill:query')")
    @GetMapping("/astDeprList")
    public AjaxResult selectAll(AstDeprBill astDeprBill) {
        startPage();
        List<AstDeprList> list = astDeprListService.selectAll(astDeprBill);
        return success(getDataTable(list));
    }

    /**
     * 查询汇总明细
     *
     * @param id 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询汇总明细")
    @PreAuthorize("@ss.hasPermi('astDeprBill:astDeprBill:query')")
    @GetMapping("/astSummaryList/{id}")
    public AjaxResult astSummaryList(@PathVariable("id") String id) {
        return success(astDeprListService.astSummaryList(id));
    }

    /**
     * 计算
     *
     * @param astDeprBill
     * @return
     */
    @ApiOperation("计算")
    @PreAuthorize("@ss.hasPermi('astDeprBill:astDeprBill:calculation')")
    @GetMapping("/calculation")
    public AjaxResult calculation(AstDeprBill astDeprBill) {
        return success(astDeprBillService.calculation(astDeprBill));
    }

    /**
     * 确认
     *
     * @param id
     * @return
     */
    @ApiOperation("确认")
    @PreAuthorize("@ss.hasPermi('astDeprBill:astDeprBill:confirm')")
    @GetMapping("/confirm/{id}")
    public AjaxResult confirm(@PathVariable("id") String id) {
        return success(astDeprBillService.confirm(id));
    }

    /**
     * 取消确认
     *
     * @param id
     * @return
     */
    @ApiOperation("取消确认")
    @PreAuthorize("@ss.hasPermi('astDeprBill:astDeprBill:cancelConfirm')")
    @GetMapping("/cancelConfirm/{id}")
    public AjaxResult cancelConfirm(@PathVariable("id") String id) {
        return success(astDeprBillService.cancelConfirm(id));
    }

    /**
     * 查询资产折旧记录
     *
     * @param id
     * @return
     */
    @ApiOperation("查询资产折旧记录")
    @GetMapping("/record/{id}")
    public AjaxResult record(@PathVariable("id") String id) {
        return success(astDeprBillService.record(id));
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id删除")
    @PreAuthorize("@ss.hasPermi('astDeprBill:astDeprBill:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult deleteById(@PathVariable("id") String id) {
        return success(astDeprBillService.deleteById(id));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astDeprBill:astDeprBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

}