package com.cxnet.asset.inventory.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.mapper.AstAnnexMapper;
import com.cxnet.asset.inventory.domain.AstInventoryList;
import com.cxnet.asset.inventory.domain.vo.AstInventoryBillVo;
import com.cxnet.asset.inventory.domain.vo.AstInventoryListVo;
import com.cxnet.asset.inventory.mapper.AstInventoryBillMapper;
import com.cxnet.asset.inventory.domain.AstInventoryBill;
import com.cxnet.asset.inventory.mapper.AstInventoryListMapper;
import com.cxnet.asset.inventory.service.AstInventoryBillService;
import com.cxnet.baseData.assetType.domain.BdAnnex;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import com.cxnet.rpc.service.basedata.BdPersonnelServiceIRpc;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (AstInventoryBill)表服务实现类
 *
 * @author zhangyl
 * @since 2021-04-02 10:02:37
 */
@Service
public class AstInventoryBillServiceImpl extends ServiceImpl<AstInventoryBillMapper, AstInventoryBill> implements AstInventoryBillService {

    @Autowired(required = false)
    private AstInventoryBillMapper astInventoryBillMapper;
    @Autowired(required = false)
    private AstInventoryListMapper astInventoryListMapper;
    @Autowired(required = false)
    private AstAnnexMapper astAnnexMapper;
    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;
    @Autowired(required = false)
    private AstInventoryListService astInventoryListService;
    @Autowired(required = false)
    private BdPersonnelServiceIRpc bdPersonnelServiceIRpc;

    @Override
    @Transactional
    public AstInventoryBill insertList(AstInventoryBill astInventoryBill) {
        QueryWrapper<AstInventoryList> listWapper = new QueryWrapper<>();
        listWapper.lambda().eq(AstInventoryList::getBillNo, astInventoryBill.getBillNo());
        List<AstInventoryList> inventoryLists = astInventoryListMapper.selectList(listWapper);
        if (inventoryLists.size() > 0) {
            throw new CustomException("该盘点任务已经生成盘点清单，不可重复生成");
        } else {
            AstInventoryListVo vo = new AstInventoryListVo();
            if (StringUtils.isNotEmpty(astInventoryBill.getAssetsTypeCode())) {
                List<String> type = Arrays.asList(astInventoryBill.getAssetsTypeCode().split(","));
                vo.setAssetsTypeCode(type);
            }
            if (StringUtils.isNotEmpty(astInventoryBill.getApplyDeptCode())) {
                List<String> deptCode = Arrays.asList(astInventoryBill.getApplyDeptCode().split(","));
                vo.setApplyDeptCode(deptCode);
            }
            if (StringUtils.isNotEmpty(astInventoryBill.getEmpCode())) {
                List<String> empCode = Arrays.asList(astInventoryBill.getEmpCode().split(","));
                vo.setEmpCode(empCode);
            }
            if (StringUtils.isNotEmpty(astInventoryBill.getPlaceCode())) {
                List<String> placeCode = Arrays.asList(astInventoryBill.getPlaceCode().split(","));
                vo.setPlaceCode(placeCode);
            }
            if (StringUtils.isNotEmpty(astInventoryBill.getApplyStatus())) {
                List<String> applyStatus = Arrays.asList(astInventoryBill.getApplyStatus().split(","));
                vo.setApplyStatus(applyStatus);
            }
            if (astInventoryBill.getAcquisitionStartDate() != null) {
                vo.setAcquisitionStartDate(astInventoryBill.getAcquisitionStartDate());
            }
            if (astInventoryBill.getAcquisitionEndDate() != null) {
                vo.setAcquisitionEndDate(astInventoryBill.getAcquisitionEndDate());
            }
            vo.setBillNo(astInventoryBill.getBillNo());
            astInventoryBillMapper.insertList(vo);
        }
        int count = astInventoryListMapper.selectCount(astInventoryBill.getBillNo());
        int sum = astInventoryListMapper.selSum(astInventoryBill.getBillNo());
        //根据单据号查询
        QueryWrapper<AstInventoryBill> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstInventoryBill::getBillNo, astInventoryBill.getBillNo());
        AstInventoryBill one = this.getOne(wrapper);

