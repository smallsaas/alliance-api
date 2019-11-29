package com.jfeat.am.module.alliance.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.domain.model.OwnerBalanceRecord;
import com.jfeat.am.module.alliance.services.gen.persistence.model.*;
import com.jfeat.am.module.alliance.util.AllianceUtil;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import com.jfeat.util.Cip;
import com.jfeat.util.SuccessCip;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.dao.DuplicateKeyException;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;

import java.math.BigDecimal;

import com.jfeat.am.module.alliance.services.domain.service.AllianceService;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;

import javax.annotation.Resource;
import java.rmi.ServerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>
 * api
 * </p>
 *
 * @author Code Generator
 * @since 2019-10-14
 */
@RestController

@Api("Alliance")
@RequestMapping("/rpc/alliance/alliances")
public class RPCAllianceEndpoint {


    @Resource
    AllianceService allianceService;

    @Resource
    QueryAllianceDao queryAllianceDao;

    @Resource
    BonusService bonusService;

    @Resource
    QueryWalletDao queryWalletDao;

    @Resource
    ConfigFieldService configFieldService;

    @Resource
    QueryBonusDao queryBonusDao;

    @Resource
    QueryWalletHistoryDao queryWalletHistoryDao;

    @Resource
    QueryOwnerBalanceDao queryOwnerBalanceDao;

    private final Integer ALLIANCE_TYPE_BONUS = 1;
    private final Integer ALLIANCE_TYPE_COMMON = 2;

    private Long millisecond = 86400000L;//24 * 60 * 60 * 1000 毫秒 一天

    //@BusinessLog(name = "Alliance", value = "create Alliance")
    @PostMapping
    @ApiOperation(value = "新建 Alliance", response = Alliance.class)
    public Cip createAlliance(@RequestHeader("X-USER-ID") Long userId, @RequestBody AllianceRequest entity) throws ServerException, ParseException {
        Date create = new Date();
        entity.setCreationTime(create);
        entity.setStartingCycle(create);
        entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
//        entity.setAllianceShipTime(new Date());
        if (entity.getAllianceDob() != null) {
            entity.setAge(AllianceUtil.getAgeByBirth(entity.getAllianceDob()));
        }
        List alliance_phone = queryAllianceDao.selectList(new Condition().eq(Alliance.ALLIANCE_PHONE, entity.getAlliancePhone()));
        if (alliance_phone.size() > 0) {
            throw new ServerException("该手机号以被注册盟友，不能重复");
        }
        String invitorPhoneNumber = entity.getInvitorPhoneNumber();
        if (invitorPhoneNumber != null && invitorPhoneNumber.length() > 0) {
            Alliance invitor = queryAllianceDao.selectOne(new Alliance().setAlliancePhone(entity.getInvitorPhoneNumber()));
            if (invitor != null) {
                entity.setInvitorAllianceId(invitor.getId());
            } else {
                throw new ServerException("该手机号码的盟友不存在");
            }
        }
        if (entity.getAllianceType().equals(ALLIANCE_TYPE_COMMON)) {
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
            entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
            Wallet walletCondition = new Wallet();
            if (userId != null) {
                walletCondition.setUserId(userId);
            } else if (entity.getUserId() != null && entity.getUserId() > 0) {
                walletCondition.setUserId(entity.getUserId());
            }
            Wallet wallet = queryWalletDao.selectOne(walletCondition);

            if (wallet == null) {
                walletCondition.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                walletCondition.setAccumulativeAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                queryWalletDao.insert(walletCondition);
                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setCreatedTime(create);
                walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                walletHistory.setAmount(new BigDecimal(0));
                walletHistory.setGiftBalance(new BigDecimal(0));
                walletHistory.setWalletId(new Long(walletCondition.getId()));
                queryWalletHistoryDao.insert(walletHistory);
            } else {
                if (wallet.getAccumulativeAmount() != null) {
                    BigDecimal common_alliance = wallet.getBalance().add(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                    wallet.setBalance(common_alliance);
                    wallet.setAccumulativeAmount(wallet.getAccumulativeGiftAmount().add(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE))));
                    queryWalletDao.updateById(wallet);
                } else {
                    wallet.setAccumulativeAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                    queryWalletDao.updateById(wallet);
                }
                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                walletHistory.setAmount(new BigDecimal(0));
                walletHistory.setGiftAmount(new BigDecimal(0));
                walletHistory.setWalletId(new Long(wallet.getId()));
                queryWalletHistoryDao.insert(walletHistory);
            }
        } else if (entity.getAllianceType().equals(ALLIANCE_TYPE_BONUS)) {
//            entity.setStockholderShipTime(new Date());
//            entity.setStockholderShip(1);
            entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
            Wallet walletCondition = new Wallet();
            if (userId != null) {
                walletCondition.setUserId(userId);
            } else if (entity.getUserId() != null && entity.getUserId() > 0) {
                walletCondition.setUserId(entity.getUserId());
            }
            Wallet wallet = queryWalletDao.selectOne(walletCondition);
            if (wallet == null) {
                wallet = new Wallet();
                walletCondition.setAccumulativeAmount(new BigDecimal(0));
                walletCondition.setGiftBalance(new BigDecimal(0));
                walletCondition.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
                queryWalletDao.insert(walletCondition);
                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
                walletHistory.setAmount(new BigDecimal(0));
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
                walletHistory.setAmount(new BigDecimal(0));
                walletHistory.setGiftAmount(new BigDecimal(0));
                walletHistory.setWalletId(new Long(wallet.getId()));
                walletHistory.setType(RechargeType.RECHARGE);
                queryWalletHistoryDao.insert(walletHistory);

            }

        }
        Integer affected = 0;
        try {
            affected = allianceService.createMaster(entity);

        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessCip.create(affected);
    }

