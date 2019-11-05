package com.jfeat.am.module.user.services.gen.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author Code Generator
 * @since 2019-11-05
 */
@TableName("t_user")
public class User extends Model<User> {

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
	@TableField("login_name")
	private String loginName;
	private String name;
	@TableField("real_name")
	private String realName;
	private String password;
	private String email;
	private String phone;
	private String weixin;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField("register_date")
	private Date registerDate;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField("last_login_date")
	private Date lastLoginDate;
	private String salt;
	private String status;
	@TableField("token_salt")
	private String tokenSalt;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField("token_expired_date")
	private Date tokenExpiredDate;
	@TableField("invitation_code")
	private String invitationCode;
	@TableField("inviter_id")
	private Integer inviterId;

	private String avatar;
	private Integer sex;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date birthday;
	private String details;
	private String uid;
	private Integer followed;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField("follow_time")
	private Date followTime;
	@TableField("invitation_qrcode_url")
	private String invitationQrcodeUrl;
	@TableField("invitation_qrcode")
	private String invitationQrcode;
	@TableField("wx_unionid")
	private String wxUnionid;
	@TableField("wxa_openid")
	private String wxaOpenid;
	@TableField("wxapp_openid")
	private String wxappOpenid;
	@TableField("app_user")
	private Integer appUser;
	@TableField("wechat_name")
	private String wechatName;
	private String grade;
    /**
     * 员工联系电话
     */
	@TableField("contact_phone")
	private String contactPhone;
    /**
     * 员工联系微信
     */
	@TableField("contact_wx_number")
	private String contactWxNumber;
    /**
     * 关注小屋
     */
	@TableField("cabin_code")
	private String cabinCode;
    /**
     * 关注门店
     */
	@TableField("store_code")
	private String storeCode;
    /**
     * 导购�?
     */
	@TableField("assistant_code")
	private String assistantCode;
    /**
     * 小程序邀请二维码
     */
	@TableField("wxa_invitation_qrcode_url")
	private String wxaInvitationQrcodeUrl;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public User setLoginName(String loginName) {
		this.loginName = loginName;
		return this;
	}

