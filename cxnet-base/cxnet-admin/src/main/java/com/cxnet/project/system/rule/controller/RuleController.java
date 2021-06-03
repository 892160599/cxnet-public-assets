package com.cxnet.project.system.rule.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.project.system.rule.domain.Rule;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.common.constant.AjaxResult;

import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 编号器控制层
 *
 * @author caixx
 * @date 2020-07-31
 */
@Slf4j
@RestController
@Api(tags = "编号器")
@RequestMapping("/system/rule")
public class RuleController extends BaseController {

    @Autowired(required = false)
    private RuleServiceI ruleService;


    /**
     * 查询编号器集合
     *
     * @param rule 编号器
     */
    @ApiOperation("查询编号器集合")
    // @PreAuthorize("@ss.hasPermi('system:rule:query')")
    @GetMapping
    public AjaxResult listRule(Rule rule) {
        startPage();
        List<Rule> list = ruleService.selectRuleList(rule);
        return success(getDataTable(list));
    }


    /**
     * 查询编号器明细
     *
     * @param id 编号器ID
     */
    @ApiOperation("查询编号器明细")
    //   @PreAuthorize("@ss.hasPermi('system:rule:query')")
    @GetMapping("/{id}")
    public AjaxResult getRule(@PathVariable String id) {
        return success(ruleService.selectRuleById(id));
    }

    /**
     * 新增编号器
     *
     * @param rule 编号器
     */
    @ApiOperation("新增编号器")
    @PreAuthorize("@ss.hasPermi('system:rule:add')")
    @Log(title = "编号器", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addRule(@RequestBody Rule rule) {
        return toAjax(ruleService.insertRule(rule));
    }

    /**
     * 修改编号器
     *
     * @param rule 编号器
     */
    @ApiOperation("修改编号器")
    @PreAuthorize("@ss.hasPermi('system:rule:update')")
    @Log(title = "编号器", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateRule(@Validated @RequestBody Rule rule) {
        return toAjax(ruleService.updateRule(rule));
    }

    /**
     * 删除编号器
     *
     * @param id 编号器ID
     */
    @ApiOperation("删除编号器")
    @PreAuthorize("@ss.hasPermi('system:rule:delete')")
    @Log(title = "编号器", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult deleteRule(@PathVariable String[] id) {
        return toAjax(ruleService.deleteRuleByIds(id));
    }

}