    //@BusinessLog(name = "Alliance", value = "查看 Alliance")
    @GetMapping("/{id}")
    @ApiOperation(value = "查看 Alliance", response = Alliance.class)
    public Cip getAlliance(@PathVariable Long id) {
        return SuccessCip.create(allianceService.retrieveMaster(id));
    }

    //@BusinessLog(name = "Alliance", value = "update Alliance")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改 Alliance", response = Alliance.class)
    public Cip updateAlliance(@PathVariable Long id, @RequestBody AllianceRequest entity) throws ServerException, ParseException {
        entity.setCreationTime(new Date());
        entity.setId(id);
        List alliance_phone = queryAllianceDao.selectList(new Condition().eq(Alliance.ALLIANCE_PHONE, entity.getAlliancePhone()).ne("id", id));
        if (alliance_phone.size() > 0) {
            throw new ServerException("该手机号以被注册盟友，不能重复");
        }
        //根据邀请人电话查找邀请人信息
        Alliance alliance = null;
        if (entity.getInvitorPhoneNumber() != null) {
            alliance = allianceService.findAllianceByPhoneNumber(entity.getInvitorPhoneNumber());
        }
        if (alliance != null) {
            Alliance allianceShip = allianceService.retrieveMaster(id);
            if (allianceShip.getAllianceShip() != null && allianceShip.getAllianceShip() == 1) {
                if (allianceShip.getTempAllianceExpiryTime() != null && new Date().getTime() < allianceShip.getTempAllianceExpiryTime().getTime()) {
                    throw new ServerException("临时盟友不能修改邀请人，请将邀请人手机号去掉");
                }
            }
            entity.setInvitorAllianceId(alliance.getId());
        }
        Alliance user = queryAllianceDao.selectById(id);
        entity.setUserId(user.getUserId());
        return SuccessCip.create(allianceService.updateMaster(entity));
    }

