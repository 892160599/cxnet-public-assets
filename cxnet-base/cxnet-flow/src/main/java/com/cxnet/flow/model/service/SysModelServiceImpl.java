package com.cxnet.flow.model.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxnet.common.constant.BillTypeCodeConstant;
import com.cxnet.common.constant.ProcessConstant;
import com.cxnet.common.core.lang.UUID;
import com.cxnet.common.exception.CustomException;
import com.cxnet.common.utils.DateUtils;
import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.tree.Zone;
import com.cxnet.flow.model.domain.*;
import com.cxnet.flow.model.mapper.SysDbYbMapper;
import com.cxnet.flow.utils.TaskUtils;
import com.cxnet.framework.security.SecurityUtils;
import com.cxnet.project.system.dict.mapper.SysDictDataMapper;
import com.cxnet.flow.model.mapper.SysModelDeploymentMapper;
import com.cxnet.flow.model.mapper.SysModelMapper;
import com.cxnet.flow.utils.BillUtils;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.mapper.SysParameterMapper;
import com.cxnet.project.system.version.domain.SysModelVersion;
import com.cxnet.project.system.version.mapper.SysModelVersionMapper;
import com.cxnet.rpc.domain.SysDbYb;
import com.cxnet.rpc.domain.budget.BugPlanRpc;
import com.cxnet.rpc.service.budget.BugPlanRpcService;
import com.cxnet.rpc.service.expense.ExpenseServiceRpc;
import com.cxnet.rpc.service.sysModel.SysModelServiceIRpc;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// FIXME generate failure  field _$BpmnDefinitions68

/**
 * 系统模块管理Service业务层处理
 *
 * @author caixx
 * @date 2020-08-12
 */
@NoArgsConstructor
@Data
@Service
public class SysModelServiceImpl implements SysModelServiceI, SysModelServiceIRpc {

    @Value("${cxnet.name}")
    private String cxnetName;

    @Value("${cxnet.version}")
    private String cxnetVersion;

    @Autowired(required = false)
    private SysModelMapper sysModelMapper;

    @Autowired(required = false)
    private SysModelDeploymentMapper sysModelDeploymentMapper;

    @Autowired(required = false)
    private SysDictDataMapper sysDictDataMapper;

    @Autowired(required = false)
    private SysDbYbMapper sysDbYbMapper;

    @Autowired(required = false)
    private SysParameterMapper sysParameterMapper;

    @Autowired(required = false)
    private SysModelVersionMapper sysModelVersionMapper;

    @Autowired(required = false)
    private ExpenseServiceRpc expenseServiceRpc;

    @Autowired(required = false)
    private BugPlanRpcService bugPlanRpcService;


    /**
     * 预算
     */
    public static final String BUG_ITEM = "BUG_ITEM";
    /**
     * 采购
     */
    public static final String PUR_PURCHASE = "PUR_PURCHASE";
    /**
     * 合同
     */
    public static final String CON_CONTRACT = "CON_CONTRACT";
    /**
     * 基建
     */
    public static final String BUG_BASICS_PROJECT = "BUG_BASICS_PROJECT";
    /**
     * 用款计划
     */
    public static final String BUG_PLAN = "bug_plan";

    /**
     * 报销模块
     */
    public static final String EXP = "EXP";

    /**
     * 查询系统模块管理
     *
     * @param modelId 系统模块管理ID
     * @return 系统模块管理
     */
    @Override
    public SysModel selectSysModelById(String modelId) {
        SysModel sysModel = sysModelMapper.selectSysModelById(modelId);
        return sysModel;
    }

    /**
     * 查询流程定义列表
     *
     * @param sysModelDeployment 查询流程定义列表
     * @return 流程定义列表
     */
    @Override
    public List<SysModelDeployment> selectModelDeploymentList(SysModelDeployment sysModelDeployment) {
        sysModelDeployment.setDelFlag("0");
        return sysModelDeploymentMapper.selectSysModelDeploymentList(sysModelDeployment);
    }

