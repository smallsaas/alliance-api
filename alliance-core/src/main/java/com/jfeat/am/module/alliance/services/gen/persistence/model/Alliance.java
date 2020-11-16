package com.jfeat.am.module.alliance.services.gen.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Code Generator
 * @since 2019-10-14
 */
@TableName("t_alliance")
public class Alliance extends Model<Alliance> {

    public static int ALLIANCE_TYPE_COMMON = 2;  // 普通盟友
    public static int ALLIANCE_TYPE_BONUS = 1;  // 分红盟友

    @TableField(exist = false)
    private com.alibaba.fastjson.JSONObject extra;

    public com.alibaba.fastjson.JSONObject getExtra() {
        return extra;
    }

    public void setExtra(com.alibaba.fastjson.JSONObject extra) {
        this.extra = extra;
    }


    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    @TableField("alliance_rank")
    private Integer allianceRank;

    @TableField("historical_balance")
    private BigDecimal historicalBalance;

    public BigDecimal getHistoricalBalance() {
        return historicalBalance;
    }

    public void setHistoricalBalance(BigDecimal historicalBalance) {
        this.historicalBalance = historicalBalance;
    }


    /**
     *
     */
    @TableField("invitor_alliance_id")
    private Long invitorAllianceId;
    /**
     *
     */
    @TableField("alliance_ship")
    private Integer allianceShip;
    /**
     *
     */
    @TableField("stockholder_ship")
    private Integer stockholderShip;