    //@BusinessLog(name = "Alliance", value = "delete Alliance")
    @DeleteMapping("/{id}")
    @ApiOperation("删除 Alliance")
    public Cip deleteAlliance(@PathVariable Long id) {
        return SuccessCip.create(allianceService.deleteMaster(id));
    }

    //@BusinessLog(name = "Alliance", value = "delete Alliance")
    @ApiOperation(value = "Alliance 列表信息", response = AllianceRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Long"),
            @ApiImplicitParam(name = "userId", dataType = "Long"),
            @ApiImplicitParam(name = "invitorAllianceId", dataType = "Integer"),
            @ApiImplicitParam(name = "allianceShip", dataType = "Integer"),
            @ApiImplicitParam(name = "stockholderShip", dataType = "Integer"),
            @ApiImplicitParam(name = "creationTime", dataType = "Date"),
            @ApiImplicitParam(name = "allianceRank", dataType = "Integer"),
            @ApiImplicitParam(name = "allianceShipTime", dataType = "Date"),
            @ApiImplicitParam(name = "stockholderShipTime", dataType = "Date"),
            @ApiImplicitParam(name = "tempAllianceExpiryTime", dataType = "Date"),
            @ApiImplicitParam(name = "allianceStatus", dataType = "Integer"),
            @ApiImplicitParam(name = "allianceInventoryAmount", dataType = "BigDecimal"),
            @ApiImplicitParam(name = "alliancePoint", dataType = "BigDecimal"),
            @ApiImplicitParam(name = "allianceName", dataType = "String"),
            @ApiImplicitParam(name = "allianceSex", dataType = "Integer"),
            @ApiImplicitParam(name = "allianceOccupation", dataType = "String"),
            @ApiImplicitParam(name = "allianceIndustry", dataType = "String"),
            @ApiImplicitParam(name = "allianceAddress", dataType = "String"),
            @ApiImplicitParam(name = "allianceBusiness", dataType = "String"),
            @ApiImplicitParam(name = "allianceHobby", dataType = "String"),
            @ApiImplicitParam(name = "alliancePhone", dataType = "String"),
            @ApiImplicitParam(name = "allianceDob", dataType = "Date"),
            @ApiImplicitParam(name = "orderBy", dataType = "String"),
            @ApiImplicitParam(name = "sort", dataType = "String")
    })
    public Cip queryAlliances(Page<AllianceRecord> page,
                              @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                              @RequestParam(name = "search", required = false) String search,
                              @RequestParam(name = "id", required = false) Long id,
                              @RequestParam(name = "userId", required = false) Long userId,
                              @RequestParam(name = "invitorAllianceId", required = false) Long invitorAllianceId,
                              @RequestParam(name = "allianceShip", required = false) Integer allianceShip,
                              @RequestParam(name = "stockholderShip", required = false) Integer stockholderShip,
                              @RequestParam(name = "creationTime", required = false) Date creationTime,
                              @RequestParam(name = "allianceRank", required = false) Integer allianceRank,
                              @RequestParam(name = "allianceShipTime", required = false) Date allianceShipTime,
                              @RequestParam(name = "stockholderShipTime", required = false) Date stockholderShipTime,
                              @RequestParam(name = "tempAllianceExpiryTime", required = false) Date tempAllianceExpiryTime,
                              @RequestParam(name = "allianceStatus", required = false) Integer allianceStatus,
                              @RequestParam(name = "allianceInventoryAmount", required = false) BigDecimal allianceInventoryAmount,
                              @RequestParam(name = "alliancePoint", required = false) BigDecimal alliancePoint,
                              @RequestParam(name = "allianceName", required = false) String allianceName,
                              @RequestParam(name = "allianceSex", required = false) Integer allianceSex,
                              @RequestParam(name = "allianceOccupation", required = false) String allianceOccupation,
                              @RequestParam(name = "allianceIndustry", required = false) String allianceIndustry,
                              @RequestParam(name = "allianceAddress", required = false) String allianceAddress,
                              @RequestParam(name = "allianceBusiness", required = false) String allianceBusiness,
                              @RequestParam(name = "allianceHobby", required = false) String allianceHobby,
                              @RequestParam(name = "alliancePhone", required = false) String alliancePhone,
                              @RequestParam(name = "allianceDob", required = false) Date allianceDob,
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
        AllianceRecord record = new AllianceRecord();
        record.setId(id);
        record.setUserId(userId);
        record.setInvitorAllianceId(invitorAllianceId);
        record.setAllianceShip(allianceShip);
        record.setStockholderShip(stockholderShip);
        record.setCreationTime(creationTime);
        record.setAllianceShipTime(allianceShipTime);
        record.setStockholderShipTime(stockholderShipTime);
        record.setTempAllianceExpiryTime(tempAllianceExpiryTime);
        record.setAllianceStatus(allianceStatus);
        record.setAllianceInventoryAmount(allianceInventoryAmount);
        record.setAlliancePoint(alliancePoint);
        record.setAllianceName(allianceName);
        record.setAllianceSex(allianceSex);
        record.setAllianceOccupation(allianceOccupation);
        record.setAllianceIndustry(allianceIndustry);
        record.setAllianceAddress(allianceAddress);
        record.setAllianceBusiness(allianceBusiness);
        record.setAllianceHobby(allianceHobby);
        record.setAlliancePhone(alliancePhone);
        record.setAllianceDob(allianceDob);
        page.setRecords(queryAllianceDao.findAlliancePageShip(page, record, search, orderBy, null, null));
        return SuccessCip.create(page);
    }

    @GetMapping("/getAlliancesByUserId")
    @ApiOperation(value = "根据X-USER-ID获取我的盟友列表", response = Alliance.class)
    public Cip getAlliancesByUserId(@RequestHeader("X-USER-ID") Long id) {
        List<Alliance> alliancesByUserId = allianceService.getAlliancesByUserId(id);
        return SuccessCip.create(alliancesByUserId);
    }

    @GetMapping("/getAllianceInformationByUserId")
    @ApiOperation(value = "根据X-USER-ID获取我的盟友信息,可以获取当月订单currentMonthOrder和我的盟友列表,还有分红信息（只有是股东能有分红）dateType参数--->1当天，2当月，3当季", response = AllianceRecord.class)
    public Cip getAllianceInformationByUserId(@RequestHeader("X-USER-ID") Long id, @RequestParam(name = "dateType", defaultValue = "2") Integer dateType) throws ParseException {
        Alliance entity = new Alliance();
        entity.setUserId(id);
        AllianceRecord alliance = queryAllianceDao.selectAllianceOneByUserId(id);
        if(alliance==null){
            throw new BusinessException(BusinessCode.BadRequest,"该盟友不存在");
        }
        Wallet wallet = queryWalletDao.selectOne(new Wallet().setUserId(id));
        if (wallet != null) {
            if (wallet.getBalance() != null)
                alliance.setBalance(wallet.getBalance());
            else
                alliance.setBalance(new BigDecimal(0.00));
        }
        List<Map> currentMonthOrderByUserId = queryAllianceDao.getCurrentMonthOrderByUserId(id);
        if (alliance != null) {
            if (currentMonthOrderByUserId != null && currentMonthOrderByUserId.size() > 0) {
                alliance.setCurrentMonthOrder(JSON.parseArray(JSON.toJSONString(queryAllianceDao.getCurrentMonthOrderByUserId(id), SerializerFeature.WriteDateUseDateFormat)));
            } else {
                alliance.setCurrentMonthOrder(new JSONArray());
            }
        } else {
            return SuccessCip.create(null);
        }
        List<Alliance> alliancesByUserId = allianceService.getAlliancesByUserId(id);
        if (alliancesByUserId != null && alliancesByUserId.size() > 0) {
            alliance.setAllianceTeam(JSON.parseArray(JSON.toJSONString(alliancesByUserId, SerializerFeature.WriteDateUseDateFormat)));
        } else {
            alliance.setAllianceTeam(new JSONArray());
        }
        //-------------------------------------------------
        if (alliance.getAllianceType() == Alliance.ALLIANCE_TYPE_BONUS) {
            BigDecimal averageBonus = queryBonusDao.getAverageBonus();
            BigDecimal zero = new BigDecimal(0.00);
            if(averageBonus==null){
                averageBonus=zero;
            }
            BigDecimal allBonusRatio = queryBonusDao.getAllBonusRatio(id);
            if(allBonusRatio==null){
                allBonusRatio=zero;
            }
            alliance.setSelfBonus(averageBonus);
            alliance.setTeamSelfBonus(allBonusRatio);
            alliance.setTotalSelfBonus(averageBonus.add(allBonusRatio));
            BigDecimal commissionOrderMonth = queryBonusDao.getCommissionTotalMonth(id);
            if(commissionOrderMonth==null){
                commissionOrderMonth=zero;
            }
            alliance.setCommissionBalance(commissionOrderMonth);
            alliance.setOrderAmount(queryBonusDao.queryOrderAmount(id));//当前月订单入货额度
            alliance.setEffectiveCommission(queryBonusDao.getCommissionTotalMonth(id));//当前月的提成
            alliance.setConditionOrderAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_WITHDRAWAL_CONDITIONS)).subtract(queryBonusDao.queryOrderAmount(id)));
        } else {
            alliance.setSelfBonus(new BigDecimal(0.00));
            alliance.setTeamSelfBonus(new BigDecimal(0.00));
            alliance.setTotalSelfBonus(new BigDecimal(0.00));
        }
        alliance.setStockholderCount(queryBonusDao.stockholderCount());
        Float aFloat = queryBonusDao.queryProportion(id);
        if(aFloat==null){
            aFloat=0F;
        }
        alliance.setProportion(aFloat*100);
