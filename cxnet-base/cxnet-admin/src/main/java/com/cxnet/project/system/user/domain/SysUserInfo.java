package com.cxnet.project.system.user.domain;

import lombok.Data;

/**
 * 登录，修改密码接参实体
 *
 * @author Aowen
 * @date 2021/5/12
 */
@Data
public class SysUserInfo {

    private String userName;
    private String nickName;
    private String oldPassword;
    private String newPassword;
    private String code;
    private String uuid;

}
