package com.jfeat.am.module.alliance.api;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class RequestBonus {
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date date;
    BigDecimal discovery;

    public BigDecimal getDiscovery() {
        return discovery;
    }

    public void setDiscovery(BigDecimal discovery) {
        this.discovery = discovery;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
