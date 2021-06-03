package com.cxnet.asset.inventory.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.dispose.domain.AstDisposeBill;
import com.cxnet.asset.dispose.service.AstDisposeBillService;
import com.cxnet.asset.inventory.domain.vo.AstInventoryBillVo;
import com.cxnet.asset.inventory.mapper.AstInventoryListMapper;
import com.cxnet.asset.inventory.domain.AstInventoryList;
import com.cxnet.asset.inventory.service.AstInventoryListService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.ServletUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.utils.BillUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import com.cxnet.rpc.service.basedata.BdPersonnelServiceIRpc;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 资产盘点子表(AstInventoryList)表服务实现类
 *
 * @author zhangyl
 * @since 2021-04-06 09:34:12
 */
@Service
public class AstInventoryListServiceImpl extends ServiceImpl<AstInventoryListMapper, AstInventoryList> implements AstInventoryListService {

    @Autowired(required = false)
    private AstInventoryListMapper astInventoryListMapper;
    @Autowired(required = false)
    private AstAnnexService astAnnexService;
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;
    @Autowired(required = false)
    private AstDisposeBillService astDisposeBillService;
    @Autowired(required = false)
    private BdPersonnelServiceIRpc bdPersonnelServiceIRpc;

    @Override
    public List<AstInventoryList> selectDept(String billNo) {
        return astInventoryListMapper.selectDept(billNo);
    }

    @Override
    public boolean insertBatchList(List<AstInventoryList> astInventoryLists) {
        String fiscalStr = ServletUtils.getFiscalStr();
        astInventoryLists.forEach(v -> {
            v.setDelFlag("0");
            v.setStatus("0");
            v.setIsDispose("2");
            v.setIsCheck("2");
            v.setFiscal(fiscalStr);
        });
        check(astInventoryLists);
        astInventoryLists.forEach(this::init);
        return this.saveBatch(astInventoryLists);
    }

    @Override
    public List<AstInventoryList> selectPan(String billNo, String unitId, String applyDeptCode, String empCode) {
        QueryWrapper<AstInventoryList> qw = new QueryWrapper<>();
        qw.lambda().eq(StringUtils.isNotEmpty(billNo), AstInventoryList::getBillNo, billNo)
                .eq(StringUtils.isNotEmpty(unitId), AstInventoryList::getUnitId, unitId)
                .eq(StringUtils.isNotEmpty(applyDeptCode), AstInventoryList::getApplyDeptCode, applyDeptCode)
                .eq(StringUtils.isNotEmpty(empCode), AstInventoryList::getEmpCode, empCode);
        List<AstInventoryList> list = this.list(qw);
        return list;
    }

