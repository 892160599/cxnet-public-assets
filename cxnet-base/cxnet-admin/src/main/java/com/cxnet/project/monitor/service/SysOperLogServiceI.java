package com.cxnet.project.monitor.service;

import java.util.List;

import com.cxnet.project.monitor.domain.SysOperLog;

/**
 * 操作日志 服务层
 *
 * @author cxnet
 */
public interface SysOperLogServiceI {
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    public void insertOperlog(SysOperLog operLog);

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    public int deleteOperLogByIds(String[] operIds);

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    public SysOperLog selectOperLogById(String operId);

    /**
     * 清空操作日志
     */
    public void cleanOperLog();
}
