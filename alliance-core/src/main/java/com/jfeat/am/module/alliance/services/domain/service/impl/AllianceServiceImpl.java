package com.jfeat.am.module.alliance.services.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jfeat.am.module.alliance.api.*;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;
import com.jfeat.am.module.alliance.services.domain.service.AllianceService;
import com.jfeat.am.module.alliance.services.gen.crud.service.impl.CRUDAllianceServiceImpl;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import com.jfeat.am.module.alliance.util.AllianceUtil;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
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

    @Resource
    QueryWalletHistoryDao queryWalletHistoryDao;
    @Resource
    QueryWalletDao queryWalletDao;
    Long millisecond = 86400000L;//一天

    @Override
    public Alliance findAllianceByPhoneNumber(String phoneNumber) {

        return queryAllianceDao.selectOne(new Alliance().setAlliancePhone(phoneNumber));
    }

    public List<Alliance> getAlliancesByUserId(Long id) {
        Alliance entity = new Alliance();
        entity.setUserId(id);
        Alliance alliance = queryAllianceDao.selectOne(entity);
        if (alliance != null) {
            return queryAllianceDao.selectList(new EntityWrapper<Alliance>().eq("invitor_alliance_id", alliance.getId()));
        } else
            return null;
    }

    /**
     * 据据绑定的用户id 查盟友，一一对应关系
     *
     * @param id
     * @return
     */
    public Alliance getAlliancesByBindingUserId(Long id) {
        Alliance entity = new Alliance();
        entity.setUserId(id);
        Alliance alliance = queryAllianceDao.selectOne(entity);
        return alliance;
    }

    @Override
    public AllianceRecord getSelfProductById(Long id) {
        AllianceRecord alliance = queryAllianceDao.selectAllianceOneByUserId(id);
        Long userId = alliance.getUserId();

        if (userId != null) {
            List<Map> selfProductByUserId = queryAllianceDao.getSelfProductByUserId(userId);
            if (selfProductByUserId != null) {
                alliance.setSelfProducts(JSON.parseArray(JSON.toJSONString(selfProductByUserId)));
            }
        }
        return alliance;
    }

    @Override
    @Transactional
    public Integer createAlliance(RequestAlliance requestAlliance, Long userId) {
        if (requestAlliance.getInvitationCode() == null || requestAlliance.getInvitationCode().length() == 0) {
            throw new BusinessException(BusinessCode.BadRequest, "邀请码不能为空");
        }
        if (requestAlliance.getAlliancePhone() == null || requestAlliance.getAlliancePhone().length() == 0) {
            throw new BusinessException(BusinessCode.BadRequest, "手机号不能为空");
        }
        if (requestAlliance.getAllianceName() == null || requestAlliance.getAllianceName().length() == 0) {
            throw new BusinessException(BusinessCode.BadRequest, "名字不能为空");
        }
        List alliance_phone = queryAllianceDao.selectList(new Condition().eq(Alliance.ALLIANCE_PHONE, requestAlliance.getAlliancePhone()).ne(Alliance.USER_ID, userId));
        if (alliance_phone.size() > 0) {
            throw new BusinessException(BusinessCode.BadRequest, "该手机号码已被注册为盟友");
        }
        Alliance alliance = new Alliance();

        alliance.setAlliancePhone(requestAlliance.getAlliancePhone());

        alliance.setAllianceName(requestAlliance.getAllianceName());

        Long invitorUserId = queryAllianceDao.selectUserIdByInvitationCode(requestAlliance.getInvitationCode());

        if (invitorUserId == null || invitorUserId == 0) {
            throw new BusinessException(BusinessCode.BadRequest, "邀请码找不到对应的用户");
        }
        Alliance invitor = queryAllianceDao.selectOne(new Alliance().setUserId(invitorUserId));
        if (invitor == null) {
            throw new BusinessException(BusinessCode.CodeBase, "邀请码找到的用户不是盟友");
        }
        alliance.setInvitorAllianceId(invitor.getId());
        Date createTime = new Date();
        alliance.setCreationTime(createTime);

        //临时盟友
        alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
        alliance.setAllianceType(Alliance.ALLIANCE_TYPE_COMMON);
        alliance.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
        //成为盟友时间
        //alliance.setAllianceShipTime(new Date());
        //获取过期天数配置
        Integer expiryTime = configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_TEMP_ALLIANCE_EXPIRY_TIME);
        //设置支付过期时间3天。
        alliance.setTempAllianceExpiryTime(new Date(createTime.getTime() + expiryTime * 24 * 60 * 60 * 1000));
        return queryAllianceDao.insert(alliance);
    }

    public Integer create(Long userId, AllianceRequest entity) throws ParseException {
        entity.setCreationTime(new Date());
        entity.setStartingCycle(new Date());
        entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
        if (entity.getAllianceDob() != null) {
            entity.setAge(AllianceUtil.getAgeByBirth(entity.getAllianceDob()));
        }
        String alliance_phone = queryAllianceDao.queryPhone(entity.getAlliancePhone());
        queryAllianceDao.upUserRealNameByPhone(alliance_phone,entity.getAllianceName());
        if (alliance_phone != null && alliance_phone.length() > 0) {
            throw new BusinessException(BusinessCode.BadRequest, AllianceShips.PHONE_EXITS_ERROR);
        }
        queryAllianceDao.upUserRealNameByPhone(alliance_phone,entity.getAllianceName());
        String invitorPhoneNumber = entity.getInvitorPhoneNumber();
        if (invitorPhoneNumber != null && invitorPhoneNumber.length() > 0) {
            Alliance invitor = queryAllianceDao.selectOne(new Alliance().setAlliancePhone(entity.getInvitorPhoneNumber()));
            if (invitor != null) {
                entity.setInvitorAllianceId(invitor.getId());
            } else {
                throw new BusinessException(BusinessCode.BadRequest, AllianceShips.ALLIANCE_NOT_EXIST);
            }
        }
        if (entity.getAllianceType().equals(Alliance.ALLIANCE_TYPE_COMMON)) {
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
            entity.setTempAllianceExpiryTime(new Date((new Date().getTime() + configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_TEMP_ALLIANCE_EXPIRY_TIME) * millisecond)));

            entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
        } else if (entity.getAllianceType().equals(Alliance.ALLIANCE_TYPE_BONUS)) {
//            entity.setAllianceShipTime(new Date());
            entity.setTempAllianceExpiryTime(new Date((new Date().getTime() + configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_TEMP_ALLIANCE_EXPIRY_TIME) * millisecond)));
            entity.setStockholderShip(1);
            entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));

        }
        Integer affected = 0;
        try {
            affected = this.createMaster(entity);

        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }
        return affected;
    }

    @Override
    @Transactional
    public Integer modify(Long id, AllianceRecord entity) throws ParseException {
        entity.setId(id);

        if (entity.getAllianceDob() != null) {
            entity.setAge(AllianceUtil.getAgeByBirth(entity.getAllianceDob()));
        }
        List alliance_phone = queryAllianceDao.selectList(new Condition().eq(Alliance.ALLIANCE_PHONE, entity.getAlliancePhone()).ne(Alliance.ID, id));
        if (alliance_phone.size() > 0) {
            throw new BusinessException(BusinessCode.BadRequest, AllianceShips.PHONE_EXITS_ERROR);
        }
        //根据邀请人电话查找邀请人信息
        Alliance alliance = null;
        Alliance alliance1 = queryAllianceDao.selectOne(new Alliance().setId(id));
        if (alliance1.getAllianceShip() == AllianceShips.ALLIANCE_SHIP_OK && entity.getAlliancePhone() != null && entity.getAlliancePhone().length() > 0) {
            if (!entity.getAlliancePhone().equals(alliance1.getAlliancePhone())) {
                throw new BusinessException(BusinessCode.BadRequest, "正式盟友的手机号不能修改");
            }
        }
        if (entity.getInvitorPhone() != null && entity.getInvitorPhone().length() > 0) {
            alliance = this.findAllianceByPhoneNumber(entity.getInvitorPhone());
        }
        if (alliance != null) {
            Alliance allianceShip = this.retrieveMaster(id);
//            if(allianceShip.getAllianceShip()!=null&&allianceShip.getAllianceShip()==1){
//                if(allianceShip.getTempAllianceExpiryTime()!=null&&new Date().getTime()<allianceShip.getTempAllianceExpiryTime().getTime()){
//                    throw new ServerException("临时盟友不能修改邀请人，请将邀请人手机号去掉");
//                }
//            }
            entity.setInvitorAllianceId(alliance.getId());
        }else {
            throw new BusinessException(BusinessCode.BadRequest,"邀请人不存在");
        }
        if (entity.getAllianceType().equals(Alliance.ALLIANCE_TYPE_COMMON)) {
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
            entity.setTempAllianceExpiryTime(new Date((new Date().getTime() + configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_TEMP_ALLIANCE_EXPIRY_TIME) * millisecond)));
//            entity.setAllianceShip(1);

        } else if (entity.getAllianceType().equals(Alliance.ALLIANCE_TYPE_BONUS)) {
//            entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
//            entity.setAllianceShipTime(new Date());
            entity.setTempAllianceExpiryTime(new Date((new Date().getTime() + configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_TEMP_ALLIANCE_EXPIRY_TIME) * millisecond)));
