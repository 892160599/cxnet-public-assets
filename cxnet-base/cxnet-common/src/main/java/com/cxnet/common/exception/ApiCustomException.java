package com.cxnet.common.exception;

/**
 * 自定义api异常
 *
 * @author cxnet
 */
public class ApiCustomException extends RuntimeException {
    private String result;

    private String message;

    public ApiCustomException(String message) {
        this.message = message;
    }

    public ApiCustomException(String message, String result) {
        this.message = message;
        this.result = result;
    }

    public ApiCustomException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getResult() {
        return result;
    }
}
