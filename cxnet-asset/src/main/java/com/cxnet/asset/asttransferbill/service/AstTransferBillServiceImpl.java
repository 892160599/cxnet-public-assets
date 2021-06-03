package com.cxnet.asset.asttransferbill.service;

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
import com.cxnet.asset.asttransferbill.domain.AstTransferBill;
import com.cxnet.asset.asttransferbill.domain.vo.AstTransferBillVO;
import com.cxnet.asset.asttransferbill.mapper.AstTransferBillMapper;
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
 * 资产移交单主表(AstTransferBill)表服务实现类
 *
 * @author guks
 * @since 2021-04-15 11:58:22
 */
@Service
public class AstTransferBillServiceImpl extends ServiceImpl<AstTransferBillMapper, AstTransferBill> implements AstTransferBillService {

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
     * @param astTransferBill
     * @return
     */
    @Override
    public List<AstTransferBill> selectAll(AstTransferBill astTransferBill) {
        String status = StringUtils.isEmpty(astTransferBill.getStatus()) ? "" : astTransferBill.getStatus();
        astTransferBill.setDelFlag("0");
        List<AstTransferBill> astTransferBills = new ArrayList<>();
        List<String> runTaskByUserNameAndStatus = null;
        switch (status) {
            // 草稿
            case "0":
                astTransferBill.setCreateBy(SecurityUtils.getUsername());
                astTransferBills = this.baseMapper.selectAll(astTransferBill);
                break;
            // 待办
            case "1":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astTransferBill.setPiids(runTaskByUserNameAndStatus);
                    astTransferBills = this.baseMapper.selectAll(astTransferBill);
                }
                break;
            // 已办
            case "2":
                runTaskByUserNameAndStatus = TaskUtils.getRunTaskByUserNameAndStatus(status);
                if (CollectionUtil.isNotEmpty(runTaskByUserNameAndStatus)) {
                    astTransferBill.setPiids(runTaskByUserNameAndStatus);
                    astTransferBills = this.baseMapper.selectAll(astTransferBill);
                }
                break;
            // 终审
            case "3":
                astTransferBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("2"));
                astTransferBills = this.baseMapper.selectAll(astTransferBill);
                break;
            // 全部
            case "":
                astTransferBill.setCreateBy(SecurityUtils.getUsername());
                astTransferBill.setPiids(TaskUtils.getRunTaskByUserNameAndStatus("3"));
                astTransferBills = this.baseMapper.selectAll(astTransferBill);
                break;
            default:
                throw new CustomException("单据状态未定义!");
        }
        return astTransferBills;
    }

    /**
     * 查询vo
     *
     * @param id
     * @return
     */
    @Override
    public AstTransferBillVO selectOne(String id) {
        AstTransferBillVO astTransferBillVO = new AstTransferBillVO();
        // 主表
        AstTransferBill astTransferBill = this.getById(id);
        if (ObjectUtil.isNull(astTransferBill) || "2".equals(astTransferBill.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        astTransferBill.setDelFlag("0");
        astTransferBillVO.setAstTransferBill(astTransferBill);

        // 明细表
        QueryWrapper<AstBillList> qw = new QueryWrapper<>();
        qw.lambda().eq(AstBillList::getBillId, id).orderByDesc(AstBillList::getCreateTime);
        List<AstBillList> astBillLists = astBillListService.list(qw);
        astTransferBillVO.setAstBillList(astBillLists);
        // 附件
        List<AstAnnex> astAnnexes = astAnnexService.listByAstIdAndAnnexType(id, "0");
        astTransferBillVO.setAstAnnexes(astAnnexes);
        return astTransferBillVO;
    }

    /**
     * 新增
     *
     * @param astTransferBillVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstTransferBillVO insert(AstTransferBillVO astTransferBillVO) {
        AstTransferBill astTransferBill = astTransferBillVO.getAstTransferBill();
        List<AstBillList> astBillList = astTransferBillVO.getAstBillList();
        List<AstAnnex> astAnnexes = astTransferBillVO.getAstAnnexes();

        if (CollectionUtil.isEmpty(astBillList)) {
            throw new CustomException("请添加资产移交明细！");
        }
        // 保存主表
        astTransferBill.setDelFlag("0");
        astTransferBill.setStatus(ProcessConstant.STATUS_0);
        astTransferBill.setCreateTime(DateUtil.date());
        addName(astTransferBill);
        String nextNum = ruleServiceI.nextNumberByModelCode(BillTypeCodeConstant.AST_TRANSFER, astTransferBill);
        astTransferBill.setAstBill(nextNum);
        this.save(astTransferBill);
        String id = astTransferBill.getId();

        // 保存子表
        astBillList.forEach(v -> {
            v.setBillId(id);
            v.setId(null);
        });
        astBillListService.saveBatch(astBillList);
        // 保存附件表
        saveAstAnnexes(astAnnexes, id);
        return selectOne(astTransferBill.getId());
    }

    /**
     * 修改
     *
     * @param astTransferBillVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AstTransferBillVO update(AstTransferBillVO astTransferBillVO) {
        List<AstBillList> astBillLists = astTransferBillVO.getAstBillList();
        // 修改子表
        if (CollectionUtil.isEmpty(astBillLists)) {
            throw new CustomException("请添加资产移交明细！");
        }

        AstTransferBill astTransferBill = astTransferBillVO.getAstTransferBill();
        String id = astTransferBill.getId();
        AstTransferBill byId = this.getById(id);
        if (ObjectUtil.isNull(byId) || "2".equals(byId.getDelFlag())) {
            throw new CustomException("单据不存在或已被删除，请刷新后重试！");
        }
        if (!ProcessConstant.STATUS_0.equals(byId.getStatus())
                && !ProcessConstant.STATUS_9.equals(byId.getStatus())) {
            throw new CustomException("只有草稿状态可以修改！");
        }
        List<AstAnnex> astAnnexes = astTransferBillVO.getAstAnnexes();
        // 修改主表
        addName(astTransferBill);
        this.updateById(astTransferBill);

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
        return selectOne(astTransferBill.getId());
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
        List<AstTransferBill> astTransferBills = this.listByIds(ids);
        if (CollectionUtil.isEmpty(astTransferBills)) {
            return "删除成功！";
        }
        for (AstTransferBill astTransferBill : astTransferBills) {
            if (!ProcessConstant.STATUS_0.equals(astTransferBill.getStatus())) {
                throw new CustomException("只有草稿状态可以删除！");
            }
            astTransferBill.setDelFlag("2");
        }
        this.updateBatchById(astTransferBills);

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
        List<AstTransferBill> astTransferBills = this.listByIds(Arrays.asList(id));
        if (CollectionUtil.isEmpty(astTransferBills)) {
            throw new CustomException("未查询到单据");
        }
        List<String> billStatusList = astTransferBills.stream().map(AstTransferBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_0, ProcessConstant.STATUS_9);
        astTransferBills.forEach(v -> {
            String processInstanceId = v.getProcessinstid();
            // 启动流程
            processInstanceId = TaskUtils.startProcessInstanceByBillTypeCodeAndUnitId(BillTypeCodeConstant.AST_TRANSFER,
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
        this.updateBatchById(astTransferBills);
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
        List<AstTransferBill> astTransferBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astTransferBills.stream().map(AstTransferBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        Map<String, Object> columnMap = new HashMap<>(1);
        astTransferBills.forEach(v -> {
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

                String acceptId = v.getAcceptId();
                String acceptCode = v.getAcceptName();

                columnMap.put("BILL_ID", v.getId());
                // 获取资产明细表
                List<AstBillList> astBillList = astBillListService.listByMap(columnMap);

                // 获取资产id列表
                List<String> astIds = astBillList.stream().map(AstBillList::getAstId).distinct()
                        .collect(Collectors.toList());

                // 查询所有要修改的资产卡片列表
                List<AstCard> astCardList = astCardService.listByIds(astIds);

                for (AstCard astCard : astCardList) {
                    astCard.setEmpId(acceptId);
                    astCard.setEmpName(acceptCode);
                    astCard.setUpdateBy(SecurityUtils.getUsername());
                    astCard.setUpdateTime(DateUtils.getNowDate());
                }

                // 更新资产卡片状态
                astCardService.updateBatchById(astCardList);

            }
        });
        this.updateBatchById(astTransferBills);
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
        List<AstTransferBill> astTransferBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astTransferBills.stream().map(AstTransferBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1, ProcessConstant.STATUS_7);
        List<String> piids = new ArrayList<>();
        astTransferBills.forEach(v -> {
            v.setStatus(ProcessConstant.STATUS_1);
            piids.add(v.getProcessinstid());
        });
        // 执行退回任务
        TaskUtils.backProcess(piids, commonProcessRequest);
        // 判断是否是首节点
        astTransferBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_9);
            } else {
                v.setStatus(ProcessConstant.STATUS_7);
            }
        });
        this.updateBatchById(astTransferBills);
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
        List<AstTransferBill> astTransferBills = this.listByIds(Arrays.asList(id));
        // 校验单据状态
        List<String> billStatusList = astTransferBills.stream().map(AstTransferBill::getStatus).collect(Collectors.toList());
        SpringContextUtils.getBean(CheckStatus.class).checkStatus(billStatusList, ProcessConstant.STATUS_1);
        List<String> piids = new ArrayList<>();
        astTransferBills.forEach(v -> piids.add(v.getProcessinstid()));
        // 执行收回操作
        TaskUtils.backTaskBack(piids, commonProcessRequest);
        // 判断是否是首节点
        astTransferBills.forEach(v -> {
            v.setApprovalPost(TaskUtils.getTaskNameByPiid(v.getProcessinstid()));
            if (TaskUtils.isFirstTask(v.getProcessinstid())) {
                v.setStatus(ProcessConstant.STATUS_0);
            }
        });
        this.updateBatchById(astTransferBills);
        return "收回成功！";
    }

    /**
     * 查询移交人
     *
     * @return
     */
    @Override
    public List<Map<String, String>> selecteTransferNameAll() {
        //查询移交人
        QueryWrapper<AstTransferBill> qw = new QueryWrapper<>();
        qw.lambda().eq(AstTransferBill::getDelFlag, "0").select(AstTransferBill::getTransferId, AstTransferBill::getTransferName)
                .groupBy(AstTransferBill::getTransferId, AstTransferBill::getTransferName);
        List<AstTransferBill> list = this.list(qw);
        List<Map<String, String>> mapList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(v -> {
                Map<String, String> map = new HashMap<>();
                if (StringUtils.isNotBlank(v.getTransferName())) {
                    map.put("transferId", v.getTransferId());
                    map.put("transferName", v.getTransferName());
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
                v.setAnnexType(AnnexType.TRANSFER.getValue());
            });
            astAnnexService.saveBatch(astAnnexes);
        }
    }

    /**
     * 设置移交人名称
     *
     * @param astTransferBill
     */
    private void addName(AstTransferBill astTransferBill) {
        if (StringUtils.isNotEmpty(astTransferBill.getTransferId())) {
            BdPersonRpc bdPersonRpc = bdPersonServiceRpc.selectPersonByDeptIdAndDeptId(astTransferBill.getTransferDeptId(),
                    astTransferBill.getTransferId());
            if (ObjectUtil.isNotNull(bdPersonRpc)) {
                astTransferBill.setTransferName(bdPersonRpc.getUserName());
            }
        }
    }


}