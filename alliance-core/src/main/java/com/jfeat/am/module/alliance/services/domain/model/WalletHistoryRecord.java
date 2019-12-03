package com.jfeat.am.module.alliance.services.domain.model;

import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;

public class WalletHistoryRecord extends WalletHistory {
       String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
