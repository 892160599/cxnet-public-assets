package com.cxnet.asset.allot.controller;

import com.alibaba.fastjson.JSON;
import com.cxnet.asset.allot.domain.vo.AstAllotListVo;
import com.cxnet.asset.allot.domain.vo.AstAllotVo;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.allot.domain.AstAllotBill;
import com.cxnet.asset.allot.service.AstAllotBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产单位调拨主表(AstAllotBill)表控制层
 *
 * @author zhaoyi
 * @since 2021-04-09 14:33:01
 */
@Slf4j
@Api(tags = "资产单位调拨主表")
@RestController
@RequestMapping("/astAllotBill")
public class AstAllotBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstAllotBillService astAllotBillService;
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
     * @param astAllotBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstAllotBill astAllotBill) {
        startPage();
        List<AstAllotBill> list = astAllotBillService.selectAll(astAllotBill);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        AstAllotVo astAllotVo = new AstAllotVo();
        //查询主表
        AstAllotBill astAllotBill = astAllotBillService.getById(id);
        //查询明细
        QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper<>();
        astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, id);
        List<AstBillList> astBillLists = astBillListService.list(astBillListQueryWrapper);
        //查询附件
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        List<AstAnnex> astAnnexes = astAnnexService.list(astAnnexQueryWrapper);
        astAllotVo.setAllotBill(astAllotBill);
        astAllotVo.setAstBillLists(astBillLists);
        astAllotVo.setAstAnnexes(astAnnexes);
        return success(astAllotVo);
    }

    /**
     * 新增数据
     *
     * @param astAllotVo 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:insert')")
    @Log(title = "资产单位调拨主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstAllotVo astAllotVo) {
        return success("保存成功", astAllotBillService.saveAstAllotVo(astAllotVo));
    }

    /**
     * 修改数据
     *
     * @param astAllotVo 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:update')")
    @Log(title = "资产单位调拨主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstAllotVo astAllotVo) {
        return success("修改成功", astAllotBillService.updateAstAllotVo(astAllotVo));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:delete')")
    @Log(title = "资产单位调拨主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success("删除成功", astAllotBillService.delete(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

    /**
     * 查询明细
     *
     * @param astAllotListVo 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询明细")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:selectList')")
    @PostMapping("/selectList")
    public AjaxResult selectList(@RequestBody AstAllotListVo astAllotListVo) {
        //查询卡片信息
        QueryWrapper<AstCard> astCardQueryWrapper = new QueryWrapper<>();
        astCardQueryWrapper.lambda().eq(AstCard::getUnitId, astAllotListVo.getUnitId())
                .eq(AstCard::getAstStatus, "1");
        //查询资产类别
        if (StringUtils.isNotEmpty(astAllotListVo.getCategoryCode())) {
            astCardQueryWrapper.lambda().likeRight(AstCard::getCategoryCode, astAllotListVo.getCategoryCode());
        }
        //去除已选中资产
        if (StringUtils.isNotEmpty(astAllotListVo.getCardIds())) {
            astCardQueryWrapper.lambda().notIn(AstCard::getId, astAllotListVo.getCardIds());
        }
        if (StringUtils.isNotEmpty(astAllotListVo.getAllotId())) {
            QueryWrapper<AstBillList> astAllocationListQueryWrapper = new QueryWrapper<>();
            astAllocationListQueryWrapper.lambda().eq(AstBillList::getBillId, astAllotListVo.getAllotId());
            List<AstBillList> astBillLists = astBillListService.list(astAllocationListQueryWrapper);
            List<String> astIds = astBillLists.stream().map(ids -> ids.getAstId()).collect(Collectors.toList());
            astCardQueryWrapper.and(wrapper -> wrapper.lambda().in(AstCard::getId, astIds).or().eq(AstCard::getInitialState, "1"));
        } else {
            astCardQueryWrapper.lambda().eq(AstCard::getInitialState, "1");
        }
        //模糊查询
        if (StringUtils.isNotEmpty(astAllotListVo.getParam())) {
            astCardQueryWrapper.and(wrapper -> wrapper.lambda().like(AstCard::getAstCode, astAllotListVo.getParam())
                    .or().like(AstCard::getAssetName, astAllotListVo.getParam())
                    .or().like(AstCard::getCategoryCode, astAllotListVo.getParam())
                    .or().like(AstCard::getCategoryName, astAllotListVo.getParam()));
        }
        startPage();
        List<AstCard> list = astCardService.list(astCardQueryWrapper);
        return success(getDataTable(list));
    }

    /**
     * 查询明细
     *
     * @param unitId 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询制单人明细")
    //@PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:selectUserList')")
    @PostMapping("/selectUserList")
    public AjaxResult selectUserList(@RequestBody String unitId) {
        List<BdPersonnel> bdPersonnels = astAllotBillService.selectUserList(unitId);
        return success(getDataTable(bdPersonnels));
    }

    /**
     * 通过用户id查询账户ID
     *
     * @param userId 查询实体
     * @return 所有数据
     */
    @ApiOperation("通过用户id查询账户ID")
    //@PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:selectUserList')")
    @GetMapping("/selectUserListByUserId")
    public AjaxResult selectUserListByUserId(String userId) {
        String bdPersonnel = astAllotBillService.selectUserListByUserId(userId);
        return success(bdPersonnel);
    }

    /**
     * 送审（调拨）
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("送审（调拨）")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:allotSubmit')")
    @PostMapping("/allotSubmit")
    public AjaxResult allotSubmit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("1");
        return success("送审成功！", astAllotBillService.allotSubmit(commonProcessRequest));
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("审核")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:allotAudit')")
    @PostMapping("/allotAudit")
    public AjaxResult allotAudit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("2");
        return success("审核成功！", astAllotBillService.allotAudit(commonProcessRequest));
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("退回")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:allotBack')")
    @PostMapping("/allotBack")
    public AjaxResult allotBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return success("退回成功！", astAllotBillService.allotBack(commonProcessRequest));
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("收回")
    @PreAuthorize("@ss.hasPermi('astAllotBill:astAllotBill:taskBack')")
    @PostMapping("/taskBack")
    public AjaxResult taskBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("4");
        return success("收回成功！", astAllotBillService.taskBack(commonProcessRequest));
    }
}

