package com.ntu.igts.enums;

import org.springframework.data.domain.Sort.Direction;

public enum OrderByEnum {

    ASC("ASC"), DESC("DESC");

    private String value;

    private OrderByEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static OrderByEnum fromValue(String value) {
        for (OrderByEnum orderByEnum : OrderByEnum.values()) {
            if (orderByEnum.value.equals(value)) {
                return orderByEnum;
            }
        }
        throw new IllegalArgumentException();
    }

    public Direction toDirectionEnum() {
        if ("ASC".equals(value)) {
            return Direction.ASC;
        } else {
            return Direction.DESC;
        }
    }
}
