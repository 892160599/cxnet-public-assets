package com.cxnet.asset.depr.controller;

import com.cxnet.asset.businessSet.domain.AstDeprMethod;
import com.cxnet.asset.businessSet.service.AstDeprMethodService;
import com.cxnet.asset.depr.domain.AstDeprList;
import com.cxnet.asset.depr.domain.AstDeprRepairList;
import com.cxnet.asset.depr.domain.vo.AstDeprRepairVo;
import com.cxnet.asset.depr.service.AstDeprRepairListService;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.depr.domain.AstDeprRepairBill;
import com.cxnet.asset.depr.service.AstDeprRepairBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产补计提折旧(AstDeprRepairBill)表控制层
 *
 * @author caixx
 * @since 2021-04-16 18:07:51
 */
@Slf4j
@Api(tags = "资产补计提折旧")
@RestController
@RequestMapping("/astDeprRepairBill")
public class AstDeprRepairBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstDeprRepairBillService astDeprRepairBillService;

    @Resource
    private AstDeprRepairListService astDeprRepairListService;

    @Resource
    private AstDeprMethodService astDeprMethodService;

    /**
     * 查询主表详情
     *
     * @param fiscal deprMo
     * @return 单条数据
     */
    @ApiOperation("查询主表详情")
    @PreAuthorize("@ss.hasPermi('astDeprRepairBill:astDeprRepairBill:query')")
    @GetMapping("/astDeprRepairBill")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fiscal", value = "年度", required = true),
            @ApiImplicitParam(name = "deprMo", value = "折旧月份", required = true)
    })
    public AjaxResult selectOne(Integer fiscal, Integer deprMo) {
        return success(astDeprRepairBillService.getAstDeprRepairBill(fiscal, deprMo));
    }

    /**
     * 分页查询补计提明细
     *
     * @param astDeprRepairBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询补计提明细")
    @PreAuthorize("@ss.hasPermi('astDeprRepairBill:astDeprRepairBill:query')")
    @GetMapping
    @ApiImplicitParam(name = "id", value = "主表id", required = true)
    public AjaxResult selectAll(AstDeprRepairBill astDeprRepairBill) {
        List<AstDeprRepairList> list = new ArrayList<>();
        String id = astDeprRepairBill.getId();
        List<AstDeprMethod> astDeprMethods = astDeprMethodService.list();
        Map<String, String> deprMethodMap = astDeprMethods.stream().filter(v -> "0".equals(v.getDelFlag())).collect(Collectors.toMap(AstDeprMethod::getDeprMethodCode, AstDeprMethod::getDeprMethodName));
        startPage();
        if (StringUtils.isNotEmpty(id)) {
            QueryWrapper<AstDeprRepairList> qw = new QueryWrapper<>();
            qw.lambda().eq(AstDeprRepairList::getAstDeprId, id);
            list = astDeprRepairListService.list(qw);
            list.forEach(v -> v.setDepMethod(deprMethodMap.get(v.getDepMethod())));
        }
        return success(getDataTable(list));
    }

    /**
     * 选择资产明细
     *
     * @param astDeprRepairBill
     * @return list
     */
    @ApiOperation("选择资产明细")
    @PreAuthorize("@ss.hasPermi('astDeprRepairBill:astDeprRepairBill:query')")
    @GetMapping("/selectAstDetails")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fiscal", value = "年度", required = true),
            @ApiImplicitParam(name = "deprMo", value = "折旧月份", required = true)
    })
    public AjaxResult selectAstDetails(AstDeprRepairBill astDeprRepairBill) {
        startPage();
        List<AstDeprRepairList> astDeprRepairLists = astDeprRepairBillService.selectAstDetails(astDeprRepairBill);
        return success(getDataTable(astDeprRepairLists));
    }

    /**
     * 确认
     *
     * @param astDeprRepairVo
     * @return
     */
    @ApiOperation("确认")
    @PreAuthorize("@ss.hasPermi('astDeprRepairBill:astDeprRepairBill:confirm')")
    @PostMapping("/confirm")
    public AjaxResult confirm(@RequestBody AstDeprRepairVo astDeprRepairVo) {
        return success(astDeprRepairBillService.confirm(astDeprRepairVo));
    }

    /**
     * 取消确认
     *
     * @param id
     * @return
     */
    @ApiOperation("取消确认")
    @PreAuthorize("@ss.hasPermi('astDeprRepairBill:astDeprRepairBill:cancelConfirm')")
    @GetMapping("/cancelConfirm/{id}")
    public AjaxResult cancelConfirm(@PathVariable("id") String id) {
        return success(astDeprRepairBillService.cancelConfirm(id));
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id删除")
    @PreAuthorize("@ss.hasPermi('astDeprRepairBill:astDeprRepairBill:delete')")
    @DeleteMapping("/{id}")
    public AjaxResult deleteById(@PathVariable("id") String id) {
        return success(astDeprRepairBillService.deleteById(id));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astDeprRepairBill:astDeprRepairBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

}