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
 * ??????????????????(AstCheckBill)??????????????????
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
     * ????????????
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
            //??????
            case "0":
                astCheckBill.setCreateBy(SecurityUtils.getUsername());
                astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                break;
            // ??????
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astCheckBill.setPiids(runTaskByUserNameAndStatus);
                    astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                }
                break;
            // ??????
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astCheckBill.setPiids(runTaskByUserNameAndStatus);
                    astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                }
                break;
            // ??????
            case "3":
                astCheckBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                break;
            // ??????
            case "":
                astCheckBill.setCreateBy(SecurityUtils.getUsername());
                astCheckBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astCheckBills = this.baseMapper.selectAstCheckBillsList(astCheckBill);
                break;
            default:
                throw new CustomException("?????????????????????!");
        }
        return astCheckBills;
    }

    /**
     * ??????vo
     *
     * @param id
     * @return
     */
    @Override
    public AstCheckVo selectOne(String id) {

        AstCheckVo astCheckVo = new AstCheckVo();
        // ??????
        AstCheckBill astCheckBill = this.getById(id);
        if (ObjectUtil.isNull(astCheckBill) || "2".equals(astCheckBill.getDelFlag())) {
            throw new CustomException("??????????????????????????????????????????????????????");
        }
        astCheckBill.setDelFlag("0");
        astCheckVo.setAstCheckBill(astCheckBill);
        // ??????
        QueryWrapper<AstCheckList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstCheckList::getCheckId, id)
                .orderByAsc(AstCheckList::getSort);
        List<AstCheckList> astCheckLists = astCheckListService.list(qw);
        astCheckVo.setAstCheckLists(astCheckLists);
        // ??????
        List<AstAnnex> astAnnexes = astAnnexService.listByAstIdAndAnnexType(id, "0");
        astCheckVo.setAstAnnexes(astAnnexes);
        return astCheckVo;
    }

    /**
     * ??????
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
        // ????????????
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
            throw new CustomException("???????????????????????????????????????");
        }
        // ????????????
        astCheckLists.forEach(v -> v.setCheckId(id));
        addName(astCheckLists);
        astCheckListService.saveBatch(astCheckLists);
        // ???????????????
        saveAstAnnexes(astAnnexes, id);
        return selectOne(astCheckBill.getId());
    }

    /**
     * ????????????
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
     * ????????????
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
     * ??????
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
            throw new CustomException("??????????????????????????????????????????????????????");
        }
        if (!ProcessConstant.STATUS_0.equals(byId.getStatus())
                && !ProcessConstant.STATUS_9.equals(byId.getStatus())) {
            throw new CustomException("?????????????????????????????????");
        }
        if ("0".equals(byId.getIsProduce())) {
            throw new CustomException("???????????????????????????????????????");
        }
        List<AstCheckList> astCheckLists = astCheckVo.getAstCheckLists();
        List<AstAnnex> astAnnexes = astCheckVo.getAstAnnexes();
        // ????????????
        addName(astCheckBill);
        this.updateById(astCheckBill);
        // ????????????
        if (CollectionUtil.isEmpty(astCheckLists)) {
            throw new CustomException("???????????????????????????????????????");
        }
        QueryWrapper<AstCheckList> astCheckListQueryWrapper = new QueryWrapper<>();
        astCheckListQueryWrapper.lambda().eq(AstCheckList::getCheckId, id);
        astCheckListService.remove(astCheckListQueryWrapper);
        astCheckLists.forEach(v -> v.setCheckId(id));
        addName(astCheckLists);
        astCheckListService.saveBatch(astCheckLists);
        // ???????????????
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        astAnnexService.remove(astAnnexQueryWrapper);
        // ???????????????
        saveAstAnnexes(astAnnexes, id);
        return selectOne(astCheckBill.getId());
    }

    /**
     * ????????????
     *
     * @param astCheckLists
     */
    private void addName(List<AstCheckList> astCheckLists) {
        String deptId = SecurityUtils.getLoginUser().getUser().getDept().getDeptId();
        // ??????
        List<SysDept> sysDepts = sysDeptServiceI.list();
        Map<String, SysDept> stringSysDeptMap = sysDepts.stream().collect(Collectors.toMap(SysDept::getDeptId, Function.identity()));
        // ??????
        List<SysUser> sysUsers = sysUserServiceI.selectUserList(new SysUser());
        Map<String, SysUser> sysUserMap = sysUsers.stream().collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
        // ????????????
        Map<String, String> astFundSourceMap = sysDictDataServiceI.selectDictDataMapByType("ast_fund_source");
        // ????????????
        Map<String, String> astTypeMap = sysDictDataServiceI.selectDictDataMapByType("ast_type");
        // ????????????
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
     * ??????
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new CustomException("?????????????????????");
        }
        List<AstCheckBill> astCheckBills = this.listByIds(ids);
        if (CollectionUtil.isEmpty(astCheckBills)) {
            return "???????????????";
        }
        for (AstCheckBill astCheckBill : astCheckBills) {
            if (!ProcessConstant.STATUS_0.equals(astCheckBill.getStatus())
                    && !ProcessConstant.STATUS_9.equals(astCheckBill.getStatus())) {
                throw new CustomException("?????????????????????????????????");
            }
            astCheckBill.setDelFlag("2");
        }
        this.updateBatchById(astCheckBills);
        return "???????????????";
    }

    /**
     * ????????????
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String produce(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            throw new CustomException("???????????????????????????");
        }
        List<AstCheckBill> astCheckBills = this.listByIds(ids);
        if (CollectionUtil.isEmpty(astCheckBills)) {
            throw new CustomException("????????????????????????????????????????????????");
        }
        ids = astCheckBills.stream().map(AstCheckBill::getId).collect(Collectors.toList());
        QueryWrapper<AstCheckList> qw = new QueryWrapper<>();
        qw.lambda().in(AstCheckList::getCheckId, ids);
        List<AstCheckList> astCheckLists = astCheckListService.list(qw);
        Map<String, List<AstCheckList>> stringListMap = astCheckLists.stream().collect(Collectors.groupingBy(v -> v.getCheckId()));
        for (AstCheckBill astCheckBill : astCheckBills) {
            if ("2".equals(astCheckBill.getDelFlag()) || !"2".equals(astCheckBill.getStatus()) || "0".equals(astCheckBill.getIsProduce())) {
                throw new CustomException("???????????????????????????????????????!");
            }
            astCheckBill.setIsProduce("0");
            List<AstCheckList> astCheckListList = stringListMap.get(astCheckBill.getId());
            // ??????????????????
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
        return "?????????????????????";
    }

    /**
     * ??????
     *
     * @param commonProcessRequest ????????????????????????
     * @return ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstCheckBill> list = this.listByIds(Arrays.asList(id));
        if (CollectionUtil.isEmpty(list)) {
            throw new CustomException("????????????????????????????????????????????????");
        }
        checkStatus(list, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        list.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // ????????????
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_ChECK, processInstanceId);
            v.setProcessinstid(processInstanceId);
            v.setStatus(ProcessConstant.STATUS_1);
            // ??????????????????
            Map<String, Object> startVariables = TaskUtils.getStartVariables(commonProcessRequest, v);
            // ????????????
            List<Task> complete = TaskUtils.complete(processInstanceId, startVariables);
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
        });
        this.updateBatchById(list);
        return "???????????????";
    }

    /**
     * ????????????
     *
     * @param list        ????????????
     * @param checkStatus ????????????????????????????????????
     */
    void checkStatus(List<AstCheckBill> list, String... checkStatus) {
        if (checkStatus != null) {
            List<String> checkStatuss = Arrays.asList(checkStatus);
            for (AstCheckBill v : list) {
                if (!checkStatuss.contains(v.getStatus())) {
                    throw new CustomException("????????????????????????");
                }
            }
        }
    }

    /**
     * ??????
     *
     * @param id
     */
    private void checkId(String[] id) {
        if (ArrayUtil.isEmpty(id)) {
            throw new CustomException("???????????????????????????");
        }
    }

    /**
     * ??????
     *
     * @param commonProcessRequest ????????????????????????
     * @return ????????????
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
            // ????????????
            List<Task> complete = TaskUtils.complete(v.getProcessinstid(), TaskUtils.getGoVariables(commonProcessRequest, v));
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
            // ????????????????????????
            if (TaskUtils.isEnd(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_2);
                v.setApprovalPost("?????????");
            }
        });
        this.updateBatchById(list);
        return "???????????????";
    }

    /**
     * ??????
     *
     * @param commonProcessRequest ????????????????????????
     * @return ????????????
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
        // ??????????????????
        TaskUtils.backProcess(piids, commonProcessRequest);
        // ?????????????????????????????????????????????
        list.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(list);
        return "???????????????";
    }

    /**
     * ??????
     *
     * @param commonProcessRequest ????????????????????????
     * @return ????????????
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
        // ??????????????????
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // ????????????????????????
        list.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(list);
        return "???????????????";
    }


    /**
     * ???????????????
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