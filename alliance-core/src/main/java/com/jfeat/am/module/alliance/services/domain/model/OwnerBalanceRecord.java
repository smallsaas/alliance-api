package com.jfeat.am.module.alliance.services.domain.model;

import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;

import java.math.BigDecimal;

public class OwnerBalanceRecord extends OwnerBalance {

    private BigDecimal expected_bonus;

    public BigDecimal getExpected_bonus() {
        return expected_bonus;
    }

    public void setExpected_bonus(BigDecimal expected_bonus) {
        this.expected_bonus = expected_bonus;
    }
}
