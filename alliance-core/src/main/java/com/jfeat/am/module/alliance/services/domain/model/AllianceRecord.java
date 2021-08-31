package com.jfeat.am.module.alliance.services.domain.model;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jfeat.am.module.alliance.api.AllianceMessage;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Code Generator on 2019-10-14
 */
public class AllianceRecord extends Alliance {
    //团队人数
    Integer teamCount;
    String invitorName;

    String invitorPhone;

    String wechatAvatar;
    String wechatNick;
    private BigDecimal balance;

    JSONArray AllianceMessages;
    JSONArray TeamAllianceOrderMessages;
    JSONArray DeliverMessage;

    private BigDecimal bonus_balance;
    private BigDecimal expected_bonus;


    private BigDecimal settlementCommission;//可结算



    private BigDecimal noEffectiveCommission;//未生效



    private BigDecimal orderAmount;//入货额
    private BigDecimal effectiveCommission;//已生效
    private BigDecimal conditionOrderAmount;//差额条件

    public BigDecimal getConditionOrderAmount() {
        return conditionOrderAmount;
    }

    public void setConditionOrderAmount(BigDecimal conditionOrderAmount) {
        this.conditionOrderAmount = conditionOrderAmount;
    }

    private Integer experienceTime;//剩余体验天数

    public Integer getExperienceTime() {
        return experienceTime;
    }

    public void setExperienceTime(Integer experienceTime) {
        this.experienceTime = experienceTime;
    }

    public BigDecimal getSettlementCommission() {
        return settlementCommission;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setSettlementCommission(BigDecimal settlementCommission) {
        this.settlementCommission = settlementCommission;
    }

    public BigDecimal getEffectiveCommission() {
        return effectiveCommission;
    }

    public void setEffectiveCommission(BigDecimal effectiveCommission) {
        this.effectiveCommission = effectiveCommission;
    }

    public BigDecimal getNoEffectiveCommission() {
        return noEffectiveCommission;
    }

    public void setNoEffectiveCommission(BigDecimal noEffectiveCommission) {
        this.noEffectiveCommission = noEffectiveCommission;
    }

    private String dividedTime;

    public String getDividedTime() {
        return dividedTime;
    }

    public void setDividedTime(String dividedTime) {
        this.dividedTime = dividedTime;
    }

    public BigDecimal getBonus_balance() {
        return bonus_balance;
    }

    public void setBonus_balance(BigDecimal bonus_balance) {
        this.bonus_balance = bonus_balance;
    }

    public BigDecimal getExpected_bonus() {
        return expected_bonus;
    }

    public void setExpected_bonus(BigDecimal expected_bonus) {
        this.expected_bonus = expected_bonus;
    }

    public JSONArray getTeamAllianceOrderMessages() {
        return TeamAllianceOrderMessages;
    }

    public void setTeamAllianceOrderMessages(JSONArray teamAllianceOrderMessages) {
        TeamAllianceOrderMessages = teamAllianceOrderMessages;
    }

    public JSONArray getAllianceMessages() {
        return AllianceMessages;
    }

    public void setAllianceMessages(JSONArray allianceMessages) {
        AllianceMessages = allianceMessages;
    }


    public JSONArray getDeliverMessage() {
        return DeliverMessage;
    }

    public void setDeliverMessage(JSONArray deliverMessage) {
        DeliverMessage = deliverMessage;
    }

    //结算时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    Date cutOffTime;

    public Date getCutOffTime() {
        return cutOffTime;
    }

    public void setCutOffTime(Date cutOffTime) {
        this.cutOffTime = cutOffTime;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

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

    //


    //自己的总分红=自己的分红+自己团队的分红
    private BigDecimal totalSelfBonus;

    //自己团队分红
    private BigDecimal teamSelfBonus;

    //自己的分红
    private BigDecimal selfBonus;

    private Integer stockholderCount;//股东总人数

    private BigDecimal commissionBalance;//提成总数

    public BigDecimal getCommissionBalance() {
        return commissionBalance;
    }

    public void setCommissionBalance(BigDecimal commissionBalance) {
        this.commissionBalance = commissionBalance;
    }

    private float proportion;

    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    public Integer getStockholderCount() {
        return stockholderCount;
    }

    public void setStockholderCount(Integer stockholderCount) {
        this.stockholderCount = stockholderCount;
    }

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


    public String getWechatAvatar() {
        return wechatAvatar;
    }

    public void setWechatAvatar(String wechatAvatar) {
        this.wechatAvatar = wechatAvatar;
    }

    public String getWechatNick() {
        return wechatNick;
    }

    public void setWechatNick(String wechatNick) {
        this.wechatNick = wechatNick;
    }

    public String getInvitorName() {
        return invitorName;
    }

    public void setInvitorName(String invitorName) {
        this.invitorName = invitorName;
    }

    public String getInvitorPhone() {
        return invitorPhone;
    }

    public void setInvitorPhone(String invitorPhone) {
        this.invitorPhone = invitorPhone;
    }

    public Integer getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(Integer teamCount) {
        this.teamCount = teamCount;
    }
}
