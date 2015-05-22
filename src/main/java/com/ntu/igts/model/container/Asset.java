package com.ntu.igts.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("asset")
public class Asset {

    @JsonProperty("money")
    private double money;
    @JsonProperty("lockedmoney")
    private double lockedMoney;
    @JsonProperty("userid")
    private String userId;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getLockedMoney() {
        return lockedMoney;
    }

    public void setLockedMoney(double lockedMoney) {
        this.lockedMoney = lockedMoney;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
