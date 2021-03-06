package com.cxnet.asset.allot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cxnet.common.utils.SpringContextUtils;
import com.cxnet.project.common.service.CheckStatus;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import com.cxnet.rpc.service.basedata.BdPersonnelServiceIRpc;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.allot.domain.AstAllotBill;
import com.cxnet.asset.allot.domain.vo.AstAllotVo;
import com.cxnet.asset.allot.mapper.AstAllotBillMapper;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.mapper.AstAnnexMapper;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.mapper.AstBillListMapper;
import com.cxnet.asset.card.mapper.AstCardMapper;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.flow.utils.BillUtils;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.service.SysUserServiceI;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ????????????????????????(AstAllotBill)??????????????????
 *
 * @author zhaoyi
 * @since 2021-04-09 14:33:01
 */
@Service
@Slf4j
public class AstAllotBillServiceImpl extends ServiceImpl<AstAllotBillMapper, AstAllotBill> implements AstAllotBillService {

    @Autowired(required = false)
    private RuleServiceI ruleServiceI;
    @Autowired(required = false)
    private SysUserServiceI sysUserServiceI;
    @Autowired(required = false)
    private SysDeptServiceI sysDeptServiceI;
    @Autowired(required = false)
    private AstAllotBillMapper astAllotBillMapper;
    @Autowired(required = false)
    private AstBillListMapper astBillListMapper;
    @Autowired(required = false)
    private AstCardMapper astCardMapper;
    @Autowired(required = false)
    private AstAnnexMapper astAnnexMapper;
    @Autowired(required = false)
    private AstBillListService astBillListService;
    @Autowired(required = false)
    private AstAnnexService astAnnexService;
    @Autowired(required = false)
    private AstCardService astCardService;
    @Autowired(required = false)
    private BdPersonnelServiceIRpc bdPersonnelServiceIRpc;

