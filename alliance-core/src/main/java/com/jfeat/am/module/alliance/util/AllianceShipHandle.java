package com.jfeat.am.module.alliance.util;

import com.jfeat.am.module.alliance.api.AllianceShips;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

//临时盟友过期处理
@Component
public class AllianceShipHandle {
    @Resource
    QueryAllianceDao queryAllianceDao;

    @Scheduled(cron = "0 0 0 * * ?")
    public void task() {
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
