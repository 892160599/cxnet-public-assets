package com.cxnet.project.system.version.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxnet.project.system.version.mapper.SysModelVersionMapper;
import com.cxnet.project.system.version.domain.SysModelVersion;
import com.cxnet.project.system.version.service.SysModelVersionService;
import org.springframework.stereotype.Service;

/**
 * 系统版本控制(SysModelVersion)表服务实现类
 *
 * @author caixx
 * @since 2020-12-25 18:10:25
 */
@Service
public class SysModelVersionServiceImpl extends ServiceImpl<SysModelVersionMapper, SysModelVersion> implements SysModelVersionService {

}