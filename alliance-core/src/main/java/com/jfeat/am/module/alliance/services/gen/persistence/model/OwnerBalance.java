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
 * @since 2019-11-26
 */
@TableName("t_owner_balance")
public class OwnerBalance extends Model<OwnerBalance> {

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
	@TableField("user_id")
	private Long userId;
	@TableField("balance")
	private BigDecimal balance;

	private Integer version;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public OwnerBalance setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getVersion() {
		return version;
	}

	public OwnerBalance setVersion(Integer version) {
		this.version = version;
		return this;
	}

	public static final String ID = "id";

	public static final String USER_ID = "user_id";

	public static final String BALANCE = "balance";

	public static final String VERSION = "version";

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "OwnerBalance{" +
			"id=" + id +
			", userId=" + userId +
			", balance=" + balance +
			", version=" + version +
			"}";
	}
}
