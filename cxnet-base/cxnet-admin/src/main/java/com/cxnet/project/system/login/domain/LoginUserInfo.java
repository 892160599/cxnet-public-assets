package com.cxnet.project.system.login.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 登录信息接参实体
 *
 * @author Aowen
 * @date 2021/5/12
 */
@Data
public class LoginUserInfo {

    private String serverAddr;
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
    private String code;
    private String uuid;

}
