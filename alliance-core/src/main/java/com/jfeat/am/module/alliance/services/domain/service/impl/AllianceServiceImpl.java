package com.jfeat.am.module.alliance.services.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jfeat.am.core.jwt.JWTKit;
import com.jfeat.am.module.alliance.api.RequestAlliance;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;
import com.jfeat.am.module.alliance.services.domain.service.AllianceService;
import com.jfeat.am.module.alliance.services.gen.crud.service.impl.CRUDAllianceServiceImpl;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2017-10-16
 */
@Service("allianceService")
public class AllianceServiceImpl extends CRUDAllianceServiceImpl implements AllianceService {

    @Resource
    QueryAllianceDao queryAllianceDao;
    @Resource
    ConfigFieldService configFieldService;
    @Override
    public Alliance findAllianceByPhoneNumber(String phoneNumber) {

        return queryAllianceDao.selectOne(new Alliance().setAlliancePhone(phoneNumber));
    }

    public List<Alliance> getAlliancesByUserId(Long id){
        Alliance entity = new Alliance();
        entity.setUserId(id);
        Alliance alliance = queryAllianceDao.selectOne(entity);
        if(alliance!=null){
            return queryAllianceDao.selectList(new EntityWrapper<Alliance>().eq("invitor_alliance_id",alliance.getId()));
        }else
            return null;
    }

    /**
     * 据据绑定的用户id 查盟友，一一对应关系
     * @param id
     * @return
     */
    public Alliance getAlliancesByBindingUserId(Long id){
        Alliance entity = new Alliance();
        entity.setUserId(id);
        Alliance alliance = queryAllianceDao.selectOne(entity);
        return alliance;
    }

    @Override
    public AllianceRecord getSelfProductById(Long id) {
        AllianceRecord alliance = queryAllianceDao.selectAllianceOneByUserId(id);
        Long userId = alliance.getUserId();

        if(userId!=null){
            List<Map> selfProductByUserId = queryAllianceDao.getSelfProductByUserId(userId);
            if(selfProductByUserId!=null){
                alliance.setSelfProducts(JSON.parseArray(JSON.toJSONString(selfProductByUserId)));
            }
        }
        return alliance;
    }

    @Override
    public Integer createAlliance(RequestAlliance requestAlliance, Long userId) {
        if(requestAlliance.getInvitationCode()==null||requestAlliance.getInvitationCode().length()==0){
            throw new BusinessException(BusinessCode.BadRequest,"邀请码不能为空");
        }
        if(requestAlliance.getAlliancePhone()==null||requestAlliance.getAlliancePhone().length()==0){
            throw new BusinessException(BusinessCode.BadRequest,"手机号不能为空");
        }
        if(requestAlliance.getAllianceName()==null||requestAlliance.getAllianceName().length()==0){
            throw new BusinessException(BusinessCode.BadRequest,"名字不能为空");
        }
        List alliance_phone = queryAllianceDao.selectList(new Condition().eq(Alliance.ALLIANCE_PHONE, requestAlliance.getAlliancePhone()).ne(Alliance.USER_ID,userId));
        if(alliance_phone.size()>0){
            throw new BusinessException(BusinessCode.BadRequest,"该手机号码已被注册为盟友");
        }
        Alliance alliance=new Alliance();

        alliance.setAlliancePhone(requestAlliance.getAlliancePhone());

        alliance.setAllianceName(requestAlliance.getAllianceName());

        Long invitorUserId = queryAllianceDao.selectUserIdByInvitationCode(requestAlliance.getInvitationCode());

        if(invitorUserId==null||invitorUserId==0){
            throw new BusinessException(BusinessCode.BadRequest,"邀请码找不到对应的用户");
        }
        Alliance invitor = queryAllianceDao.selectOne(new Alliance().setUserId(invitorUserId));
        if(invitor==null){
            throw new BusinessException(BusinessCode.CodeBase,"邀请码找到的用户不是盟友");
        }
        alliance.setInvitorAllianceId(invitor.getId());

        alliance.setCreationTime(new Date());

        //临时盟友
        alliance.setAllianceShip(1);
        Long selfUserId = JWTKit.getUserId();
        if(selfUserId!=null&&selfUserId!=0){
            alliance.setUserId(selfUserId);
        }
        //成为盟友时间
        //alliance.setAllianceShipTime(new Date());
        //获取过期天数配置
        Integer expiryTime =configFieldService.getFieldInteger("temp_alliance_expiry_time");
        //设置支付过期时间3天。
        alliance.setTempAllianceExpiryTime(new Date(new Date().getTime()+ expiryTime* 24 * 60 * 60 * 1000));
        return queryAllianceDao.insert(alliance);
    }
}
