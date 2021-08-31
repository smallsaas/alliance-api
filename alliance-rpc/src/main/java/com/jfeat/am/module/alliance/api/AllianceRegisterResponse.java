package com.jfeat.am.module.alliance.api;

public class AllianceRegisterResponse {

    public  static final String COMMON_ALLIANCE_FIELD = "common_alliance";  // 分红盟友配置项
    public  static final String BONUS_ALLIANCE_FIELD = "bonus_alliance";  // 分红盟友配置项

    public static final int ALLIANCE_SHIP_OK = 0;        // 确定是盟友 （已确认）
    public static final int ALLIANCE_SHIP_NO = 1;        // 不是盟友
    public static final int ALLIANCE_SHIP_INVITED = 2;   // 盟友申请中 （待支付）
    public static final int ALLIANCE_SHIP_PAID = 3;      // 盟友已支付 (等待绑定）
    public static final int ALLIANCE_SHIP_EXPIRED = 4;   // 支付过期 （支付过期）可改为已支付
    public static final int ALLIANCE_SHIP_ERROR = 5;     // 盟友状态错误

    private int allianceType;

    public int getAllianceType() {
        return allianceType;
    }

    public void setAllianceType(int allianceType) {
        this.allianceType = allianceType;
    }
}
