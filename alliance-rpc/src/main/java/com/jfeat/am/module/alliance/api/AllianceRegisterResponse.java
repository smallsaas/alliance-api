package com.jfeat.am.module.alliance.api;

public class AllianceRegisterResponse {
    public  static String COMMON_ALLIANCE_FIELD = "common_alliance";  // 分红盟友配置项
    public  static String BONUS_ALLIANCE_FIELD = "bonus_alliance";  // 分红盟友配置项

    private int allianceType;

    public int getAllianceType() {
        return allianceType;
    }

    public void setAllianceType(int allianceType) {
        this.allianceType = allianceType;
    }
}