//            entity.setStockholderShip(1);
//            entity.setAllianceShip(2);
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));


        }
        Alliance user = queryAllianceDao.selectById(id);
        entity.setUserId(user.getUserId());
        //阻止用户修改电话
        entity.setAlliancePhone(null);
        Integer integer = this.updateMaster(entity,false);
        return integer;
    }


    @Transactional
    @Override
    public Integer modifyAllianceShip(Long id) {
        Integer set = 0;
        Alliance alliance = this.retrieveMaster(id);
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "该盟友不存在");
        }
        alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_PAID);
//<<<<<<< Updated upstream
        //alliance.setAllianceShipTime(new Date());
        this.updateMaster(alliance);
//=======
        alliance.setAllianceShipTime(new Date());
        set += this.updateMaster(alliance);
//>>>>>>> Stashed changes

        Long userId = alliance.getUserId();
        if (userId == null) {
            userId = 1L;
        }
        Wallet walletCondition = new Wallet();

        List<Wallet> wallets = queryWalletDao.selectList(new Condition().eq(Wallet.USER_ID, userId));
        Wallet wallet = null;
        if (wallets != null && wallets.size() > 0) {
            wallet = wallets.get(0);
        } else {
            wallet = new Wallet();
            if (userId != null) {
                wallet.setUserId(userId);
            }
        }

        String tmp = null;
        if (alliance.getAllianceType().equals(Alliance.ALLIANCE_TYPE_BONUS)) {
            tmp = AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE;
        } else if (alliance.getAllianceType().equals(Alliance.ALLIANCE_TYPE_COMMON)) {
            tmp = AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE;
        } else {
            throw new BusinessException(BusinessCode.CodeBase, "该盟友状态可能有误");
        }

        if (wallet.getId() == null) {
            ///设置初始累计额度
            walletCondition.setAccumulativeAmount(new BigDecimal(configFieldService.getFieldFloat(tmp)));
            walletCondition.setGiftBalance(new BigDecimal(0));
            walletCondition.setBalance(new BigDecimal(configFieldService.getFieldFloat(tmp)));
            walletCondition.setUserId(userId);
            set += queryWalletDao.insert(walletCondition);
            WalletHistory walletHistory = new WalletHistory();
            walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
            walletHistory.setAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
            walletHistory.setGiftAmount(new BigDecimal(0));
            walletHistory.setWalletId(new Long(walletCondition.getId()));
            walletHistory.setType(RechargeType.RECHARGE);
            queryWalletHistoryDao.insert(walletHistory);

        } else {

            if (wallet.getAccumulativeAmount() != null) {
                BigDecimal common_alliance = wallet.getBalance().add(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                wallet.setBalance(common_alliance);
                wallet.setAccumulativeAmount(wallet.getAccumulativeAmount().add(common_alliance));
                queryWalletDao.updateById(wallet);
            } else {
                wallet.setAccumulativeAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                wallet.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                queryWalletDao.updateById(wallet);
            }
            WalletHistory walletHistory = new WalletHistory();
            walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
            walletHistory.setAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
            walletHistory.setGiftAmount(new BigDecimal(0));
            walletHistory.setWalletId(new Long(wallet.getId()));
            walletHistory.setType(RechargeType.RECHARGE);
            set += queryWalletHistoryDao.insert(walletHistory);
        }
        return set;
    }
}
