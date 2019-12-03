package com.jfeat.am.module.alliance.services.gen.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Code Generator
 * @since 2019-11-21
 */
@TableName("t_wallet_history")
public class WalletHistory extends Model<WalletHistory> {

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
	private Integer id;
	@TableField("wallet_id")
	private Long walletId;
	@TableField("created_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;
    /**
     */
	private String type;
    /**
     * 发生金额
     */
	private BigDecimal amount;
    /**
     * 赠�?�储值，充�?�时�?
     */
	@TableField("gift_amount")
	private BigDecimal giftAmount;
    /**
     * 实际余额
     */
	private BigDecimal balance;
    /**
     * 赠�?�余�?
     */
	@TableField("gift_balance")
	private BigDecimal giftBalance;
	private String note;


	public Integer getId() {
		return id;
	}

	public WalletHistory setId(Integer id) {
		this.id = id;
		return this;
	}

	public Long getWalletId() {
		return walletId;
	}

	public WalletHistory setWalletId(Long walletId) {
		this.walletId = walletId;
		return this;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public WalletHistory setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
		return this;
	}

	public String getType() {
		return type;
	}

	public WalletHistory setType(String type) {
		this.type = type;
		return this;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public WalletHistory setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public BigDecimal getGiftAmount() {
		return giftAmount;
	}

	public WalletHistory setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
		return this;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public WalletHistory setBalance(BigDecimal balance) {
		this.balance = balance;
		return this;
	}

	public BigDecimal getGiftBalance() {
		return giftBalance;
	}

	public WalletHistory setGiftBalance(BigDecimal giftBalance) {
		this.giftBalance = giftBalance;
		return this;
	}

	public String getNote() {
		return note;
	}

	public WalletHistory setNote(String note) {
		this.note = note;
		return this;
	}

	public static final String ID = "id";

	public static final String WALLET_ID = "wallet_id";

	public static final String CREATED_TIME = "created_time";

	public static final String TYPE = "type";

	public static final String AMOUNT = "amount";

	public static final String GIFT_AMOUNT = "gift_amount";

	public static final String BALANCE = "balance";

	public static final String GIFT_BALANCE = "gift_balance";

	public static final String NOTE = "note";

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "WalletHistory{" +
			"id=" + id +
			", walletId=" + walletId +
			", createdTime=" + createdTime +
			", type=" + type +
			", amount=" + amount +
			", giftAmount=" + giftAmount +
			", balance=" + balance +
			", giftBalance=" + giftBalance +
			", note=" + note +
			"}";
	}
}
