package com.ntu.igts.enums;

public enum RoleEnum {

    ADMIN("ADMIN"), USER("USER"), ALL("ALL");
    private String value;

    private RoleEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static RoleEnum fromValue(String value) {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if (roleEnum.value.equals(value)) {
                return roleEnum;
            }
        }
        throw new IllegalArgumentException();
    }
}
