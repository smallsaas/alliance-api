package com.jfeat.am.module.alliance.services.domain.model;

import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;

import java.math.BigDecimal;

public class OwnerBalanceRecord extends OwnerBalance {

    private String userName;

    private BigDecimal money;

    private BigDecimal expected_bonus;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getExpected_bonus() {
        return expected_bonus;
    }

    public void setExpected_bonus(BigDecimal expected_bonus) {
        this.expected_bonus = expected_bonus;
    }
}
