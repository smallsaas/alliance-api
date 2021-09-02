package com.jfeat.util;

import com.jfeat.am.module.alliance.api.AllianceShips;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

//计算分红
@Component
@EnableScheduling
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

    @Scheduled(cron = "0 0 1 1 * ?")//每月1号1点
    public void task() {
        List<Long> ids = queryBonusDao.queryOrderId();
        if(ids!=null&&ids.size()>0){
            for(Long id:ids){
                settlementCenterService.settlementOrder(id);
            }
        }
    }

    //凌晨
    @Scheduled(cron = "0 0 0 * * ?")//每天0点0分0秒
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

        // 计算所有分红盟友总订单
       // Integer i = settlementCenterService.setTotal();



    }
   // @Scheduled(cron = "0/5 * * * * ?")//秒分时日月周  0/5 从0秒开始 每5秒执行一次   0-5 区间 0,1,2,3,4,5
  /*  public void task3() {
        List<Long> ids = queryBonusDao.queryOrderId();
        if(ids!=null&&ids.size()>0){
            for(Long id:ids){
                System.out.println(id);
                //settlementCenterService.settlementOrder(id);
            }
        }
    }*/

}