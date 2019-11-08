package com.jfeat.am.module.bonus.services.domain.service;

import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;

import java.math.BigDecimal;
import java.util.List;

public interface BonusService {
  //获得自己的分红
  BigDecimal getSelfBonus(Long paramLong, Integer dateType);

  //获得自己的团队分红比
  BigDecimal getTeamProportionBonus(Long paramLong, Integer dateType);

  //获得自己的团队奖励
  BigDecimal getTeamBonus(Long paramLong, Integer dateType);

  List<AllianceReconciliation> getAllianceReconciliation(Integer pageNum, Integer pageSize);
}

