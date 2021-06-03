package com.cxnet.asset.dispose.controller;

import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.dispose.domain.AstDisposeBill;
import com.cxnet.asset.dispose.domain.vo.AstCardListVo;
import com.cxnet.asset.dispose.domain.vo.AstDisposeVo;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.service.BdAssetTypeService;
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
import com.cxnet.asset.dispose.service.AstDisposeBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * 资产处置主表(AstDisposeBill)表控制层
 *
 * @author zhaoyi
 * @since 2021-03-25 10:13:22
 */
@Slf4j
@Api(tags = "资产处置主表")
@RestController
@RequestMapping("/astDisposeBill")
public class AstDisposeBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstDisposeBillService astDisposeBillService;
    @Resource
    private AstCardService astCardService;
    @Resource
    private AstBillListService astBillListService;
    @Resource
    private AstAnnexService astAnnexService;
    @Resource
    private AstDeptUserService astDeptUserService;
    @Resource
    private BdAssetTypeService bdAssetTypeService;

    /**
     * 分页查询所有数据
     *
     * @param astDisposeBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstDisposeBill astDisposeBill) {
        startPage();
        List<AstDisposeBill> list = astDisposeBillService.selectAll(astDisposeBill);
        return success(getDataTable(list));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("通过主键查询单条数据")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:query')")
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        AstDisposeVo astDisposeVo = new AstDisposeVo();
        //查询主表
        AstDisposeBill astDisposeBill = astDisposeBillService.getById(id);
        //查询明细
        QueryWrapper<AstBillList> astDisposeListQueryWrapper = new QueryWrapper<>();
        astDisposeListQueryWrapper.lambda().eq(AstBillList::getBillId, id);
        List<AstBillList> astDisposeLists = astBillListService.list(astDisposeListQueryWrapper);
        //查询附件
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        List<AstAnnex> astAnnexes = astAnnexService.list(astAnnexQueryWrapper);
        astDisposeVo.setAstDisposeBill(astDisposeBill);
        astDisposeVo.setAstBillLists(astDisposeLists);
        astDisposeVo.setAstAnnexes(astAnnexes);
        return success(astDisposeVo);
    }

    /**
     * 新增数据
     *
     * @param astDisposeVo 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:insert')")
    @Log(title = "资产处置主表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstDisposeVo astDisposeVo) {
        return success("保存成功！", astDisposeBillService.saveAstDisposeVo(astDisposeVo));
    }

    /**
     * 修改数据
     *
     * @param astDisposeVo 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改数据")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:update')")
    @Log(title = "资产处置主表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstDisposeVo astDisposeVo) {
        return success("修改成功！", astDisposeBillService.updateAstDisposeVo(astDisposeVo));
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:delete')")
    @Log(title = "资产处置主表", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        return success("删除成功！", astDisposeBillService.delete(ids));
    }

    /**
     * 获取帆软打印路径
     *
     * @param commonPrint 通用的打印对象
     * @return 帆软打印路径
     */
    @ApiOperation("获取帆软打印路径")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:print')")
    @GetMapping("/print")
    public AjaxResult print(CommonPrint commonPrint) {
        // 获取帆软路径
        return success(basePrint(commonPrint));
    }

    /**
     * 查询资产类别二级树
     */
    @ApiOperation("查询资产类别树")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(String unitId) {
        QueryWrapper<BdAssetType> bdAssetTypeQueryWrapper = new QueryWrapper<>();
        bdAssetTypeQueryWrapper.lambda().eq(BdAssetType::getUnitId, unitId).isNotNull(BdAssetType::getParentId).apply("length(asset_code)=1").orderByAsc(BdAssetType::getAssetCode);
        List<BdAssetType> bdAssetTypes = bdAssetTypeService.list(bdAssetTypeQueryWrapper);
        return success(bdAssetTypes);
    }


    /**
     * 查询明细
     *
     * @param astCardListVo 查询实体
     * @return 所有数据
     */
    @ApiOperation("查询明细")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:selectList')")
    @GetMapping("/selectList")
    public AjaxResult selectList(AstCardListVo astCardListVo) {
        //查询卡片信息
        QueryWrapper<AstCard> astCardQueryWrapper = new QueryWrapper<>();
        //判断当前登录用户是部门管理员还是单位管理员
        Map<String, List<String>> idsMap = astDeptUserService.getMap();
        if (idsMap.containsKey("dept")) {
            astCardQueryWrapper.lambda().eq(AstCard::getUnitId, astCardListVo.getUnitId())
                    .in(AstCard::getDepartmentId, astCardListVo.getDeptId());
        } else {
            astCardQueryWrapper.lambda().eq(AstCard::getUnitId, astCardListVo.getUnitId());
        }
        //查询使用中的资产
        astCardQueryWrapper.lambda().eq(AstCard::getClassCode, astCardListVo.getDisposetypeCode())
                .eq(AstCard::getAstStatus, "1");
        //判断是否已选中
        if (StringUtils.isNotEmpty(astCardListVo.getDisposeId())) {
            QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper<>();
            astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, astCardListVo.getDisposeId());
            List<AstBillList> astDisposeLists = astBillListService.list(astBillListQueryWrapper);
            List<String> astIds = astDisposeLists.stream().map(ids -> ids.getAstId()).collect(Collectors.toList());
            astCardQueryWrapper.and(wrapper -> wrapper.lambda().in(AstCard::getId, astIds).or().eq(AstCard::getInitialState, "1"));
        } else {
            astCardQueryWrapper.lambda().eq(AstCard::getInitialState, "1");
        }
        //模糊查询
        if (StringUtils.isNotEmpty(astCardListVo.getParam())) {
            astCardQueryWrapper.and(wrapper -> wrapper.lambda().like(AstCard::getAstCode, astCardListVo.getParam())
                    .or().like(AstCard::getAssetName, astCardListVo.getParam())
                    .or().like(AstCard::getCategoryCode, astCardListVo.getParam())
                    .or().like(AstCard::getCategoryName, astCardListVo.getParam()));
        }
        startPage();
        List<AstCard> list = astCardService.list(astCardQueryWrapper);
        return success(getDataTable(list));
    }

    /**
     * 送审（处置）
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("送审（处置）")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:disSubmit')")
    @PostMapping("/disSubmit")
    public AjaxResult disSubmit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("1");
        return success("送审成功！", astDisposeBillService.disSubmit(commonProcessRequest));
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("审核")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:disAudit')")
    @PostMapping("/disAudit")
    public AjaxResult disAudit(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("2");
        return success("审核成功！", astDisposeBillService.disAudit(commonProcessRequest));
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("退回")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:disBack')")
    @PostMapping("/disBack")
    public AjaxResult disBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("3");
        return success("退回成功！", astDisposeBillService.disBack(commonProcessRequest));
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     */
    @ApiOperation("收回")
    @PreAuthorize("@ss.hasPermi('astDisposeBill:astDisposeBill:taskBack')")
    @PostMapping("/taskBack")
    public AjaxResult taskBack(@RequestBody CommonProcessRequest commonProcessRequest) {
        commonProcessRequest.setSubmitFlag("4");
        return success("收回成功！", astDisposeBillService.taskBack(commonProcessRequest));
    }

    /**
     * 查询操作记录
     *
     * @param astId 查询操作记录
     */
    @ApiOperation("查询操作记录")
    @GetMapping("/selectAstDisposeBillList")
    public AjaxResult selectAstDisposeBillList(String astId) {
        return success(astDisposeBillService.selectAstDisposeBillList(astId));
    }
}

