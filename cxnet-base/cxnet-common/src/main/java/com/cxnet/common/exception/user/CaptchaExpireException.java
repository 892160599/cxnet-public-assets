package com.cxnet.common.exception.user;

/**
 * 验证码失效异常类
 *
 * @author cxnet
 */
public class CaptchaExpireException extends UserException {
    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}
