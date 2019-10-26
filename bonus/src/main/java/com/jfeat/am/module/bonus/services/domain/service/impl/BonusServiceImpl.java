package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;


@Service("bonusService")
public class BonusServiceImpl
        implements BonusService {
    @Resource
    QueryBonusDao queryBonusDao;

    public BigDecimal getTotalSelfBonus(Long id) {
        return getSelfBonus(id).add(getTeamBonus(id).multiply(new BigDecimal(0.4D)));
    }


    /* 30 */
    public BigDecimal getSelfBonus(Long id) {
        return this.queryBonusDao.getSelfBonusByUserId(id);
    }


    public BigDecimal getTeamBonus(Long id) {
        /* 35 */
        List<Long> inviters = this.queryBonusDao.getInviters(id);
        /* 36 */
        BigDecimal teamBonus = new BigDecimal(0);
        /* 37 */
        if (inviters != null && inviters.size() > 0) {
            /* 38 */
            for (Long inviter : inviters) {
                /* 39 */
                if (inviter != null && inviter.longValue() > 0L) {
                    /* 40 */
                    BigDecimal inviterBonus = this.queryBonusDao.getSelfBonusByUserId(inviter);
                    /* 41 */
                    if (inviterBonus != null && inviterBonus.compareTo(new BigDecimal(0)) != 0) {
                        /* 42 */
                        teamBonus.add(inviterBonus);
                    }
                }
            }
        }

        /* 48 */
        return teamBonus;
    }
}


/* Location:              C:\Users\39250\Desktop\新建文件夹\alliance-api-1.0.0-standalone.jar!\BOOT-INF\lib\bonus-1.0.0.jar!\com\jfeat\am\module\bonus\services\domain\service\impl\BonusServiceImpl.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.0.7
 */