package com.jfeat.am.module.alliance.services.domain.dao;

public interface DevMapping {

    Integer deleteOrder(String type);
    Integer deleteOrderService(String type);
    Integer deleteOrderServiceItem(String type);
    Integer deleteOrderRefunds();
    Integer deleteOrderRefundsService();
    Integer deleteOrderRefundsServiceItem();
    Integer deleteOwnerBalance();
}
