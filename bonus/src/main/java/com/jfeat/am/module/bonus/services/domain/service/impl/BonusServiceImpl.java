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
        if(allianceBonus==null){
            allianceBonus=new BigDecimal(0);
        }
        BigDecimal proportion = queryBonusDao.getAllianceOrTeamProportion("ALLIANCE");
        if(proportion==null){
            proportion=new BigDecimal(0);
        }
        BigDecimal alliance = allianceBonus.multiply(proportion);
        return alliance;
    }

    //获得团队的占比分红
    @Override
    public BigDecimal getTeamProportionBonus(Long id) {
        BigDecimal allianceBonus = queryBonusDao.getAllianceBonus(id);
        if(allianceBonus==null){
            allianceBonus=new BigDecimal(0);
        }
        BigDecimal team1 = queryBonusDao.getAllianceOrTeamProportion("TEAM");
        if(team1==null){
            team1=new BigDecimal(0);
        }
        BigDecimal team = allianceBonus.multiply(team1);
        BigDecimal inventoryAmount = queryBonusDao.getInventoryAmount(id);
        if(inventoryAmount==null){
            inventoryAmount=new BigDecimal(0);
        }
        BigDecimal teamInventoryAmount = queryBonusDao.getTeamInventoryAmount(id);
        if(teamInventoryAmount==null){
            teamInventoryAmount=new BigDecimal(0);
        }
        BigDecimal totalInventoryAmount = queryBonusDao.getTotalInventoryAmount();

        BigDecimal teamProportion = (teamInventoryAmount.add(inventoryAmount)).divide(totalInventoryAmount);

        BigDecimal bonus = team.multiply(teamProportion) ;
        return bonus;
    }

    //获得团队的奖励
    @Override
    public BigDecimal getTeamBonus(Long id) {
        BigDecimal teamBonus = queryBonusDao.getTeamBonus(id);
        if (teamBonus == null) {
            teamBonus=new BigDecimal(0);
        }
        return teamBonus;
    }
}