	public String getName() {
		return name;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	public String getRealName() {
		return realName;
	}

	public User setRealName(String realName) {
		this.realName = realName;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public User setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getWeixin() {
		return weixin;
	}

	public User setWeixin(String weixin) {
		this.weixin = weixin;
		return this;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public User setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
		return this;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public User setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
		return this;
	}

	public String getSalt() {
		return salt;
	}

	public User setSalt(String salt) {
		this.salt = salt;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public User setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getTokenSalt() {
		return tokenSalt;
	}

	public User setTokenSalt(String tokenSalt) {
		this.tokenSalt = tokenSalt;
		return this;
	}

	public Date getTokenExpiredDate() {
		return tokenExpiredDate;
	}

	public User setTokenExpiredDate(Date tokenExpiredDate) {
		this.tokenExpiredDate = tokenExpiredDate;
		return this;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public User setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
		return this;
	}

	public Integer getInviterId() {
		return inviterId;
	}

	public User setInviterId(Integer inviterId) {
		this.inviterId = inviterId;
		return this;
	}

	public String getAvatar() {
		return avatar;
	}

	public User setAvatar(String avatar) {
		this.avatar = avatar;
		return this;
	}

	public Integer getSex() {
		return sex;
	}

	public User setSex(Integer sex) {
		this.sex = sex;
		return this;
	}

	public Date getBirthday() {
		return birthday;
	}

	public User setBirthday(Date birthday) {
		this.birthday = birthday;
		return this;
	}

	public String getDetails() {
		return details;
	}

	public User setDetails(String details) {
		this.details = details;
		return this;
	}

	public String getUid() {
		return uid;
	}

	public User setUid(String uid) {
		this.uid = uid;
		return this;
	}

	public Integer getFollowed() {
		return followed;
	}

	public User setFollowed(Integer followed) {
		this.followed = followed;
		return this;
	}

	public Date getFollowTime() {
		return followTime;
	}

	public User setFollowTime(Date followTime) {
		this.followTime = followTime;
		return this;
	}

	public String getInvitationQrcodeUrl() {
		return invitationQrcodeUrl;
	}

	public User setInvitationQrcodeUrl(String invitationQrcodeUrl) {
		this.invitationQrcodeUrl = invitationQrcodeUrl;
		return this;
	}

	public String getInvitationQrcode() {
		return invitationQrcode;
	}

	public User setInvitationQrcode(String invitationQrcode) {
		this.invitationQrcode = invitationQrcode;
		return this;
	}

	public String getWxUnionid() {
		return wxUnionid;
	}

	public User setWxUnionid(String wxUnionid) {
		this.wxUnionid = wxUnionid;
		return this;
	}

	public String getWxaOpenid() {
		return wxaOpenid;
	}

	public User setWxaOpenid(String wxaOpenid) {
		this.wxaOpenid = wxaOpenid;
		return this;
	}

	public String getWxappOpenid() {
		return wxappOpenid;
	}

	public User setWxappOpenid(String wxappOpenid) {
		this.wxappOpenid = wxappOpenid;
		return this;
	}

	public Integer getAppUser() {
		return appUser;
	}

	public User setAppUser(Integer appUser) {
		this.appUser = appUser;
		return this;
	}

	public String getWechatName() {
		return wechatName;
	}

	public User setWechatName(String wechatName) {
		this.wechatName = wechatName;
		return this;
	}

	public String getGrade() {
		return grade;
	}

	public User setGrade(String grade) {
		this.grade = grade;
		return this;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public User setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
		return this;
	}

	public String getContactWxNumber() {
		return contactWxNumber;
	}

	public User setContactWxNumber(String contactWxNumber) {
		this.contactWxNumber = contactWxNumber;
		return this;
	}

	public String getCabinCode() {
		return cabinCode;
	}

	public User setCabinCode(String cabinCode) {
		this.cabinCode = cabinCode;
		return this;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public User setStoreCode(String storeCode) {
		this.storeCode = storeCode;
		return this;
	}

	public String getAssistantCode() {
		return assistantCode;
	}

	public User setAssistantCode(String assistantCode) {
		this.assistantCode = assistantCode;
		return this;
	}

	public String getWxaInvitationQrcodeUrl() {
		return wxaInvitationQrcodeUrl;
	}

	public User setWxaInvitationQrcodeUrl(String wxaInvitationQrcodeUrl) {
		this.wxaInvitationQrcodeUrl = wxaInvitationQrcodeUrl;
		return this;
	}

	public static final String ID = "id";

	public static final String LOGIN_NAME = "login_name";

	public static final String NAME = "name";

	public static final String REAL_NAME = "real_name";

	public static final String PASSWORD = "password";

	public static final String EMAIL = "email";

	public static final String PHONE = "phone";

	public static final String WEIXIN = "weixin";

	public static final String REGISTER_DATE = "register_date";

	public static final String LAST_LOGIN_DATE = "last_login_date";

	public static final String SALT = "salt";

	public static final String STATUS = "status";

	public static final String TOKEN_SALT = "token_salt";

	public static final String TOKEN_EXPIRED_DATE = "token_expired_date";

	public static final String INVITATION_CODE = "invitation_code";

	public static final String INVITER_ID = "inviter_id";

	public static final String AVATAR = "avatar";

	public static final String SEX = "sex";

	public static final String BIRTHDAY = "birthday";

	public static final String DETAILS = "details";

	public static final String UID = "uid";

	public static final String FOLLOWED = "followed";

	public static final String FOLLOW_TIME = "follow_time";

	public static final String INVITATION_QRCODE_URL = "invitation_qrcode_url";

	public static final String INVITATION_QRCODE = "invitation_qrcode";

	public static final String WX_UNIONID = "wx_unionid";

	public static final String WXA_OPENID = "wxa_openid";

	public static final String WXAPP_OPENID = "wxapp_openid";

	public static final String APP_USER = "app_user";

	public static final String WECHAT_NAME = "wechat_name";

	public static final String GRADE = "grade";

	public static final String CONTACT_PHONE = "contact_phone";

	public static final String CONTACT_WX_NUMBER = "contact_wx_number";

	public static final String CABIN_CODE = "cabin_code";

	public static final String STORE_CODE = "store_code";

	public static final String ASSISTANT_CODE = "assistant_code";

	public static final String WXA_INVITATION_QRCODE_URL = "wxa_invitation_qrcode_url";

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", loginName=" + loginName +
			", name=" + name +
			", realName=" + realName +
			", password=" + password +
			", email=" + email +
			", phone=" + phone +
			", weixin=" + weixin +
			", registerDate=" + registerDate +
			", lastLoginDate=" + lastLoginDate +
			", salt=" + salt +
			", status=" + status +
			", tokenSalt=" + tokenSalt +
			", tokenExpiredDate=" + tokenExpiredDate +
			", invitationCode=" + invitationCode +
			", inviterId=" + inviterId +
			", avatar=" + avatar +
			", sex=" + sex +
			", birthday=" + birthday +
			", details=" + details +
			", uid=" + uid +
			", followed=" + followed +
			", followTime=" + followTime +
			", invitationQrcodeUrl=" + invitationQrcodeUrl +
			", invitationQrcode=" + invitationQrcode +
			", wxUnionid=" + wxUnionid +
			", wxaOpenid=" + wxaOpenid +
			", wxappOpenid=" + wxappOpenid +
			", appUser=" + appUser +
			", wechatName=" + wechatName +
			", grade=" + grade +
			", contactPhone=" + contactPhone +
			", contactWxNumber=" + contactWxNumber +
			", cabinCode=" + cabinCode +
			", storeCode=" + storeCode +
			", assistantCode=" + assistantCode +
			", wxaInvitationQrcodeUrl=" + wxaInvitationQrcodeUrl +
			"}";
	}
}
