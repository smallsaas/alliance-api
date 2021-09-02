package com.jfeat.am.module.alliance.api;

public class RequestAlliance {
    private String alliancePhone;
    private String allianceName;
    private String invitationCode;

    public String getAlliancePhone() {
        return alliancePhone;
    }

    public void setAlliancePhone(String alliancePhone) {
        this.alliancePhone = alliancePhone;
    }

    public String getAllianceName() {
        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }
}
