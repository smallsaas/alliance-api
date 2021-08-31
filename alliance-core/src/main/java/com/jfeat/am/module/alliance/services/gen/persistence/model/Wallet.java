package com.jfeat.am.module.alliance.services.gen.persistence.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Code Generator
 * @since 2019-11-05
 */
@TableName("t_wallet")
public class Wallet extends Model<Wallet> {

    @TableField(exist = false)
    private com.alibaba.fastjson.JSONObject extra;

    public com.alibaba.fastjson.JSONObject getExtra() {
        return extra;
    }
    public void setExtra(com.alibaba.fastjson.JSONObject extra) {
        this.extra = extra;
    }


    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	@TableField("user_id")
	private Long userId;
    /**
     */
	@TableField("accumulative_amount")
	private BigDecimal accumulativeAmount;
    /**
     */
	@TableField("accumulative_gift_amount")
	private BigDecimal accumulativeGiftAmount;
    /**
     */
	private BigDecimal balance;
    /**
     */
	@TableField("gift_balance")
	private BigDecimal giftBalance;
    /**
     */
	private String password;
    /**
     */
	private String salt;


	public Long getId() {
		return id;
	}

	public Wallet setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public Wallet setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public BigDecimal getAccumulativeAmount() {
		return accumulativeAmount;
	}

	public Wallet setAccumulativeAmount(BigDecimal accumulativeAmount) {
		this.accumulativeAmount = accumulativeAmount;
		return this;
	}

	public BigDecimal getAccumulativeGiftAmount() {
		return accumulativeGiftAmount;
	}

	public Wallet setAccumulativeGiftAmount(BigDecimal accumulativeGiftAmount) {
		this.accumulativeGiftAmount = accumulativeGiftAmount;
		return this;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Wallet setBalance(BigDecimal balance) {
		this.balance = balance;
		return this;
	}

	public BigDecimal getGiftBalance() {
		return giftBalance;
	}

	public Wallet setGiftBalance(BigDecimal giftBalance) {
		this.giftBalance = giftBalance;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public Wallet setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getSalt() {
		return salt;
	}

	public Wallet setSalt(String salt) {
		this.salt = salt;
		return this;
	}

	public static final String ID = "id";

	public static final String USER_ID = "user_id";

	public static final String ACCUMULATIVE_AMOUNT = "accumulative_amount";

	public static final String ACCUMULATIVE_GIFT_AMOUNT = "accumulative_gift_amount";

	public static final String BALANCE = "balance";

	public static final String GIFT_BALANCE = "gift_balance";

	public static final String PASSWORD = "password";

	public static final String SALT = "salt";

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Wallet{" +
			"id=" + id +
			", userId=" + userId +
			", accumulativeAmount=" + accumulativeAmount +
			", accumulativeGiftAmount=" + accumulativeGiftAmount +
			", balance=" + balance +
			", giftBalance=" + giftBalance +
			", password=" + password +
			", salt=" + salt +
			"}";
	}
}