    /**
     * ??????????????????????????????
     *
     * @param astBillLists
     * @author zhaoyi
     */
    public void ifExist(List<AstBillList> astBillLists) {
        astBillLists.forEach(a -> {
            AstCard astCard = astCardMapper.selectById(a.getAstId());
            if (("0").equals(astCard.getInitialState())) {
                throw new CustomException("??????" + astCard.getAstCode() + "???????????????");
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAstAllotVo(AstAllotVo astAllotVo) {
        AstAllotBill allotBill = astAllotVo.getAllotBill();
        List<AstBillList> astBillLists = astAllotVo.getAstBillLists();
        List<AstAnnex> astAnnexes = astAllotVo.getAstAnnexes();
        allotBill.setCreateTime(new Date());
        this.ifExist(astBillLists);
        //????????????????????????
        String allotCode = ruleServiceI.nextNumber(BillUtils.getRuleCode(BillTypeCodeConstant.AST_ALLOT_CODE), allotBill);
        allotBill.setAllotCode(allotCode);
        allotBill.setStatus("0");
        allotBill.setDelFlag("0");
        SysDept sysDept = sysDeptServiceI.selectDeptById(allotBill.getDeptIdIn());
        allotBill.setDeptCodeIn(sysDept.getDeptCode());
        allotBill.setDeptNameIn(sysDept.getDeptName());
        BdPersonnel bdPersonnel = bdPersonnelServiceIRpc.selectBdPersonnelById(allotBill.getEmpIdIn());
        allotBill.setEmpCodeIn(bdPersonnel.getUserCode());
        allotBill.setEmpNameIn(bdPersonnel.getUserName());
        int i = astAllotBillMapper.insert(allotBill);
        //??????????????????
        if (i > 0) {
            //????????????
            List<AstBillList> astBillLists1 = new ArrayList<>();
            List<AstCard> astCardList = new ArrayList<>();
            astBillLists.forEach(a -> {
                a.setBillId(allotBill.getId());
                a.setId("");
                a.setBillType("ast_allot");
                astBillLists1.add(a);
                AstCard astCard = astCardMapper.selectById(a.getAstId());
                //??????????????????
                astCard.setInitialState("0");
                astCardList.add(astCard);
            });
            astBillListService.saveBatch(astBillLists1);
            astCardService.updateBatchById(astCardList);
            //????????????
            List<AstAnnex> astAnnexes1 = new ArrayList<>();
            astAnnexes.forEach(b -> {
                b.setAstId(allotBill.getId());
                b.setAnnexType("42");
                astAnnexes1.add(b);
            });
            astAnnexService.saveBatch(astAnnexes1);
        }
        return allotBill.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAstAllotVo(AstAllotVo astAllotVo) {
        AstAllotBill allotBill = astAllotVo.getAllotBill();
        List<AstBillList> astBillLists = astAllotVo.getAstBillLists();
        List<AstAnnex> astAnnexes = astAllotVo.getAstAnnexes();
        String id = allotBill.getId();
        AstAllotBill selectById = this.getById(id);
        if (ObjectUtil.isNull(selectById) || "2".equals(selectById.getDelFlag())) {
            throw new CustomException("??????????????????????????????????????????????????????");
        }
        if (!"0".equals(selectById.getStatus())) {
            throw new CustomException("?????????????????????????????????");
        }
        SysDept sysDept = sysDeptServiceI.selectDeptById(allotBill.getDeptIdIn());
        allotBill.setDeptCodeIn(sysDept.getDeptCode());
        allotBill.setDeptNameIn(sysDept.getDeptName());
        BdPersonnel bdPersonnel = bdPersonnelServiceIRpc.selectBdPersonnelById(allotBill.getEmpIdIn());
        allotBill.setEmpCodeIn(bdPersonnel.getUserCode());
        allotBill.setEmpNameIn(bdPersonnel.getUserName());
        //????????????
        this.updateById(allotBill);
        //???????????????
        QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper<>();
        //????????????????????????????????????
        astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, id);
        List<AstBillList> astBillLists1 = astBillListService.list(astBillListQueryWrapper);
        astBillListService.remove(astBillListQueryWrapper);
        //??????????????????
        List<AstCard> astCardList = new ArrayList<>();
        astBillLists1.forEach(a -> {
            AstCard astCard = astCardMapper.selectById(a.getAstId());
            //??????????????????,????????????
            astCard.setInitialState("1");
            astCardList.add(astCard);
        });
        astCardService.updateBatchById(astCardList);
        List<AstCard> astCardList1 = new ArrayList<>();
        astBillLists.forEach(a -> {
            a.setBillId(id);
            a.setId("");
            a.setBillType("ast_allocation");
            AstCard astCard = astCardMapper.selectById(a.getAstId());
            //??????????????????,????????????
            astCard.setInitialState("0");
            astCardList1.add(astCard);
        });
        astCardService.updateBatchById(astCardList1);
        astBillListService.saveBatch(astBillLists);
        //???????????????
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        astAnnexService.remove(astAnnexQueryWrapper);
        astAnnexes.forEach(b -> {
            b.setAstId(id);
            b.setAnnexType("41");
        });
        astAnnexService.saveBatch(astAnnexes);
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(List<String> ids) {
        List<AstAllotBill> astAllotBills = astAllotBillMapper.selectBatchIds(ids);
        List<AstCard> astCardList = new ArrayList<>();
        astAllotBills.forEach(a -> {
            a.setDelFlag("2");
            QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper<>();
            astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, a.getId());
            List<AstBillList> astBillLists = astBillListService.list(astBillListQueryWrapper);
            astBillLists.forEach(b -> {
                AstCard astCard = astCardMapper.selectById(b.getAstId());
                //?????????????????????????????????
                astCard.setInitialState("1");
                astCardList.add(astCard);
            });
        });
        astCardService.updateBatchById(astCardList);
        this.updateBatchById(astAllotBills);
        return "success";
    }

    /**
     * ????????????
     *
     * @param id
     */
    public void checkId(String[] id) {
        if (ArrayUtil.isEmpty(id)) {
            throw new CustomException("????????????????????????");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String allotSubmit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstAllotBill> astAllotBills = this.listByIds(Arrays.asList(ids));
        if (CollectionUtil.isEmpty(astAllotBills)) {
            throw new CustomException("?????????????????????");
        }
        // ??????????????????
        List<String> billStatusList = astAllotBills.stream().map(AstAllotBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astAllotBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // ????????????
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_ALLOT_CODE, processInstanceId);
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
        this.updateBatchById(astAllotBills);
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String allotAudit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstAllotBill> astAllotBills = this.listByIds(Arrays.asList(ids));
        if (CollectionUtils.isEmpty(astAllotBills)) {
            throw new CustomException("?????????????????????");
        }
        // ??????????????????
        List<String> billStatusList = astAllotBills.stream().map(AstAllotBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        astAllotBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            // ????????????
            List<Task> complete = TaskUtils.complete(v.getProcessinstid(), TaskUtils.getGoVariables(commonProcessRequest, v));
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
            // ????????????????????????
            if (TaskUtils.isEnd(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_2);
                v.setApprovalPost(ProcessConstant.END);
                v.setVerificationDate(new Date());
                List<AstCard> astCardList = new ArrayList<>();
                List<AstCard> newAstCardList = new ArrayList<>();
                astAllotBills.forEach(a -> {
                    a.setVerificationDate(new Date());
                    QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper();
                    //??????????????????
                    astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, a.getId());
                    List<AstBillList> astDisposeLists = astBillListService.list(astBillListQueryWrapper);
                    //??????????????????
                    astDisposeLists.forEach(b -> {
                        AstCard astCard = astCardMapper.selectById(b.getAstId());
                        AstCard newAstCard = new AstCard();
                        BeanUtils.copyProperties(astCard, newAstCard);
                        //???????????????????????????
                        astCard.setInitialState("1");
                        astCard.setAstStatus("3");
                        astCardList.add(astCard);
                        //???????????????????????????
                        newAstCard.setId("");
                        newAstCard.setCreateTime(new Date());
                        newAstCard.setAstCode(ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_REGISTER, newAstCard));
                        newAstCard.setUnitId(v.getUnitIdIn());
                        newAstCard.setUnitCode(v.getUnitCode());
                        newAstCard.setUnitName(v.getUnitName());
                        newAstCard.setDepartmentId(v.getDeptIdIn());
                        newAstCard.setDepartmentCode(v.getDeptCodeIn());
                        newAstCard.setDepartmentName(v.getDeptNameIn());
                        newAstCard.setEmpId(v.getEmpIdIn());
                        newAstCard.setEmpCode(v.getEmpCodeIn());
                        newAstCard.setEmpName(v.getEmpNameIn());
                        newAstCard.setInitialState("1");
                        newAstCard.setAstStatus("0");
                        newAstCard.setAddCode("2");
                        newAstCard.setAddDate(new Date());
                        newAstCard.setIsVou("2");
                        newAstCard.setCreateBy("");
                        newAstCard.setCreateName("");
                        newAstCard.setUpdateBy("");
                        newAstCard.setUpdateTime(null);
                        newAstCard.setOldAstCode(astCard.getAstCode());
                        newAstCard.setAcceptanceNo(v.getAllotCode());
                        newAstCardList.add(newAstCard);
                    });
                });
                astCardService.saveBatch(newAstCardList);
                astCardService.updateBatchById(astCardList);
            }
        });
        this.updateBatchById(astAllotBills);
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String allotBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstAllotBill> astAllotBills = this.listByIds(Arrays.asList(ids));
        // ??????????????????
        List<String> billStatusList = astAllotBills.stream().map(AstAllotBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astAllotBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // ??????????????????
        TaskUtils.backProcess(piids, commonProcessRequest);
        // ????????????????????????
        astAllotBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        boolean a = this.updateBatchById(astAllotBills);
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String taskBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstAllotBill> astAllotBills = this.listByIds(Arrays.asList(ids));
        // ??????????????????
        List<String> billStatusList = astAllotBills.stream().map(AstAllotBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astAllotBills.forEach(v -> piids.add(v.getProcessinstid()));
        // ??????????????????
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // ????????????????????????
        astAllotBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(astAllotBills);
        return "success";
    }

    @Override
    public List<AstAllotBill> selectAll(AstAllotBill astAllotBill) {
        String status = StringUtils.isEmpty(astAllotBill.getStatus()) ? "" : astAllotBill.getStatus();
        astAllotBill.setDelFlag("0");
        List<AstAllotBill> astAllotBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            //??????
            case "0":
                astAllotBill.setCreateBy(SecurityUtils.getUsername());
                astAllotBills = this.baseMapper.selectAll(astAllotBill);
                break;
            // ??????
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astAllotBill.setPiids(runTaskByUserNameAndStatus);
                    astAllotBills = this.baseMapper.selectAll(astAllotBill);
                }
                break;
            // ??????
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astAllotBill.setPiids(runTaskByUserNameAndStatus);
                    astAllotBills = this.baseMapper.selectAll(astAllotBill);
                }
                break;
            // ??????
            case "3":
                astAllotBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astAllotBills = this.baseMapper.selectAll(astAllotBill);
                break;
            // ??????
            case "":
                astAllotBill.setCreateBy(SecurityUtils.getUsername());
                astAllotBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astAllotBills = this.baseMapper.selectAll(astAllotBill);
                break;
            default:
                throw new CustomException("?????????????????????!");
        }
        return astAllotBills;
    }

    @Override
    public List<BdPersonnel> selectUserList(String unitId) {
        return astAllotBillMapper.selectUserList(unitId);
    }

    @Override
    public String selectUserListByUserId(String userId) {
        return astAllotBillMapper.selectUserListByUserId(userId);
    }

}

