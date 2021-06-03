package com.cxnet.flow.model.service;

import com.cxnet.common.utils.tree.Zone;
import com.cxnet.flow.model.domain.*;
import com.cxnet.rpc.domain.SysDbYb;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 系统模块管理Service接口
 *
 * @author caixx
 * @date 2020-08-12
 */
public interface SysModelServiceI {
    /**
     * 查询系统模块管理
     *
     * @param modelId 系统模块管理ID
     * @return 系统模块管理
     */
    public SysModel selectSysModelById(String modelId);

    /**
     * 查询流程定义列表
     *
     * @param sysModelDeployment 查询流程定义列表
     * @return 流程定义列表
     */
    public List<SysModelDeployment> selectModelDeploymentList(SysModelDeployment sysModelDeployment);

    /**
     * 查询单据绑定的流程列表
     *
     * @param sysModelDeployment 查询单据绑定的流程列表
     * @return 查询可绑定业务流程列表
     */
    public List<SysModelDeployment> selectDeploymentListByModelId(SysModelDeployment sysModelDeployment);

    /**
     * 单据绑定流程
     *
     * @return 结果
     */
    public int bindBill(SysModelDeployment sysModelDeployment);

    /**
     * 查询系统模块业务单据集合
     *
     * @param modelId 系统模块管理
     * @return 系统业务单据集合
     */
    public List<SysModelBill> selectModelBill(String modelId);

    /**
     * 查询系统模块管理tree
     *
     * @param sysModel 查询系统模块管理tree
     * @return 查询系统模块管理tree
     */
    public List<Zone> selectSysModelTree(SysModel sysModel);

    /**
     * 新增系统模块管理
     *
     * @param sysModel 系统模块管理
     * @return 结果
     */
    public int insertSysModel(SysModel sysModel);

    /**
     * 新增流程
     *
     * @param sysModelDeployment 新增流程
     * @return 结果
     */
    public int insertSysModelDeployment(SysModelDeployment sysModelDeployment);

    /**
     * 批量新增系统模块管理
     *
     * @param sysModels 系统模块管理
     * @return 结果
     */
    public int insertBatchSysModel(List<SysModel> sysModels);

    /**
     * 修改系统模块管理
     *
     * @param sysModel 系统模块管理
     * @return 结果
     */
    public int updateSysModel(SysModel sysModel);

    /**
     * 批量修改系统模块管理
     *
     * @param sysModels 系统模块管理
     * @return 结果
     */
    public int updateBatchSysModel(List<SysModel> sysModels);

    /**
     * 批量删除系统模块管理
     *
     * @param modelIds 需要删除的系统模块管理ID
     * @return 结果
     */
    public int deleteSysModelByIds(String[] modelIds);

    /**
     * 删除系统模块管理信息
     *
     * @param modelId 系统模块管理ID
     * @return 结果
     */
    public int deleteSysModelById(String modelId);

    /**
     * 删除流程
     *
     * @param deploymentId
     * @return
     */
    int delSysModelDeployment(String deploymentId);

    /**
     * 停用流程
     *
     * @param deploymentId
     * @return
     */
    int stopSysModelDeployment(String deploymentId);

    /**
     * 查询单据配置信息
     *
     * @param billTypeCode
     * @return
     */
    SysBillConf selectBillConf(String billTypeCode);

    List<SysDbYb> selectAllAcivitiDb(String userAccount, Date startTime, Date endTime, String modelName, String searchValue, String deptCode, String billNo, BigDecimal minAmt, BigDecimal maxAmt, String status, String parentPath);

    List<SysDbYb> selectAllAcivitiYb(String userAccount, Date startTime, Date endTime, String modelName, String searchValue, String deptCode, String billNo, BigDecimal minAmt, BigDecimal maxAmt, String status, String parentPath);

    /**
     * 移动端查看我的发起
     *
     * @param createName 当前登陆人
     * @return
     */
    List<SysDbYb> mobileSelectMySelfStart(String createName, Date startTime, Date endTime, String modelName, String deptCode, String billNo, BigDecimal minAmt, BigDecimal maxAmt, String status, String parentPath);

}
