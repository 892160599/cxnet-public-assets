package com.cxnet.project.system.login.service;

import com.cxnet.project.system.login.mapper.SysLoginMapper;
import com.cxnet.project.system.user.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户 业务层处理
 *
 * @author cxnet
 */
@Service
public class UserLoginServiceIImpl implements UserLoginServiceI {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired(required = false)
    private SysLoginMapper loginMapper;


    /**
     * 根据用户账号查询用户信息
     *
     * @param userName 用户账号
     * @return 用户信息
     */
    @Override
    public SysUser selectUserInfoByUserName(String userName) {
        SysUser user = loginMapper.selectUserInfoByUserName(userName);
        return user;
    }


}
