package com.ntu.igts.enums;

import com.ntu.igts.i18n.MessageKeys;

public enum IndentStatusEnum {

    UNPAID(MessageKeys.STATUS_UNPAID), // Not paid
    PAID(MessageKeys.STATUS_PAID), // Just paid
    DELIVERED(MessageKeys.STATUS_DELIVERED), // Already delivered
    RETURNING(MessageKeys.STATUS_RETURNING), // Return goods in progress
    COMPLETE(MessageKeys.STATUS_COMPLETE), // Deal complete
    CANCELLED(MessageKeys.STATUS_CANCELLED); // The indent is cancelled

    private String value;

    private IndentStatusEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static IndentStatusEnum fromValue(String value) {
        for (IndentStatusEnum indentStatusEnum : IndentStatusEnum.values()) {
            if (indentStatusEnum.value.equals(value)) {
                return indentStatusEnum;
            }
        }
        throw new IllegalArgumentException();
    }
}
