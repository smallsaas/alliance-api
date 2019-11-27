package com.jfeat.am.module.alliance.services.gen.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

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
	private Long id;
	@TableField("user_id")
	private Long userId;
	@TableField("balance")
	private BigDecimal bonus_balance;
	private Integer version;


	public Long getId() {
		return id;
	}

	public OwnerBalance setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public OwnerBalance setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public BigDecimal getBonus_balance() {
		return bonus_balance;
	}

	public OwnerBalance setBonus_balance(BigDecimal bonus_balance) {
		this.bonus_balance = bonus_balance;
		return this;
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
			", balance=" + bonus_balance +
			", version=" + version +
			"}";
	}
}