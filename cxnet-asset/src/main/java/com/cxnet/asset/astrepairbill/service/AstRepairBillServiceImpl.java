package com.cxnet.asset.astrepairbill.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cxnet.asset.dispose.domain.AstDisposeBill;
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
import com.cxnet.asset.astrepairbill.domain.AstRepairBill;
import com.cxnet.asset.astrepairbill.domain.vo.AstRepairBillVO;
import com.cxnet.asset.astrepairbill.mapper.AstRepairBillMapper;
import com.cxnet.asset.card.domain.AstBillList;
import com.cxnet.asset.card.domain.AstCard;
import com.cxnet.asset.card.service.AstBillListService;
import com.cxnet.asset.card.service.AstCardService;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.enums.AnnexType;
import com.cxnet.common.exception.CustomException;
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
 * 资产报修单主表(AstRepairBill)表服务实现类
 *
 * @author guks
 * @since 2021-04-15 11:57:18
 */
@Service
public class AstRepairBillServiceImpl extends ServiceImpl<AstRepairBillMapper, AstRepairBill> implements AstRepairBillService {


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
     * @param astRepairBill
     * @return
     */
    @Override
    public List<AstRepairBill> selectAll(AstRepairBill astRepairBill) {
        String status = StringUtils.isEmpty(astRepairBill.getStatus()) ? "" : astRepairBill.getStatus();
        astRepairBill.setDelFlag("0");
        List<AstRepairBill> astRepairBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            // 草稿
            case "0":
                astRepairBill.setCreateBy(SecurityUtils.getUsername());
                astRepairBills = this.baseMapper.selectAll(astRepairBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astRepairBill.setPiids(runTaskByUserNameAndStatus);
                    astRepairBills = this.baseMapper.selectAll(astRepairBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astRepairBill.setPiids(runTaskByUserNameAndStatus);
                    astRepairBills = this.baseMapper.selectAll(astRepairBill);
                }
                break;
            // 终审
            case "3":
                astRepairBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astRepairBills = this.baseMapper.selectAll(astRepairBill);
                break;
            // 全部
            case "":
                astRepairBill.setCreateBy(SecurityUtils.getUsername());
                astRepairBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astRepairBills = this.baseMapper.selectAll(astRepairBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astRepairBills;
    }

    /**
     * 查询vo
     *
     * @param id
     * @return
     */
    @Override
    public AstRepairBillVO selectOne(String id) {

        AstRepairBillVO astRepairBillVO = new AstRepairBillVO();
        // 主表
        AstRepairBill astRepairBill = this.getById(id);
        if (ObjectUtil.isNull(astRepairBill) || "2".equals(astRepairBill.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        astRepairBill.setDelFlag("0");
        astRepairBillVO.setAstRepairBill(astRepairBill);

        // 明细表
        QueryWrapper<AstBillList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstBillList::getBillId, id).orderByDesc(AstBillList::getCreateTime);
        List<AstBillList> astBillLists = astBillListService.list(qw);
        astRepairBillVO.setAstBillList(astBillLists);
        // 附件
        List<AstAnnex> astAnnexes = astAnnexService.listByAstIdAndAnnexType(id, "0");
        astRepairBillVO.setAstAnnexes(astAnnexes);
        return astRepairBillVO;
    }

    /**
     * 新增
     *
     * @param astRepairBillVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstRepairBillVO insert(AstRepairBillVO astRepairBillVO) {
        AstRepairBill astRepairBill = astRepairBillVO.getAstRepairBill();
        List<AstBillList> astBillList = astRepairBillVO.getAstBillList();
        List<AstAnnex> astAnnexes = astRepairBillVO.getAstAnnexes();

        if (CollectionUtil.isEmpty(astBillList)) {
            throw new CustomException("请添加资产报修明细！");
        }
        // 保存主表
        astRepairBill.setDelFlag("0");
        astRepairBill.setStatus(ProcessConstant.STATUS_0);
        astRepairBill.setCreateTime(DateUtil.date());
        addName(astRepairBill);
        String nextNum = ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_REPAIR, astRepairBill);
        astRepairBill.setAstBill(nextNum);
        this.save(astRepairBill);
        String id = astRepairBill.getId();

        // 保存子表
        astBillList.forEach(v -> {
            v.setBillId(id);
            v.setId(null);
        });
        astBillListService.saveBatch(astBillList);
        // 保存附件表
        saveAstAnnexes(astAnnexes, id);
        return selectOne(astRepairBill.getId());
    }

    /**
     * 修改
     *
     * @param astRepairBillVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstRepairBillVO update(AstRepairBillVO astRepairBillVO) {
        List<AstBillList> astBillLists = astRepairBillVO.getAstBillList();
        // 修改子表
        if (CollectionUtil.isEmpty(astBillLists)) {
            throw new CustomException("请添加资产报修明细！");
        }

        AstRepairBill astRepairBill = astRepairBillVO.getAstRepairBill();
        String id = astRepairBill.getId();
        AstRepairBill byId = this.getById(id);
        if (ObjectUtil.isNull(byId) || "2".equals(byId.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        if (!ProcessConstant.STATUS_0.equals(byId.getStatus())
                && !ProcessConstant.STATUS_9.equals(byId.getStatus())) {
            throw new CustomException("只有草稿状态可以修改！");
        }
        List<AstAnnex> astAnnexes = astRepairBillVO.getAstAnnexes();
        // 修改主表
        addName(astRepairBill);
        this.updateById(astRepairBill);

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
        return selectOne(astRepairBill.getId());
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
        List<AstRepairBill> astRepairBills = this.listByIds(ids);
        if (CollectionUtil.isEmpty(astRepairBills)) {
            return "删除成功！";
        }
        for (AstRepairBill astRepairBill : astRepairBills) {
            if (!ProcessConstant.STATUS_0.equals(astRepairBill.getStatus())) {
                throw new CustomException("只有草稿状态可以删除！");
            }
            astRepairBill.setDelFlag("2");
        }
        this.updateBatchById(astRepairBills);

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
        List<AstRepairBill> astRepairBills = this.listByIds(Arrays.asList(id));
        if (CollectionUtil.isEmpty(astRepairBills)) {
            throw new CustomException("未查询到采购单");
        }
        // 校验单据状态
        List<String> billStatusList = astRepairBills.stream().map(AstRepairBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astRepairBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_REPAIR,
                    processInstanceId);
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
        this.updateBatchById(astRepairBills);
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
        List<AstRepairBill> astRepairBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astRepairBills.stream().map(AstRepairBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        Map<String, Object> columnMap = new HashMap<>(1);
        astRepairBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            // 执行任务
            List<Task> complete = TaskUtils.complete(v.getProcessinstid(),
                    TaskUtils.getGoVariables(commonProcessRequest, v));
            if (CollectionUtil.isNotEmpty(complete)) {
                v.setApprovalPost(complete.get(0).getName());
            }
            // 判断流程是否结束
            if (TaskUtils.isEnd(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_2);
                v.setApprovalPost(ProcessConstant.END);
            }
        });
        this.updateBatchById(astRepairBills);
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
        List<AstRepairBill> astRepairBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astRepairBills.stream().map(AstRepairBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astRepairBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点
        astRepairBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(astRepairBills);
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
        List<AstRepairBill> astRepairBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astRepairBills.stream().map(AstRepairBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astRepairBills.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        astRepairBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(astRepairBills);
        return "收回成功！";
    }

    /**
     * 查询报修人
     *
     * @return
     */
    @Override
    public List<Map<String, String>> selectRepairNameAll() {
        //查询领用人
        QueryWrapper<AstRepairBill> qw = new QueryWrapper<>();
        qw.lambda().eq(AstRepairBill::getDelFlag, "0").select(AstRepairBill::getRepairId, AstRepairBill::getRepairName)
                .groupBy(AstRepairBill::getRepairId, AstRepairBill::getRepairName);
        List<AstRepairBill> list = this.list(qw);
        List<Map<String, String>> mapList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(v -> {
                Map<String, String> map = new HashMap<>();
                if (StringUtils.isNotBlank(v.getRepairName())) {
                    map.put("repairId", v.getRepairId());
                    map.put("repairName", v.getRepairName());
                    mapList.add(map);
                }
            });
        }
        return mapList;
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
                v.setAnnexType(AnnexType.REPAIR.getValue());
            });
            astAnnexService.saveBatch(astAnnexes);
        }
    }

    /**
     * 设置报修人名称
     *
     * @param astRepairBill
     */
    private void addName(AstRepairBill astRepairBill) {
        if (StringUtils.isNotEmpty(astRepairBill.getRepairId())) {
            BdPersonRpc bdPersonRpc = bdPersonServiceRpc.selectPersonByDeptIdAndDeptId(astRepairBill.getRepairDeptId(),
                    astRepairBill.getRepairId());
            if (ObjectUtil.isNotNull(bdPersonRpc)) {
                astRepairBill.setRepairName(bdPersonRpc.getUserName());
            }
        }
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
                .eq(AstCard::getDelFlag, "0").eq(AstCard::getUsageStatus, usageStatus).isNull(AstCard::getEmpId)
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
}