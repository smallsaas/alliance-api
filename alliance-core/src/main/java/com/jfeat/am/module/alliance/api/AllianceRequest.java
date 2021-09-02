package com.jfeat.am.module.alliance.api;

import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;

public class AllianceRequest extends Alliance {
    private String invitorPhoneNumber;

    public String getInvitorPhoneNumber() {
        return invitorPhoneNumber;
    }

    public void setInvitorPhoneNumber(String phoneNumber) {
        this.invitorPhoneNumber = phoneNumber;
    }
}
