package com.cxnet.common.enums;

public enum AnnexType {
    CHECK("资产验收", "0"),
    REGISTER("资产登记", "1"),
    COLLECT("资产领用", "2"),
    SURRENDER("资产交回", "3"),
    TRANSFER("资产移交", "4"),
    REPAIR("资产报修", "5");


    private String name;

    private String value;

    private AnnexType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getName(String value) {
        for (AnnexType c : AnnexType.values()) {
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
