package com.jfeat.am.module.bonus.services.domain.service;

import java.math.BigDecimal;

public interface SettlementCenterService {
    public boolean settlementOrder(Long orderId);

    public boolean cancelSettlementOrder(Long orderId);

    /// 动态分红占比
    public BigDecimal getRatioBonus(Long userId);

    /// 动态分红占比百分比
    public BigDecimal getRatioBonusPercent(Long userId);


    public BigDecimal getRatioBonusMonth(Long userId);
}
