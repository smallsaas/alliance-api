package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.jfeat.am.module.bonus.api.BonusDateType;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.filter.AllianceField;
import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;

import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import org.springframework.stereotype.Service;


@Service("bonusService")
public class BonusServiceImpl implements BonusService {
    @Resource
    QueryBonusDao queryBonusDao;


    //获得自己的分红 user_id=4 ---> 15.75 只是订单所得
    @Override
    public BigDecimal getSelfBonus(Long id, Integer dateType) {
        Integer allianceExist = queryBonusDao.queryAllianceExist(id);
        if (allianceExist == 0) {
            return new BigDecimal(0);
        }
        BigDecimal allianceBonus = queryBonusDao.getAllianceBonus(id, dateType);
        if (allianceBonus == null) {
            allianceBonus = new BigDecimal(0);
        }
        BigDecimal proportion = queryBonusDao.getAllianceOrTeamProportion(AllianceField.FIXED_PROPORTION);
        if (proportion == null) {
            proportion = new BigDecimal(0);
        }
        BigDecimal alliance = allianceBonus.multiply(proportion);
        if (alliance.compareTo(new BigDecimal(0)) == 0) {
            alliance = new BigDecimal(0);
        }
        return alliance.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    //获得团队贡献占比分红
    @Override
    public BigDecimal getTeamProportionBonus(Long id, Integer dateType) {
        Integer allianceExist = queryBonusDao.queryAllianceExist(id);
        if (allianceExist == 0) {
            return new BigDecimal(0);
        }
        BigDecimal allianceBonus = queryBonusDao.getAllianceBonus(id, dateType);
        if (allianceBonus == null) {
            allianceBonus = new BigDecimal(0);
        }
        BigDecimal team1 = queryBonusDao.getAllianceOrTeamProportion(AllianceField.RATIO_PROPORTION);
        if (team1 == null) {
            team1 = new BigDecimal(0);
        }

        //团队贡献比的分红 15.75
        BigDecimal team = allianceBonus.multiply(team1);
        //自己的入货额度
        BigDecimal inventoryAmount = queryBonusDao.getInventoryAmount(id);
        if (inventoryAmount == null) {
            inventoryAmount = new BigDecimal(0);
        }
        //团队的入货额度
        BigDecimal teamInventoryAmount = queryBonusDao.getTeamInventoryAmount(id);
        if (teamInventoryAmount == null) {
            teamInventoryAmount = new BigDecimal(0);
        }
        BigDecimal totalInventoryAmount = queryBonusDao.getTotalInventoryAmount();
        //自己团队的入货总额度
        BigDecimal add = teamInventoryAmount.add(inventoryAmount);
        if (totalInventoryAmount != null && totalInventoryAmount.compareTo(new BigDecimal(0.00)) != 0) {
            BigDecimal teamProportion = add.divide(totalInventoryAmount, 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal bonus = team.multiply(teamProportion);
            if (bonus.compareTo(new BigDecimal(0)) == 0) {
                bonus = new BigDecimal(0);
            }
            return bonus.setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            throw new BusinessException(BusinessCode.BadRequest, "数据库数据异常");
        }
    }

    //提成
    @Override
    public BigDecimal getTeamBonus(Long id, Integer dateType) {
        Integer allianceExist = queryBonusDao.queryAllianceExist(id);
        if (allianceExist == 0) {
            return new BigDecimal(0);
        }
//        if(queryBonusDao.queryType(id)!=AllianceField.ALLIANCE_TYPE_BONUS){
//            return new BigDecimal(0);
//        }
        List<Long> teamUserIds = queryBonusDao.getTeam(id);
        BigDecimal teamBonus = new BigDecimal(0);
        for (Long teamUserId : teamUserIds) {
            BigDecimal tmp = queryBonusDao.getTeamBonus(teamUserId, dateType);
            if (teamBonus == null) {
                teamBonus = new BigDecimal(0);
            }
            if (teamBonus.compareTo(new BigDecimal(0)) == 0) {
                teamBonus = new BigDecimal(0);
            }
            if (tmp != null) {
                teamBonus = teamBonus.add(tmp);
            }
        }
        return teamBonus.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public List<AllianceReconciliation> getAllianceReconciliation(Integer pageNum, Integer pageSize, String search) {
        List<AllianceReconciliation> allianceReconciliations = queryBonusDao.queryReInformation(search);
        if (allianceReconciliations != null && allianceReconciliations.size() > 0) {
            for (AllianceReconciliation r : allianceReconciliations) {
                r.setRoyalty(queryBonusDao.getCommissionTotal(r.getId()));
                if (r.getAllianceType() == AllianceField.ALLIANCE_TYPE_BONUS) {
                    BigDecimal month = this.getSelfBonus(r.getUserId(), BonusDateType.MONTH)
                            .add(this.getTeamProportionBonus(r.getUserId(), BonusDateType.MONTH));
                    r.setCurrentMonthBonus(month.setScale(2, BigDecimal.ROUND_HALF_UP));
                    BigDecimal year = this.getSelfBonus(r.getUserId(), null)
                            .add(this.getTeamProportionBonus(r.getUserId(), null));
                    r.setTotalBonus(year.setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    r.setCurrentMonthBonus(new BigDecimal(0.00));
                    r.setTotalBonus(new BigDecimal(0.00));
                }
            }
        }
        Collections.sort(allianceReconciliations);
        return allianceReconciliations;
    }
}


