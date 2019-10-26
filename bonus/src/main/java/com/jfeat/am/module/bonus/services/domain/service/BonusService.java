package com.jfeat.am.module.bonus.services.domain.service;

import java.math.BigDecimal;

public interface BonusService {
  BigDecimal getTotalSelfBonus(Long paramLong);
  
  BigDecimal getSelfBonus(Long paramLong);
  
  BigDecimal getTeamBonus(Long paramLong);
}