    /**
     * 查询单据绑定的流程列表
     *
     * @param sysModelDeployment 查询单据绑定的流程列表
     * @return
     */
    @Override
    public List<SysModelDeployment> selectDeploymentListByModelId(SysModelDeployment sysModelDeployment) {
        String modelId = sysModelDeployment.getModelId();
        String unitId = sysModelDeployment.getUnitId();
        if (modelId == null) {
            return new ArrayList<>();
        }
        SysModel sysModel = sysModelMapper.selectSysModelById(modelId);
//        SysModel sysModel = sysModelMapper.selectSysModelByIdAndUnitId(modelId, unitId);
        if (1 == sysModel.getModelType()) {
            return new ArrayList<>();
        }
//        SysModelDeployment sysModelDeploymentById = sysModelDeploymentMapper.selectSysModelDeploymentById(modelId);
        SysModelDeployment sysModelDeploymentById = sysModelDeploymentMapper.selectSysModelDeploymentByIdAndUnitId(modelId, unitId);
        sysModelDeployment.setModelId(sysModel.getParentId());
        sysModelDeployment.setStatus("0");
        sysModelDeployment.setDelFlag("0");
        sysModelDeployment.setUnitId("0");
        List<SysModelDeployment> sysModelDeployments = sysModelDeploymentMapper.selectSysModelDeploymentList(sysModelDeployment);
        if (sysModelDeploymentById != null) {
            String key = sysModelDeploymentById.getKey();
            sysModelDeployments.forEach(v -> {
                if (v.getKey().equals(key)) {
                    v.setIsBind(0);
                }
            });
        }
        return sysModelDeployments;
    }

    @Override
    @Transactional
    public int bindBill(SysModelDeployment sysModelDeployment) {
        String bindModelId = sysModelDeployment.getBindModelId();
        // 取消绑定
        if (sysModelDeployment.getModelId() == null) {
            SysModel sysModel = sysModelMapper.selectSysModelById(bindModelId);
            if (sysModel != null && sysModel.getModelType() != 1) {
                return sysModelDeploymentMapper.deleteSysModelDeploymentByIdAndUnitId(bindModelId, sysModelDeployment.getUnitId());
            }
            return 1;
        }
        String key = sysModelDeployment.getKey();
        SysModelDeployment oldSysModelDeployment = sysModelDeploymentMapper.selectSysModelDeploymentByIdAndUnitId(bindModelId, sysModelDeployment.getUnitId());
        // 新增绑定
        if (oldSysModelDeployment == null) {
            sysModelDeployment.setModelId(bindModelId);
            sysModelDeployment.setDelFlag("0");
            return sysModelDeploymentMapper.insertSysModelDeployment(sysModelDeployment);
        }
        // 修改绑定
        oldSysModelDeployment.setKey(key);
        return sysModelDeploymentMapper.updateSysModelDeployment(oldSysModelDeployment);
    }

    /**
     * 查询系统模块业务单据集合
     *
     * @param modelId 系统模块管理
     * @return 系统业务单据集合
     */
    @Override
    public List<SysModelBill> selectModelBill(String modelId) {
        return sysModelMapper.selectModelBill(modelId);
    }

    /**
     * 查询系统模块管理tree
     *
     * @param sysModel 查询系统模块管理tree
     * @return list
     */
    @Override
    public List<Zone> selectSysModelTree(SysModel sysModel) {
        return sysModelMapper.selectSysModelTree(sysModel);
    }

    /**
     * 新增系统模块管理
     *
     * @param sysModel 系统模块管理
     * @return 结果
     */
    @Override
    public int insertSysModel(SysModel sysModel) {
        sysModel.setCreateBy(SecurityUtils.getUsername());
        sysModel.setCreateTime(DateUtils.getNowDate());
        return sysModelMapper.insertSysModel(sysModel);
    }

    /**
     * 新增流程
     *
     * @param sysModelDeployment 新增流程
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSysModelDeployment(SysModelDeployment sysModelDeployment) {
        String key = "act-" + UUID.randomUUID();
        sysModelDeployment.setKey(key);
        Deployment deployment = TaskUtils.createDeploymentByKeyAndName(key, sysModelDeployment.getDeploymentName());
        sysModelDeployment.setUnitId("0");
        sysModelDeployment.setDeploymentId(deployment.getId());
        sysModelDeployment.setCreateBy(SecurityUtils.getUsername());
        sysModelDeployment.setCreateTime(deployment.getDeploymentTime());
        sysModelDeployment.setUpdateBy(SecurityUtils.getUsername());
        sysModelDeployment.setUpdateTime(deployment.getDeploymentTime());
        return sysModelDeploymentMapper.insertSysModelDeployment(sysModelDeployment);
    }

    /**
     * 批量新增系统模块管理
     *
     * @param sysModels 系统模块管理
     * @return 结果
     */
    @Override
    public int insertBatchSysModel(List<SysModel> sysModels) {
        return sysModelMapper.insertBatchSysModel(sysModels);
    }

    /**
     * 修改系统模块管理
     *
     * @param sysModel 系统模块管理
     * @return 结果
     */
    @Override
    public int updateSysModel(SysModel sysModel) {
        sysModel.setUpdateBy(SecurityUtils.getUsername());
        sysModel.setUpdateTime(DateUtils.getNowDate());
        return sysModelMapper.updateSysModel(sysModel);
    }

