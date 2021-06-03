package com.cxnet.asset.astchange.controller;

import com.cxnet.asset.allocation.domain.AstAllocationBill;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.cxnet.asset.astchange.domain.vo.AstAstchangeVo;
import com.cxnet.asset.astchange.domain.vo.AstChangeListVo;
import com.cxnet.asset.astchange.service.AstAstchangeListService;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
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
import com.cxnet.asset.astchange.domain.AstAstchangeBill;
import com.cxnet.asset.astchange.service.AstAstchangeBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产变动主表(AstAstchangeBill)表控制层
 *
 * @author zhaoyi
 * @since 2021-04-23 10:06:14
 */
@Slf4j
@Api(tags = "资产变动主表")
@RestController
@RequestMapping("/astAstchangeBill")
public class AstAstchangeBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstAstchangeBillService astAstchangeBillService;
    @Resource
    private AstDeptUserService astDeptUserService;
    @Resource
    private AstAstchangeListService astAstchangeListService;
    @Resource
    private AstCardService astCardService;
    @Resource
    private AstAnnexService astAnnexService;

    /**
     * 分页查询所有数据
     *
     * @param astAstchangeBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstAstchangeBill astAstchangeBill) {
        startPage();
        List<AstAstchangeBill> list = astAstchangeBillService.selectAll(astAstchangeBill);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        AstAstchangeVo astAstchangeVo = new AstAstchangeVo();
        //查询主表信息
        AstAstchangeBill astAstchangeBill = astAstchangeBillService.getById(id);
        //查询明细信息
        QueryWrapper<AstAstchangeList> astAstchangeListQueryWrapper = new QueryWrapper<>();
        astAstchangeListQueryWrapper.lambda().eq(AstAstchangeList::getBillId, id);
        List<AstAstchangeList> astchangeLists = astAstchangeListService.list(astAstchangeListQueryWrapper);
        //查询附件
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        List<AstAnnex> astAnnexes = astAnnexService.list(astAnnexQueryWrapper);
        astAstchangeVo.setAstAstchangeBill(astAstchangeBill);
        astAstchangeVo.setAstAstchangeLists(astchangeLists);
        astAstchangeVo.setAstAnnexes(astAnnexes);
        return success(astAstchangeVo);
    }

    /**
     * 新增数据
     *
     * @param astAstchangeVo 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:insert')")
    @Log(title = "资产变动主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstAstchangeVo astAstchangeVo) {
        return success("保存成功", astAstchangeBillService.saveAstAstchangeVo(astAstchangeVo));
    }

    /**
     * 修改数据
     *
     * @param astAstchangeVo 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:update')")
    @Log(title = "资产变动主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstAstchangeVo astAstchangeVo) {
        return success("修改成功", astAstchangeBillService.updateAstAstchangeVo(astAstchangeVo));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:delete')")
    @Log(title = "资产变动主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success("删除成功", astAstchangeBillService.delete(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

    /**
     * 查询明细
     *
     * @param astChangeListVo 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询明细")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:selectList')")
    @PostMapping("/selectList")
    public AjaxResult selectList(@RequestBody AstChangeListVo astChangeListVo) {
        //查询卡片信息
        QueryWrapper<AstCard> astCardQueryWrapper = new QueryWrapper<>();
        //判断当前登录用户是部门管理员还是单位管理员
        Map<String, List<String>> idsMap = astDeptUserService.getMap();
        if (idsMap.containsKey("dept")) {
            astCardQueryWrapper.lambda().eq(AstCard::getUnitId, astChangeListVo.getUnitId())
                    .in(AstCard::getDepartmentId, astChangeListVo.getDeptId());
        } else {
            astCardQueryWrapper.lambda().in(AstCard::getUnitId, astChangeListVo.getUnitId());
        }
        //查询使用中的卡片
        astCardQueryWrapper.lambda().eq(AstCard::getAstStatus, "1");
        //查询资产类别
        if (StringUtils.isNotEmpty(astChangeListVo.getCategoryCode())) {
            astCardQueryWrapper.lambda().likeRight(AstCard::getCategoryCode, astChangeListVo.getCategoryCode());
        }
        //剔除已选卡片
        if (StringUtils.isNotEmpty(astChangeListVo.getCardIds())) {
            astCardQueryWrapper.lambda().notIn(AstCard::getId, astChangeListVo.getCardIds());
        }
        if (StringUtils.isNotEmpty(astChangeListVo.getAstchangeId())) {
            QueryWrapper<AstAstchangeList> astAstchangeListQueryWrapper = new QueryWrapper<>();
            astAstchangeListQueryWrapper.lambda().eq(AstAstchangeList::getBillId, astChangeListVo.getAstchangeId());
            List<AstAstchangeList> astchangeLists = astAstchangeListService.list(astAstchangeListQueryWrapper);
            List<String> astIds = astchangeLists.stream().map(ids -> ids.getAstId()).collect(Collectors.toList());
            astCardQueryWrapper.lambda().in(AstCard::getId, astIds).or().eq(AstCard::getInitialState, "1");
        } else {
            astCardQueryWrapper.lambda().eq(AstCard::getInitialState, "1");
        }
        //模糊查询
        if (StringUtils.isNotEmpty(astChangeListVo.getParam())) {
            astCardQueryWrapper.and(wrapper -> wrapper.lambda().like(AstCard::getAstCode, astChangeListVo.getParam())
                    .or().like(AstCard::getAssetName, astChangeListVo.getParam())
                    .or().like(AstCard::getCategoryCode, astChangeListVo.getParam())
                    .or().like(AstCard::getCategoryName, astChangeListVo.getParam()));
        }
        startPage();
        List<AstCard> list = astCardService.list(astCardQueryWrapper);
        return success(getDataTable(list));
    }

    /**
     * 送审（使用人变更）
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("送审（资产变更）")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:astSubmit')")
    @PostMapping("/astSubmit")
    public AjaxResult astSubmit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("1");
        return success("送审成功！", astAstchangeBillService.astSubmit(commonProcessRequest));
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("审核")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:astAudit')")
    @PostMapping("/astAudit")
    public AjaxResult astAudit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("2");
        return success("审核成功！", astAstchangeBillService.astAudit(commonProcessRequest));
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("退回")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:astBack')")
    @PostMapping("/astBack")
    public AjaxResult astBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return success("退回成功！", astAstchangeBillService.astBack(commonProcessRequest));
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("收回")
    @PreAuthorize("@ss.hasPermi('astAstchangeBill:astAstchangeBill:taskBack')")
    @PostMapping("/taskBack")
    public AjaxResult taskBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("4");
        return success("收回成功！", astAstchangeBillService.taskBack(commonProcessRequest));
    }

    /**
     * 查询操作记录
     *
     * @param astId 查询变更操作记录
     */
    @ApiOperation("查询操作记录")
    @GetMapping("/selectAstAstChangeList")
    public AjaxResult selectAstAstChangeList(String astId) {
        return success(astAstchangeBillService.selectAstAstChangeList(astId));
    }
}

