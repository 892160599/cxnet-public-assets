package com.cxnet.asset.empchange.controller;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.dispose.domain.vo.AstCardListVo;
import com.cxnet.asset.empchange.domain.AstEmpchangeList;
import com.cxnet.asset.empchange.domain.vo.AstEmpchangeListVo;
import com.cxnet.asset.empchange.domain.vo.AstEmpchangeVo;
import com.cxnet.asset.empchange.service.AstEmpchangeListService;
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
import com.cxnet.asset.empchange.domain.AstEmpchangeBill;
import com.cxnet.asset.empchange.service.AstEmpchangeBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产使用人变动主表(AstEmpchangeBill)表控制层
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:10
 */
@Slf4j
@Api(tags = "资产使用人变动主表")
@RestController
@RequestMapping("/astEmpchangeBill")
public class AstEmpchangeBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstEmpchangeBillService astEmpchangeBillService;
    @Resource
    private AstDeptUserService astDeptUserService;
    @Resource
    private AstCardService astCardService;
    @Resource
    private AstEmpchangeListService astEmpchangeListService;
    @Resource
    private AstAnnexService astAnnexService;

    /**
     * 分页查询所有数据
     *
     * @param astEmpchangeBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstEmpchangeBill astEmpchangeBill) {
        startPage();
        List<AstEmpchangeBill> list = astEmpchangeBillService.selectAll(astEmpchangeBill);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        AstEmpchangeVo astEmpchangeVo = new AstEmpchangeVo();
        //查询主表
        astEmpchangeVo.setAstEmpchangeBill(astEmpchangeBillService.getById(id));
        //查询明细
        QueryWrapper<AstEmpchangeList> astEmpchangeListQueryWrapper = new QueryWrapper<>();
        astEmpchangeListQueryWrapper.lambda().eq(AstEmpchangeList::getBillId, id);
        astEmpchangeVo.setAstEmpchangeLists(astEmpchangeListService.list(astEmpchangeListQueryWrapper));
        //查询附件
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        astEmpchangeVo.setAstAnnexes(astAnnexService.list(astAnnexQueryWrapper));
        return success(astEmpchangeVo);
    }

    /**
     * 新增数据
     *
     * @param astEmpchangeVo 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:insert')")
    @Log(title = "资产使用人变动主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstEmpchangeVo astEmpchangeVo) {
        return success("保存成功", astEmpchangeBillService.saveAstEmpchangeVo(astEmpchangeVo));
    }

    /**
     * 修改数据
     *
     * @param astEmpchangeVo 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:update')")
    @Log(title = "资产使用人变动主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstEmpchangeVo astEmpchangeVo) {
        return success("修改成功", astEmpchangeBillService.updateAstEmpchangeVo(astEmpchangeVo));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:delete')")
    @Log(title = "资产使用人变动主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success("删除成功", astEmpchangeBillService.delete(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

    /**
     * 查询明细
     *
     * @param astEmpchangeListVo 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询明细")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:selectList')")
    @PostMapping("/selectList")
    public AjaxResult selectList(@RequestBody AstEmpchangeListVo astEmpchangeListVo) {
        //查询卡片信息
        QueryWrapper<AstCard> astCardQueryWrapper = new QueryWrapper<>();
        //判断当前登录用户是部门管理员还是单位管理员
        Map<String, List<String>> idsMap = astDeptUserService.getMap();
        if (idsMap.containsKey("dept")) {
            astCardQueryWrapper.lambda().eq(AstCard::getUnitId, astEmpchangeListVo.getUnitId())
                    .in(AstCard::getDepartmentId, astEmpchangeListVo.getDeptId());
        } else {
            astCardQueryWrapper.lambda().in(AstCard::getUnitId, astEmpchangeListVo.getUnitId());
        }
        //查询使用中的资产
        astCardQueryWrapper.lambda().eq(AstCard::getAstStatus, "1");
        if (StringUtils.isNotEmpty(astEmpchangeListVo.getCategoryCode())) {
            astCardQueryWrapper.lambda().likeRight(AstCard::getCategoryCode, astEmpchangeListVo.getCategoryCode());
        }
        //去除已选中资产
        if (StringUtils.isNotEmpty(astEmpchangeListVo.getCardIds())) {
            astCardQueryWrapper.lambda().notIn(AstCard::getId, astEmpchangeListVo.getCardIds());
        }
        if (StringUtils.isNotEmpty(astEmpchangeListVo.getEmpchangeId())) {
            QueryWrapper<AstEmpchangeList> astEmpchangeListQueryWrapper = new QueryWrapper<>();
            astEmpchangeListQueryWrapper.lambda().eq(AstEmpchangeList::getBillId, astEmpchangeListVo.getEmpchangeId());
            List<AstEmpchangeList> astEmpchangeLists = astEmpchangeListService.list(astEmpchangeListQueryWrapper);
            List<String> astIds = astEmpchangeLists.stream().map(ids -> ids.getAstId()).collect(Collectors.toList());
            astCardQueryWrapper.lambda().in(AstCard::getId, astIds).or().eq(AstCard::getInitialState, "1");
        } else {
            astCardQueryWrapper.lambda().eq(AstCard::getInitialState, "1");
        }
        //模糊查询
        if (StringUtils.isNotEmpty(astEmpchangeListVo.getParam())) {
            astCardQueryWrapper.and(wrapper -> wrapper.lambda().like(AstCard::getAstCode, astEmpchangeListVo.getParam())
                    .or().like(AstCard::getAssetName, astEmpchangeListVo.getParam())
                    .or().like(AstCard::getCategoryCode, astEmpchangeListVo.getParam())
                    .or().like(AstCard::getCategoryName, astEmpchangeListVo.getParam()));
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
    @ApiOperation("送审（使用人变更）")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:empSubmit')")
    @PostMapping("/empSubmit")
    public AjaxResult empSubmit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("1");
        return success("送审成功！", astEmpchangeBillService.empSubmit(commonProcessRequest));
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("审核")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:empAudit')")
    @PostMapping("/empAudit")
    public AjaxResult empAudit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("2");
        return success("审核成功！", astEmpchangeBillService.empAudit(commonProcessRequest));
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("退回")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:empBack')")
    @PostMapping("/empBack")
    public AjaxResult empBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return success("退回成功！", astEmpchangeBillService.empBack(commonProcessRequest));
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("收回")
    @PreAuthorize("@ss.hasPermi('astEmpchangeBill:astEmpchangeBill:taskBack')")
    @PostMapping("/taskBack")
    public AjaxResult taskBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("4");
        return success("收回成功！", astEmpchangeBillService.taskBack(commonProcessRequest));
    }
}

