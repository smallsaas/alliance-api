package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;

import java.math.BigDecimal;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;


@Service("bonusService")
public class BonusServiceImpl implements BonusService {
    @Resource
    QueryBonusDao queryBonusDao;


    //获得自己的分红比例
    @Override
    public BigDecimal getSelfBonus(Long id) {
        BigDecimal allianceBonus = queryBonusDao.getAllianceBonus(id);
        BigDecimal alliance = allianceBonus.multiply(queryBonusDao.getAllianceOrTeamProportion("ALLIANCE"));
        return alliance;
    }

    //获得团队的占比分红
    @Override
    public BigDecimal getTeamProportionBonus(Long id) {
        BigDecimal allianceBonus = queryBonusDao.getAllianceBonus(id);
        BigDecimal team = allianceBonus.multiply(queryBonusDao.getAllianceOrTeamProportion("TEAM"));
        BigDecimal teamProportion = (queryBonusDao.getTeamInventoryAmount(id).add(queryBonusDao.getInventoryAmount(id))).divide(queryBonusDao.getTotalInventoryAmount());
        BigDecimal bonus = team.multiply(teamProportion) ;
        return bonus;
    }

    //获得团队的奖励
    @Override
    public BigDecimal getTeamBonus(Long id) {
        return queryBonusDao.getTeamBonus(id);
    }
}

