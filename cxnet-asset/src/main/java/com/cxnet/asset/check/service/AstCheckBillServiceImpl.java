package com.cxnet.asset.check.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.check.domain.AstCheckList;
import com.cxnet.asset.check.domain.vo.AstCheckVo;
import com.cxnet.asset.check.mapper.AstCheckBillMapper;
import com.cxnet.asset.check.domain.AstCheckBill;
import com.cxnet.asset.check.service.AstCheckBillService;
import com.cxnet.baseData.assetType.domain.BdAssetType;
import com.cxnet.baseData.assetType.service.BdAssetTypeService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.dict.service.SysDictDataServiceI;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.mapper.SysUserMapper;
import com.cxnet.project.system.user.service.SysUserServiceI;
import com.cxnet.rpc.domain.PurPurchase;
import com.cxnet.rpc.domain.basedata.BdPersonRpc;
import com.cxnet.rpc.service.basedata.BdPersonServiceRpc;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资产验收主表(AstCheckBill)表服务实现类
 *
 * @author caixx
 * @since 2021-03-25 09:39:41
 */
@Service
public class AstCheckBillServiceImpl extends ServiceImpl<AstCheckBillMapper, AstCheckBill> implements AstCheckBillService {

    @Autowired(required = false)
    private AstCheckListService astCheckListService;

    @Autowired(required = false)
    private AstAnnexService astAnnexService;

    @Autowired(required = false)
    private RuleServiceI ruleServiceI;

    @Autowired(required = false)
    private AstCardService astCardService;

    @Autowired(required = false)
    private BdPersonServiceRpc bdPersonServiceRpc;

    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;

    @Autowired(required = false)
    private SysUserServiceI sysUserServiceI;

    @Autowired(required = false)
    private SysDictDataServiceI sysDictDataServiceI;

    @Autowired(required = false)
    private BdAssetTypeService bdAssetTypeService;


