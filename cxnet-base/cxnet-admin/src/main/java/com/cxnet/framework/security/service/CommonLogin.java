package com.cxnet.framework.security.service;

import com.cxnet.framework.security.service.logindomain.LoginDomain;
import com.cxnet.project.system.parameter.domain.SysParameter;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.Map;

public interface CommonLogin {
    /**
     * 登陆
     *
     * @return
     */
    Map<String, Object> login(LoginDomain loginDomain);

    /**
     * 判断密码类型
     *
     * @param pwd 密码
     * @return 密码强度是否过低，true 过低、false 正常
     */
    boolean pwdType(String pwd, SysParameter sysParameter);


    /**
     * 验证密码过期控制规则
     *
     * @param validityType 密码过期控制规则类型  密码是否过期，0仅提醒，1强制修改
     * @param validity     密码有效期
     * @return
     */
    Map<String, Object> pwdValidity(String validityType, Date validity);

    /**
     * 登陆超时
     *
     * @param authentication
     * @param username
     * @return
     */
    Map<String, Object> loginOverTime(Authentication authentication, String username, String type);

    /**
     * 用户验证
     *
     * @param authentication
     * @param username       用户名
     * @param password       密码
     * @param sysParameter   系统参数
     * @param loginNum
     * @return
     */
    Map<String, Object> authentication(Authentication authentication, String username, String password, SysParameter sysParameter, Integer loginNum);
}
