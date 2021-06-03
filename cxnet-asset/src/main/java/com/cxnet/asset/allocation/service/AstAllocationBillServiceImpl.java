package com.cxnet.asset.allocation.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.allocation.domain.vo.AstAllocationVo;
import com.cxnet.asset.allocation.mapper.AstAllocationBillMapper;
import com.cxnet.asset.allocation.domain.AstAllocationBill;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.mapper.AstAnnexMapper;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.astchange.domain.AstAstchangeBill;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.mapper.AstBillListMapper;
import com.cxnet.asset.card.mapper.AstCardMapper;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.SpringContextUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.flow.utils.BillUtils;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.common.service.CheckStatus;
import com.cxnet.project.system.dept.domain.SysDept;
import com.cxnet.project.system.dept.domain.SysDeptVO;
import com.cxnet.project.system.dept.service.SysDeptServiceI;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.service.SysUserServiceI;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import com.cxnet.rpc.service.basedata.BdPersonnelServiceIRpc;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产部门调剂主表(AstAllocationBill)表服务实现类
 *
 * @author zhaoyi
 * @since 2021-04-02 09:55:49
 */
@Service
public class AstAllocationBillServiceImpl extends ServiceImpl<AstAllocationBillMapper, AstAllocationBill> implements AstAllocationBillService {

    @Autowired(required = false)
    private AstAllocationBillMapper astAllocationBillMapper;
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;
    @Autowired(required = false)
    private AstCardMapper astCardMapper;
    @Autowired(required = false)
    private AstBillListService astBillListService;
    @Autowired(required = false)
    private AstAnnexService astAnnexService;
    @Autowired(required = false)
    private AstCardService astCardService;
    @Autowired(required = false)
    private SysUserServiceI sysUserServiceI;
    @Autowired(required = false)
    private AstDeptUserService astDeptUserService;
    @Autowired(required = false)
    private BdPersonnelServiceIRpc bdPersonnelServiceIRpc;

