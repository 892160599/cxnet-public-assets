package com.cxnet.project.businesslog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.project.businesslog.domain.SysBusinessLog;
import com.cxnet.project.businesslog.mapper.SysBusinessLogMapper;
import org.springframework.stereotype.Service;

/**
 * 业务日志表(SysBusinessLog)表服务实现类
 *
 * @author guks
 * @since 2021-04-21 18:01:45
 */
@Service
public class SysBusinessLogServiceImpl extends ServiceImpl<SysBusinessLogMapper, SysBusinessLog> implements SysBusinessLogService {

}