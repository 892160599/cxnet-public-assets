package com.cxnet.asset.empchange.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.astchange.domain.AstAstchangeList;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.mapper.AstCardMapper;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.empchange.domain.AstEmpchangeList;
import com.cxnet.asset.empchange.domain.vo.AstEmpchangeVo;
import com.cxnet.asset.empchange.mapper.AstEmpchangeBillMapper;
import com.cxnet.asset.empchange.domain.AstEmpchangeBill;
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
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.project.system.user.domain.SysUser;
import com.cxnet.project.system.user.service.SysUserServiceI;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import com.cxnet.rpc.service.basedata.BdPersonnelServiceIRpc;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产使用人变动主表(AstEmpchangeBill)表服务实现类
 *
 * @author zhaoyi
 * @since 2021-04-16 14:28:10
 */
@Service
public class AstEmpchangeBillServiceImpl extends ServiceImpl<AstEmpchangeBillMapper, AstEmpchangeBill> implements AstEmpchangeBillService {

    @Autowired(required = false)
    private AstEmpchangeBillMapper astEmpchangeBillMapper;
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;
    @Autowired(required = false)
    private AstEmpchangeListService astEmpchangeListService;
    @Autowired(required = false)
    private AstCardMapper astCardMapper;
    @Autowired(required = false)
    private AstCardService astCardService;
    @Autowired(required = false)
    private AstAnnexService astAnnexService;
    @Autowired(required = false)
    private AstDeptUserService astDeptUserService;
    @Autowired(required = false)
    private SysUserServiceI sysUserServiceI;
    @Autowired(required = false)
    private BdPersonnelServiceIRpc bdPersonnelServiceIRpc;

