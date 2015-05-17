package com.ntu.igts.model.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("account")
public class AccountContainer {

    @JsonProperty("totalmoney")
    private double totalMoney;
    @JsonProperty("lockedmoney")
    private double lockedMoney;

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getLockedMoney() {
        return lockedMoney;
    }

    public void setLockedMoney(double lockedMoney) {
        this.lockedMoney = lockedMoney;
    }

}