    /**
     * 分页查询
     *
     * @param astCheckBill
     * @return
     */
    @Override
    public List<AstCheckBill> selectAll(AstCheckBill astCheckBill) {
        String status = StringUtils.isEmpty(astCheckBill.getStatus()) ? "" : astCheckBill.getStatus();
        astCheckBill.setDelFlag("0");
        List<AstCheckBill> astCheckBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            //草稿
            case "0":
                astCheckBill.setCreateBy(SecurityUtils.getUsername());
                astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astCheckBill.setPiids(runTaskByUserNameAndStatus);
                    astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astCheckBill.setPiids(runTaskByUserNameAndStatus);
                    astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                }
                break;
            // 终审
            case "3":
                astCheckBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                break;
            // 全部
            case "":
                astCheckBill.setCreateBy(SecurityUtils.getUsername());
                astCheckBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astCheckBills;
    }

    /**
     * 查询vo
     *
     * @param id
     * @return
     */
    @Override
    public AstCheckVo selectOne(String id) {

        AstCheckVo astCheckVo = new AstCheckVo();
        // 主表
        AstCheckBill astCheckBill = this.getById(id);
        if (ObjectUtil.isNull(astCheckBill) || "2".equals(astCheckBill.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        astCheckBill.setDelFlag("0");
        astCheckVo.setAstCheckBill(astCheckBill);
        // 子表
        QueryWrapper<AstCheckList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstCheckList::getCheckId, id)
                .orderByAsc(AstCheckList::getSort);
        List<AstCheckList> astCheckLists = astCheckListService.list(qw);
        astCheckVo.setAstCheckLists(astCheckLists);
        // 附件
        List<AstAnnex> astAnnexes = astAnnexService.listByAstIdAndAnnexType(id, "0");
        astCheckVo.setAstAnnexes(astAnnexes);
        return astCheckVo;
    }

    /**
     * 新增
     *
     * @param astCheckVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstCheckVo insert(AstCheckVo astCheckVo) {
        SysDept dept = SecurityUtils.getLoginUser().getUser().getDept();
        AstCheckBill astCheckBill = astCheckVo.getAstCheckBill();
        List<AstCheckList> astCheckLists = astCheckVo.getAstCheckLists();
        List<AstAnnex> astAnnexes = astCheckVo.getAstAnnexes();
        // 保存主表
        astCheckBill.setDelFlag("0");
        astCheckBill.setStatus("0");
        astCheckBill.setIsProduce("2");
        astCheckBill.setCreateTime(DateUtil.date());
        astCheckBill.setUnitId(dept.getDeptId());
        astCheckBill.setUnitCode(dept.getDeptCode());
        astCheckBill.setUnitName(dept.getDeptName());
        addName(astCheckBill);
        String nextNum = ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_ChECK, astCheckBill);
        astCheckBill.setCheckCode(nextNum);
        this.save(astCheckBill);
        String id = astCheckBill.getId();
        if (CollectionUtil.isEmpty(astCheckLists)) {
            throw new CustomException("请补全验收资产明细后保存！");
        }
        // 保存子表
        astCheckLists.forEach(v -> v.setCheckId(id));
        addName(astCheckLists);
        astCheckListService.saveBatch(astCheckLists);
        // 保存附件表
        saveAstAnnexes(astAnnexes, id);
        return selectOne(astCheckBill.getId());
    }

    /**
     * 保存附件
     *
     * @param astAnnexes
     * @param id
     */
    private void saveAstAnnexes(List<AstAnnex> astAnnexes, String id) {
        if (CollectionUtil.isNotEmpty(astAnnexes)) {
            astAnnexes.forEach(v -> {
                v.setAstId(id);
                v.setAnnexType("0");
            });
            astAnnexService.saveBatch(astAnnexes);
        }
    }

    /**
     * 名称转换
     *
     * @param astCheckBill
     */
    private void addName(AstCheckBill astCheckBill) {
        if (StringUtils.isNotEmpty(astCheckBill.getCheckPerson())) {
            BdPersonRpc bdPersonRpc = bdPersonServiceRpc.selectPersonByDeptIdAndDeptId(astCheckBill.getCheckDeptId(), astCheckBill.getCheckPerson());
            if (ObjectUtil.isNotNull(bdPersonRpc)) {
                astCheckBill.setCheckPersonName(bdPersonRpc.getUserName());
            }
        }
    }

    /**
     * 修改
     *
     * @param astCheckVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstCheckVo update(AstCheckVo astCheckVo) {
        AstCheckBill astCheckBill = astCheckVo.getAstCheckBill();
        String id = astCheckBill.getId();
        AstCheckBill byId = this.getById(id);
        if (ObjectUtil.isNull(byId) || "2".equals(byId.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        if (!ProcessConstant.STATUS_0.equals(byId.getStatus())
                && !ProcessConstant.STATUS_9.equals(byId.getStatus())) {
            throw new CustomException("只有草稿状态可以修改！");
        }
        if ("0".equals(byId.getIsProduce())) {
            throw new CustomException("单据已生成卡片，不可修改！");
        }
        List<AstCheckList> astCheckLists = astCheckVo.getAstCheckLists();
        List<AstAnnex> astAnnexes = astCheckVo.getAstAnnexes();
        // 修改主表
        addName(astCheckBill);
        this.updateById(astCheckBill);
        // 修改子表
        if (CollectionUtil.isEmpty(astCheckLists)) {
            throw new CustomException("请补全验收资产明细后保存！");
        }
        QueryWrapper<AstCheckList> astCheckListQueryWrapper = new QueryWrapper<>();
        astCheckListQueryWrapper.lambda().eq(AstCheckList::getCheckId, id);
        astCheckListService.remove(astCheckListQueryWrapper);
        astCheckLists.forEach(v -> v.setCheckId(id));
        addName(astCheckLists);
        astCheckListService.saveBatch(astCheckLists);
        // 修改附件表
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        astAnnexService.remove(astAnnexQueryWrapper);
        // 保存附件表
        saveAstAnnexes(astAnnexes, id);
        return selectOne(astCheckBill.getId());
    }

    /**
     * 名称转换
     *
     * @param astCheckLists
     */
    private void addName(List<AstCheckList> astCheckLists) {
        String deptId = SecurityUtils.getLoginUser().getUser().getDept().getDeptId();
        // 部门
        List<SysDept> sysDepts = sysDeptServiceI.list();
        Map<String, SysDept> stringSysDeptMap = sysDepts.stream().collect(Collectors.toMap(SysDept::getDeptId, Function.identity()));
        // 用户
        List<SysUser> sysUsers = sysUserServiceI.selectUserList(new SysUser());
        Map<String, SysUser> sysUserMap = sysUsers.stream().collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
        // 经费来源
        Map<String, String> astFundSourceMap = sysDictDataServiceI.selectDictDataMapByType("ast_fund_source");
        // 资产大类
        Map<String, String> astTypeMap = sysDictDataServiceI.selectDictDataMapByType("ast_type");
        // 资产类别
        QueryWrapper<BdAssetType> qw = new QueryWrapper<>();
        qw.lambda().eq(BdAssetType::getUnitId, deptId);
        List<BdAssetType> bdAssetTypes = bdAssetTypeService.list(qw);
        Map<String, BdAssetType> bdAssetTypeMap = bdAssetTypes.stream().collect(Collectors.toMap(BdAssetType::getAssetCode, Function.identity()));
        for (AstCheckList astCheckList : astCheckLists) {
            SysDept sysDept = stringSysDeptMap.get(astCheckList.getDepartmentId());
            if (sysDept != null) {
                astCheckList.setDepartmentCode(sysDept.getDeptCode());
                astCheckList.setDepartmentCode(sysDept.getDeptName());
            }
            SysUser sysUser = sysUserMap.get(astCheckList.getEmpId());
            if (sysUser != null) {
                astCheckList.setEmpCode(sysUser.getUserName());
                astCheckList.setEmpName(sysUser.getNickName());
            }
            astCheckList.setFundsourceName(astFundSourceMap.get(astCheckList.getFundsourceCode()));
            BdAssetType bdAssetType = bdAssetTypeMap.get(astCheckList.getCategoryCode());
            if (bdAssetType != null) {
                astCheckList.setCategoryName(bdAssetType.getAssetName());
                astCheckList.setClassCode(bdAssetType.getClassification());
                astCheckList.setClassName(astTypeMap.get(bdAssetType.getClassification()));
                astCheckList.setTypeCode(bdAssetType.getAssetType());
                BdAssetType bdAssetTypeName = bdAssetTypeMap.get(bdAssetType.getAssetType());
                if (bdAssetTypeName != null) {
                    astCheckList.setTypeName(bdAssetTypeName.getAssetName());
                }
                astCheckList.setDepMethod(bdAssetType.getDepreciationMethod());

            }
        }
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new CustomException("至少选中一条！");
        }
        List<AstCheckBill> astCheckBills = this.listByIds(ids);
        if (CollectionUtil.isEmpty(astCheckBills)) {
            return "删除成功！";
        }
        for (AstCheckBill astCheckBill : astCheckBills) {
            if (!ProcessConstant.STATUS_0.equals(astCheckBill.getStatus())
                    && !ProcessConstant.STATUS_9.equals(astCheckBill.getStatus())) {
                throw new CustomException("只有草稿状态可以删除！");
            }
            astCheckBill.setDelFlag("2");
        }
        this.updateBatchById(astCheckBills);
        return "删除成功！";
    }

    /**
     * 生成卡片
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String produce(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new CustomException("至少选中一条数据！");
        }
        List<AstCheckBill> astCheckBills = this.listByIds(ids);
        if (CollectionUtil.isEmpty(astCheckBills)) {
            throw new CustomException("未查询到所选单据，请刷新后重试！");
        }
        ids = astCheckBills.stream().map(AstCheckBill::getId).collect(Collectors.toList());
        QueryWrapper<AstCheckList> qw = new QueryWrapper<>();
        qw.lambda().in(AstCheckList::getCheckId, ids);
        List<AstCheckList> astCheckLists = astCheckListService.list(qw);
        Map<String, List<AstCheckList>> stringListMap = astCheckLists.stream().collect(Collectors.groupingBy(v -> v.getCheckId()));
        for (AstCheckBill astCheckBill : astCheckBills) {
            if ("2".equals(astCheckBill.getDelFlag()) || !"2".equals(astCheckBill.getStatus()) || "0".equals(astCheckBill.getIsProduce())) {
                throw new CustomException("单据状态异常，请刷新后重试!");
            }
            astCheckBill.setIsProduce("0");
            List<AstCheckList> astCheckListList = stringListMap.get(astCheckBill.getId());
            // 生成资产卡片
            if (CollectionUtil.isNotEmpty(astCheckListList)) {
                List<AstCard> saveCard = new ArrayList<>();
                for (AstCheckList v : astCheckListList) {
                    AstCard astCard = new AstCard(astCheckBill, v);
                    String nextNum = ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_REGISTER, astCheckBill);
                    astCard.setAstCode(nextNum);
                    saveCard.add(astCard);
                    v.setAstCode(astCard.getAstCode());
                    v.setAstId(astCard.getId());
                }
                if (CollectionUtil.isNotEmpty(saveCard)) {
                    astCardService.saveOrUpdateBatch(saveCard);
                }
                astCheckListService.updateBatchById(astCheckListList);
            }
        }
        this.updateBatchById(astCheckBills);
        return "生成卡片成功！";
    }

    /**
     * 送审
     *
     * @param commonProcessRequest 流程提交参数对象
     * @return 送审结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstCheckBill> list = this.listByIds(Arrays.asList(id));
        if (CollectionUtil.isEmpty(list)) {
            throw new CustomException("未查询到单据信息，请刷新后重试！");
        }
        checkStatus(list, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        list.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_ChECK, processInstanceId);
            v.setProcessinstid(processInstanceId);
            v.setStatus(ProcessConstant.STATUS_1);
            // 设置流程变量
            Map<String, Object> startVariables = TaskUtils.getStartVariables(commonProcessRequest, v);
            // 执行任务
            List<Task> complete = TaskUtils.complete(processInstanceId, startVariables);
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
        });
        this.updateBatchById(list);
        return "送审成功！";
    }

    /**
     * 校验状态
     *
     * @param list        单据列表
     * @param checkStatus 校验状态，必须是其中一个
     */
    void checkStatus(List<AstCheckBill> list, String... checkStatus) {
        if (checkStatus != null) {
            List<String> checkStatuss = Arrays.asList(checkStatus);
            for (AstCheckBill v : list) {
                if (!checkStatuss.contains(v.getStatus())) {
                    throw new CustomException("单据状态不合法！");
                }
            }
        }
    }

    /**
     * 验空
     *
     * @param id
     */
    private void checkId(String[] id) {
        if (ArrayUtil.isEmpty(id)) {
            throw new CustomException("至少选择一条单据！");
        }
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 流程提交参数对象
     * @return 审核结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String audit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstCheckBill> list = this.listByIds(Arrays.asList(id));
        checkStatus(list, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        list.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            // 执行任务
            List<Task> complete = TaskUtils.complete(v.getProcessinstid(), TaskUtils.getGoVariables(commonProcessRequest, v));
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
            // 判断流程是否结束
            if (TaskUtils.isEnd(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_2);
                v.setApprovalPost("已终审");
            }
        });
        this.updateBatchById(list);
        return "审核成功！";
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 流程提交参数对象
     * @return 退回结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String back(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstCheckBill> list = this.listByIds(Arrays.asList(id));
        checkStatus(list, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        list.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点并设置单据状态
        list.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(list);
        return "退回成功！";
    }

    /**
     * 收回
     *
     * @param commonProcessRequest 流程提交参数对象
     * @return 收回结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String taskBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstCheckBill> list = this.listByIds(Arrays.asList(id));
        checkStatus(list, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        list.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        list.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(list);
        return "收回成功！";
    }


    /**
     * 查询验收人
     *
     * @return
     */
    @Override
    public List<Map<String, String>> selectCheckPersonAll() {
        QueryWrapper<AstCheckBill> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(AstCheckBill::getDelFlag, "0")
                .select(AstCheckBill::getCheckPerson, AstCheckBill::getCheckPersonName)
                .groupBy(AstCheckBill::getCheckPerson, AstCheckBill::getCheckPersonName);
        List<AstCheckBill> list = this.list(qw);
        List<Map<String, String>> mapList = new ArrayList<>();
        if (CollectionUtil.isEmpty(list)) {
            return mapList;
        }
        list.forEach(v -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("checkPerson", v.getCheckPerson());
            map.put("checkPersonName", v.getCheckPersonName());
            mapList.add(map);
        });
        return mapList.stream().filter(v -> StringUtils.isNotEmpty(v.get("checkPersonName"))).collect(Collectors.toList());
    }


}