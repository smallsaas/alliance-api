package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.AmApplication;
import com.jfeat.am.module.alliance.api.RechargeType;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.gen.persistence.dao.AllianceMapper;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import com.jfeat.am.module.bonus.api.BonusStatus;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jfeat.am.module.alliance.api.AllianceType.ALLIANCE_TYPE_DIVIDEND;


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

    @Resource
    QueryOwnerBalanceDao queryOwnerBalanceDao;

    protected final static Logger logger = LoggerFactory.getLogger(AmApplication.class);


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
            res += settlementAlliance(id,true);
        }
        return res;
    }

    @Override
    @Transactional
    public Integer settlementAlliance(Long id,Boolean isBatch){
        Integer res = 0;
        Alliance alliance = allianceMapper.selectById(id);


        Integer dateType = null;
        Long userId;
        if(alliance.getUserId() == null && !isBatch){
            throw new BusinessException(BusinessCode.CRUD_GENERAL_ERROR,"该盟友没有被绑定，无法进行结算");
        }else if(!BonusStatus.NOT_SETTLEMENT.equals(alliance.getBonusSettlement()) && !isBatch){
            throw new BusinessException(BusinessCode.CRUD_GENERAL_ERROR,"该盟友已结算");
        }
        else
            //确保 未结算 才执行   且用户id不为空
            if(BonusStatus.NOT_SETTLEMENT.equals(alliance.getBonusSettlement()) && alliance.getUserId() != null){
            userId = alliance.getUserId();

         //如果是分红盟友才价钱
         if(alliance.getAllianceType().equals(ALLIANCE_TYPE_DIVIDEND)){


        /** 分红计算 **/
        BigDecimal averageBonus = queryBonusDao.getAverageBonus();
        BigDecimal allBonusRatio = settlementCenterService.getRatioBonus(userId);
        BigDecimal selfBonus = averageBonus.setScale(2, BigDecimal.ROUND_HALF_UP);  //平均分红
        BigDecimal  teamSelfBonus = allBonusRatio.setScale(2, BigDecimal.ROUND_HALF_UP); //占比分红
        BigDecimal balance = averageBonus.add(allBonusRatio).setScale(2, BigDecimal.ROUND_HALF_UP);  //总收益
        logger.info("Balance: {} ",balance);
        /****/

        //提成钱包 增加数据
        OwnerBalance queryOwnerBalance = new OwnerBalance();
        queryOwnerBalance.setUserId(userId);
        OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(new LambdaQueryWrapper<>(queryOwnerBalance));
        if(ownerBalance!=null){
            BigDecimal selfBalance = ownerBalance.getBalance();
            selfBalance = selfBalance.add(balance);
            logger.info("selfBalance: {} ",selfBalance);
            ownerBalance.setBalance(selfBalance);
            res +=queryOwnerBalanceDao.updateAllColumnById(ownerBalance);
        }else{
            //为空 设置金钱
            queryOwnerBalance.setBalance(balance);
            res +=queryOwnerBalanceDao.insert(queryOwnerBalance);
        }

        //res +=createWalletRecord(alliance.getUserId(),selfBonus,"分红结算-平均分红");
        //res +=createWalletRecord(alliance.getUserId(),teamSelfBonus,"分红结算-动态分红");

        };

        //设为已结算
        alliance.setBonusSettlement(BonusStatus.SETTLEMENT_END);
        allianceMapper.updateAllColumnById(alliance);
        }


        return res;
    }

    //钱包加钱 同时加记录
    Integer createWalletRecord(Long userId,BigDecimal balance,String note){
        Wallet wallet = null ;
        List<Wallet> wallets = queryWalletDao.selectList(new QueryWrapper<Wallet>().eq(Wallet.USER_ID, userId));
        Integer res = 0;
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
            wallet.setUserId(userId);
            wallet.setBalance(balance);
            wallet.setAccumulativeAmount(balance);
            res += queryWalletDao.insert(wallet);
        }
        //判断空 再查询一次获取id
        if(wallet.getId()==null){
            List<Wallet> walletList = queryWalletDao.selectList(new LambdaQueryWrapper<>().eq(Wallet.USER_ID,userId));
            wallet =  walletList.get(0);
        }
        res += createWalletHistory(wallet,balance,note);

        return res;
    }

    //钱包提取记录
    Integer createWalletHistory(Wallet wallet,BigDecimal balance,String note){
        Integer res = 0;
        WalletHistory walletHistory = new WalletHistory();
        walletHistory.setWalletId(wallet.getId());
        walletHistory.setType(RechargeType.RECHARGE);
        walletHistory.setNote(note);
        walletHistory.setAmount(balance);
        walletHistory.setBalance(wallet.getBalance());
        walletHistory.setCreatedTime(new Date());
        res += queryWalletHistoryDao.insert(walletHistory);
        return res;
    }

}


