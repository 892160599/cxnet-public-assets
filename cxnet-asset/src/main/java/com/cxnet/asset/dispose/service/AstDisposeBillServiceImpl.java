package com.cxnet.asset.dispose.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.allocation.domain.AstAllocationBill;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.mapper.AstAnnexMapper;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.businessSet.service.AstDeptUserService;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.mapper.AstBillListMapper;
import com.cxnet.asset.card.mapper.AstCardMapper;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.dispose.domain.AstDisposeBill;
import com.cxnet.asset.dispose.domain.vo.AstDisposeVo;
import com.cxnet.asset.dispose.domain.vo.SelectAstDisposeBillVo;
import com.cxnet.asset.dispose.mapper.AstDisposeBillMapper;
import com.cxnet.asset.empchange.domain.AstEmpchangeList;
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
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产处置主表(AstDisposeBill)表服务实现类
 *
 * @author zhaoyi
 * @since 2021-03-25 10:13:22
 */
@Service
public class AstDisposeBillServiceImpl extends ServiceImpl<AstDisposeBillMapper, AstDisposeBill> implements AstDisposeBillService {

    @Autowired(required = false)
    private AstDisposeBillMapper astDisposeBillMapper;
    @Autowired(required = false)
    private RuleServiceI ruleServiceI;
    @Autowired(required = false)
    private AstBillListService astBillListService;
    @Autowired(required = false)
    private AstAnnexService astAnnexService;
    @Autowired(required = false)
    private AstCardMapper astCardMapper;
    @Autowired(required = false)
    private AstDisposeBillService astDisposeBillService;
    @Autowired(required = false)
    private AstCardService astCardService;
    @Autowired(required = false)
    private AstDeptUserService astDeptUserService;

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
    public String saveAstDisposeVo(AstDisposeVo astDisposeVo) {
        AstDisposeBill astDisposeBill = astDisposeVo.getAstDisposeBill();
        List<AstBillList> astBillLists = astDisposeVo.getAstBillLists();
        List<AstAnnex> astAnnexes = astDisposeVo.getAstAnnexes();
        astDisposeBill.setCreateTime(new Date());
        this.ifExist(astBillLists);
        //编号器生成单据号
        String disposeCode = ruleServiceI.nextNumber(BillUtils.getRuleCode(BillTypeCodeConstant.AST_DISPOSE), astDisposeBill);
        astDisposeBill.setDisposeCode(disposeCode);
        astDisposeBill.setStatus("0");
        astDisposeBill.setDelFlag("0");
        int i = astDisposeBillMapper.insert(astDisposeBill);
        //主表保存成功
        if (i > 0) {
            //保存明细
            List<AstBillList> astBillLists1 = new ArrayList<>();
            List<AstCard> astCardList = new ArrayList<>();
            astBillLists.forEach(a -> {
                a.setBillId(astDisposeBill.getId());
                a.setId("");
                a.setBillType("ast_dispose");
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
                b.setAstId(astDisposeBill.getId());
                b.setAnnexType("6");
                astAnnexes1.add(b);
            });
            astAnnexService.saveBatch(astAnnexes1);
        }
        return astDisposeBill.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAstDisposeVo(AstDisposeVo astDisposeVo) {
        AstDisposeBill astDisposeBill = astDisposeVo.getAstDisposeBill();
        List<AstBillList> astBillLists = astDisposeVo.getAstBillLists();
        List<AstAnnex> astAnnexes = astDisposeVo.getAstAnnexes();
        String id = astDisposeBill.getId();
        AstDisposeBill selectById = this.getById(id);
        if (ObjectUtil.isNull(selectById) || "2".equals(selectById.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        if (!"0".equals(selectById.getStatus())) {
            throw new CustomException("只有草稿状态可以修改！");
        }
        //修改主表
        this.updateById(astDisposeBill);
        //修改明细表
        QueryWrapper<AstBillList> astDisposeListQueryWrapper = new QueryWrapper<>();
        //查询当前单据下的明细信息
        astDisposeListQueryWrapper.lambda().eq(AstBillList::getBillId, id);
        List<AstBillList> astBillLists1 = astBillListService.list(astDisposeListQueryWrapper);
        astBillListService.remove(astDisposeListQueryWrapper);
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
            a.setBillType("ast_dispose");
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
            b.setAnnexType("6");
        });
        astAnnexService.saveBatch(astAnnexes);
        return "success";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(List<String> ids) {
        List<AstDisposeBill> astDisposeBills = this.listByIds(ids);
        List<AstCard> astCardList = new ArrayList<>();
        astDisposeBills.forEach(a -> {
            a.setDelFlag("2");
            QueryWrapper<AstBillList> astDisposeListQueryWrapper = new QueryWrapper<>();
            astDisposeListQueryWrapper.lambda().eq(AstBillList::getBillId, a.getId());
            List<AstBillList> astBillLists = astBillListService.list(astDisposeListQueryWrapper);
            astBillLists.forEach(b -> {
                AstCard astCard = astCardMapper.selectById(b.getAstId());
                //修改明细状态
                astCard.setInitialState("1");
                astCardList.add(astCard);
            });
        });
        astCardService.updateBatchById(astCardList);
        this.updateBatchById(astDisposeBills);
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
     * @param commonProcessRequest 处置申报单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int disSubmit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] ids = commonProcessRequest.getId();
        List<AstDisposeBill> astDisposeBills = this.listByIds(Arrays.asList(ids));
        if (CollectionUtil.isEmpty(astDisposeBills)) {
            throw new CustomException("未查询到处置单");
        }
        // 校验单据状态
        List<String> billStatusList = astDisposeBills.stream().map(AstDisposeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astDisposeBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_DISPOSE, processInstanceId);
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
        boolean a = astDisposeBillService.updateBatchById(astDisposeBills);
        int i = 0;
        if (a) {
            i = 1;
        }
        return i;
    }

    /**
     * 审核
     *
     * @param commonProcessRequest 资产处置单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int disAudit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstDisposeBill> astDisposeBills = this.listByIds(Arrays.asList(id));
        if (CollectionUtils.isEmpty(astDisposeBills)) {
            throw new CustomException("未查询到处置单");
        }
        // 校验单据状态
        List<String> billStatusList = astDisposeBills.stream().map(AstDisposeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        astDisposeBills.forEach(v -> {
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
                astDisposeBills.forEach(a -> {
                    a.setVerificationDate(new Date());
                    QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper();
                    //查询明细信息
                    astBillListQueryWrapper.lambda().eq(AstBillList::getBillId, a.getId());
                    List<AstBillList> astDisposeLists = astBillListService.list(astBillListQueryWrapper);
                    //回写资产卡片
                    astDisposeLists.forEach(b -> {
                        AstCard astCard = astCardMapper.selectById(b.getAstId());
                        astCard.setInitialState("1");
                        astCard.setAstStatus("2");
                        astCardList.add(astCard);
                    });

                });
                astCardService.updateBatchById(astCardList);
            }
        });
        boolean a = astDisposeBillService.updateBatchById(astDisposeBills);
        int i = 0;
        if (a) {
            i = 1;
        }
        return i;
    }

    /**
     * 退回
     *
     * @param commonProcessRequest 资产退回单
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int disBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstDisposeBill> astDisposeBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astDisposeBills.stream().map(AstDisposeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astDisposeBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点
        astDisposeBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        boolean a = astDisposeBillService.updateBatchById(astDisposeBills);
        int i = 0;
        if (a) {
            i = 1;
        }
        return i;
    }

    /**
     * 收回
     *
     * @param commonProcessRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int taskBack(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstDisposeBill> astDisposeBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astDisposeBills.stream().map(AstDisposeBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astDisposeBills.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        astDisposeBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        boolean a = astDisposeBillService.updateBatchById(astDisposeBills);
        int i = 0;
        if (a) {
            i = 1;
        }
        return i;
    }

    /**
     * 分页查询
     *
     * @param astDisposeBill
     * @return
     */
    @Override
    public List<AstDisposeBill> selectAll(AstDisposeBill astDisposeBill) {
        String status = StringUtils.isEmpty(astDisposeBill.getStatus()) ? "" : astDisposeBill.getStatus();
        //判断当前登录用户是部门管理员还是单位管理员
        Map<String, List<String>> idsMap = astDeptUserService.getMap();
        if (idsMap.containsKey("dept")) {
            astDisposeBill.setDeptIds(idsMap.get("dept"));
        }
        astDisposeBill.setDelFlag("0");
        List<AstDisposeBill> astDisposeBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            //草稿
            case "0":
                astDisposeBill.setCreateBy(SecurityUtils.getUsername());
                astDisposeBills = this.baseMapper.selectAstDisposeBillList(astDisposeBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astDisposeBill.setPiids(runTaskByUserNameAndStatus);
                    astDisposeBills = this.baseMapper.selectAstDisposeBillList(astDisposeBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astDisposeBill.setPiids(runTaskByUserNameAndStatus);
                    astDisposeBills = this.baseMapper.selectAstDisposeBillList(astDisposeBill);
                }
                break;
            // 终审
            case "3":
                astDisposeBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astDisposeBills = this.baseMapper.selectAstDisposeBillList(astDisposeBill);
                break;
            // 全部
            case "":
                astDisposeBill.setCreateBy(SecurityUtils.getUsername());
                astDisposeBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astDisposeBills = this.baseMapper.selectAstDisposeBillList(astDisposeBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astDisposeBills;
    }

    @Override
    public List<SelectAstDisposeBillVo> selectAstDisposeBillList(String astId) {
        List<SelectAstDisposeBillVo> selectAstDisposeBillVos = new ArrayList<>();
        if (StringUtils.isNotEmpty(astId)) {
            QueryWrapper<AstBillList> astBillListQueryWrapper = new QueryWrapper<>();
            astBillListQueryWrapper.lambda().eq(AstBillList::getAstId, astId)
                    .eq(AstBillList::getBillType, "ast_dispose");
            List<AstBillList> astBillLists = astBillListService.list(astBillListQueryWrapper);
            if (ObjectUtils.isNotEmpty(astBillLists)) {
                List<String> disposeIds = astBillLists.stream().map(a -> a.getBillId()).collect(Collectors.toList());
                List<AstDisposeBill> astDisposeBills = astDisposeBillService.listByIds(disposeIds);
                astDisposeBills.forEach(a -> {
                    SelectAstDisposeBillVo selectAstDisposeBillVo = new SelectAstDisposeBillVo();
                    selectAstDisposeBillVo.setDisposeCode(a.getDisposeCode());
                    selectAstDisposeBillVo.setDisposetypeCode(a.getDisposetypeCode());
                    selectAstDisposeBillVo.setVerificationDate(a.getVerificationDate());
                    selectAstDisposeBillVo.setCreateBy(a.getCreateBy());
                    selectAstDisposeBillVos.add(selectAstDisposeBillVo);
                });
            }
        }
        return selectAstDisposeBillVos;
    }
}

