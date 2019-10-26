package com.jfeat.am.module.friend.api;

import java.math.BigDecimal;

public class RequestOrder {
    //价格
    private BigDecimal totalPrice;
    //商品条码
    private String barcode;
    //数量
    private Integer quantity;
    //最终单价
    private BigDecimal finalPrice;
    //收货电话
    private String phone;
    //收货地址
    private String detail;
    //收货人姓名
    private String name;
    //下单人
    private String byId;
    //商品名称
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getById() {
        return byId;
    }

    public void setById(String byId) {
        this.byId = byId;
    }
}
