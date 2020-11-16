package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.module.alliance.api.RechargeType;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.gen.persistence.dao.AllianceMapper;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import com.jfeat.am.module.bonus.api.BonusDateType;
import com.jfeat.am.module.bonus.api.BonusError;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.filter.AllianceField;
import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import org.springframework.stereotype.Service;


@Service("bonusService")
public class BonusServiceImpl implements BonusService {
    @Resource
    QueryBonusDao queryBonusDao;
    @Resource
    AllianceMapper allianceMapper;
    @Resource
    QueryWalletDao queryWalletDao;

    @Resource
    QueryWalletHistoryDao queryWalletHistoryDao;

    @Resource
    SettlementCenterService settlementCenterService;
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

    //盟友结算
    @Override
    public List<AllianceReconciliation> getAllianceReconciliation(Page<AllianceReconciliation> page, String search) {
        //查询正式盟友的信息
        List<AllianceReconciliation> allianceReconciliations = queryBonusDao.queryReInformation(page,search);
        if (allianceReconciliations != null && allianceReconciliations.size() > 0) {
            for (AllianceReconciliation r : allianceReconciliations) {
                BigDecimal commissionTotal = queryBonusDao.getCommissionTotalMonth(r.getUserId());
                if(commissionTotal==null){
                    commissionTotal=new BigDecimal(0.00);
                }
                r.setRoyalty(commissionTotal);
                //如果为分红盟友
                if (r.getAllianceType() == AllianceField.ALLIANCE_TYPE_BONUS) {
                    BigDecimal averageBonusMonth = queryBonusDao.getAverageBonusMonth();
                    BigDecimal allBonusRatioMonth = settlementCenterService.getRatioBonusMonth(r.getUserId());
                    if(averageBonusMonth==null){
                        averageBonusMonth=new BigDecimal(0.00);
                    }
                    if(allBonusRatioMonth==null){
                        allBonusRatioMonth=new BigDecimal(0.00);
                    }
                    BigDecimal month = allBonusRatioMonth.add(averageBonusMonth);

                    r.setCurrentMonthBonus(month.setScale(2, BigDecimal.ROUND_HALF_UP));

                    BigDecimal averageBonus = queryBonusDao.getAverageBonus();
                    BigDecimal allBonusRatio = settlementCenterService.getRatioBonus(r.getUserId());
                    if(averageBonus==null){
                        averageBonus=new BigDecimal(0.00);
                    }
                    if(allBonusRatio==null){
                        allBonusRatio=new BigDecimal(0.00);
                    }
                    BigDecimal year = allBonusRatio.add(averageBonus);
                    r.setTotalBonus(year.setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    r.setCurrentMonthBonus(new BigDecimal(0.00));
                    r.setTotalBonus(new BigDecimal(0.00));
                }
            }
        }
        Collections.sort(allianceReconciliations);
        List<AllianceReconciliation> currentPageList=new ArrayList<>();
        return allianceReconciliations;
    }

    @Override
    public Integer settlementAllicanceBatch(List<Long> ids){
        Integer res = 0;
        for (Long id : ids){
            res += settlementAlliance(id);
        }
        return res;
    }

    @Override
    public Integer settlementAlliance(Long id){
        Integer res = 0;
        Alliance alliance = allianceMapper.selectById(id);
        //dateType--->1当天，2当月，3当季，null 算总的
        Integer dateType = null;
        Long userId;
        if(alliance.getUserId() == null){
            throw new BusinessException(BusinessCode.CRUD_GENERAL_ERROR,"该盟友没有被绑定，无法进行结算");
        }else{
            userId = alliance.getUserId();
        }
        Wallet wallet = null ;
        List<Wallet> wallets = queryWalletDao.selectList(new Condition().eq(Wallet.USER_ID, alliance.getUserId()));


        /****/
        Integer allianceExist = queryBonusDao.queryAllianceExist(userId);
        if(allianceExist==0){
            throw new BusinessException(BusinessCode.BadRequest, BonusError.ALLIANCE_NOT_EXIST);
        }
        BigDecimal selfBonus = this.getSelfBonus(userId,dateType).add(this.getTeamProportionBonus(userId,dateType)).add(this.getTeamBonus(userId,dateType));
        selfBonus = selfBonus.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal balance = selfBonus;
        /****/



        //设置钱包逻辑
        if(wallets!=null && wallets.size()>0){
            wallet = wallets.get(0);
            BigDecimal balance1 = wallet.getBalance();
            if (balance1 == null) {
                balance1 = new BigDecimal(0.00);
            }
            wallet.setBalance(balance1.add(balance));
            BigDecimal accumulativeAmount = wallet.getAccumulativeAmount();
            if (accumulativeAmount == null) {
                accumulativeAmount = new BigDecimal(0.00);
            }
            wallet.setAccumulativeAmount(accumulativeAmount.add(balance));
            res += queryWalletDao.updateById(wallet);
        }else{
            wallet = new Wallet();
            wallet.setUserId(alliance.getUserId());
            wallet.setBalance(balance);
            wallet.setAccumulativeAmount(balance);
            res += queryWalletDao.insert(wallet);
        }

        WalletHistory walletHistory = new WalletHistory();
        walletHistory.setWalletId(wallet.getId());
        walletHistory.setType(RechargeType.RECHARGE);
        walletHistory.setNote("分红结算-充值");
        walletHistory.setAmount(balance);
        walletHistory.setBalance(wallet.getBalance());
        walletHistory.setCreatedTime(new Date());
        res += queryWalletHistoryDao.insert(walletHistory);

        return res;
    }

}