    @TableField("alliance_type")
    private Integer allianceType;
    /**
     * creation_time
     */
    @TableField("creation_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private Date creationTime;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    @TableField("alliance_ship_time")
    private Date allianceShipTime;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("starting_cycle")
    private Date startingCycle;
    /**
     *
     */
    @TableField("stockholder_ship_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private Date stockholderShipTime;
    /**
     * 临时盟友过期时间
     */
    @TableField("temp_alliance_expiry_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private Date tempAllianceExpiryTime;
    /**
     *
     */
    @TableField("alliance_status")
    private Integer allianceStatus;

    private Integer age;
    /**
     * 库存金额
     */
    @TableField("alliance_inventory_amount")
    private BigDecimal allianceInventoryAmount;
    /**
     * 去年结余
     */
//    @TableField("balance")
//    private BigDecimal balance;
    /**
     * 积分
     */
    @TableField("alliance_point")
    private BigDecimal alliancePoint;
    /**
     * 盟友姓名
     */
    @TableField("alliance_name")
    private String allianceName;
    /**
     * 性别
     */
    @TableField("alliance_sex")
    private Integer allianceSex;
    /**
     * 职业
     */
    @TableField("alliance_occupation")
    private String allianceOccupation;
    /**
     * 行业
     */
    @TableField("alliance_industry")
    private String allianceIndustry;
    /**
     *
     */
    @TableField("alliance_address")
    private String allianceAddress;
    /**
     * 头像
     */
    @TableField("avator")
    private String avator;
    /**
     * 业务方向
     */
    @TableField("alliance_business")
    private String allianceBusiness;
    /**
     * 兴趣爱好
     */
    @TableField("alliance_hobby")
    private String allianceHobby;

    @TableField("role")
    private String role;
    /**
     * 电话
     */
    @TableField("alliance_phone")
    private String alliancePhone;
    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("alliance_dob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date allianceDob;

    /**
     * 0 未结算
     * 1 已结算
     * */
    @TableField("bonus_settlement")
    private Integer bonusSettlement;

    public Integer getBonusSettlement() {
        return bonusSettlement;
    }

    public void setBonusSettlement(Integer bonusSettlement) {
        this.bonusSettlement = bonusSettlement;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public Alliance setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getAllianceType() {
        return allianceType;
    }

    public void setAllianceType(Integer allianceType) {
        this.allianceType = allianceType;
    }

    public Long getUserId() {
        return userId;
    }

    public Alliance setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public Date getStartingCycle() {
        return startingCycle;
    }

    public void setStartingCycle(Date startingCycle) {
        this.startingCycle = startingCycle;
    }

//    public BigDecimal getBalance() {
//        return balance;
//    }
//
//    public void setBalance(BigDecimal balance) {
//        this.balance = balance;
//    }

    public Integer getAllianceRank() {
        return allianceRank;
    }

    public void setAllianceRank(Integer allianceRank) {
        this.allianceRank = allianceRank;
    }

    public Long getInvitorAllianceId() {
        return invitorAllianceId;
    }

    public Alliance setInvitorAllianceId(Long invitorAllianceId) {
        this.invitorAllianceId = invitorAllianceId;
        return this;
    }

    public Integer getAllianceShip() {
        return allianceShip;
    }

    public Alliance setAllianceShip(Integer allianceShip) {
        this.allianceShip = allianceShip;
        return this;
    }

    public Integer getStockholderShip() {
        return stockholderShip;
    }

    public Alliance setStockholderShip(Integer stockholderShip) {
        this.stockholderShip = stockholderShip;
        return this;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Alliance setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public Date getAllianceShipTime() {
        return allianceShipTime;
    }

    public Alliance setAllianceShipTime(Date allianceShipTime) {
        this.allianceShipTime = allianceShipTime;
        return this;
    }

    public Date getStockholderShipTime() {
        return stockholderShipTime;
    }

    public Alliance setStockholderShipTime(Date stockholderShipTime) {
        this.stockholderShipTime = stockholderShipTime;
        return this;
    }

    public Date getTempAllianceExpiryTime() {
        return tempAllianceExpiryTime;
    }

    public Alliance setTempAllianceExpiryTime(Date tempAllianceExpiryTime) {
        this.tempAllianceExpiryTime = tempAllianceExpiryTime;
        return this;
    }

    public Integer getAllianceStatus() {
        return allianceStatus;
    }

    public Alliance setAllianceStatus(Integer allianceStatus) {
        this.allianceStatus = allianceStatus;
        return this;
    }

    public BigDecimal getAllianceInventoryAmount() {
        return allianceInventoryAmount;
    }

    public Alliance setAllianceInventoryAmount(BigDecimal allianceInventoryAmount) {
        this.allianceInventoryAmount = allianceInventoryAmount;
        return this;
    }

    public BigDecimal getAlliancePoint() {
        return alliancePoint;
    }

    public Alliance setAlliancePoint(BigDecimal alliancePoint) {
        this.alliancePoint = alliancePoint;
        return this;
    }

    public String getAllianceName() {
        return allianceName;
    }

    public Alliance setAllianceName(String allianceName) {
        this.allianceName = allianceName;
        return this;
    }

    public Integer getAllianceSex() {
        return allianceSex;
    }

    public Alliance setAllianceSex(Integer allianceSex) {
        this.allianceSex = allianceSex;
        return this;
    }

    public String getAllianceOccupation() {
        return allianceOccupation;
    }

    public Alliance setAllianceOccupation(String allianceOccupation) {
        this.allianceOccupation = allianceOccupation;
        return this;
    }

    public String getAllianceIndustry() {
        return allianceIndustry;
    }

    public Alliance setAllianceIndustry(String allianceIndustry) {
        this.allianceIndustry = allianceIndustry;
        return this;
    }

    public String getAllianceAddress() {
        return allianceAddress;
    }

    public Alliance setAllianceAddress(String allianceAddress) {
        this.allianceAddress = allianceAddress;
        return this;
    }

    public String getAllianceBusiness() {
        return allianceBusiness;
    }

    public Alliance setAllianceBusiness(String allianceBusiness) {
        this.allianceBusiness = allianceBusiness;
        return this;
    }

    public String getAllianceHobby() {
        return allianceHobby;
    }

    public Alliance setAllianceHobby(String allianceHobby) {
        this.allianceHobby = allianceHobby;
        return this;
    }

    public String getAlliancePhone() {
        return alliancePhone;
    }

    public Alliance setAlliancePhone(String alliancePhone) {
        this.alliancePhone = alliancePhone;
        return this;
    }

    public Date getAllianceDob() {
        return allianceDob;
    }

    public Alliance setAllianceDob(Date allianceDob) {
        this.allianceDob = allianceDob;
        return this;
    }

    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String INVITOR_ALLIANCE_ID = "invitor_alliance_id";

    public static final String ALLIANCE_SHIP = "alliance_ship";

    public static final String STOCKHOLDER_SHIP = "stockholder_ship";

    public static final String CREATION_TIME = "creation_time";

    public static final String ALLIANCE_SHIP_TIME = "alliance_ship_time";

    public static final String STOCKHOLDER_SHIP_TIME = "stockholder_ship_time";

    public static final String TEMP_ALLIANCE_EXPIRY_TIME = "temp_alliance_expiry_time";

    public static final String ALLIANCE_STATUS = "alliance_status";

    public static final String ALLIANCE_INVENTORY_AMOUNT = "alliance_inventory_amount";

    public static final String ALLIANCE_POINT = "alliance_point";

    public static final String ALLIANCE_NAME = "alliance_name";

    public static final String ALLIANCE_SEX = "alliance_sex";

    public static final String ALLIANCE_OCCUPATION = "alliance_occupation";

    public static final String ALLIANCE_INDUSTRY = "alliance_industry";

    public static final String ALLIANCE_ADDRESS = "alliance_address";

    public static final String ALLIANCE_BUSINESS = "alliance_business";

    public static final String ALLIANCE_HOBBY = "alliance_hobby";

    public static final String ALLIANCE_PHONE = "alliance_phone";

    public static final String ALLIANCE_DOB = "alliance_dob";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Alliance{" +
                "id=" + id +
                ", userId=" + userId +
                ", invitorAllianceId=" + invitorAllianceId +
                ", allianceShip=" + allianceShip +
                ", stockholderShip=" + stockholderShip +
                ", creationTime=" + creationTime +
                ", allianceShipTime=" + allianceShipTime +
                ", stockholderShipTime=" + stockholderShipTime +
                ", tempAllianceExpiryTime=" + tempAllianceExpiryTime +
                ", allianceStatus=" + allianceStatus +
                ", allianceInventoryAmount=" + allianceInventoryAmount +
                ", alliancePoint=" + alliancePoint +
                ", allianceName=" + allianceName +
                ", allianceSex=" + allianceSex +
                ", allianceOccupation=" + allianceOccupation +
                ", allianceIndustry=" + allianceIndustry +
                ", allianceAddress=" + allianceAddress +
                ", allianceBusiness=" + allianceBusiness +
                ", allianceHobby=" + allianceHobby +
                ", alliancePhone=" + alliancePhone +
                ", allianceDob=" + allianceDob +
                "}";
    }
}
