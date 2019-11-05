package com.jfeat.am.module.cg.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.dao.DuplicateKeyException;
import com.jfeat.am.module.cg.services.domain.dao.QueryUserDao;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Ids;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.plus.CRUDObject;

import java.math.BigDecimal;

import com.jfeat.am.module.cg.services.domain.service.UserService;
import com.jfeat.am.module.cg.services.domain.model.UserRecord;
import com.jfeat.am.module.cg.services.gen.persistence.model.User;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;


/**
 * <p>
 * api
 * </p>
 *
 * @author Code Generator
 * @since 2019-11-05
 */
@RestController

@Api("微信用户 User")
@RequestMapping("/api/WeChat/users")
public class UserEndpoint {


    @Resource
    UserService userService;

    @Resource
    QueryUserDao queryUserDao;

    @BusinessLog(name = "User", value = "create User")
    @PostMapping
    @ApiOperation(value = "新建微信用户", response = User.class)
    public Tip createUser(@RequestBody User entity) {

        Integer affected = 0;
        try {
            affected = userService.createMaster(entity);

        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
    }

    @BusinessLog(name = "User", value = "查看 User")
    @GetMapping("/{id}")
    @ApiOperation(value = "根据查看微信用户", response = User.class)
    public Tip getUser(@PathVariable Long id) {
        return SuccessTip.create(userService.retrieveMaster(id));
    }

    @BusinessLog(name = "User", value = "update User")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改微信用户", response = User.class)
    public Tip updateUser(@PathVariable Long id, @RequestBody User entity) {
        entity.setId(id);
        return SuccessTip.create(userService.updateMaster(entity));
    }

    @BusinessLog(name = "User", value = "delete User")
    @DeleteMapping("/{id}")
    @ApiOperation("删除微信用户")
    public Tip deleteUser(@PathVariable Long id) {
        return SuccessTip.create(userService.deleteMaster(id));
    }

    @BusinessLog(name = "User", value = "查询微信用户列表")
    @ApiOperation(value = "查看微信用户列表信息", response = UserRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Integer"),
            @ApiImplicitParam(name = "loginName", dataType = "String"),
            @ApiImplicitParam(name = "name", dataType = "String"),
            @ApiImplicitParam(name = "realName", dataType = "String"),
            @ApiImplicitParam(name = "password", dataType = "String"),
            @ApiImplicitParam(name = "email", dataType = "String"),
            @ApiImplicitParam(name = "phone", dataType = "String"),
            @ApiImplicitParam(name = "weixin", dataType = "String"),
            @ApiImplicitParam(name = "registerDate", dataType = "Date"),
            @ApiImplicitParam(name = "lastLoginDate", dataType = "Date"),
            @ApiImplicitParam(name = "salt", dataType = "String"),
            @ApiImplicitParam(name = "status", dataType = "String"),
            @ApiImplicitParam(name = "tokenSalt", dataType = "String"),
            @ApiImplicitParam(name = "tokenExpiredDate", dataType = "Date"),
            @ApiImplicitParam(name = "invitationCode", dataType = "String"),
            @ApiImplicitParam(name = "inviterId", dataType = "Integer"),
            @ApiImplicitParam(name = "avatar", dataType = "String"),
            @ApiImplicitParam(name = "sex", dataType = "Integer"),
            @ApiImplicitParam(name = "birthday", dataType = "Date"),
            @ApiImplicitParam(name = "details", dataType = "String"),
            @ApiImplicitParam(name = "uid", dataType = "String"),
            @ApiImplicitParam(name = "followed", dataType = "Integer"),
            @ApiImplicitParam(name = "followTime", dataType = "Date"),
            @ApiImplicitParam(name = "invitationQrcodeUrl", dataType = "String"),
            @ApiImplicitParam(name = "invitationQrcode", dataType = "String"),
            @ApiImplicitParam(name = "wxUnionid", dataType = "String"),
            @ApiImplicitParam(name = "wxaOpenid", dataType = "String"),
            @ApiImplicitParam(name = "wxappOpenid", dataType = "String"),
            @ApiImplicitParam(name = "appUser", dataType = "Integer"),
            @ApiImplicitParam(name = "wechatName", dataType = "String"),
            @ApiImplicitParam(name = "grade", dataType = "String"),
            @ApiImplicitParam(name = "contactPhone", dataType = "String"),
            @ApiImplicitParam(name = "contactWxNumber", dataType = "String"),
            @ApiImplicitParam(name = "cabinCode", dataType = "String"),
            @ApiImplicitParam(name = "storeCode", dataType = "String"),
            @ApiImplicitParam(name = "assistantCode", dataType = "String"),
            @ApiImplicitParam(name = "wxa_invitationQrcodeUrl", dataType = "String"),
            @ApiImplicitParam(name = "orderBy", dataType = "String"),
            @ApiImplicitParam(name = "sort", dataType = "String")
    })
    public Tip queryUsers(Page<UserRecord> page,
                          @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                          @RequestParam(name = "search", required = false) String search,
                          @RequestParam(name = "id", required = false) Long id,
                          @RequestParam(name = "loginName", required = false) String loginName,
                          @RequestParam(name = "name", required = false) String name,
                          @RequestParam(name = "realName", required = false) String realName,
                          @RequestParam(name = "password", required = false) String password,
                          @RequestParam(name = "email", required = false) String email,
                          @RequestParam(name = "phone", required = false) String phone,
                          @RequestParam(name = "weixin", required = false) String weixin,
                          @RequestParam(name = "registerDate", required = false) Date registerDate,
                          @RequestParam(name = "lastLoginDate", required = false) Date lastLoginDate,
                          @RequestParam(name = "salt", required = false) String salt,
                          @RequestParam(name = "status", required = false) String status,
                          @RequestParam(name = "tokenSalt", required = false) String tokenSalt,
                          @RequestParam(name = "tokenExpiredDate", required = false) Date tokenExpiredDate,
                          @RequestParam(name = "invitationCode", required = false) String invitationCode,
                          @RequestParam(name = "inviterId", required = false) Integer inviterId,
                          @RequestParam(name = "avatar", required = false) String avatar,
                          @RequestParam(name = "sex", required = false) Integer sex,
                          @RequestParam(name = "birthday", required = false) Date birthday,
                          @RequestParam(name = "details", required = false) String details,
                          @RequestParam(name = "uid", required = false) String uid,
                          @RequestParam(name = "followed", required = false) Integer followed,
                          @RequestParam(name = "followTime", required = false) Date followTime,
                          @RequestParam(name = "invitationQrcodeUrl", required = false) String invitationQrcodeUrl,
                          @RequestParam(name = "invitationQrcode", required = false) String invitationQrcode,
                          @RequestParam(name = "wxUnionid", required = false) String wxUnionid,
                          @RequestParam(name = "wxaOpenid", required = false) String wxaOpenid,
                          @RequestParam(name = "wxappOpenid", required = false) String wxappOpenid,
                          @RequestParam(name = "appUser", required = false) Integer appUser,
                          @RequestParam(name = "wechatName", required = false) String wechatName,
                          @RequestParam(name = "grade", required = false) String grade,
                          @RequestParam(name = "contactPhone", required = false) String contactPhone,
                          @RequestParam(name = "contactWxNumber", required = false) String contactWxNumber,
                          @RequestParam(name = "cabinCode", required = false) String cabinCode,
                          @RequestParam(name = "storeCode", required = false) String storeCode,
                          @RequestParam(name = "assistantCode", required = false) String assistantCode,
                          @RequestParam(name = "wxaInvitationQrcodeUrl", required = false) String wxaInvitationQrcodeUrl,
                          @RequestParam(name = "orderBy", required = false) String orderBy,
                          @RequestParam(name = "sort", required = false) String sort) {
        if (orderBy != null && orderBy.length() > 0) {
            if (sort != null && sort.length() > 0) {
                String pattern = "(ASC|DESC|asc|desc)";
                if (!sort.matches(pattern)) {
                    throw new BusinessException(BusinessCode.BadRequest.getCode(), "sort must be ASC or DESC");//此处异常类型根据实际情况而定
                }
            } else {
                sort = "ASC";
            }
            orderBy = "`" + orderBy + "`" + " " + sort;
        }
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        UserRecord record = new UserRecord();
        record.setId(id);
        record.setLoginName(loginName);
        record.setName(name);
        record.setRealName(realName);
        record.setPassword(password);
        record.setEmail(email);
        record.setPhone(phone);
        record.setWeixin(weixin);
        record.setRegisterDate(registerDate);
        record.setLastLoginDate(lastLoginDate);
        record.setSalt(salt);
        record.setStatus(status);
        record.setTokenSalt(tokenSalt);
        record.setTokenExpiredDate(tokenExpiredDate);
        record.setInvitationCode(invitationCode);
        record.setInviterId(inviterId);
        record.setAvatar(avatar);
        record.setSex(sex);
        record.setBirthday(birthday);
        record.setDetails(details);
        record.setUid(uid);
        record.setFollowed(followed);
        record.setFollowTime(followTime);
        record.setInvitationQrcodeUrl(invitationQrcodeUrl);
        record.setInvitationQrcode(invitationQrcode);
        record.setWxUnionid(wxUnionid);
        record.setWxaOpenid(wxaOpenid);
        record.setWxappOpenid(wxappOpenid);
        record.setAppUser(appUser);
        record.setWechatName(wechatName);
        record.setGrade(grade);
        record.setContactPhone(contactPhone);
        record.setContactWxNumber(contactWxNumber);
        record.setCabinCode(cabinCode);
        record.setStoreCode(storeCode);
        record.setAssistantCode(assistantCode);
        record.setWxaInvitationQrcodeUrl(wxaInvitationQrcodeUrl);
        page.setRecords(queryUserDao.findUserPage(page, record, search, orderBy, null, null));

        return SuccessTip.create(page);
    }
}
