package com.jfeat.am.module.friend.api;

public class OrderStatus {
    public final static String CREATED_PAY_PENDING = "待支付";
    public final static String CLOSED_PAY_TIMEOUT ="支付超时关闭";
    public final static String CLOSED_CANCELED ="已取消";
    public final static String PAID_CONFIRM_PENDING ="已支付";
    public final static String CONFIRMED_DELIVER_PENDING ="待发货";
    public final static String DELIVERING ="发货中";
    public final static String DELIVERED_CONFIRM_PENDING="已发货";
    public final static String CANCELED_RETURN_PENDING ="待退货";
    public final static String CLOSED_CONFIRMED ="已确认收货";
    public final static String CANCELED_REFUND_PENDING ="待退款";
    public final static String CLOSED_REFUNDED ="已退款";
    public final static String CONFIRMED_PICK_PENDING ="待取货";

}
