package com.jfeat.am.module.alliance.services.gen.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@TableName("t_wallet_history")
public class WalletHistory extends Model<WalletHistory> {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    @TableField("wallet_id")
    private Long walletId;
    @TableField("created_time")
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss")
    @DateTimeFormat(pattern = "YYYY-MM-DD hh:mm:ss")
    private Date createdTime;
    @TableField("amount")
    private BigDecimal amount;
    @TableField("gift_amount")
    private BigDecimal gift_amount;
    @TableField("balance")
    private BigDecimal balance;
    @TableField("gift_balance")
    private BigDecimal gift_balance;
    @TableField("type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getGift_amount() {
        return gift_amount;
    }

    public void setGift_amount(BigDecimal gift_amount) {
        this.gift_amount = gift_amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getGift_balance() {
        return gift_balance;
    }

    public void setGift_balance(BigDecimal gift_balance) {
        this.gift_balance = gift_balance;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
