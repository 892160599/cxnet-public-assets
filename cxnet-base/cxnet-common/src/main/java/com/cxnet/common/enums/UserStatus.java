package com.cxnet.common.enums;

/**
 * 用户状态
 *
 * @author cxnet
 */
public enum UserStatus {

    /**
     * 类型
     */
    OK("0", "正常"), DISABLE("1", "停用"), DELETED("2", "删除"), LOCK("3", "锁定");

    private final String code;
    private final String info;

    UserStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
