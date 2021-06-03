package com.cxnet.project.businesslog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxnet.project.businesslog.domain.SysBusinessLog;

import java.util.List;

/**
 * 业务日志表(SysBusinessLog)表数据库访问层
 *
 * @author guks
 * @since 2021-04-21 18:01:47
 */
public interface SysBusinessLogMapper extends BaseMapper<SysBusinessLog> {

    List<SysBusinessLog> selectAll(SysBusinessLog sysBusinessLog);
}