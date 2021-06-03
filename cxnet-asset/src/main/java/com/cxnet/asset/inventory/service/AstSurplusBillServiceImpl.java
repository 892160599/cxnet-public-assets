package com.cxnet.asset.inventory.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.astcollectbill.domain.AstCollectBill;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.asset.check.domain.AstCheckBill;
import com.cxnet.asset.inventory.domain.vo.AstSurplusBillVo;
import com.cxnet.asset.inventory.mapper.AstSurplusBillMapper;
import com.cxnet.asset.inventory.domain.AstSurplusBill;
import com.cxnet.asset.inventory.service.AstSurplusBillService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.SpringContextUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.common.service.CheckStatus;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.rpc.domain.basedata.BdPersonnel;
import com.cxnet.rpc.service.basedata.BdPersonnelServiceIRpc;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产盘盈主表(AstSurplusBill)表服务实现类
 *
 * @author zhangyl
 * @since 2021-04-19 14:14:44
 */
@Service
public class AstSurplusBillServiceImpl extends ServiceImpl<AstSurplusBillMapper, AstSurplusBill> implements AstSurplusBillService {

    @Autowired(required = false)
    private RuleServiceI ruleServiceI;
    @Autowired(required = false)
    private AstAnnexService astAnnexService;
    @Autowired(required = false)
    private AstBillListService astBillListService;
    @Autowired(required = false)
    private AstCardService astCardService;
    @Autowired(required = false)
    private BdPersonnelServiceIRpc bdPersonnelServiceIRpc;

