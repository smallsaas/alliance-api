package com.jfeat.am.module.alliance.services.domain.service;

import org.springframework.transaction.annotation.Transactional;

public interface DevService {

    public Integer deleteOrder(String type);

    @Transactional
    Integer deleteOrderRefunds();

    @Transactional
    Integer deleteOwnerBalance();

    Integer deleteOfflineWithdrawal();
}