    /**
     * 批量修改系统模块管理
     *
     * @param sysModels 系统模块管理
     * @return 结果
     */
    @Override
    public int updateBatchSysModel(List<SysModel> sysModels) {
        return sysModelMapper.updateBatchSysModel(sysModels);
    }

    /**
     * 批量删除系统模块管理
     *
     * @param modelIds 需要删除的系统模块管理ID
     * @return 结果
     */
    @Override
    public int deleteSysModelByIds(String[] modelIds) {
        return sysModelMapper.deleteSysModelByIds(modelIds);
    }

    /**
     * 删除系统模块管理信息
     *
     * @param modelId 系统模块管理ID
     * @return 结果
     */
    @Override
    public int deleteSysModelById(String modelId) {
        return sysModelMapper.deleteSysModelById(modelId);
    }

    /**
     * 删除流程
     *
     * @param deploymentId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delSysModelDeployment(String deploymentId) {
        ProcessDefinition processDefinition = TaskUtils.selectDeployment(deploymentId);
        String key = processDefinition.getKey();
        TaskUtils.deleteDeployment(deploymentId);
        return sysModelDeploymentMapper.deleteSysModelDeploymentByKey(key);
    }

    /**
     * 停用流程
     *
     * @param deploymentId
     * @return
     */
    @Override
    @Transactional
    public int stopSysModelDeployment(String deploymentId) {
        ProcessDefinition processDefinition = TaskUtils.selectDeployment(deploymentId);
        String key = processDefinition.getKey();
        TaskUtils.suspendProcessDefinitionById(deploymentId);
        return sysModelDeploymentMapper.stopSysModelDeploymentByKey(key);
    }

    /**
     * 查询单据配置信息
     *
     * @param billTypeCode
     * @return
     */
    @Override
    public SysBillConf selectBillConf(String billTypeCode) {
        SysBillConf sysBillConf = new SysBillConf();
        // 附件类型值集编码
        if (StringUtils.isNotEmpty(billTypeCode)) {
            SysModel sysModel = sysModelMapper.selectSysModelByBillTypeCode(billTypeCode);
            if (ObjectUtil.isNotNull(sysModel)) {
                sysBillConf.setSysModel(sysModel);
            }
            String dictTypeCode = BillUtils.getDictTypeCode(billTypeCode);
            if (StringUtils.isNotEmpty(dictTypeCode)) {
                sysBillConf.setDictType(dictTypeCode);
                sysBillConf.setSysDictData(sysDictDataMapper.selectDictDataByType(dictTypeCode));
            }
        }
        return sysBillConf;
    }

    /**
     * 查询个人的所有待办任务
     *
     * @param
     * @return
     */
    @Override
    public List<SysDbYb> selectAllAcivitiDb(String userAccount, Date startTime, Date endTime, String modelName, String searchValue, String deptCode, String billNo, BigDecimal minAmt, BigDecimal maxAmt, String status, String parentPath) {
        //关联业务数据查询个人待办
        List<SysDbYb> sysDbYbs = sysModelMapper.selectAcivitiTodo(userAccount, modelName, startTime, endTime, searchValue, deptCode, billNo, minAmt, maxAmt, status, parentPath);
        String nickName = SecurityUtils.getLoginUser().getUser().getNickName();
        sysDbYbs.forEach(v -> {
            if (ProcessConstant.STATUS_9.equals(v.getStatus()) && StringUtils.isNotEmpty(v.getModelCode())) {
                if (BUG_ITEM.equals(v.getTableMain()) || PUR_PURCHASE.equals(v.getTableMain()) || CON_CONTRACT.equals(v.getTableMain()) || BUG_BASICS_PROJECT.equals(v.getTableMain())) {
                    v.setPath(sysModelMapper.selectPatchByModeCode(v.getModelCode() + "-1"));
                }
            }
            v.setNickName(nickName);
        });
        // 设置内控模块待办
        setExpShareDb(sysDbYbs);

        setHomeUnitDeptStyle(sysDbYbs);
        return sysDbYbs;
    }

    /**
     * 用款计划追减路径
     *
     * @param sysDbYb
     */
    private void bugPlanPath(SysDbYb sysDbYb) {
        if (BUG_PLAN.equals(sysDbYb.getModelCode())) {
            BugPlanRpc bugPlanRpc = bugPlanRpcService.selectBugPlanByPlanId(sysDbYb.getPrimaryId());
            if (ObjectUtil.isNotNull(bugPlanRpc) && "2".equals(bugPlanRpc.getPlanType())) {
                //用款计划追减
                sysDbYb.setPath(sysModelMapper.selectPatchByModeCode(sysDbYb.getModelCode() + "_sub"));
                sysDbYb.setBillName("用款计划追减");
                sysDbYb.setMoney(bugPlanRpc.getRedMoney().toString());
            }
        }
    }

