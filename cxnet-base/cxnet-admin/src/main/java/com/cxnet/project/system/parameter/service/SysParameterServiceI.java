package com.cxnet.project.system.parameter.service;

import com.cxnet.project.system.parameter.domain.SysParameter;

/**
 * 系统配置服务层
 *
 * @Author ssw
 */
public interface SysParameterServiceI {

    /**
     * 查询系统参数配置
     *
     * @return
     */
    public SysParameter selectSysParameter();


    /**
     * 添加系统参数配置
     *
     * @param sysParameter
     * @return
     */
    public int insertSysParameter(SysParameter sysParameter);

    /**
     * 修改系统参数配置
     *
     * @param sysParameter
     * @return
     */
    public int updateSysparameter(SysParameter sysParameter);
}
