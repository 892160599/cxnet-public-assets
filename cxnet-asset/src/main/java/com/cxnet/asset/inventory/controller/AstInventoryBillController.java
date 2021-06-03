package com.cxnet.asset.inventory.controller;

import com.cxnet.asset.inventory.domain.AstInventoryList;
import com.cxnet.asset.inventory.domain.vo.AstInventoryBillVo;
import com.cxnet.asset.inventory.mapper.AstInventoryListMapper;
import com.cxnet.asset.inventory.service.AstInventoryListService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.cxnet.framework.web.controller.BaseController;
import com.cxnet.framework.aspectj.lang.enums.BusinessType;
import com.cxnet.common.constant.AjaxResult;
import com.cxnet.common.domain.CommonPrint;
import com.cxnet.framework.aspectj.lang.annotation.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.asset.inventory.domain.AstInventoryBill;
import com.cxnet.asset.inventory.service.AstInventoryBillService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import static com.cxnet.common.constant.AjaxResult.success;

/**
 * (AstInventoryBill)表控制层
 *
 * @author zhangyl
 * @since 2021-04-02 10:02:37
 */
@Slf4j
@Api(description = "资产盘点")
@RestController
@RequestMapping("/astInventoryBill")
public class AstInventoryBillController extends BaseController {

    /**
     * 服务对象
     */
    @Resource
    private AstInventoryBillService astInventoryBillService;
    @Resource
    private AstInventoryListService astInventoryListService;

    /**
     * 分页查询所有数据
     *
     * @param astInventoryBill 查询实体
     * @return 所有数据
     */
    @ApiOperation("分页查询所有数据")
    @PreAuthorize("@ss.hasPermi('astInventoryBill:astInventoryBill:query')")
    @GetMapping
    public AjaxResult selectAll(AstInventoryBill astInventoryBill) {
        startPage();
        QueryWrapper<AstInventoryBill> qw = new QueryWrapper<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        qw.lambda().eq(AstInventoryBill::getDelFlag, "0")
                .eq(StringUtils.isNotEmpty(astInventoryBill.getUnitId()), AstInventoryBill::getUnitId, astInventoryBill.getUnitId())
                .like(StringUtils.isNotEmpty(astInventoryBill.getCheckPlanCode()), AstInventoryBill::getCheckPlanCode, astInventoryBill.getCheckPlanCode())
                .like(StringUtils.isNotEmpty(astInventoryBill.getCheckPlanName()), AstInventoryBill::getCheckPlanName, astInventoryBill.getCheckPlanName());
        if (StringUtils.isNotEmpty(astInventoryBill.getStatus())) {
            List<String> applyStatus = Arrays.asList(astInventoryBill.getStatus().split(","));
            qw.lambda().in(AstInventoryBill::getStatus, applyStatus);
        }
        if (StringUtils.isNotEmpty(astInventoryBill.getDeptCode())) {
            List<String> deptCode = Arrays.asList(astInventoryBill.getDeptCode().split(","));
            qw.lambda().in(AstInventoryBill::getDeptCode, deptCode);
        }
        if (astInventoryBill.getAcquisitionStartDate() != null) {
            String start = sdf.format(astInventoryBill.getAcquisitionStartDate());
            qw.ge("to_char(CREATE_TIME,'yyyy-MM-dd')", start);
        }
        if (astInventoryBill.getAcquisitionEndDate() != null) {
            String end = sdf.format(astInventoryBill.getAcquisitionEndDate());
            qw.le("to_char(CREATE_TIME,'yyyy-MM-dd')", end);
        }
        List<AstInventoryBill> list = astInventoryBillService.list(qw);
        return success(getDataTable(list));
    }

