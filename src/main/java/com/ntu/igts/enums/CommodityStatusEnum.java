package com.ntu.igts.enums;

import com.ntu.igts.i18n.MessageKeys;

public enum CommodityStatusEnum {

    INFORMED(MessageKeys.STATUS_INFORMED), NORMAL(MessageKeys.STATUS_NORMAL);

    private String value;

    private CommodityStatusEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static CommodityStatusEnum fromValue(String value) {
        for (CommodityStatusEnum commodityStatusEnum : CommodityStatusEnum.values()) {
            if (commodityStatusEnum.value.equals(value)) {
                return commodityStatusEnum;
            }
        }
        throw new IllegalArgumentException();
    }

}
