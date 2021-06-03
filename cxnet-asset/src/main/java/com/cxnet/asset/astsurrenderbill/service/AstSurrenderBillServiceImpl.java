package com.cxnet.asset.astsurrenderbill.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cxnet.common.utils.SpringContextUtils;
import com.cxnet.project.common.service.CheckStatus;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.asset.annex.domain.AstAnnex;
import com.cxnet.asset.annex.service.AstAnnexService;
import com.cxnet.asset.astsurrenderbill.domain.AstSurrenderBill;
import com.cxnet.asset.astsurrenderbill.domain.vo.AstSurrenderBillVO;
import com.cxnet.asset.astsurrenderbill.mapper.AstSurrenderBillMapper;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.enums.AnnexType;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.flow.domain.CommonProcessRequest;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.rule.service.RuleServiceI;
import com.cxnet.rpc.domain.basedata.BdPersonRpc;
import com.cxnet.rpc.service.basedata.BdPersonServiceRpc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * 资产交回单主表(AstSurrenderBill)表服务实现类
 *
 * @author guks
 * @since 2021-04-15 11:57:52
 */
@Service
public class AstSurrenderBillServiceImpl extends ServiceImpl<AstSurrenderBillMapper, AstSurrenderBill> implements AstSurrenderBillService {

    @Autowired(required = false)
    private AstAnnexService astAnnexService;

    @Autowired(required = false)
    private RuleServiceI ruleServiceI;

    @Autowired(required = false)
    private AstCardService astCardService;

    @Autowired(required = false)
    private BdPersonServiceRpc bdPersonServiceRpc;

    @Autowired(required = false)
    private AstBillListService astBillListService;

