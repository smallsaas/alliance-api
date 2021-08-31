package com.jfeat.am.module.alliance.api;

public class AllianceMessage {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setTeamMessage(String name) {
        this.message = "恭喜您；团队加入新盟友\""+name+"\"";
    }
    public void setTeamAllianceOrderMessage(String name,String description) {
        this.message = "恭喜您的团队 \""+name+"\""+"下单了"+"\""+description+"\"";
    }
    public void setDeliverMessage(String orderName) {
        this.message = "您的订单 \""+orderName+"\""+"已发货";
    }
}
