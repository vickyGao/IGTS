package com.ntu.igts.enums;

public enum PayTypeEnum {

    DEFAULT("默认支付方式");

    private String value;

    private PayTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static PayTypeEnum fromValue(String value) {
        for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
            if (payTypeEnum.value.equals(value)) {
                return payTypeEnum;
            }
        }
        throw new IllegalArgumentException();
    }
}
