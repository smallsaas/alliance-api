package com.jfeat.am.module.alliance.services.domain.service.impl;

import com.jfeat.am.module.alliance.services.domain.dao.DevMapping;
import com.jfeat.am.module.alliance.services.domain.service.DevService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class DevServiceImpl implements DevService {

    @Resource
    DevMapping devMapping;

    @Transactional
    @Override
    public Integer deleteOrder(String type) {
        Integer i = 0;
        i += devMapping.deleteOrderServiceItem(type);
        i += devMapping.deleteOrderService(type);
        i += devMapping.deleteOrder(type);

        return i;
    }

    @Transactional
    @Override
    public Integer deleteOrderRefunds() {
        Integer i = 0;
        i += devMapping.deleteOrderRefundsServiceItem();
        i += devMapping.deleteOrderRefundsService();
        i += devMapping.deleteOrderRefunds();
        return i;
    }

    @Transactional
    @Override
    public Integer deleteOwnerBalance() {
        Integer i = 0;
        i += devMapping.deleteOwnerBalance();

        return i;
    }

    @Transactional
    @Override
    public Integer deleteOfflineWithdrawal(){
        Integer i = 0;
        i += devMapping.deleteOfflineWithdrawal();

        return i;
    }

}
