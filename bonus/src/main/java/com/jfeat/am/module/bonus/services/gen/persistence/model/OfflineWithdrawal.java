package com.jfeat.am.module.bonus.services.gen.persistence.model;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Code Generator
 * @since 2019-12-03
 */
@TableName("t_offline_withdrawal")
public class OfflineWithdrawal extends Model<OfflineWithdrawal> {
    @TableField(exist = false)
    private String allianceName;

    public String getAllianceName() {

        return allianceName;
    }

    public void setAllianceName(String allianceName) {
        this.allianceName = allianceName;
    }

    @TableField(exist = false)
    private com.alibaba.fastjson.JSONObject extra;

    public com.alibaba.fastjson.JSONObject getExtra() {
        return extra;
    }

    public void setExtra(com.alibaba.fastjson.JSONObject extra) {
        this.extra = extra;
    }

    private static final long serialVersionUID = 1L;
    private Long id;
    private BigDecimal balance;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer status;
    @TableField("user_id")
    private Long userId;
    private String note;


    public Long getId() {
        return id;
    }

    public OfflineWithdrawal setId(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public OfflineWithdrawal setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public OfflineWithdrawal setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public OfflineWithdrawal setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public OfflineWithdrawal setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getNote() {
        return note;
    }

    public OfflineWithdrawal setNote(String note) {
        this.note = note;
        return this;
    }

    public static final String ID = "id";

    public static final String BALANCE = "balance";

    public static final String CREATE_TIME = "create_time";

    public static final String STATUS = "status";

    public static final String USER_ID = "user_id";

    public static final String NOTE = "note";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OfflineWithdrawal{" +
                "id=" + id +
                ", balance=" + balance +
                ", createTime=" + createTime +
                ", status=" + status +
                ", userId=" + userId +
                ", note=" + note +
                "}";
    }
}
