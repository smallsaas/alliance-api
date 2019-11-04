package com.jfeat.am.module.alliance.services.domain.model;

import com.alibaba.fastjson.JSONArray;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;

import java.math.BigDecimal;

/**
 * Created by Code Generator on 2019-10-14
 */
public class AllianceRecord extends Alliance{

    Integer invitedCount;
    private JSONArray currentMonthOrder;
    private JSONArray selfProducts;
    private JSONArray allianceTeam;

    //提成
    private JSONArray commissionOrder;

    public JSONArray getCommissionOrder() {
        return commissionOrder;
    }

    public void setCommissionOrder(JSONArray commissionOrder) {
        this.commissionOrder = commissionOrder;
    }



    //自己的总分红=自己的分红+自己团队的分红
    private BigDecimal totalSelfBonus;

    //自己团队分红
    private BigDecimal teamSelfBonus;

    //自己的分红
    private BigDecimal selfBonus;

    public BigDecimal getTotalSelfBonus() {
        return totalSelfBonus;
    }

    public void setTotalSelfBonus(BigDecimal totalSelfBonus) {
        this.totalSelfBonus = totalSelfBonus;
    }

    public BigDecimal getTeamSelfBonus() {
        return teamSelfBonus;
    }

    public void setTeamSelfBonus(BigDecimal teamSelfBonus) {
        this.teamSelfBonus = teamSelfBonus;
    }

    public BigDecimal getSelfBonus() {
        return selfBonus;
    }

    public void setSelfBonus(BigDecimal selfBonus) {
        this.selfBonus = selfBonus;
    }

    public JSONArray getAllianceTeam() {
        return allianceTeam;
    }

    public void setAllianceTeam(JSONArray allianceTeam) {
        this.allianceTeam = allianceTeam;
    }

    public JSONArray getSelfProducts() {
        return selfProducts;
    }

    public void setSelfProducts(JSONArray selfProducts) {
        this.selfProducts = selfProducts;
    }

    public JSONArray getCurrentMonthOrder() {
        return currentMonthOrder;
    }

    public void setCurrentMonthOrder(JSONArray currentMonthOrder) {
        this.currentMonthOrder = currentMonthOrder;
    }


    public Integer getInvitedCount() {
        return invitedCount;
    }

    public void setInvitedCount(Integer invitedCount) {
        this.invitedCount = invitedCount;
    }
}
