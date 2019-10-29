package com.jfeat.am.module.bonus.services.domain.service;

import java.math.BigDecimal;

public interface BonusService {
  //获得自己的分红
  BigDecimal getSelfBonus(Long paramLong);

  //获得自己的团队分红比
  BigDecimal getTeamProportionBonus(Long paramLong);

  //获得自己的团队奖励
  BigDecimal getTeamBonus(Long paramLong);
}