    /**
     * 生成盘点清单
     *
     * @param astInventoryBill 实体对象
     * @return 生成盘点清单
     */
    @ApiOperation("生成盘点清单")
    @PreAuthorize("@ss.hasPermi('astInventoryBill:astInventoryBill:insert')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping("/insertList")
    public AjaxResult insertList(@RequestBody AstInventoryBill astInventoryBill) {
        astInventoryBillService.insertList(astInventoryBill);
        List<AstInventoryList> inventoryLists =
                astInventoryListService.selectPan(astInventoryBill.getBillNo(), astInventoryBill.getUnitId(),
                        astInventoryBill.getApplyDeptCode(), astInventoryBill.getEmpCode());
        return success(inventoryLists);
    }

    /**
     * 启动或者结束任务
     *
     * @param astInventoryBill 实体对象
     * @return 启动任务
     */
    @ApiOperation("启动或者结束任务")
    @PreAuthorize("@ss.hasPermi('astInventoryBill:astInventoryBill:update')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @GetMapping("/updateInventouy")
    public AjaxResult updateInventouy(String billNo, String status) {
        //修改主表状态
        QueryWrapper<AstInventoryBill> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstInventoryBill::getBillNo, billNo);
        List<AstInventoryBill> list = astInventoryBillService.list(wrapper);
        if (list.size() > 0) {
            list.get(0).setStatus(status);
        }
        astInventoryBillService.updateBatchById(list);
        //修改子表状态
        QueryWrapper<AstInventoryList> listWapper = new QueryWrapper<>();
        listWapper.lambda().eq(AstInventoryList::getBillNo, billNo);
        List<AstInventoryList> inventoryLists = astInventoryListService.list(listWapper);
        if (inventoryLists.size() > 0) {
            inventoryLists.forEach(v -> {
                v.setStatus(status);
                astInventoryListService.updateById(v);
            });
        }
        return success("修改成功");
    }

    /**
     * 新增数据
     *
     * @param astInventoryBill 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增数据")
    @PreAuthorize("@ss.hasPermi('astInventoryBill:astInventoryBill:insert')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult insert(@RequestBody AstInventoryBill astInventoryBill) {
        AstInventoryBill list = astInventoryBillService.getList(astInventoryBill);
        return success(astInventoryBillService.save(list));
    }

    /**
     * 修改资产盘点主表数据
     *
     * @param astInventoryBill 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改资产盘点主表数据")
    @PreAuthorize("@ss.hasPermi('astInventoryBill:astInventoryBill:update')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody AstInventoryBill astInventoryBill) {
        AstInventoryBill inventoryBill = astInventoryBillService.updateList(astInventoryBill);
        return success(astInventoryBillService.updateById(inventoryBill));
    }

    /**
     * 查询明细表信息
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("查询明细表信息")
    @PreAuthorize("@ss.hasPermi('astInventoryBill:astInventoryBill:query')")
    @GetMapping("/selectOne")
    public AjaxResult selectOne(String billNo, String applyDeptCode, String empCode) {
        AstInventoryBillVo billVo = astInventoryBillService.selectAll(billNo, applyDeptCode, empCode);
        return success(billVo);
    }

    /**
     * 修改明细表信息
     *
     * @param astInventoryBill 实体对象
     * @return 修改结果
     */
    @ApiOperation("修改明细表信息")
    @PreAuthorize("@ss.hasPermi('astInventoryBill:astInventoryBill:update')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PostMapping("/updateBill")
    public AjaxResult updateBill(@RequestBody AstInventoryBillVo astInventoryBillVo) {
        String id = astInventoryBillService.update(astInventoryBillVo);
        String billNo = astInventoryBillVo.getAstInventoryBill().getBillNo();
        String applyDeptCode = astInventoryBillVo.getAstInventoryBill().getApplyDeptCode();
        String empCode = astInventoryBillVo.getAstInventoryBill().getEmpCode();
        AstInventoryBillVo billVo = astInventoryBillService.selectAll(billNo, applyDeptCode, empCode);
        return success(billVo);
    }

    /**
     * 删除数据
     *
     * @param ids 主键结合
     * @return 删除结果
     */
    @ApiOperation("删除数据")
    @PreAuthorize("@ss.hasPermi('astInventoryBill:astInventoryBill:delete')")
    @Log(title = "", businessType = BusinessType.DELETE)
    @DeleteMapping
    public AjaxResult delete(@RequestBody List<String> ids) {
        ids.forEach(id -> {
            AstInventoryBill byId = astInventoryBillService.getById(id);
            byId.setDelFlag("2");
            astInventoryBillService.updateById(byId);
        });
        return success("删除成功");
    }

    /**
     * 根据部门ids查询该部门下的人员
     *
     * @param deptIds
     * @return
     */
    @ApiOperation("根据部门ids查询该部门下的人员")
    @PostMapping("/selList")
    public AjaxResult selList(@RequestBody List<String> deptIds) {
        List<BdPersonnel> bdPersonnels = astInventoryBillService.selList(deptIds);
        return success(bdPersonnels);
    }

    /**
     * 查询盘点日志
     *
     * @param id
     * @return
     */
    @ApiOperation("查询盘点日志")
    @PreAuthorize("@ss.hasPermi('astInventoryBill:astInventoryBill:query')")
    @GetMapping("/getBillOne")
    public AjaxResult getBillOne(String id) {
        List<AstInventoryBill> billOne = astInventoryBillService.getBillOne(id);
        return success(billOne);
    }
}

