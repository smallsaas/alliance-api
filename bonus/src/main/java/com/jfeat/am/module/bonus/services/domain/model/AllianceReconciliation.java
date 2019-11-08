package com.jfeat.am.module.bonus.services.domain.model;

import java.math.BigDecimal;

public class AllianceReconciliation implements Comparable<AllianceReconciliation>{
    private Long id;
    private String allianceName;
    private Integer teamCount;
    private BigDecimal totalBonus;
    private BigDecimal currentMonthBonus;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAllianceName() {
        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    public Integer getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(Integer teamCount) {
        this.teamCount = teamCount;
    }

    public BigDecimal getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }

    public BigDecimal getCurrentMonthBonus() {
        return currentMonthBonus;
    }

    public void setCurrentMonthBonus(BigDecimal currentMonthBonus) {
        this.currentMonthBonus = currentMonthBonus;
    }

    @Override
    public int compareTo(AllianceReconciliation o) {
        return o.getTotalBonus().compareTo(this.getTotalBonus());
    }
}
