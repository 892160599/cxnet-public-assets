package com.cxnet.common.exception.user;

/**
 * 验证码错误异常类
 *
 * @author cxnet
 */
public class CaptchaException extends UserException {
    public CaptchaException() {
        super("user.jcaptcha.error", null);
    }
}