    /**
     * 设置内控报销模块分摊经费待办
     *
     * @param sysDbYbs
     */
    private void setExpShareDb(List<SysDbYb> sysDbYbs) {
        QueryWrapper<SysModelVersion> qw = new QueryWrapper<>();
        qw.lambda().eq(SysModelVersion::getSystemName, cxnetName)
                .eq(SysModelVersion::getModelName, EXP)
                .eq(SysModelVersion::getVersion, cxnetVersion)
                .eq(SysModelVersion::getIsUse, "1");
        Integer integer = sysModelVersionMapper.selectCount(qw);
        if (integer > 0) {
            sysDbYbs.forEach(v -> {
                bugPlanPath(v);
            });
            List<SysDbYb> expShareDb = expenseServiceRpc.getExpShareDb();
            if (CollectionUtil.isNotEmpty(expShareDb)) {
                sysDbYbs.addAll(0, expShareDb);
            }
        }
    }

    /**
     * 查询已办
     *
     * @param userAccount
     * @param startTime
     * @param endTime
     * @param modelName
     * @return
     */
    @Override
    public List<SysDbYb> selectAllAcivitiYb(String userAccount, Date startTime, Date endTime, String modelName, String searchValue, String deptCode, String billNo, BigDecimal minAmt, BigDecimal maxAmt, String status, String parentPath) {
        String nickName = SecurityUtils.getLoginUser().getUser().getNickName();
        //关联业务数据查询个人已办
        List<SysDbYb> sysDbYbs = sysModelMapper.selectActiviDone(userAccount, modelName, startTime, endTime, searchValue, deptCode, billNo, minAmt, maxAmt, status, parentPath);
        sysDbYbs.forEach(v -> v.setNickName(nickName));
        // 设置内控模块已办
        setExpShareYb(sysDbYbs);

        setHomeUnitDeptStyle(sysDbYbs);
        return sysDbYbs;
    }

    @Override
    public List<SysDbYb> mobileSelectMySelfStart(String createName, Date startTime, Date endTime, String modelName, String deptCode, String billNo, BigDecimal minAmt, BigDecimal maxAmt, String status, String parentPath) {
        return sysModelMapper.mobileSelectMyselfStart(createName, startTime, endTime, modelName, deptCode, billNo, minAmt, maxAmt, status, parentPath);
    }

    /**
     * 设置内控报销模块分摊经费已办
     *
     * @param sysDbYbs
     */
    private void setExpShareYb(List<SysDbYb> sysDbYbs) {
        QueryWrapper<SysModelVersion> qw = new QueryWrapper<>();
        qw.lambda().eq(SysModelVersion::getSystemName, cxnetName)
                .eq(SysModelVersion::getModelName, EXP)
                .eq(SysModelVersion::getVersion, cxnetVersion)
                .eq(SysModelVersion::getIsUse, "1");
        Integer integer = sysModelVersionMapper.selectCount(qw);
        if (integer > 0) {
            sysDbYbs.forEach(v -> {
                bugPlanPath(v);
            });
            List<SysDbYb> expShareYb = expenseServiceRpc.getExpShareYb();
            if (CollectionUtil.isNotEmpty(expShareYb)) {
                sysDbYbs.addAll(0, expShareYb);
            }
        }
    }

    /**
     * 单位部门名称处理
     *
     * @param sysDbYbs
     */
    private void setHomeUnitDeptStyle(List<SysDbYb> sysDbYbs) {
        SysParameter sysParameter = sysParameterMapper.selectSysParameterById("1");
        if (ObjectUtil.isNotNull(sysParameter)) {
            String homeUnitDeptStyle = sysParameter.getHomeUnitDeptStyle();
            sysDbYbs.forEach(v -> {
                switch (homeUnitDeptStyle) {
                    case "1":
                        v.setUnitDeptName("".concat(String.valueOf(v.getUnitName())).concat("  ").concat(String.valueOf(v.getDeptName())));
                        break;
                    case "2":
                        v.setUnitDeptName(v.getUnitName());
                        break;
                    case "3":
                        v.setUnitDeptName(v.getDeptName());
                        break;
                    case "4":
                        break;
                    default:
                }
            });

        }
    }

    /**
     * 查询单据编号器编码
     *
     * @return
     */
    @Override
    public String getRuleCode(String billTypeCode) {
        return BillUtils.getRuleCode(billTypeCode);
    }
}
