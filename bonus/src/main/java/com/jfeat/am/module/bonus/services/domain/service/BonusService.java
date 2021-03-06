package com.jfeat.am.module.bonus.services.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface BonusService {
  //获得自己的分红
  BigDecimal getSelfBonus(Long paramLong, Integer dateType);

  //获得自己的团队分红比
  BigDecimal getTeamProportionBonus(Long paramLong, Integer dateType);

  //获得自己的团队奖励
  BigDecimal getTeamBonus(Long paramLong, Integer dateType);

  List<AllianceReconciliation> getAllianceReconciliation(Page<AllianceReconciliation> page, String search);

    Integer settlementAllicanceBatch(List<Long> ids);


    @Transactional
    Integer settlementAlliance(Long id, Boolean isBatch);
}

