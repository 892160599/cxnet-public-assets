package com.cxnet.project.system.parameter.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.cxnet.common.exception.CustomException;
import com.cxnet.project.system.parameter.domain.SysParameter;
import com.cxnet.project.system.parameter.mapper.SysParameterMapper;
import com.cxnet.project.system.parameter.service.SysParameterServiceI;
import com.cxnet.project.system.user.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统配置服务层
 *
 * @Author ssw
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysParameterServiceImpl implements SysParameterServiceI {

    @Autowired(required = false)
    private SysParameterMapper sysParameterMapper;
    @Autowired(required = false)
    private SysUserMapper userMapper;

    @Override
    public SysParameter selectSysParameter() {
        return sysParameterMapper.selectSysParameter();
    }

    @Override
    public int insertSysParameter(SysParameter sysParameter) {
        //查询系统参数是不是存在
        SysParameter parameter = sysParameterMapper.selectSysParameterById(sysParameter.getParameterId());
        int count = 0;
        //不存在
        if (parameter == null) {
            //添加系统参数
            count = sysParameterMapper.insertSysParameter(sysParameter);
        } else {
            //修改系统参数
            count = updateSysparameter(sysParameter);
        }
        return count;
    }

    @Override
    public int updateSysparameter(SysParameter sysParameter) {
        int res = sysParameterMapper.updateSysparameter(sysParameter);
        if (res > 0) {
            String validity = sysParameter.getExpExtend2();
            DateTime date = DateUtil.date();
            DateTime validityTime = null;
            switch (validity) {
                case "1":
                    validityTime = DateUtil.offsetMonth(date, 1);
                    break;
                case "2":
                    validityTime = DateUtil.offsetMonth(date, 3);
                    break;
                case "3":
                    validityTime = DateUtil.offsetMonth(date, 6);
                    break;
                case "0":
                    validityTime = DateUtil.parse("2099-12-31");
                    break;
                default:
                    throw new CustomException("未定义的密码过期规则");
            }
            userMapper.updateUserValidity(validityTime);
        }
        return res;
    }
}