//        JSONArray royalties = new JSONArray();
//        List<Long> team = queryBonusDao.getTeam(id);
//        if (team != null && team.size() > 0) {
//            for (Long t : team) {
//                if (t != null && t > 0) {
//                    if (queryBonusDao.queryShip(t) == 0 && queryBonusDao.queryType(id) == Alliance.ALLIANCE_TYPE_BONUS) {
//                        String name = queryBonusDao.queryAllianceName(t);
//                        BigDecimal teamProportionBonus = queryBonusDao.getTeamBonus(t, dateType);
//                        BigDecimal orderBonus = queryBonusDao.queryBonusOrder(t);
//                        Royalty ls = new Royalty();
//                        ls.setInvitorName(name);
//                        ls.setOrderMoney(orderBonus);
//                        ls.setCommission(teamProportionBonus);
//                        ls.setCreateTime(new Date());
//                        royalties.add(ls);
//                    }
//                }
//            }
//        }
        //当月提成
        alliance.setCommissionOrder(JSONArray.parseArray(JSON.toJSONString(queryBonusDao.getCommissionOrderMonth(id))));
        //盟友消息
        List<Alliance> alliances = queryAllianceDao.queryWeekAlliance(id);
        if (alliance != null) {
            alliance.setAllianceMessages(JSONArray.parseArray(JSON.toJSONString(alliances)));
        } else {
            alliance.setAllianceMessages(new JSONArray());
        }
        List<JSONObject> jsonOrders = queryAllianceDao.queryWeekOrder(id);

        if (jsonOrders != null) {
            alliance.setTeamAllianceOrderMessages(JSONArray.parseArray(JSON.toJSONString(jsonOrders)));
        } else {
            alliance.setTeamAllianceOrderMessages(new JSONArray());
        }
        List<JSONObject> orderDelivers = queryAllianceDao.queryWeekOrderDeliver(id);
        if (orderDelivers != null) {
            alliance.setDeliverMessage(JSONArray.parseArray(JSON.toJSONString(orderDelivers)));
        } else {
            alliance.setDeliverMessage(new JSONArray());
        }
        OwnerBalanceRecord ownerBalanceRecord = queryOwnerBalance(id);
        alliance.setBonus_balance(ownerBalanceRecord.getBalance());
        alliance.setExpected_bonus(ownerBalanceRecord.getExpected_bonus());
        String starting_time = configFieldService.getFieldString(AllianceFields.ALLIANCE_FIELD_STARTING_TIME);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(starting_time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_STARTING_CYCLE));
        Date endTime = calendar.getTime();
        alliance.setDividedTime(starting_time+"至"+formatter.format(endTime));
        return SuccessCip.create(alliance);
    }

    @ApiOperation(value = "根据盟友id获取我的盟友信息,携带自营商品selfProducts", response = Alliance.class)
    @GetMapping("/getAllianceInformationByUserId/{id}")
    public Cip getSelfProductById(@PathVariable Long id) {
        return SuccessCip.create(allianceService.getSelfProductById(id));
    }

    @ApiOperation(value = "充值套餐")
    @GetMapping("/setMeal")
    public Cip getSetMeal() {
        List<JSONObject> setMeals = queryAllianceDao.getSetMeal();
        for (JSONObject setMeal : setMeals) {
            if (setMeal.getString("value").equals("10000")) {
                setMeal.put("type", "分红盟友");
            } else {
                setMeal.put("type", "普通盟友");
            }
        }
        return SuccessCip.create(setMeals);
    }

    @ApiOperation(value = "根据手机号，姓名，邀请码，添加盟友", response = Cip.class)
    @PostMapping("/createAlliance")
    public Cip createAlliance(@RequestHeader("X-USER-ID") Long userId, @RequestBody RequestAlliance requestAlliance) {
        return SuccessCip.create(allianceService.createAlliance(requestAlliance, userId));
    }



    private OwnerBalanceRecord queryOwnerBalance(Long id) {
        Alliance alliance = queryAllianceDao.selectOne(new Alliance().setUserId(id));
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "当前盟友不存在");
        }
        if (alliance.getAllianceShip()==AllianceShips.ALLIANCE_SHIP_OK) {
            OwnerBalance theownerBalance =  new OwnerBalance();
            theownerBalance.setUserId(id);
            OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(theownerBalance);
            if (ownerBalance == null) {
                ownerBalance = new OwnerBalance();
            }
            OwnerBalanceRecord ownerBalanceRecord = JSON.parseObject(JSON.toJSONString(ownerBalance), OwnerBalanceRecord.class);
            BigDecimal add = queryBonusDao.getCommissionOrderTotalLastMonth(id);
            if (add == null) {
                add = new BigDecimal(0.00);
            }
            ownerBalanceRecord.setExpected_bonus(add);
            Date allianceShipTime = alliance.getAllianceShipTime();
            int monthDiff = getMonthDiff(allianceShipTime, new Date());
            if (monthDiff < 2) {
                BigDecimal bonus_balance = ownerBalanceRecord.getBalance();
                if(bonus_balance==null){
                    bonus_balance=new BigDecimal(0.00);
                }

                BigDecimal expected_bonus = ownerBalanceRecord.getExpected_bonus();
                if(expected_bonus==null){
                    expected_bonus=new BigDecimal(0.00);
                }
                expected_bonus = expected_bonus.add(bonus_balance);
                ownerBalanceRecord.setBalance(new BigDecimal(0.00));
            }
            return ownerBalanceRecord;
        } else {
            OwnerBalanceRecord ownerBalanceRecord = new OwnerBalanceRecord();
            ownerBalanceRecord.setBalance(new BigDecimal(0.00));
            ownerBalanceRecord.setExpected_bonus(new BigDecimal(0.00));
            return ownerBalanceRecord;
        }

    }

    public int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return monthsDiff;
    }

}