    /**
     * 查询录入信息数据
     *
     * @param billNo
     * @param userId
     * @return
     */
    @Override
    public List<AstInventoryList> selectTory(String billNo, String userId) {
        //查询当前登录人的数据
        QueryWrapper<BdPersonnel> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StringUtils.isNotEmpty(userId), BdPersonnel::getUserId, userId).eq(BdPersonnel::getDelFlag, "0");
        List<BdPersonnel> personnelList = bdPersonnelServiceIRpc.list(wrapper);
        String userCode = "";
        String deptCode = "";
        for (BdPersonnel a : personnelList) {
            if (StringUtils.isNotEmpty(a.getDeptCode())) {
                userCode = a.getUserCode();
                deptCode = a.getDeptCode();
            }
        }
        //查询资产盘点明细表数据
        QueryWrapper<AstInventoryList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstInventoryList::getBillNo, billNo).eq(AstInventoryList::getDelFlag, "0")
                .eq(StringUtils.isNotEmpty(deptCode), AstInventoryList::getApplyDeptCode, deptCode)
                .eq(StringUtils.isNotEmpty(userCode), AstInventoryList::getEmpCode, userCode);
        List<AstInventoryList> list = this.list(qw);
        list.forEach(v -> {
            //查询每一条中的附件信息
            QueryWrapper<AstAnnex> annexQueryWrapper = new QueryWrapper<>();
            annexQueryWrapper.lambda().eq(AstAnnex::getAstId, v.getId());
            List<AstAnnex> astAnnexList = astAnnexService.list(annexQueryWrapper);
            v.setAstAnnexList(astAnnexList);
        });
        return list;
    }

    @Override
    @Transactional
    public List<AstInventoryList> updateTory(List<AstInventoryList> astInventoryLists) {
        astInventoryLists.forEach(v -> {
            v.setIsCheck("1");
            //处理附件表的数据
            QueryWrapper<AstAnnex> annexQueryWrapper = new QueryWrapper<>();
            annexQueryWrapper.lambda().eq(AstAnnex::getAstId, v.getId());
            astAnnexService.remove(annexQueryWrapper);
            //得到附件信息
            List<AstAnnex> astAnnexList = v.getAstAnnexList();
            if (CollectionUtils.isNotEmpty(astAnnexList)) {
                astAnnexList.forEach(a -> {
                    a.setAstId(v.getId());
                    astAnnexService.save(a);
                });
            }
            if (StringUtils.isEmpty(v.getPlanResults())) {
                v.setPlanResults("盘亏");
            }
            this.updateById(v);
        });
        //反向查询登录人id
        String billNo = astInventoryLists.get(0).getBillNo();
        String applyDeptCode = astInventoryLists.get(0).getApplyDeptCode();
        String empCode = astInventoryLists.get(0).getEmpCode();
        QueryWrapper<BdPersonnel> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BdPersonnel::getUserCode, empCode).eq(BdPersonnel::getDeptCode, applyDeptCode)
                .eq(BdPersonnel::getDelFlag, "0");
        List<BdPersonnel> personnelList = bdPersonnelServiceIRpc.list(wrapper);

        List<AstInventoryList> inventoryLists = this.selectTory(billNo, personnelList.get(0).getUserId());
        return inventoryLists;
    }

    /**
     * 保存时校验
     *
     * @param astInventoryLists
     */
    private void check(List<AstInventoryList> astInventoryLists) {
        astInventoryLists.forEach(this::check);
    }

    /**
     * 保存时校验
     */
    private void check(AstInventoryList astInventoryList) {
        QueryWrapper<AstInventoryList> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AstInventoryList::getCardCode, astInventoryList.getCardCode())
                .eq(AstInventoryList::getStatus, "0")
                .eq(AstInventoryList::getDelFlag, "0");
        AstInventoryList one = this.getOne(queryWrapper);
        if (ObjectUtil.isNotNull(one)) {
            throw new CustomException("卡片编号" + astInventoryList.getCardCode() + "已存在");
        }
    }

    /**
     * 初始化
     *
     * @param astInventoryList
     */
    private void init(AstInventoryList astInventoryList) {
        astInventoryList.setCreateName(SecurityUtils.getUsername());
        astInventoryList.setCreateBy(SecurityUtils.getLoginUser().getUser().getNickName());
        astInventoryList.setCreateTime(DateUtils.getNowDate());
    }

    @Transactional
    public String insertDispose(List<AstInventoryList> astInventoryLists) {
        //根据资产类别进行分组
        if (CollectionUtils.isNotEmpty(astInventoryLists)) {
            Map<String, List<AstInventoryList>> collect = astInventoryLists.stream().collect(Collectors.groupingBy(AstInventoryList::getClassCode));
            for (String key : collect.keySet()) {
                int count = astInventoryListMapper.selectCountBill(collect.get(key).get(0).getBillNo(), collect.get(key).get(0).getClassCode());
                int sum = astInventoryListMapper.selSumBill(collect.get(key).get(0).getBillNo(), collect.get(key).get(0).getClassCode());
                AstDisposeBill disposeBill = new AstDisposeBill();
                String id = UUID.randomUUID().toString().replace("-", "");
                disposeBill.setId(id);
                disposeBill.setCost(new BigDecimal(sum));
                disposeBill.setQty(count);
                disposeBill.setAstTypeCode(collect.get(key).get(0).getClassCode());
                disposeBill.setAstTypeName(collect.get(key).get(0).getClassName());
                disposeBill.setUnitId(collect.get(key).get(0).getUnitId());
                disposeBill.setUnitCode(collect.get(key).get(0).getUnitCode());
                disposeBill.setUnitName(collect.get(key).get(0).getUnitName());
                //设置处置形式代码
                disposeBill.setDisposetypeCode("05");
                disposeBill.setDelFlag("0");
                disposeBill.setStatus("0");
                disposeBill.setCreateTime(new Date());
                disposeBill.setDisposeCode(ruleServiceI.nextNumber(BillUtils.getRuleCode(BillTypeCodeConstant.AST_DISPOSE), disposeBill));
                //把数据插入到处置主表
                astDisposeBillService.save(disposeBill);

                //插入数据到处置明细表
                collect.get(key).forEach(v -> v.setBillId(id));
                astInventoryListMapper.insertAstIntory(collect.get(key));
            }
            //将生成处置状态改为1
            astInventoryLists.forEach(a -> a.setIsDispose("1"));
            this.updateBatchById(astInventoryLists);
        }
        return "生成成功";
    }
}