    /**
     * 分页查询
     *
     * @param astSurrenderBill
     * @return
     */
    @Override
    public List<AstSurrenderBill> selectAll(AstSurrenderBill astSurrenderBill) {
        String status = StringUtils.isEmpty(astSurrenderBill.getStatus()) ? "" : astSurrenderBill.getStatus();
        astSurrenderBill.setDelFlag("0");
        List<AstSurrenderBill> astSurrenderBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            // 草稿
            case "0":
                astSurrenderBill.setCreateBy(SecurityUtils.getUsername());
                astSurrenderBills = this.baseMapper.selectAll(astSurrenderBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astSurrenderBill.setPiids(runTaskByUserNameAndStatus);
                    astSurrenderBills = this.baseMapper.selectAll(astSurrenderBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astSurrenderBill.setPiids(runTaskByUserNameAndStatus);
                    astSurrenderBills = this.baseMapper.selectAll(astSurrenderBill);
                }
                break;
            // 终审
            case "3":
                astSurrenderBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astSurrenderBills = this.baseMapper.selectAll(astSurrenderBill);
                break;
            // 全部
            case "":
                astSurrenderBill.setCreateBy(SecurityUtils.getUsername());
                astSurrenderBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astSurrenderBills = this.baseMapper.selectAll(astSurrenderBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astSurrenderBills;
    }

    /**
     * 查询单条记录
     *
     * @param id
     * @return
     */
    @Override
    public AstSurrenderBillVO selectOne(String id) {
        AstSurrenderBillVO astSurrenderBillVO = new AstSurrenderBillVO();
        // 主表
        AstSurrenderBill astSurrenderBill = this.getById(id);
        if (ObjectUtil.isNull(astSurrenderBill) || "2".equals(astSurrenderBill.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        astSurrenderBill.setDelFlag("0");
        astSurrenderBillVO.setAstSurrenderBill(astSurrenderBill);

        // 明细表
        QueryWrapper<AstBillList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstBillList::getBillId, id).orderByDesc(AstBillList::getCreateTime);
        List<AstBillList> astBillLists = astBillListService.list(qw);
        astSurrenderBillVO.setAstBillList(astBillLists);
        // 附件
        List<AstAnnex> astAnnexes = astAnnexService.listByAstIdAndAnnexType(id, "0");
        astSurrenderBillVO.setAstAnnexes(astAnnexes);
        return astSurrenderBillVO;
    }

    /**
     * 插入资产交回数据
     *
     * @param astSurrenderBillVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AstSurrenderBillVO insert(AstSurrenderBillVO astSurrenderBillVO) {
        AstSurrenderBill astSurrenderBill = astSurrenderBillVO.getAstSurrenderBill();
        List<AstBillList> astBillList = astSurrenderBillVO.getAstBillList();
        List<AstAnnex> astAnnexes = astSurrenderBillVO.getAstAnnexes();

        if (CollectionUtil.isEmpty(astBillList)) {
            throw new CustomException("请添加资产交回明细！");
        }
        // 保存主表
        astSurrenderBill.setDelFlag("0");
        astSurrenderBill.setStatus("0");
        astSurrenderBill.setCreateTime(DateUtil.date());
        addName(astSurrenderBill);
        String nextNum = ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_SURRENDER, astSurrenderBill);
        astSurrenderBill.setAstBill(nextNum);
        this.save(astSurrenderBill);
        String id = astSurrenderBill.getId();

        // 保存子表
        astBillList.forEach(v -> {
            v.setBillId(id);
            v.setId(null);
        });
        astBillListService.saveBatch(astBillList);
        // 保存附件表
        saveAstAnnexes(astAnnexes, id);
        return selectOne(astSurrenderBill.getId());
    }

    /**
     * 更新资产交回数据
     *
     * @param astSurrenderBillVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstSurrenderBillVO update(AstSurrenderBillVO astSurrenderBillVO) {
        List<AstBillList> astBillLists = astSurrenderBillVO.getAstBillList();
        // 修改子表
        if (CollectionUtil.isEmpty(astBillLists)) {
            throw new CustomException("请添加资产交回明细！");
        }

        AstSurrenderBill astSurrenderBill = astSurrenderBillVO.getAstSurrenderBill();
        String id = astSurrenderBill.getId();
        AstSurrenderBill byId = this.getById(id);
        if (ObjectUtil.isNull(byId) || "2".equals(byId.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        if (!ProcessConstant.STATUS_0.equals(byId.getStatus())
                && !ProcessConstant.STATUS_9.equals(byId.getStatus())) {
            throw new CustomException("只有草稿状态可以修改！");
        }
        List<AstAnnex> astAnnexes = astSurrenderBillVO.getAstAnnexes();
        // 修改主表
        addName(astSurrenderBill);
        this.updateById(astSurrenderBill);

        QueryWrapper<AstBillList> astCollectListQueryWrapper = new QueryWrapper<>();
        astCollectListQueryWrapper.lambda().eq(AstBillList::getBillId, id);
        astBillListService.remove(astCollectListQueryWrapper);
        astBillLists.forEach(v -> {
            v.setBillId(id);
            v.setId(null);
        });
        astBillListService.saveBatch(astBillLists);
        // 修改附件表
        QueryWrapper<AstAnnex> astAnnexQueryWrapper = new QueryWrapper<>();
        astAnnexQueryWrapper.lambda().eq(AstAnnex::getAstId, id);
        astAnnexService.remove(astAnnexQueryWrapper);
        // 保存附件表
        saveAstAnnexes(astAnnexes, id);
        return selectOne(astSurrenderBill.getId());
    }

    /**
     * 批量删除资产交回数据
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
        List<AstSurrenderBill> astSurrenderBills = this.listByIds(ids);
        if (CollectionUtil.isEmpty(astSurrenderBills)) {
            return "删除成功！";
        }
        for (AstSurrenderBill astSurrenderBill : astSurrenderBills) {
            if (!"0".equals(astSurrenderBill.getStatus())) {
                throw new CustomException("只有草稿状态可以删除！");
            }
            astSurrenderBill.setDelFlag("2");
        }
        this.updateBatchById(astSurrenderBills);

        // 删除明细表中的数据
        return "删除成功！";
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
        List<AstSurrenderBill> astSurrenderBills = this.listByIds(Arrays.asList(id));
        if (CollectionUtil.isEmpty(astSurrenderBills)) {
            throw new CustomException("未查询到采购单");
        }
        // 校验单据状态
        List<String> billStatusList = astSurrenderBills.stream().map(AstSurrenderBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astSurrenderBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_SURRENDER,
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
        this.updateBatchById(astSurrenderBills);
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
        List<AstSurrenderBill> astSurrenderBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astSurrenderBills.stream().map(AstSurrenderBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        Map<String, Object> columnMap = new HashMap<>(1);
        astSurrenderBills.forEach(v -> {
            v.setStatus("1");
            // 执行任务
            List<Task> complete = TaskUtils.complete(v.getProcessinstid(),
                    TaskUtils.getGoVariables(commonProcessRequest, v));
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
            // 判断流程是否结束
            if (TaskUtils.isEnd(v.getProcessinstid())) {
                v.setStatus("2");
                v.setApprovalPost("已终审");

                columnMap.put("BILL_ID", v.getId());
                // 获取资产明细表
                List<AstBillList> astBillList = astBillListService.listByMap(columnMap);

                // 获取资产id列表
                List<String> astIds = astBillList.stream().map(AstBillList::getAstId).distinct()
                        .collect(Collectors.toList());

                // 查询所有要修改的资产卡片列表
                List<AstCard> astCardList = astCardService.listByIds(astIds);

                for (AstCard astCard : astCardList) {
                    astCard.setUsageStatus("4");
                    astCard.setEmpId("");
                    astCard.setEmpCode("");
                    astCard.setEmpName("");
                    astCard.setUpdateBy(SecurityUtils.getUsername());
                    astCard.setUpdateTime(DateUtils.getNowDate());
                }

                // 更新资产卡片状态
                astCardService.updateBatchById(astCardList);

            }
        });
        this.updateBatchById(astSurrenderBills);
        return "审核成功！";
    }

    /***
     * 退回
     * @param commonProcessRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String back(CommonProcessRequest commonProcessRequest) {
        checkId(commonProcessRequest.getId());
        String[] id = commonProcessRequest.getId();
        List<AstSurrenderBill> astSurrenderBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astSurrenderBills.stream().map(AstSurrenderBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astSurrenderBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点
        astSurrenderBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(astSurrenderBills);
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
        List<AstSurrenderBill> astSurrenderBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astSurrenderBills.stream().map(AstSurrenderBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astSurrenderBills.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        astSurrenderBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus("0");
            }
        });
        this.updateBatchById(astSurrenderBills);
        return "收回成功！";
    }

    /**
     * 查询经办人
     *
     * @return
     */
    @Override
    public List<Map<String, String>> selectAgentAll() {
        //查询经办人
        QueryWrapper<AstSurrenderBill> qw = new QueryWrapper<>();
        qw.lambda().eq(AstSurrenderBill::getDelFlag, "0").select(AstSurrenderBill::getAgentId, AstSurrenderBill::getAgentName)
                .groupBy(AstSurrenderBill::getAgentId, AstSurrenderBill::getAgentName);
        List<AstSurrenderBill> list = this.list(qw);
        List<Map<String, String>> mapList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(v -> {
                Map<String, String> map = new HashMap<>();
                if (StringUtils.isNotBlank(v.getAgentName())) {
                    map.put("agentId", v.getAgentId());
                    map.put("agentName", v.getAgentName());
                    mapList.add(map);
                }
            });
        }
        return mapList;
    }

    /**
     * 获取资产卡片列表
     *
     * @param utitId      单位id
     * @param usageStatus 使用状态
     */
    @Override
    public List<AstCard> getAstCardList(String utitId, String usageStatus) {

        QueryWrapper<AstCard> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(AstCard::getInitialState, "1").eq(AstCard::getUnitId, utitId)
                .eq(AstCard::getDelFlag, "0").eq(AstCard::getUsageStatus, usageStatus).isNotNull(AstCard::getEmpId)
                .eq(AstCard::getAstStatus, "1");
        List<AstCard> astCardList = astCardService.list(queryWrapper);

        // 排除掉已经选用的资产卡片
        QueryWrapper<AstBillList> qw = new QueryWrapper();

        qw.lambda().eq(AstBillList::getUnitId, utitId).in(AstBillList::getBillType, "collect", "surrender", "transfer",
                "repair");
        List<AstBillList> astBillList = astBillListService.list(qw);
        List<String> astIds = new ArrayList<>();
        // 获取其中的资产卡片id数组
        if (CollectionUtil.isNotEmpty(astBillList)) {
            astIds = astBillList.stream().map(AstBillList::getAstId).distinct().collect(Collectors.toList());
        }
        AstCard astCard = null;
        // 过滤掉已经选用的卡片
        if (CollectionUtil.isNotEmpty(astCardList)) {
            Iterator<AstCard> iterator = astCardList.iterator();
            while (iterator.hasNext()) {
                astCard = iterator.next();
                if (astIds.contains(astCard.getId())) {
                    iterator.remove();
                }
            }
        }
        return astCardList;
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

    /**
     * 保存资产领用附件
     *
     * @param astAnnexes
     * @param id
     */
    private void saveAstAnnexes(List<AstAnnex> astAnnexes, String id) {
        if (CollectionUtil.isNotEmpty(astAnnexes)) {
            astAnnexes.forEach(v -> {
                v.setAstId(id);
                v.setAnnexType(AnnexType.SURRENDER.getValue());
            });
            astAnnexService.saveBatch(astAnnexes);
        }
    }


    /**
     * 设置经办人名称
     *
     * @param astSurrenderBill
     */
    private void addName(AstSurrenderBill astSurrenderBill) {
        if (StringUtils.isNotEmpty(astSurrenderBill.getAgentId())) {
            BdPersonRpc bdPersonRpc = bdPersonServiceRpc.selectPersonByDeptIdAndDeptId(astSurrenderBill.getRestoreDeptId(),
                    astSurrenderBill.getAgentId());
            if (ObjectUtil.isNotNull(bdPersonRpc)) {
                astSurrenderBill.setAgentName(bdPersonRpc.getUserName());
            }
        }
    }


}