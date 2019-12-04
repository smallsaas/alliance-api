package com.jfeat.am.module.alliance.services.domain.model;

import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;

import java.math.BigDecimal;

public class WalletHistoryRecord extends WalletHistory {
       String userName;
       BigDecimal ownBlance;

    public BigDecimal getOwnBlance() {
        return ownBlance;
    }

    public void setOwnBlance(BigDecimal ownBlance) {
        this.ownBlance = ownBlance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
