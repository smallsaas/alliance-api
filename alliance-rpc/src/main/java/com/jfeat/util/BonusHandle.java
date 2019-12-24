package com.jfeat.util;

import com.jfeat.am.module.alliance.api.AllianceFields;
import com.jfeat.am.module.alliance.api.AllianceShips;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//计算分红
@Component
public class BonusHandle {
    @Resource
    QueryAllianceDao queryAllianceDao;

    @Resource
    QueryBonusDao queryBonusDao;
    @Resource
    QueryOwnerBalanceDao queryOwnerBalanceDao;
    @Resource
    BonusService bonusService;
    @Resource
    ConfigFieldService configFieldService;
    @Resource
    SettlementCenterService settlementCenterService;

    @Scheduled(cron = "0 0 1 1 * ?")//凌晨一点？
    public void task() {
//        BigDecimal config = new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_WITHDRAWAL_CONDITIONS));
//        List<AllianceReconciliation> allianceReconciliations = queryBonusDao.queryReInformation(null);
//        for(AllianceReconciliation r:allianceReconciliations){
//            BigDecimal add = queryBonusDao.getCommissionOrderTotalLastMonth(r.getUserId());
//            if(add==null){
//                add=new BigDecimal(0.00);
//            }
//            int condition = add.subtract(config).compareTo(new BigDecimal(0.00));
//            if(condition>=0){
//                OwnerBalance theownerBalance =  new OwnerBalance();
//                theownerBalance.setUserId(r.getUserId());
//                OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(theownerBalance);
//                if(ownerBalance==null){
//                    ownerBalance=new OwnerBalance();
//                    ownerBalance.setUserId(r.getUserId());
//                    ownerBalance.setBalance(add);
//
//                    queryOwnerBalanceDao.insert(ownerBalance);
//                }else {
//                    BigDecimal bonus_balance = ownerBalance.getBalance();
//                    if(bonus_balance!=null){
//                        BigDecimal add1 = bonus_balance.add(add);
//                        ownerBalance.setBalance(add1);
//                    }
//                    queryOwnerBalanceDao.updateById(ownerBalance);
//                }
//            }
//        }
        List<Long> ids = queryBonusDao.queryOrderId();
        if(ids!=null&&ids.size()>0){
            for(Long id:ids){
                settlementCenterService.settlementOrder(id);
            }
        }
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void task2() {
        List<Alliance> alliances = queryAllianceDao.selectList(null);
        if (alliances != null && alliances.size() > 0) {
            for (Alliance alliance : alliances) {
                if (alliance.getTempAllianceExpiryTime().before(new Date()) && alliance.getAllianceShip() == AllianceShips.ALLIANCE_SHIP_INVITED) {
                    alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_EXPIRED);
                    queryAllianceDao.updateById(alliance);
                }
            }
        }
    }
}