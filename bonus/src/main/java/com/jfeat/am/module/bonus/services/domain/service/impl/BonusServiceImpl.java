package com.jfeat.am.module.bonus.services.domain.service.impl;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import java.math.BigDecimal;
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
        BigDecimal allianceBonus = queryBonusDao.getAllianceBonus(id,dateType);
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

    //获得团队贡献占比分红
    @Override
    public BigDecimal getTeamProportionBonus(Long id, Integer dateType) {
        BigDecimal allianceBonus = queryBonusDao.getAllianceBonus(id,dateType);
        if(allianceBonus==null){
            allianceBonus=new BigDecimal(0);
        }
        BigDecimal team1 = queryBonusDao.getAllianceOrTeamProportion("TEAM");
        if(team1==null){
            team1=new BigDecimal(0);
        }

        //团队贡献比的分红 15.75
        BigDecimal team = allianceBonus.multiply(team1);
        //自己的入货额度
        BigDecimal inventoryAmount = queryBonusDao.getInventoryAmount(id);
        if(inventoryAmount==null){
            inventoryAmount=new BigDecimal(0);
        }
        //团队的入货额度
        BigDecimal teamInventoryAmount = queryBonusDao.getTeamInventoryAmount(id);
        if(teamInventoryAmount==null){
            teamInventoryAmount=new BigDecimal(0);
        }
        BigDecimal totalInventoryAmount = queryBonusDao.getTotalInventoryAmount();
        //自己团队的入货总额度
        BigDecimal add = teamInventoryAmount.add(inventoryAmount);
        if(totalInventoryAmount!=null&&totalInventoryAmount.compareTo(new BigDecimal(0.00))!=0){
            BigDecimal teamProportion = add.divide(totalInventoryAmount,2,BigDecimal.ROUND_HALF_UP);
            BigDecimal bonus = team.multiply(teamProportion) ;
            return bonus;
        }else {
            throw new BusinessException(BusinessCode.BadRequest,"数据库数据异常");
        }
    }

    //团队的奖励有问题
    @Override
    public BigDecimal getTeamBonus(Long id, Integer dateType) {
        List<Long> teamUserIds = queryBonusDao.getTeam(id);
        for(Long teamUserId:teamUserIds){

        }
        BigDecimal teamBonus = queryBonusDao.getTeamBonus(id,dateType);
        if (teamBonus == null) {
            teamBonus=new BigDecimal(0);
        }
        return teamBonus;
    }
}