    /**
     * 判断该明细是否已保存
     *
     * @param astBillLists
     * @author zhaoyi
     */
    public void ifExist(List<AstBillList> astBillLists) {
        astBillLists.forEach(a -> {
            AstCard astCard = astCardMapper.selectById(a.getAstId());
            if (("0").equals(astCard.getInitialState())) {
                throw new CustomException("卡片" + astCard.getAstCode() + "已在操作中");
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAstAllocationVo(AstAllocationVo astAllocationVo) {
        AstAllocationBill astAllocationBill = astAllocationVo.getAstAllocationBill();
        List<AstBillList> astBillLists = astAllocationVo.getAstBillLists();
        List<AstAnnex> astAnnexes = astAllocationVo.getAstAnnexes();
        astAllocationBill.setCreateTime(new Date());
        this.ifExist(astBillLists);
        //编号器生成单据号
        String allocationCode = ruleServiceI.nextNumber(BillUtils.getRuleCode(BillTypeCodeConstant.AST_ALLOCATION), astAllocationBill);
        astAllocationBill.setAllocationCode(allocationCode);
        astAllocationBill.setStatus("0");
        astAllocationBill.setDelFlag("0");
        BdPersonnel bdPersonnel = bdPersonnelServiceIRpc.selectBdPersonnelById(astAllocationBill.getEmpIdIn());
        astAllocationBill.setEmpCodeIn(bdPersonnel.getUserCode());
        astAllocationBill.setEmpNameIn(bdPersonnel.getUserName());
        int i = astAllocationBillMapper.insert(astAllocationBill);

        //主表保存成功
        if (i > 0) {
            //保存明细
            List<AstBillList> astBillLists1 = new ArrayList<>();
            List<AstCard> astCardList = new ArrayList<>();
            astBillLists.forEach(a -> {
                a.setBillId(astAllocationBill.getId());
                a.setId("");
                a.setBillType("ast_allocation");
                astBillLists1.add(a);
                AstCard astCard = astCardMapper.selectById(a.getAstId());
                //回写卡片状态
                astCard.setInitialState("0");
                astCardList.add(astCard);
            });
            astBillListService.saveBatch(astBillLists1);
            astCardService.updateBatchById(astCardList);
            //保存附件
            List<AstAnnex> astAnnexes1 = new ArrayList<>();
            astAnnexes.forEach(b -> {
                b.setAstId(astAllocationBill.getId());
                b.setAnnexType("41");
                astAnnexes1.add(b);
            });
            astAnnexService.saveBatch(astAnnexes1);
        }
        return astAllocationBill.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAstAllocationVo(AstAllocationVo astAllocationVo) {
        AstAllocationBill astAllocationBill = astAllocationVo.getAstAllocationBill();
        List<AstBillList> astBillLists = astAllocationVo.getAstBillLists();
        List<AstAnnex> astAnnexes = astAllocationVo.getAstAnnexes();
        String id = astAllocationBill.getId();
        AstAllocationBill selectById = this.getById(id);
        if (ObjectUtil.isNull(selectById) || "2".equals(selectById.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        if (!"0".equals(selectById.getStatus())) {
            throw new CustomException("只有草稿状态可以修改！");
        }
        BdPersonnel bdPersonnel = bdPersonnelServiceIRpc.selectBdPersonnelById(astAllocationBill.getEmpIdIn());
        astAllocationBill.setEmpCodeIn(bdPersonnel.getUserCode());
        astAllocationBill.setEmpNameIn(bdPersonnel.getUserName());
        //修改主表
        this.updateById(astAllocationBill);
        //修改明细表
        QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper<>();
        //查询当前单据下的明细信息
        astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, id);
        List<AstBillList> astBillLists1 = astBillListService.list(astBillListQueryWrapper);
        astBillListService.remove(astBillListQueryWrapper);
        //重新添加明细
        List<AstCard> astCardList = new ArrayList<>();
        astBillLists1.forEach(a -> {
            AstCard astCard = astCardMapper.selectById(a.getAstId());
            //回写资产卡片,解除锁定
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
            //回写资产卡片,锁定资产
            astCard.setInitialState("0");
            astCardList1.add(astCard);
        });
        astCardService.updateBatchById(astCardList1);
        astBillListService.saveBatch(astBillLists);
        //修改附件表
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
        List<AstAllocationBill> astAllocationBills = astAllocationBillMapper.selectBatchIds(ids);
        List<AstCard> astCardList = new ArrayList<>();
        astAllocationBills.forEach(a -> {
            a.setDelFlag("2");
            QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper<>();
            astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, a.getId());
            List<AstBillList> astBillLists = astBillListService.list(astBillListQueryWrapper);
            astBillLists.forEach(b -> {
                AstCard astCard = astCardMapper.selectById(b.getAstId());
                //回写资产卡片，解除锁定
                astCard.setInitialState("1");
                astCardList.add(astCard);
            });
        });
        astCardService.updateBatchById(astCardList);
        this.updateBatchById(astAllocationBills);
        return "success";
    }

    /**
     * 单据编号
     *
     * @param id
     */
    public void checkId(String[] id) {
        if (ArrayUtil.isEmpty(id)) {
            throw new CustomException("单据编号不能为空");
        }
    }

    /**
     * 送审
     *
     * @param commonProcessRequest 资产调剂单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String allocationSubmit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstAllocationBill> astAllocationBills = this.listByIds(Arrays.asList(ids));
        if (CollectionUtil.isEmpty(astAllocationBills)) {
            throw new CustomException("未查询到调剂单");
        }
        // 校验单据状态
        List<String> billStatusList = astAllocationBills.stream().map(AstAllocationBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astAllocationBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_ALLOCATION, processInstanceId);
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
        this.updateBatchById(astAllocationBills);
        return "success";
    }


    /**
     * 审核
     *
     * @param commonProcessRequest 资产调剂单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String allocationAudit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstAllocationBill> astAllocationBills = this.listByIds(Arrays.asList(ids));
        if (CollectionUtils.isEmpty(astAllocationBills)) {
            throw new CustomException("未查询到调剂单");
        }
        // 校验单据状态
        List<String> billStatusList = astAllocationBills.stream().map(AstAllocationBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        astAllocationBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            // 执行任务
            List<Task> complete = TaskUtils.complete(v.getProcessinstid(), TaskUtils.getGoVariables(commonProcessRequest, v));
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
            // 判断流程是否结束
            if (TaskUtils.isEnd(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_2);
                v.setApprovalPost(ProcessConstant.END);
                v.setVerificationDate(new Date());
                List<AstCard> astCardList = new ArrayList<>();
                astAllocationBills.forEach(a -> {
                    a.setVerificationDate(new Date());
                    QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper();
                    //查询明细信息
                    astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, a.getId());
                    List<AstBillList> astDisposeLists = astBillListService.list(astBillListQueryWrapper);
                    //回写资产卡片
                    astDisposeLists.forEach(b -> {
                        AstCard astCard = astCardMapper.selectById(b.getAstId());
                        astCard.setDepartmentId(v.getDeptIdIn());
                        astCard.setDepartmentCode(v.getDeptCodeIn());
                        astCard.setDepartmentName(v.getDeptNameIn());
                        astCard.setEmpId(v.getEmpIdIn());
                        astCard.setEmpCode(v.getEmpCodeIn());
                        astCard.setEmpName(v.getEmpNameIn());
                        astCard.setInitialState("1");
                        astCardList.add(astCard);
                    });
                });
                astCardService.updateBatchById(astCardList);
            }
        });
        this.updateBatchById(astAllocationBills);
        return "success";
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 资产退回单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String allocationBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstAllocationBill> astAllocationBills = this.listByIds(Arrays.asList(ids));
        // 校验单据状态
        List<String> billStatusList = astAllocationBills.stream().map(AstAllocationBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astAllocationBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点
        astAllocationBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(astAllocationBills);
        return "success";
    }

    /**
     * 收回
     *
     * @param commonProcessRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String taskBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstAllocationBill> astAllocationBills = this.listByIds(Arrays.asList(ids));
        // 校验单据状态
        List<String> billStatusList = astAllocationBills.stream().map(AstAllocationBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astAllocationBills.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        astAllocationBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(astAllocationBills);
        return "success";
    }

    @Override
    public List<AstAllocationBill> selectAll(AstAllocationBill astAllocationBill) {
        String status = StringUtils.isEmpty(astAllocationBill.getStatus()) ? "" : astAllocationBill.getStatus();
        astAllocationBill.setDelFlag("0");
        //判断当前登录用户是部门管理员还是单位管理员
        Map<String, List<String>> idsMap = astDeptUserService.getMap();
        if (idsMap.containsKey("dept")) {
            astAllocationBill.setDeptIds(idsMap.get("dept"));
        }
        List<AstAllocationBill> astAllocationBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            //草稿
            case "0":
                astAllocationBill.setCreateBy(SecurityUtils.getUsername());
                astAllocationBills = this.baseMapper.selectAll(astAllocationBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astAllocationBill.setPiids(runTaskByUserNameAndStatus);
                    astAllocationBills = this.baseMapper.selectAll(astAllocationBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astAllocationBill.setPiids(runTaskByUserNameAndStatus);
                    astAllocationBills = this.baseMapper.selectAll(astAllocationBill);
                }
                break;
            // 终审
            case "3":
                astAllocationBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astAllocationBills = this.baseMapper.selectAll(astAllocationBill);
                break;
            // 全部
            case "":
                astAllocationBill.setCreateBy(SecurityUtils.getUsername());
                astAllocationBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astAllocationBills = this.baseMapper.selectAll(astAllocationBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astAllocationBills;
    }
}