    @Override
    @Transactional
    public AstSurplusBillVo insertSurplus(AstSurplusBillVo astSurplusBillVo) {
        AstSurplusBill astSurplusBill = astSurplusBillVo.getAstSurplusBill();
        //根据经办人id查询详情信息
        QueryWrapper<BdPersonnel> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BdPersonnel::getUserId, astSurplusBill.getHandlingId())
                .eq(BdPersonnel::getDelFlag, "0").eq(BdPersonnel::getStatus, ProcessConstant.STATUS_0)
                .eq(BdPersonnel::getDeptCode, astSurplusBill.getHandlingDeptCode());
        BdPersonnel one = bdPersonnelServiceIRpc.getOne(wrapper);
        astSurplusBill.setHandlingCode(one.getUserCode());
        astSurplusBill.setHandlingName(one.getUserName());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        astSurplusBill.setId(uuid);
        astSurplusBill.setCreateTime(new Date());
        astSurplusBill.setDelFlag("0");
        astSurplusBill.setStatus(ProcessConstant.STATUS_0);
        astSurplusBill.setIsProduce("2");
        // 设置单据号
        astSurplusBill.setBillNo(ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_INVENTORY_BILL, astSurplusBill));
        this.save(astSurplusBill);
        //新增明细表
        List<AstBillList> astBillListList = astSurplusBillVo.getAstBillList();
        if (CollectionUtils.isNotEmpty(astBillListList)) {
            astBillListList.forEach(v -> {
                v.setAstId(uuid);
                astBillListService.save(v);
            });
        }
        //新增附件表
        List<AstAnnex> astAnnexList = astSurplusBillVo.getAstAnnexList();
        if (CollectionUtils.isNotEmpty(astAnnexList)) {
            astAnnexList.forEach(a -> {
                a.setAstId(uuid);
                astAnnexService.save(a);
            });
        }
        return getList(uuid);
    }

    @Override
    public AstSurplusBillVo getList(String id) {
        AstSurplusBill byId = this.getById(id);
        AstSurplusBillVo billVo = new AstSurplusBillVo();
        //查询明细表
        QueryWrapper<AstBillList> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstBillList::getAstId, byId.getId());
        List<AstBillList> astBillList = astBillListService.list(wrapper);
        //查询附件表
        QueryWrapper<AstAnnex> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AstAnnex::getAstId, byId.getId());
        List<AstAnnex> astAnnexList = astAnnexService.list(queryWrapper);
        billVo.setAstSurplusBill(byId);
        billVo.setAstBillList(astBillList);
        billVo.setAstAnnexList(astAnnexList);
        return billVo;
    }

    @Override
    @Transactional
    public AstSurplusBillVo updateSurplus(AstSurplusBillVo astSurplusBillVo) {
        //修改主表
        AstSurplusBill astSurplusBill = astSurplusBillVo.getAstSurplusBill();
        this.updateById(astSurplusBill);
        //修改明细表
        List<AstBillList> astBillList = astSurplusBillVo.getAstBillList();
        QueryWrapper<AstBillList> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstBillList::getAstId, astSurplusBill.getId());
        astBillListService.remove(wrapper);
        astBillList.forEach(v -> {
            v.setAstId(astSurplusBill.getId());
            astBillListService.save(v);
        });
        //修改附件表
        List<AstAnnex> astAnnexList = astSurplusBillVo.getAstAnnexList();
        QueryWrapper<AstAnnex> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AstAnnex::getAstId, astSurplusBill.getId());
        astAnnexService.remove(queryWrapper);
        astAnnexList.forEach(a -> {
            a.setAstId(astSurplusBill.getId());
            astAnnexService.save(a);
        });
        AstSurplusBillVo list = this.getList(astSurplusBill.getId());
        return list;
    }

    @Override
    @Transactional
    public int deleteSurplus(List<String> ids) {
        for (String id : ids) {
            //删除主表
            AstSurplusBill byId = this.getById(id);
            byId.setDelFlag("2");
            this.updateById(byId);
        }
        return 0;
    }

    @Override
    @Transactional
    public int insertCard(String id) {
        AstSurplusBill byId = this.getById(id);
        if (!"2".equals(byId.getStatus())) {
            throw new CustomException("流程未结束，不可生成资产卡片");
        }
        if ("0".equals(byId.getIsProduce())) {
            throw new CustomException("卡片以生成，不可重复生成");
        }
        QueryWrapper<AstBillList> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AstBillList::getAstId, byId.getId());
        List<AstBillList> astBillLists = astBillListService.list(wrapper);
        List<AstCard> astCardList = new ArrayList<>();
        astBillLists.forEach(v -> {
            AstCard astCard = new AstCard();
            BeanUtils.copyProperties(v, astCard);
            astCardList.add(astCard);
        });
        astCardService.saveBatch(astCardList);
        //修改盘盈主表是否生成卡片状态
        byId.setIsProduce("0");
        this.updateById(byId);
        return 0;
    }


    /**
     * 分页查询
     *
     * @param astSurplusBill
     * @return
     */
    @Override
    public List<AstSurplusBill> selectAll(AstSurplusBill astSurplusBill) {
        String status = StringUtils.isEmpty(astSurplusBill.getStatus()) ? "" : astSurplusBill.getStatus();
        astSurplusBill.setDelFlag("0");
        List<AstSurplusBill> astCheckBillList = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            //草稿
            case "0":
                astSurplusBill.setCreateBy(SecurityUtils.getUsername());
                astCheckBillList = this.baseMapper.selectAstSurplusList(astSurplusBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astSurplusBill.setPiids(runTaskByUserNameAndStatus);
                    astCheckBillList = this.baseMapper.selectAstSurplusList(astSurplusBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astSurplusBill.setPiids(runTaskByUserNameAndStatus);
                    astCheckBillList = this.baseMapper.selectAstSurplusList(astSurplusBill);
                }
                break;
            // 终审
            case "3":
                astSurplusBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astCheckBillList = this.baseMapper.selectAstSurplusList(astSurplusBill);
                break;
            // 全部
            case "":
                astSurplusBill.setCreateBy(SecurityUtils.getUsername());
                astSurplusBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astCheckBillList = this.baseMapper.selectAstSurplusList(astSurplusBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astCheckBillList;
    }

    /**
     * 送审
     *
     * @param commonProcessRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstSurplusBill> astSurplusBills = this.listByIds(Arrays.asList(id));
        if (CollectionUtil.isEmpty(astSurplusBills)) {
            throw new CustomException("未查询到单据");
        }
        List<String> billStatusList = astSurplusBills.stream().map(AstSurplusBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astSurplusBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_ChECK,
                    processInstanceId);
            v.setProcessinstid(processInstanceId);
            v.setStatus("1");
            // 设置流程变量
            Map<String, Object> startVariables = TaskUtils.getStartVariables(commonProcessRequest, v);
            // 执行任务
            List<Task> complete = TaskUtils.complete(processInstanceId, startVariables);
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
        });
        this.updateBatchById(astSurplusBills);
        return "送审成功！";
    }

    /**
     * 审核
     *
     * @param commonProcessRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String audit(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstSurplusBill> astSurplusBills = this.listByIds(Arrays.asList(id));
        // 校验状态
        List<String> billStatusList = astSurplusBills.stream().map(AstSurplusBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        Map<String, Object> columnMap = new HashMap<>(1);
        astSurplusBills.forEach(v -> {
            v.setStatus("1");
            // 执行任务
            List<Task> complete = TaskUtils.complete(v.getProcessinstid(),
                    TaskUtils.getGoVariables(commonProcessRequest, v));
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
            // 判断流程是否结束
            if (TaskUtils.isEnd(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_2);
                v.setApprovalPost("已终审");

                columnMap.put("AST_ID", v.getId());
                // 获取资产明细表
                List<AstBillList> astBillList = astBillListService.listByMap(columnMap);
                astBillList.forEach(a -> {
                    a.setCreateTime(new Date());
                    a.setAstCode(ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_REGISTER, v));
                });
                astBillListService.updateBatchById(astBillList);
            }
        });
        this.updateBatchById(astSurplusBills);
        return "审核成功！";
    }

    /**
     * 退回
     *
     * @param commonProcessRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String back(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstSurplusBill> astSurplusBills = this.listByIds(Arrays.asList(id));
        List<String> billStatusList = astSurplusBills.stream().map(AstSurplusBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astSurplusBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点
        astSurplusBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(astSurplusBills);
        return "退回成功！";
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
        List<AstSurplusBill> astSurplusBills = this.listByIds(Arrays.asList(id));
        List<String> billStatusList = astSurplusBills.stream().map(AstSurplusBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astSurplusBills.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        astSurplusBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(astSurplusBills);
        return "收回成功！";
    }

    /**
     * 验空
     *
     * @param id
     */
    private void checkId(String[] id) {
        if (ArrayUtil.isEmpty(id)) {
            throw new CustomException("单据编号不能为空");
        }
    }

}

