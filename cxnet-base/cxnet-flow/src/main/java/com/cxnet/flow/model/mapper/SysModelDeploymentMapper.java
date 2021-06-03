package com.cxnet.flow.model.mapper;

import com.cxnet.flow.model.domain.SysModelDeployment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统模块流程表Mapper接口
 *
 * @author caixx
 * @date 2020-08-12
 */
public interface SysModelDeploymentMapper {
    /**
     * 查询系统模块流程表
     *
     * @param modelId 系统模块流程表ID
     * @return 系统模块流程表
     */
    public SysModelDeployment selectSysModelDeploymentById(String modelId);

    public SysModelDeployment selectSysModelDeploymentByIdAndUnitId(@Param("modelId") String modelId, @Param("unitId") String unitId);

    /**
     * 查询系统模块流程表集合
     *
     * @param sysModelDeployment 系统模块流程表
     * @return 系统模块流程表集合
     */
    public List<SysModelDeployment> selectSysModelDeploymentList(SysModelDeployment sysModelDeployment);

    /**
     * 查询可绑定业务流程列表
     *
     * @param sysModelDeployment 查询可绑定业务流程列表
     * @return 可绑定业务流程列表
     */
    public List<SysModelDeployment> selectDeploymentListByModelId(SysModelDeployment sysModelDeployment);

    /**
     * 新增系统模块流程表
     *
     * @param sysModelDeployment 系统模块流程表
     * @return 结果
     */
    public int insertSysModelDeployment(SysModelDeployment sysModelDeployment);

    /**
     * 批量新增系统模块流程表
     *
     * @param sysModelDeployments 系统模块流程表
     * @return 结果
     */
    public int insertBatchSysModelDeployment(List<SysModelDeployment> sysModelDeployments);

    /**
     * 修改系统模块流程表
     *
     * @param sysModelDeployment 系统模块流程表
     * @return 结果
     */
    public int updateSysModelDeployment(SysModelDeployment sysModelDeployment);

    /**
     * 批量修改系统模块流程表
     *
     * @param sysModelDeployments 系统模块流程表
     * @return 结果
     */
    public int updateBatchSysModelDeployment(List<SysModelDeployment> sysModelDeployments);

    /**
     * 删除系统模块流程表
     *
     * @param modelId 系统模块流程表ID
     * @return 结果
     */
    public int deleteSysModelDeploymentById(String modelId);

    public int deleteSysModelDeploymentByIdAndUnitId(@Param("modelId") String modelId, @Param("unitId") String unitId);

    /**
     * 根据流程key删除
     *
     * @param key
     * @return
     */
    int deleteSysModelDeploymentByKey(String key);

    /**
     * 根据流程key停用
     *
     * @param key
     * @return
     */
    int stopSysModelDeploymentByKey(String key);


    /**
     * 批量删除系统模块流程表
     *
     * @param modelIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysModelDeploymentByIds(String[] modelIds);

    Integer selectActProcdefByKey(String key);
}
