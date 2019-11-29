package com.jfeat.am.module.friend.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    //下单人id
    private Long userId;
    //商品名称
    private String productName;
    //商品id
    private Long productId;
    //支付类型
    private String paymentType;
    //下单时间
    private Date createDate;

    //省
    private String province;
    //市
    private String city;
    //区
    private String district;

    //产品 用于接收前端返回的数据 用于存入放入orderItem中
    private List<RequestProduct> items;

    private List<friendImage> imges;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<friendImage> getImges() {
        return imges;
    }

    public void setImges(List<friendImage> imges) {
        this.imges = imges;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public List<RequestProduct> getItems() {
        return items;
    }

    public void setItems(List<RequestProduct> items) {
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

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
