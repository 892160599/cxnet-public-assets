package com.cxnet.common.enums;

public enum AssertType {

    FIXED("固定资产", "1"),
    INTANGIBLE("无形资产", "2"),
    PUBLIC("公共资产", "9");


    private String name;

    private String value;

    private AssertType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getName(String value) {
        for (AssertType c : AssertType.values()) {
            if (c.getValue().equals(value)) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