    /**
     * 判断该明细是否已保存
     *
     * @param astEmpchangeLists
     * @author zhaoyi
     */
    public void ifExist(List<AstEmpchangeList> astEmpchangeLists) {
        astEmpchangeLists.forEach(a -> {
            AstCard astCard = astCardMapper.selectById(a.getAstId());
            if (("0").equals(astCard.getInitialState())) {
                throw new CustomException("卡片" + astCard.getAstCode() + "已在操作中");
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAstEmpchangeVo(AstEmpchangeVo astEmpchangeVo) {
        AstEmpchangeBill astEmpchangeBill = astEmpchangeVo.getAstEmpchangeBill();
        List<AstEmpchangeList> astEmpchangeLists = astEmpchangeVo.getAstEmpchangeLists();
        List<AstAnnex> astAnnexes = astEmpchangeVo.getAstAnnexes();
        this.ifExist(astEmpchangeLists);
        //编号器生成单据号
        astEmpchangeBill.setCreateTime(new Date());
        String empchangeCode = ruleServiceI.nextNumber(BillUtils.getRuleCode(BillTypeCodeConstant.AST_EMPCHANGE_CODE), astEmpchangeBill);
        astEmpchangeBill.setEmpchangeCode(empchangeCode);
        BdPersonnel bdPersonnel = bdPersonnelServiceIRpc.selectBdPersonnelById(astEmpchangeBill.getOperatorId());
        astEmpchangeBill.setOperatorCode(bdPersonnel.getUserCode());
        astEmpchangeBill.setOperatorName(bdPersonnel.getUserName());
        astEmpchangeBill.setStatus("0");
        astEmpchangeBill.setDelFlag("0");
        int i = astEmpchangeBillMapper.insert(astEmpchangeBill);
        //主表保存成功
        if (i > 0) {
            //保存明细
            List<AstEmpchangeList> astEmpchangeLists1 = new ArrayList<>();
            List<AstCard> astCardList = new ArrayList<>();
            astEmpchangeLists.forEach(a -> {
                a.setBillId(astEmpchangeBill.getId());
                a.setId("");
                BdPersonnel bdPersonnel1 = bdPersonnelServiceIRpc.selectBdPersonnelById(a.getEmpAfterId());
                a.setEmpAfterCode(bdPersonnel1.getUserCode());
                a.setEmpAfterName(bdPersonnel1.getUserName());
                astEmpchangeLists1.add(a);
                AstCard astCard = astCardMapper.selectById(a.getAstId());
                //回写卡片状态
                astCard.setInitialState("0");
                astCardList.add(astCard);
            });
            astEmpchangeListService.saveBatch(astEmpchangeLists1);
            astCardService.updateBatchById(astCardList);
            //保存附件信息
            List<AstAnnex> astAnnexes1 = new ArrayList<>();
            astAnnexes.forEach(b -> {
                b.setAstId(astEmpchangeBill.getId());
                b.setAnnexType("11");
                astAnnexes1.add(b);
            });
            astAnnexService.saveBatch(astAnnexes1);
        }
        return astEmpchangeBill.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAstEmpchangeVo(AstEmpchangeVo astEmpchangeVo) {
        AstEmpchangeBill astEmpchangeBill = astEmpchangeVo.getAstEmpchangeBill();
        List<AstEmpchangeList> astEmpchangeLists = astEmpchangeVo.getAstEmpchangeLists();
        List<AstAnnex> astAnnexes = astEmpchangeVo.getAstAnnexes();
        String id = astEmpchangeBill.getId();
        AstEmpchangeBill selectById = this.getById(id);
        if (ObjectUtil.isNull(selectById) || "2".equals(selectById.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        if (!"0".equals(selectById.getStatus())) {
            throw new CustomException("只有草稿状态可以修改！");
        }
        BdPersonnel bdPersonnel = bdPersonnelServiceIRpc.selectBdPersonnelById(astEmpchangeBill.getOperatorId());
        astEmpchangeBill.setOperatorCode(bdPersonnel.getUserCode());
        astEmpchangeBill.setOperatorName(bdPersonnel.getUserName());
        //修改主表
        this.updateById(astEmpchangeBill);
        //修改明细表
        QueryWrapper<AstEmpchangeList> astEmpchangeListQueryWrapper = new QueryWrapper<>();
        //查询当前单据下的明细信息
        astEmpchangeListQueryWrapper.lambda().eq(AstEmpchangeList::getBillId, id);
        List<AstEmpchangeList> astEmpchangeLists1 = astEmpchangeListService.list(astEmpchangeListQueryWrapper);
        List<AstCard> astCardList = new ArrayList<>();
        astEmpchangeLists1.forEach(a -> {
            AstCard astCard = astCardMapper.selectById(a.getAstId());
            //回写资产卡片,解除锁定
            astCard.setInitialState("1");
            astCardList.add(astCard);
        });
        astCardService.updateBatchById(astCardList);
        astEmpchangeListService.remove(astEmpchangeListQueryWrapper);
        //重新添加明细
        List<AstCard> astCardList1 = new ArrayList<>();
        astEmpchangeLists.forEach(b -> {
            b.setId("");
            b.setBillId(id);
            BdPersonnel bdPersonnel1 = bdPersonnelServiceIRpc.selectBdPersonnelById(b.getEmpAfterId());
            b.setEmpAfterCode(bdPersonnel1.getUserCode());
            b.setEmpAfterName(bdPersonnel1.getUserName());
            AstCard astCard = astCardMapper.selectById(b.getAstId());
            //回写资产卡片,锁定资产
            astCard.setInitialState("0");
            astCardList1.add(astCard);
        });
        astEmpchangeListService.saveBatch(astEmpchangeLists);
        astCardService.updateBatchById(astCardList1);
        //修改附件表
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        astAnnexService.remove(astAnnexQueryWrapper);
        astAnnexes.forEach(b -> {
            b.setAstId(id);
            b.setAnnexType("6");
        });
        astAnnexService.saveBatch(astAnnexes);
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(List<String> ids) {
        List<AstEmpchangeBill> astEmpchangeBills = this.listByIds(ids);
        List<AstCard> astCardList = new ArrayList<>();
        astEmpchangeBills.forEach(a -> {
            a.setDelFlag("2");
            QueryWrapper<AstEmpchangeList> astEmpchangeListQueryWrapper = new QueryWrapper<>();
            astEmpchangeListQueryWrapper.lambda().eq(AstEmpchangeList::getBillId, a.getId());
            List<AstEmpchangeList> astEmpchangeLists = astEmpchangeListService.list(astEmpchangeListQueryWrapper);
            astEmpchangeLists.forEach(b -> {
                AstCard astCard = astCardMapper.selectById(b.getAstId());
                //回写卡片锁定状态
                astCard.setInitialState("1");
                astCardList.add(astCard);
            });
        });
        astCardService.updateBatchById(astCardList);
        this.updateBatchById(astEmpchangeBills);
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
     * @param commonProcessRequest 资产使用人变更单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String empSubmit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstEmpchangeBill> astEmpchangeBills = this.listByIds(Arrays.asList(ids));
        if (CollectionUtil.isEmpty(astEmpchangeBills)) {
            throw new CustomException("未查询到变更单");
        }
        List<String> billStatusList = astEmpchangeBills.stream().map(AstEmpchangeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astEmpchangeBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_EMPCHANGE_CODE, processInstanceId);
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
        this.updateBatchById(astEmpchangeBills);
        return "success";
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 资产使用人变更单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String empAudit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstEmpchangeBill> astEmpchangeBills = this.listByIds(Arrays.asList(id));
        if (CollectionUtils.isEmpty(astEmpchangeBills)) {
            throw new CustomException("未查询到变更单");
        }
        List<String> billStatusList = astEmpchangeBills.stream().map(AstEmpchangeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        astEmpchangeBills.forEach(v -> {
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
                v.setVerificationDate(new Date());
                List<AstCard> astCardList = new ArrayList<>();
                astEmpchangeBills.forEach(a -> {
                    a.setVerificationDate(new Date());
                    QueryWrapper<AstEmpchangeList> astEmpchangeListQueryWrapper = new QueryWrapper();
                    //查询明细信息
                    astEmpchangeListQueryWrapper.lambda().eq(AstEmpchangeList::getBillId, a.getId());
                    List<AstEmpchangeList> astEmpchangeLists = astEmpchangeListService.list(astEmpchangeListQueryWrapper);
                    //回写资产卡片
                    astEmpchangeLists.forEach(b -> {
                        AstCard astCard = astCardMapper.selectById(b.getAstId());
                        astCard.setInitialState("1");
                        astCard.setAstStatus("2");
                        if (StringUtils.isNotEmpty(b.getDepartmentAfterId())) {
                            astCard.setDepartmentId(b.getDepartmentAfterId());
                            astCard.setDepartmentCode(b.getDepartmentAfterCode());
                            astCard.setDepartmentName(b.getDepartmentAfterName());
                        }
                        if (StringUtils.isNotEmpty(b.getEmpAfterId())) {
                            astCard.setEmpId(b.getEmpAfterId());
                            astCard.setEmpCode(b.getEmpAfterCode());
                            astCard.setEmpName(b.getEmpAfterName());
                        }
                        astCardList.add(astCard);
                    });
                });
                astCardService.updateBatchById(astCardList);
            }
        });
        this.updateBatchById(astEmpchangeBills);
        return "success";
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 资产使用人变更单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String empBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstEmpchangeBill> astEmpchangeBills = this.listByIds(Arrays.asList(id));
        List<String> billStatusList = astEmpchangeBills.stream().map(AstEmpchangeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astEmpchangeBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点
        astEmpchangeBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(astEmpchangeBills);
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
        String[] id = commonProcessRequest.getId();
        List<AstEmpchangeBill> astEmpchangeBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astEmpchangeBills.stream().map(AstEmpchangeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astEmpchangeBills.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        astEmpchangeBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(astEmpchangeBills);
        return "success";
    }

    /**
     * 分页查询
     *
     * @param astEmpchangeBill
     * @return
     */
    @Override
    public List<AstEmpchangeBill> selectAll(AstEmpchangeBill astEmpchangeBill) {
        String status = StringUtils.isEmpty(astEmpchangeBill.getStatus()) ? "" : astEmpchangeBill.getStatus();
        //判断当前登录用户是部门管理员还是单位管理员
        Map<String, List<String>> idsMap = astDeptUserService.getMap();
        if (idsMap.containsKey("dept")) {
            astEmpchangeBill.setDeptIds(idsMap.get("dept"));
        }
        astEmpchangeBill.setDelFlag("0");
        List<AstEmpchangeBill> astEmpchangeBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            //草稿
            case "0":
                astEmpchangeBill.setCreateBy(SecurityUtils.getUsername());
                astEmpchangeBills = this.baseMapper.selectAll(astEmpchangeBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astEmpchangeBill.setPiids(runTaskByUserNameAndStatus);
                    astEmpchangeBills = this.baseMapper.selectAll(astEmpchangeBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astEmpchangeBill.setPiids(runTaskByUserNameAndStatus);
                    astEmpchangeBills = this.baseMapper.selectAll(astEmpchangeBill);
                }
                break;
            // 终审
            case "3":
                astEmpchangeBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astEmpchangeBills = this.baseMapper.selectAll(astEmpchangeBill);
                break;
            // 全部
            case "":
                astEmpchangeBill.setCreateBy(SecurityUtils.getUsername());
                astEmpchangeBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astEmpchangeBills = this.baseMapper.selectAll(astEmpchangeBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astEmpchangeBills;
    }
}

