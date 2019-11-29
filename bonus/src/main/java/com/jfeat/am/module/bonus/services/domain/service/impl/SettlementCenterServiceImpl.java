package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.jfeat.am.module.alliance.api.AllianceFields;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.model.OrderCommissionInfo;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("settlementCenterService")
public class SettlementCenterServiceImpl implements SettlementCenterService {
    private Integer OK = 1;//结算成功
    private Integer NOT_OK = 2;//结算失败

    @Resource
    QueryBonusDao queryBonusDao;
    @Resource
    QueryOwnerBalanceDao queryOwnerBalanceDao;
    @Resource
    ConfigFieldService configFieldService;

    @Override
    @Transactional
    public boolean settlementOrder(Long orderId) {
        OrderCommissionInfo orderCommissionInfo = queryBonusDao.queryEveryOrderCommission(orderId);
        if (orderCommissionInfo != null) {
            Long invitorUserId = queryBonusDao.queryInvitorUserId(orderCommissionInfo.getUserId());
            if (invitorUserId != null) {
                BigDecimal condition = queryBonusDao.queryOrderAmount(invitorUserId).subtract(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_WITHDRAWAL_CONDITIONS)));
                if (condition.compareTo(new BigDecimal(0.00)) >= 0) {
                    OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(new OwnerBalance().setUserId(invitorUserId));
                    if (ownerBalance != null) {
                        BigDecimal bonus_balance = ownerBalance.getBalance();
                        if (bonus_balance == null) {
                            bonus_balance = new BigDecimal(0.00);
                        }
                        List<OrderCommissionInfo> orderCommissionInfos = queryBonusDao.queryFormerOrder(invitorUserId, orderCommissionInfo.getCreateTime());
                        if (orderCommissionInfos != null && orderCommissionInfos.size() > 0) {
                            for (OrderCommissionInfo item : orderCommissionInfos) {
                                if(!item.getId().equals(orderCommissionInfo.getId()))
                                bonus_balance.add(item.getCommission());
                            }
                            queryBonusDao.upOrderSettlementStatus(OK, orderCommissionInfo.getId());
                        }
                        ownerBalance.setBalance(bonus_balance.add(orderCommissionInfo.getCommission()));
                        queryBonusDao.upOrderSettlementStatus(OK, orderCommissionInfo.getId());
                        queryOwnerBalanceDao.updateById(ownerBalance);
                    } else {
                        ownerBalance = new OwnerBalance();
                        ownerBalance.setUserId(invitorUserId);
                        List<OrderCommissionInfo> orderCommissionInfos = queryBonusDao.queryFormerOrder(invitorUserId, orderCommissionInfo.getCreateTime());
                        BigDecimal bonus_balance = new BigDecimal(0.00);
                        if (orderCommissionInfos != null && orderCommissionInfos.size() > 0) {
                            for (OrderCommissionInfo item : orderCommissionInfos) {
                                if(!item.getId().equals(orderCommissionInfo.getId()))
                                bonus_balance.add(item.getCommission());
                            }
                            queryBonusDao.upOrderSettlementStatus(OK, orderCommissionInfo.getId());
                        }
                        ownerBalance.setBalance(bonus_balance.add(orderCommissionInfo.getCommission()));
                        queryBonusDao.upOrderSettlementStatus(OK, orderCommissionInfo.getId());
                        queryOwnerBalanceDao.insert(ownerBalance);
                    }
                } else {
                    queryBonusDao.upOrderSettlementStatus(NOT_OK, orderCommissionInfo.getId());
                }


            } else {
                queryBonusDao.upOrderSettlementStatus(NOT_OK, orderCommissionInfo.getId());
            }
        }
        return true;
    }
}