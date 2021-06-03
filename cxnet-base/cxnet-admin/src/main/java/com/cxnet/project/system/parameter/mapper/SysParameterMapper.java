package com.cxnet.project.system.parameter.mapper;

import com.cxnet.project.system.parameter.domain.SysParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysParameterMapper {
    /**
     * 查询系统参数配置
     *
     * @return
     */
    public SysParameter selectSysParameter();

    /**
     * 根据编号查找系统配置信息
     *
     * @param parameterId
     * @return
     */
    public SysParameter selectSysParameterById(@Param("parameterId") String parameterId);

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
