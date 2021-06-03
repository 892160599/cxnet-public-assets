package com.cxnet.asset.allocation.controller;

import com.cxnet.asset.allocation.domain.vo.AstAllocationListVo;
import com.cxnet.asset.allocation.domain.vo.AstAllocationVo;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
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
import com.cxnet.asset.allocation.domain.AstAllocationBill;
import com.cxnet.asset.allocation.service.AstAllocationBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产部门调剂主表(AstAllocationBill)表控制层
 *
 * @author zhaoyi
 * @since 2021-04-02 09:55:49
 */
@Slf4j
@Api(tags = "资产部门调剂主表")
@RestController
@RequestMapping("/astAllocationBill")
public class AstAllocationBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstAllocationBillService astAllocationBillService;
    @Resource
    private AstCardService astCardService;
    @Resource
    private AstBillListService astBillListService;
    @Resource
    private AstAnnexService astAnnexService;
    @Resource
    private AstDeptUserService astDeptUserService;

    /**
     * 分页查询所有数据
     *
     * @param astAllocationBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstAllocationBill astAllocationBill) {
        startPage();
        List<AstAllocationBill> list = astAllocationBillService.selectAll(astAllocationBill);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        AstAllocationVo astAllocationVo = new AstAllocationVo();
        //查询主表
        AstAllocationBill astAllocationBill = astAllocationBillService.getById(id);
        //查询明细
        QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper<>();
        astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, id);
        List<AstBillList> astBillLists = astBillListService.list(astBillListQueryWrapper);
        //查询附件
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        List<AstAnnex> astAnnexes = astAnnexService.list(astAnnexQueryWrapper);
        astAllocationVo.setAstAllocationBill(astAllocationBill);
        astAllocationVo.setAstBillLists(astBillLists);
        astAllocationVo.setAstAnnexes(astAnnexes);
        return success(astAllocationVo);
    }

    /**
     * 新增数据
     *
     * @param astAllocationVo 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:insert')")
    @Log(title = "资产部门调剂主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstAllocationVo astAllocationVo) {
        return success("保存成功", astAllocationBillService.saveAstAllocationVo(astAllocationVo));
    }

    /**
     * 修改数据
     *
     * @param astAllocationVo 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:update')")
    @Log(title = "资产部门调剂主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstAllocationVo astAllocationVo) {
        return success("修改成功！", astAllocationBillService.updateAstAllocationVo(astAllocationVo));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:delete')")
    @Log(title = "资产部门调剂主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success("删除成功！", astAllocationBillService.delete(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

    /**
     * 查询明细
     *
     * @param astAllocationListVo 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询明细")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:selectList')")
    @PostMapping("/selectList")
    public AjaxResult selectList(@RequestBody AstAllocationListVo astAllocationListVo) {
        //查询卡片信息
        QueryWrapper<AstCard> astCardQueryWrapper = new QueryWrapper<>();
        //判断当前登录用户是部门管理员还是单位管理员
        Map<String, List<String>> idsMap = astDeptUserService.getMap();
        if (idsMap.containsKey("dept")) {
            astCardQueryWrapper.lambda().eq(AstCard::getUnitId, astAllocationListVo.getUnitId())
                    .in(AstCard::getDepartmentId, astAllocationListVo.getDeptId());
        } else {
            astCardQueryWrapper.lambda().in(AstCard::getUnitId, astAllocationListVo.getUnitId());
        }
        //查询使用中的卡片
        astCardQueryWrapper.lambda().eq(AstCard::getAstStatus, "1");
        //查询资产类别
        if (StringUtils.isNotEmpty(astAllocationListVo.getCategoryCode())) {
            astCardQueryWrapper.lambda().likeRight(AstCard::getCategoryCode, astAllocationListVo.getCategoryCode());
        }
        //剔除已选卡片
        if (StringUtils.isNotEmpty(astAllocationListVo.getCardIds())) {
            astCardQueryWrapper.lambda().notIn(AstCard::getId, astAllocationListVo.getCardIds());
        }
        if (StringUtils.isNotEmpty(astAllocationListVo.getAllocationId())) {
            QueryWrapper<AstBillList> astAllocationListQueryWrapper = new QueryWrapper<>();
            astAllocationListQueryWrapper.lambda().eq(AstBillList::getBillId, astAllocationListVo.getAllocationId());
            List<AstBillList> astBillLists = astBillListService.list(astAllocationListQueryWrapper);
            List<String> astIds = astBillLists.stream().map(ids -> ids.getAstId()).collect(Collectors.toList());
            astCardQueryWrapper.and(wrapper -> wrapper.lambda().in(AstCard::getId, astIds).or().eq(AstCard::getInitialState, "1"));
        } else {
            astCardQueryWrapper.lambda().eq(AstCard::getInitialState, "1");
        }
        //模糊查询
        if (StringUtils.isNotEmpty(astAllocationListVo.getParam())) {
            astCardQueryWrapper.and(wrapper -> wrapper.lambda().like(AstCard::getAstCode, astAllocationListVo.getParam())
                    .or().like(AstCard::getAssetName, astAllocationListVo.getParam())
                    .or().like(AstCard::getCategoryCode, astAllocationListVo.getParam())
                    .or().like(AstCard::getCategoryName, astAllocationListVo.getParam()));
        }
        startPage();
        List<AstCard> list = astCardService.list(astCardQueryWrapper);
        return success(getDataTable(list));
    }

    /**
     * 送审（调剂）
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("送审（调剂）")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:allocationSubmit')")
    @PostMapping("/allocationSubmit")
    public AjaxResult allocationSubmit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("1");
        return success("送审成功！", astAllocationBillService.allocationSubmit(commonProcessRequest));
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("审核")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:allocationAudit')")
    @PostMapping("/allocationAudit")
    public AjaxResult allocationAudit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("2");
        return success("审核成功！", astAllocationBillService.allocationAudit(commonProcessRequest));
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("退回")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:allocationBack')")
    @PostMapping("/allocationBack")
    public AjaxResult allocationBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return success("退回成功！", astAllocationBillService.allocationBack(commonProcessRequest));
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("收回")
    @PreAuthorize("@ss.hasPermi('astAllocationBill:astAllocationBill:taskBack')")
    @PostMapping("/taskBack")
    public AjaxResult taskBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("4");
        return success("收回成功！", astAllocationBillService.taskBack(commonProcessRequest));
    }
}

