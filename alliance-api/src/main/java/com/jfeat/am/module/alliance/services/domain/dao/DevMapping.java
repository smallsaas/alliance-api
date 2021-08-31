package com.jfeat.am.module.alliance.services.domain.dao;

public interface DevMapping {
    //根据type删除订单
    Integer deleteOrder(String type);
    Integer deleteOrderService(String type);
    Integer deleteOrderServiceItem(String type);
    //退货 - 订单
    Integer deleteOrderRefunds();
    Integer deleteOrderRefundsService();
    Integer deleteOrderRefundsServiceItem();
    //提现额
    Integer deleteOwnerBalance();
    //线下提现记录
    Integer deleteOfflineWithdrawal();
}
