package com.jfeat.am.module.bonus.services.domain.service;

import java.math.BigDecimal;

public interface SettlementCenterService {
    public boolean settlementOrder(Long orderId);

    public BigDecimal getRatioBonus(Long userId);
    public BigDecimal getRatioBonusMonth(Long userId);
}
