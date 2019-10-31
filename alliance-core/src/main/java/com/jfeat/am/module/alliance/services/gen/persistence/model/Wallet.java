package com.jfeat.am.module.alliance.services.gen.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("t_wallet")
public class Wallet extends Model<Wallet> {
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private Long userId;
    @TableField("accumulative_amount")
    private BigDecimal accumulativeAmount;

    @TableField("balance")
    private BigDecimal balance;

    @TableField("gift_balance")
    private BigDecimal giftBalance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getGiftBalance() {
        return giftBalance;
    }

    public void setGiftBalance(BigDecimal giftBalance) {
        this.giftBalance = giftBalance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAccumulativeAmount() {
        return accumulativeAmount;
    }

    public void setAccumulativeAmount(BigDecimal accumulativeAmount) {
        this.accumulativeAmount = accumulativeAmount;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
