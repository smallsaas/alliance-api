package com.jfeat.am.module.friend.services.gen.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Code Generator
 * @since 2019-10-14
 */
@TableName("t_moments_friend")
public class MomentsFriend extends Model<MomentsFriend> {

    @TableField(exist = false)
    private com.alibaba.fastjson.JSONObject extra;

    public com.alibaba.fastjson.JSONObject getExtra() {
        return extra;
    }
    public void setExtra(com.alibaba.fastjson.JSONObject extra) {
        this.extra = extra;
    }


    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	/**
	 * 用户id
	 */
//	@TableId(value="user_id", type= IdType.AUTO)
//	private Long userId;
    /**
     * 盟友id
     */
	@TableField("alliance_id")
	private Long allianceId;
    /**
     * 电话号码
     */
	@TableField("contact_phone")
	private String contactPhone;
    /**
     * 名字
     */
	private String name;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createTime;
    /**
     * webapp user_id
     */
	@TableField("alliance_user_id")
	private Long allianceUserId;
    /**
     * 省份
     */
	@TableField("pcd_province")
	private String pcdProvince;
    /**
     * 城市
     */
	@TableField("pcd_city")
	private String pcdCity;
    /**
     * distinct
     */
	@TableField("pcd_distinct")
	private String pcdDistinct;
    /**
     * 邮编
     */
	@TableField("post_code")
	private String postCode;
    /**
     * 地址
     */
	private String address;
    /**
     * 头像
     */
	private String avator;
    /**
     * 昵称
     */
	private String nick;
    /**
     * 备注
     */
	private String remark;


	public Long getId() {
		return id;
	}

	public MomentsFriend setId(Long id) {
		this.id = id;
		return this;
	}

//	public Long getUserId() {
//		return userId;
//	}
//
//	public void setUserId(Long userId) {
//		this.userId = userId;
//	}

	public Long getAllianceId() {
		return allianceId;
	}

	public MomentsFriend setAllianceId(Long allianceId) {
		this.allianceId = allianceId;
		return this;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public MomentsFriend setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
		return this;
	}

	public String getName() {
		return name;
	}

	public MomentsFriend setName(String name) {
		this.name = name;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public MomentsFriend setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Long getAllianceUserId() {
		return allianceUserId;
	}

	public MomentsFriend setAllianceUserId(Long allianceUserId) {
		this.allianceUserId = allianceUserId;
		return this;
	}

	public String getPcdProvince() {
		return pcdProvince;
	}

	public MomentsFriend setPcdProvince(String pcdProvince) {
		this.pcdProvince = pcdProvince;
		return this;
	}

	public String getPcdCity() {
		return pcdCity;
	}

	public MomentsFriend setPcdCity(String pcdCity) {
		this.pcdCity = pcdCity;
		return this;
	}

	public String getPcdDistinct() {
		return pcdDistinct;
	}

	public MomentsFriend setPcdDistinct(String pcdDistinct) {
		this.pcdDistinct = pcdDistinct;
		return this;
	}

	public String getPostCode() {
		return postCode;
	}

	public MomentsFriend setPostCode(String postCode) {
		this.postCode = postCode;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public MomentsFriend setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getAvator() {
		return avator;
	}

	public MomentsFriend setAvator(String avator) {
		this.avator = avator;
		return this;
	}

	public String getNick() {
		return nick;
	}

	public MomentsFriend setNick(String nick) {
		this.nick = nick;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public MomentsFriend setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public static final String ID = "id";

	public static final String ALLIANCE_ID = "alliance_id";

	public static final String CONTACT_PHONE = "contact_phone";

	public static final String NAME = "name";

	public static final String CREATE_TIME = "create_time";

	public static final String ALLIANCE_USER_ID = "alliance_user_id";

	public static final String PCD_PROVINCE = "pcd_province";

	public static final String PCD_CITY = "pcd_city";

	public static final String PCD_DISTINCT = "pcd_distinct";

	public static final String POST_CODE = "post_code";

	public static final String ADDRESS = "address";

	public static final String AVATOR = "avator";

	public static final String NICK = "nick";

	public static final String REMARK = "remark";

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MomentsFriend{" +
			"id=" + id +
			", allianceId=" + allianceId +
			", contactPhone=" + contactPhone +
			", name=" + name +
			", createTime=" + createTime +
			", allianceUserId=" + allianceUserId +
			", pcdProvince=" + pcdProvince +
			", pcdCity=" + pcdCity +
			", pcdDistinct=" + pcdDistinct +
			", postCode=" + postCode +
			", address=" + address +
			", avator=" + avator +
			", nick=" + nick +
			", remark=" + remark +
			"}";
	}
}
