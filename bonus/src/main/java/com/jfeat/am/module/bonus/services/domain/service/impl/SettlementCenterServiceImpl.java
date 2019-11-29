package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("settlementCenterService")
public class SettlementCenterServiceImpl implements SettlementCenterService {
    @Resource
    QueryBonusDao queryBonusDao;
    @Override
    public void settlementOrder(Long orderId) {

    }
}
