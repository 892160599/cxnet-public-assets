package com.cxnet.project.system.login.service;

import com.cxnet.project.system.user.domain.SysUser;

/**
 * 用户 业务层
 *
 * @author cxnet
 */
public interface UserLoginServiceI {

    /**
     * 根据用户账号查询用户信息
     *
     * @param userName 用户账号
     * @return 用户信息
     */
    public SysUser selectUserInfoByUserName(String userName);

}