        one.setCheckPlanCount(Integer.toString(count));
        one.setCheckPlanSum(Integer.toString(sum));
        one.setStatus("2");
        this.updateById(one);
        return one;
    }

    @Override
    public AstInventoryBillVo selectAll(String billNo, String applyDeptCode, String empCode) {
        //查询主表数据
        AstInventoryBillVo billVo = new AstInventoryBillVo();
        QueryWrapper<AstInventoryBill> billQueryWrapper = new QueryWrapper<>();
        billQueryWrapper.lambda().eq(AstInventoryBill::getBillNo, billNo)
                .eq(AstInventoryBill::getDelFlag, "0");
        AstInventoryBill byId = this.getOne(billQueryWrapper);
        //根据部门code查询部门id
        QueryWrapper<BdPersonnel> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(byId.getApplyDeptCode())) {
            List<String> applyDeptCodes = Arrays.asList(byId.getApplyDeptCode().split(","));
            queryWrapper.lambda().in(BdPersonnel::getDeptCode, applyDeptCodes);
        }
        List<BdPersonnel> list = bdPersonnelServiceIRpc.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            list = list.stream().filter(p -> p.getDeptCode() != null).collect(Collectors.toList());
            //根据部门code去重
            list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(BdPersonnel::getDeptCode))), ArrayList::new));
            //取出去重后的部门id的集合
            List<String> applyDeptIds = list.stream().map(p -> p.getDeptId()).collect(Collectors.toList());
            byId.setDeptIds(applyDeptIds);
        }
        //查询明细表数据
        QueryWrapper<AstInventoryList> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstInventoryList::getBillNo, byId.getBillNo()).eq(AstInventoryList::getDelFlag, "0")
                .eq(StringUtils.isNotEmpty(applyDeptCode), AstInventoryList::getApplyDeptCode, applyDeptCode)
                .eq(StringUtils.isNotEmpty(empCode), AstInventoryList::getEmpCode, empCode);
        List<AstInventoryList> astInventoryLists = astInventoryListMapper.selectList(wrapper);
        //查询附件表数据
        QueryWrapper<AstAnnex> annexQueryWrapper = new QueryWrapper<>();
        annexQueryWrapper.lambda().eq(AstAnnex::getAstId, byId.getId());
        List<AstAnnex> astAnnexes = astAnnexMapper.selectList(annexQueryWrapper);
        //数据封装到对象中
        billVo.setAstInventoryBill(byId);
        billVo.setAstInventoryList(astInventoryLists);
        billVo.setAstAnnexList(astAnnexes);
        return billVo;
    }

    @Override
    @Transactional
    public String update(AstInventoryBillVo astInventoryBillVo) {
        AstInventoryBill astInventoryBill = astInventoryBillVo.getAstInventoryBill();
        if (ObjectUtil.isNull(astInventoryBill) || "2".equals(astInventoryBill.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        List<AstAnnex> astAnnexList = astInventoryBillVo.getAstAnnexList();
        //保存主表
        this.updateById(astInventoryBill);
        // 修改附件表
        QueryWrapper<AstAnnex> Wrapper = new QueryWrapper<>();
        Wrapper.lambda().eq(AstAnnex::getAstId, astInventoryBill.getId());
        astAnnexMapper.delete(Wrapper);
        astAnnexList.forEach(v -> {
            v.setAstId(astInventoryBill.getId());
            astAnnexMapper.insert(v);
        });
        return astInventoryBill.getId();
    }

    /**
     * 新增盘点方案
     *
     * @param astInventoryBill
     * @return
     */
    @Override
    public AstInventoryBill getList(AstInventoryBill astInventoryBill) {
        //使用部门循环
        deptId(astInventoryBill);
        astInventoryBill.setCreateTime(new Date());
        astInventoryBill.setStatus("1");
        astInventoryBill.setDelFlag("0");
        String nextNum = ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_INVENTORY_BILL, astInventoryBill);
        astInventoryBill.setCheckPlanCode(nextNum);
        String billNo = nextNum + "_" + astInventoryBill.getUnitId();
        astInventoryBill.setBillNo(billNo);
        return astInventoryBill;
    }

    @Override
    public AstInventoryBill updateList(AstInventoryBill astInventoryBill) {
        //使用部门循环
        deptId(astInventoryBill);
        return astInventoryBill;
    }


    /**
     * 查询盘点日志
     *
     * @param id
     * @return
     */
    @Override
    public List<AstInventoryBill> getBillOne(String id) {
        QueryWrapper<AstInventoryList> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstInventoryList::getCardId, id);
        List<AstInventoryList> list = astInventoryListService.list(wrapper);
        List<AstInventoryBill> inventoryBillList = new ArrayList<>();
        list.forEach(v -> {
            AstInventoryBill billOne = astInventoryBillMapper.getBillOne(v.getBillNo());
            inventoryBillList.add(billOne);
        });
        return inventoryBillList;
    }

    /**
     * 根据部门ids查询该部门下的人员
     *
     * @param deptIds
     * @return
     */
    @Override
    public List<BdPersonnel> selList(List<String> deptIds) {
        List<BdPersonnel> list = new ArrayList<>();
        for (String deptId : deptIds) {
            QueryWrapper<BdPersonnel> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(BdPersonnel::getDeptId, deptId).eq(BdPersonnel::getDelFlag, "0");
            List<BdPersonnel> bdPersonnels = bdPersonnelServiceIRpc.list(wrapper);
            for (BdPersonnel a : bdPersonnels) {
                list.add(a);
            }
        }
        //根据人员code去重
        list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(BdPersonnel::getUserCode))), ArrayList::new));
        return list;
    }

    private void deptId(AstInventoryBill astInventoryBill) {
        //使用部门循环
        List<String> deptIds = astInventoryBill.getDeptIds();
        String deptCode = "";
        String deptName = "";
        if (CollectionUtils.isNotEmpty(deptIds)) {
            for (String id : deptIds) {
                QueryWrapper<SysDept> deptQueryWrapper = new QueryWrapper<>();
                deptQueryWrapper.lambda().eq(SysDept::getDeptId, id)
                        .eq(SysDept::getDelFlag, "0").eq(SysDept::getStatus, "0");
                SysDept one = sysDeptServiceI.getOne(deptQueryWrapper);
                deptCode += one.getDeptCode() + ",";
                deptName += one.getDeptName() + ",";
            }
            deptCode = deptCode.substring(0, deptCode.length() - 1);
            deptName = deptName.substring(0, deptName.length() - 1);
            astInventoryBill.setApplyDeptCode(deptCode);
            astInventoryBill.setApplyDeptName(deptName);
        }
    }

}

