package com.cxnet.asset.astcollectbill.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cxnet.asset.empchange.domain.AstEmpchangeBill;
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
import com.cxnet.asset.astcollectbill.domain.AstCollectBill;
import com.cxnet.asset.astcollectbill.domain.vo.AstCollectBillVO;
import com.cxnet.asset.astcollectbill.mapper.AstCollectBillMapper;
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
 * 资产领用单主表(AstCollectBill)表服务实现类
 *
 * @author guks
 * @since 2021-04-12 10:52:13
 */
@Service
public class AstCollectBillServiceImpl extends ServiceImpl<AstCollectBillMapper, AstCollectBill>
        implements AstCollectBillService {

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
     * @param astCollectBill
     * @return
     */
    @Override
    public List<AstCollectBill> selectAll(AstCollectBill astCollectBill) {
        String status = StringUtils.isEmpty(astCollectBill.getStatus()) ? "" : astCollectBill.getStatus();
        astCollectBill.setDelFlag("0");
        List<AstCollectBill> astCollectBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            // 草稿
            case "0":
                astCollectBill.setCreateBy(SecurityUtils.getUsername());
                astCollectBills = this.baseMapper.selectAstCollectBillsList(astCollectBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astCollectBill.setPiids(runTaskByUserNameAndStatus);
                    astCollectBills = this.baseMapper.selectAstCollectBillsList(astCollectBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astCollectBill.setPiids(runTaskByUserNameAndStatus);
                    astCollectBills = this.baseMapper.selectAstCollectBillsList(astCollectBill);
                }
                break;
            // 终审
            case "3":
                astCollectBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astCollectBills = this.baseMapper.selectAstCollectBillsList(astCollectBill);
                break;
            // 全部
            case "":
                astCollectBill.setCreateBy(SecurityUtils.getUsername());
                astCollectBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astCollectBills = this.baseMapper.selectAstCollectBillsList(astCollectBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astCollectBills;
    }

    /**
     * 查询vo
     *
     * @param id
     * @return
     */
    @Override
    public AstCollectBillVO selectOne(String id) {

        AstCollectBillVO astCollectBillVO = new AstCollectBillVO();
        // 主表
        AstCollectBill astCollectBill = this.getById(id);
        if (ObjectUtil.isNull(astCollectBill) || "2".equals(astCollectBill.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        astCollectBill.setDelFlag("0");
        astCollectBillVO.setAstCollectBill(astCollectBill);

        // 明细表
        QueryWrapper<AstBillList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstBillList::getBillId, id).orderByDesc(AstBillList::getCreateTime);
        List<AstBillList> astBillLists = astBillListService.list(qw);
        astCollectBillVO.setAstBillList(astBillLists);
        // 附件
        List<AstAnnex> astAnnexes = astAnnexService.listByAstIdAndAnnexType(id, "0");
        astCollectBillVO.setAstAnnexes(astAnnexes);
        return astCollectBillVO;
    }

    /**
     * 新增
     *
     * @param astCollectBillVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstCollectBillVO insert(AstCollectBillVO astCollectBillVO) {
        AstCollectBill astCollectBill = astCollectBillVO.getAstCollectBill();
        List<AstBillList> astBillList = astCollectBillVO.getAstBillList();
        List<AstAnnex> astAnnexes = astCollectBillVO.getAstAnnexes();

        if (CollectionUtil.isEmpty(astBillList)) {
            throw new CustomException("请添加资产领用明细！");
        }
        // 保存主表
        astCollectBill.setDelFlag("0");
        astCollectBill.setStatus(ProcessConstant.STATUS_0);
        astCollectBill.setCreateTime(DateUtil.date());
        addName(astCollectBill);
        String nextNum = ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_COLLECT, astCollectBill);
        astCollectBill.setAstBill(nextNum);
        this.save(astCollectBill);
        String id = astCollectBill.getId();

        // 保存子表
        astBillList.forEach(v -> {
            v.setBillId(id);
            v.setId(null);
        });
        astBillListService.saveBatch(astBillList);
        // 保存附件表
        saveAstAnnexes(astAnnexes, id);
        return selectOne(astCollectBill.getId());
    }

    /**
     * 修改
     *
     * @param astCollectBillVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstCollectBillVO update(AstCollectBillVO astCollectBillVO) {
        List<AstBillList> astBillLists = astCollectBillVO.getAstBillList();
        // 修改子表
        if (CollectionUtil.isEmpty(astBillLists)) {
            throw new CustomException("请添加资产领用明细！");
        }

        AstCollectBill astCollectBill = astCollectBillVO.getAstCollectBill();
        String id = astCollectBill.getId();
        AstCollectBill byId = this.getById(id);
        if (ObjectUtil.isNull(byId) || "2".equals(byId.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        if (!ProcessConstant.STATUS_0.equals(byId.getStatus())
                && !ProcessConstant.STATUS_9.equals(byId.getStatus())) {
            throw new CustomException("只有草稿状态可以修改！");
        }
        List<AstAnnex> astAnnexes = astCollectBillVO.getAstAnnexes();
        // 修改主表
        addName(astCollectBill);
        this.updateById(astCollectBill);

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
        return selectOne(astCollectBill.getId());
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
        List<AstCollectBill> astCollectBills = this.listByIds(ids);
        if (CollectionUtil.isEmpty(astCollectBills)) {
            return "删除成功！";
        }
        for (AstCollectBill astCollectBill : astCollectBills) {
            if (!ProcessConstant.STATUS_0.equals(astCollectBill.getStatus())) {
                throw new CustomException("只有草稿状态可以删除！");
            }
            astCollectBill.setDelFlag("2");
        }
        this.updateBatchById(astCollectBills);

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
        List<AstCollectBill> astCollectBills = this.listByIds(Arrays.asList(id));
        if (CollectionUtil.isEmpty(astCollectBills)) {
            throw new CustomException("未查询到单据");
        }
        List<String> billStatusList = astCollectBills.stream().map(AstCollectBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astCollectBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_COLLECT,
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
        this.updateBatchById(astCollectBills);
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
        List<AstCollectBill> astCollectBills = this.listByIds(Arrays.asList(id));
        List<String> billStatusList = astCollectBills.stream().map(AstCollectBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);

        Map<String, Object> columnMap = new HashMap<>(1);

        astCollectBills.forEach(v -> {
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
                v.setApprovalPost("已终审");

                String empId = v.getEmpId();
                String empCode = v.getEmpCode();
                String empName = v.getEmpName();

                columnMap.put("BILL_ID", v.getId());
                // 获取资产明细表
                List<AstBillList> astBillList = astBillListService.listByMap(columnMap);

                // 获取资产id列表
                List<String> astIds = astBillList.stream().map(AstBillList::getAstId).distinct()
                        .collect(Collectors.toList());

                // 查询所有要修改的资产卡片列表
                List<AstCard> astCardList = astCardService.listByIds(astIds);

                for (AstCard astCard : astCardList) {
                    astCard.setUsageStatus("1");
                    astCard.setEmpId(empId);
                    astCard.setEmpCode(empCode);
                    astCard.setEmpName(empName);
                    astCard.setUpdateBy(SecurityUtils.getUsername());
                    astCard.setUpdateTime(DateUtils.getNowDate());
                }

                // 更新资产卡片状态
                astCardService.updateBatchById(astCardList);

            }
        });
        this.updateBatchById(astCollectBills);
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
        List<AstCollectBill> astCollectBills = this.listByIds(Arrays.asList(id));
        List<String> billStatusList = astCollectBills.stream().map(AstCollectBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astCollectBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点
        astCollectBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(astCollectBills);
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
        List<AstCollectBill> astCollectBills = this.listByIds(Arrays.asList(id));
        List<String> billStatusList = astCollectBills.stream().map(AstCollectBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astCollectBills.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        astCollectBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(astCollectBills);
        return "收回成功！";
    }

    /**
     * 查询领用人
     *
     * @return
     */
    @Override
    public List<Map<String, String>> selectEmpNameAll() {
        //查询领用人
        QueryWrapper<AstCollectBill> qw = new QueryWrapper<>();
        qw.lambda().eq(AstCollectBill::getDelFlag, "0").select(AstCollectBill::getEmpId, AstCollectBill::getEmpName)
                .groupBy(AstCollectBill::getEmpId, AstCollectBill::getEmpName);
        List<AstCollectBill> list = this.list(qw);
        List<Map<String, String>> mapList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(v -> {
                Map<String, String> map = new HashMap<>();
                if (StringUtils.isNotBlank(v.getEmpName())) {
                    map.put("empId", v.getEmpId());
                    map.put("empName", v.getEmpName());
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
                v.setAnnexType(AnnexType.COLLECT.getValue());
            });
            astAnnexService.saveBatch(astAnnexes);
        }
    }

    /**
     * 设置领用人名称
     *
     * @param astCollectBill
     */
    private void addName(AstCollectBill astCollectBill) {
        if (StringUtils.isNotEmpty(astCollectBill.getEmpId())) {
            BdPersonRpc bdPersonRpc = bdPersonServiceRpc.selectPersonByDeptIdAndDeptId(astCollectBill.getDepartmentId(),
                    astCollectBill.getEmpId());
            if (ObjectUtil.isNotNull(bdPersonRpc)) {
                astCollectBill.setEmpName(bdPersonRpc.getUserName());
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