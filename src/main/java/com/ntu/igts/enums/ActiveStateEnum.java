package com.ntu.igts.enums;

public enum ActiveStateEnum {

    ACTIVE("Y"), NEGATIVE("N");

    private String value;

    private ActiveStateEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ActiveStateEnum fromValue(String value) {
        for (ActiveStateEnum activeStateEnum : ActiveStateEnum.values()) {
            if (activeStateEnum.value.equals(value)) {
                return activeStateEnum;
            }
        }
        throw new IllegalArgumentException();
    }
}
