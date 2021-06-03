package com.cxnet.framework.security.service.logindomain;

import lombok.Data;

@Data
public class LoginDomain {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 验证码
     */
    private String code;
    /**
     * uuid
     */
    private String uuid;

    private Integer loginNum;

    public LoginDomain() {
    }

    public LoginDomain(String username, String password, String code, String uuid) {
        this.username = username;
        this.password = password;
        this.code = code;
        this.uuid = uuid;
    }
}
