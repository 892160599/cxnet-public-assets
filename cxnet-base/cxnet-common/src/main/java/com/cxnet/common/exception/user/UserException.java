package com.cxnet.common.exception.user;

import com.cxnet.common.exception.BaseException;

/**
 * 用户信息异常类
 *
 * @author cxnet
 */
public class UserException extends BaseException {
    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
