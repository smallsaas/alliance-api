package com.jfeat.am.module.alliance.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class AllianceRequestShipTime {

    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date allianceShipTime;

    public Date getAllianceShipTime() {
        return allianceShipTime;
    }

    public void setAllianceShipTime(Date allianceShipTime) {
        this.allianceShipTime = allianceShipTime;
    }
}
