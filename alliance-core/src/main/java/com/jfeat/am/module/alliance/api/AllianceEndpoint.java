package com.jfeat.am.module.alliance.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.common.annotation.Permission;
import com.jfeat.am.core.jwt.JWTKit;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletHistoryDao;

import com.jfeat.am.module.alliance.services.domain.definition.AlliancePermission;
import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import com.jfeat.am.module.alliance.util.AllianceUtil;
import com.jfeat.am.module.alliance.util.Md5Utils;
import com.jfeat.am.module.alliance.util.RestClient;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import com.jfeat.am.module.log.annotation.BusinessLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;

import java.io.*;
import java.math.BigDecimal;

import com.jfeat.am.module.alliance.services.domain.service.AllianceService;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;

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
@RequestMapping("/api/crud/alliance/alliances")
public class AllianceEndpoint {
    Logger logger = LoggerFactory.getLogger(AllianceEndpoint.class.getName());

    private final static RestClient rest = new RestClient();
    @Resource
    AllianceService allianceService;

    @Resource
    QueryAllianceDao queryAllianceDao;

    @Resource
    ConfigFieldService configFieldService;

    @Resource
    QueryWalletDao queryWalletDao;

    @Resource
    QueryWalletHistoryDao queryWalletHistoryDao;

    @Resource
    QueryOwnerBalanceDao queryOwnerBalanceDao;


    private final Integer ALLIANCE_TYPE_BONUS = 1;
    private final Integer ALLIANCE_TYPE_COMMON = 2;

    private Long millisecond = 86400000L;//24 * 60 * 60 * 1000 ?????? ??????


    @BusinessLog(name = "??????", value = "????????????")
    @PostMapping
    @ApiOperation(value = "?????? Alliance", response = Alliance.class)
    @Permission(AlliancePermission.ALLIANCE_ADD)
    public Tip createAlliance(@RequestHeader(required = false, name = "X-USER-ID") Long userId, @RequestBody AllianceRequest entity) throws ServerException, ParseException {
        Integer affected = allianceService.create(userId, entity);
        return SuccessTip.create(affected);
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "?????? Alliance", response = Alliance.class)
    @Permission(AlliancePermission.ALLIANCE_VIEW)
    public Tip getAlliance(@PathVariable Long id) {
        return SuccessTip.create(queryAllianceDao.allianceDetails(id));
    }


    @BusinessLog(name = "??????", value = "????????????")
    @PutMapping("/{id}")
    @ApiOperation(value = "?????? Alliance", response = Alliance.class)
    @Permission(AlliancePermission.ALLIANCE_EDIT)
    public Tip updateAlliance(@PathVariable Long id, @RequestBody AllianceRecord entity) throws ServerException, ParseException {

        return SuccessTip.create(allianceService.modify(id, entity));
    }

    @BusinessLog(name = "??????", value = "????????????")
    @DeleteMapping("/{id}")
    @ApiOperation("?????? Alliance")
    @Permission(AlliancePermission.ALLIANCE_DEL)
    public Tip deleteAlliance(@PathVariable Long id) {
        Alliance alliance = queryAllianceDao.selectOne(new LambdaQueryWrapper<Alliance>(new Alliance().setId(id)));
        if (alliance != null) {
            Long userId = queryAllianceDao.queryUserIdByPhone(alliance.getAlliancePhone());
            if (userId != null && userId > 0) {
                Wallet wallet = queryWalletDao.selectOne(new LambdaQueryWrapper<>(new Wallet().setUserId(userId)));
                if (wallet != null) {
                    queryWalletHistoryDao.delete(new QueryWrapper<WalletHistory>().eq(WalletHistory.WALLET_ID, wallet.getId()));
                    queryWalletDao.deleteById(wallet.getId());
                }
            }
        }
        return SuccessTip.create(allianceService.deleteMaster(id));
    }

    //@BusinessLog(name = "Alliance", value = "delete Alliance")
    @ApiOperation(value = "Alliance ????????????", response = AllianceRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Long"),
            @ApiImplicitParam(name = "userId", dataType = "Long"),
            @ApiImplicitParam(name = "invitorAllianceId", dataType = "Long"),
            @ApiImplicitParam(name = "allianceType", dataType = "Integer"),
            @ApiImplicitParam(name = "allianceShip", dataType = "Integer"),
            @ApiImplicitParam(name = "stockholderShip", dataType = "Integer"),
            @ApiImplicitParam(name = "creationTime", dataType = "Date"),
            @ApiImplicitParam(name = "allianceRank", dataType = "Integer"),
            @ApiImplicitParam(name = "allianceShipTime", dataType = "Date"),
            @ApiImplicitParam(name = "stockholderShipTime", dataType = "Date"),
            @ApiImplicitParam(name = "tempAllianceExpiryTime", dataType = "Date"),
            @ApiImplicitParam(name = "startingCycle", dataType = "Date"),
            @ApiImplicitParam(name = "allianceStatus", dataType = "Integer"),
            @ApiImplicitParam(name = "allianceInventoryAmount", dataType = "BigDecimal"),
            @ApiImplicitParam(name = "alliancePoint", dataType = "BigDecimal"),
//            @ApiImplicitParam(name = "balance", dataType = "BigDecimal"),
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
    @Permission(AlliancePermission.ALLIANCE_VIEW)
    public Tip queryAlliances(Page<AllianceRecord> page,
                              @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                              @RequestParam(name = "search", required = false) String search,
                              @RequestParam(name = "id", required = false) Long id,
                              @RequestParam(name = "userId", required = false) Long userId,
                              @RequestParam(name = "invitorAllianceId", required = false) Long invitorAllianceId,
                              @RequestParam(name = "allianceType", required = false) Integer allianceType,
                              @RequestParam(name = "allianceShip", required = false) Integer allianceShip,
                              @RequestParam(name = "stockholderShip", required = false) Integer stockholderShip,
                              @RequestParam(name = "creationTime", required = false) Date creationTime,
                              @RequestParam(name = "allianceRank", required = false) Integer allianceRank,

                              @RequestParam(name = "stockholderShipTime", required = false) Date stockholderShipTime,
                              @RequestParam(name = "balance", required = false) BigDecimal balance,
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
                              @DateTimeFormat(pattern = "yyyy-MM-dd")
                              @RequestParam(name = "allianceShipTime", required = false) Date allianceShipTime[],
                              @RequestParam(name = "searchNumber", required = false) Integer searchNumber[],
                              @RequestParam(name = "sort", required = false) String sort) {
        if (orderBy != null && orderBy.length() > 0) {
            if (sort != null && sort.length() > 0) {
                String pattern = "(ASC|DESC|asc|desc)";
                if (!sort.matches(pattern)) {
                    throw new BusinessException(BusinessCode.BadRequest.getCode(), "sort must be ASC or DESC");//??????????????????????????????????????????
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
        /*record.setStartingCycle(startingCycle);*/
        record.setBalance(balance);
        record.setAllianceRank(allianceRank);
        record.setInvitorAllianceId(invitorAllianceId);
        record.setAllianceType(allianceType);
        record.setAllianceShip(allianceShip);
        record.setStockholderShip(stockholderShip);
        record.setCreationTime(creationTime);

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
        List<AllianceRecord> alliancePage = null;
        Date allianceShipTimeStartTime = allianceShipTime!=null? (allianceShipTime.length > 0?allianceShipTime[0]:null) : null;
        Date allianceShipTimeEndTime = allianceShipTime!=null ? (allianceShipTime.length==2?allianceShipTime[1]:(allianceShipTime.length==1?allianceShipTime[0]:null)) : null;

        Integer leftNumber = searchNumber!=null? (searchNumber.length > 0?searchNumber[0]:null) : null;
        Integer rightNumber = searchNumber!=null ? (searchNumber.length==2?searchNumber[1]:(searchNumber.length==1?searchNumber[0]:null)) : null;



        try {
            alliancePage = queryAllianceDao
                    .findAlliancePage
                            (page, record, search, orderBy,
                                    allianceShipTimeStartTime, allianceShipTimeEndTime,leftNumber,rightNumber);
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
//        Date end = calculationEndTime();
//        for(AllianceRecord allianceRecord: alliancePage){
//            allianceRecord.setCutOffTime(end);
//            if(allianceRecord.getBalance()!=null&&allianceRecord.getBalance().compareTo(new BigDecimal(0))==0){
//                if(allianceRecord.getAllianceType()==Alliance.ALLIANCE_TYPE_BONUS){
//                    allianceRecord.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
//                }else {
//                    allianceRecord.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
//                }
//            }
//        }

        page.setRecords(alliancePage);
        return SuccessTip.create(page);
    }

    private Date calculationEndTime() {
        //????????????
        String str = configFieldService.getFieldString(AllianceFields.ALLIANCE_FIELD_STARTING_TIME);
        //????????????
        Integer endMonth = configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_STARTING_CYCLE);
        Date starting = null;
        try {
            starting = new SimpleDateFormat("yyyy-MM-dd").parse(str);
        } catch (ParseException e) {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????????????????");
        }
        //????????????????????????
        return AllianceUtil.stepMonth(starting, endMonth);
    }


    @GetMapping("/getAlliancesByUserId")
    @ApiOperation(value = "???????????????X-USER-ID????????????????????????", response = Alliance.class)
    public Tip getAlliancesByUserId(@RequestHeader("X-USER-ID") Long id) {
        List<Alliance> alliances = allianceService.getAlliancesByUserId(id);
//        if(alliances!=null&&alliances.size()>0){
//            for(Alliance alliance:alliances){
//                alliance.setCutOffTime(calculationEndTime());
//            }
//        }
        return SuccessTip.create(alliances);
    }

    @GetMapping("/getAllianceInformationByUserId")
    @ApiOperation(value = "???????????????X-USER-ID??????????????????????????????currentMonthOrder???????????????", response = Alliance.class)
    @Permission(AlliancePermission.ALLIANCE_VIEW)
    public Tip getAllianceInformationByUserId(@RequestHeader("X-USER-ID") Long id) {
        Alliance entity = new Alliance();
        entity.setUserId(id);
        AllianceRecord alliance = queryAllianceDao.selectAllianceOneByUserId(id);
        if (alliance == null) {
            return SuccessTip.create(alliance);
        }
        List<Map> currentMonthOrderByUserId = queryAllianceDao.getCurrentMonthOrderByUserId(id);
        if (currentMonthOrderByUserId != null && currentMonthOrderByUserId.size() > 0) {
            alliance.setCurrentMonthOrder(JSON.parseArray(JSON.toJSONString(queryAllianceDao.getCurrentMonthOrderByUserId(id))));
        }
        alliance.setCutOffTime(calculationEndTime());
        return SuccessTip.create(alliance);
    }

    @GetMapping("/getAllianceInformationByUserId/{id}")
    @ApiOperation(value = "????????????id????????????????????????,??????????????????", response = Alliance.class)
    @Permission(AlliancePermission.ALLIANCE_VIEW)
    public Tip getSelfProductById(@PathVariable Long id) {
        AllianceRecord allianceRecord = allianceService.getSelfProductById(id);
        allianceRecord.setCutOffTime(calculationEndTime());
        return SuccessTip.create(allianceService.getSelfProductById(id));
    }

    @BusinessLog(name = "??????", value = "??????????????????????????????")
    @PutMapping("/updateAllianceShip/{id}")
    @ApiOperation("??????????????????????????????")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE)
    public Tip modifyAllianceShip(@PathVariable Long id) {
        return SuccessTip.create(allianceService.modifyAllianceShip(id));
    }

    @BusinessLog(name = "??????", value = "????????????????????????-??????????????????")
    @PostMapping("/{id}/action/setpaid")
    @ApiOperation("????????????????????????-??????????????????   ")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE)
    public Tip paid(@PathVariable Long id) {
        Alliance alliance = allianceService.retrieveMaster(id);
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????");
        }

        if (alliance.getAllianceShip().equals(AllianceShips.ALLIANCE_SHIP_EXISTPAID)) {
//        if (alliance.getAllianceShip().equals(AllianceShips.ALLIANCE_SHIP_INVITED)) {
            alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_PAID);
            alliance.setAllianceShipTime(new Date());
        } else {
            throw new BusinessException(BusinessCode.CodeBase, "????????????");
        }
        int res = allianceService.updateMaster(alliance);
        return SuccessTip.create(res);
    }

    @BusinessLog(name = "??????", value = "????????????????????????-??????????????????")
    @PostMapping("/{id}/action/setexistpaid")
    @ApiOperation("????????????????????????-??????????????????   ")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE)
    public Tip setexistpaid(@PathVariable Long id) {
        Alliance alliance = allianceService.retrieveMaster(id);
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????");
        }
        Long userId = queryAllianceDao.queryUserIdByPhone(alliance.getAlliancePhone());
        //???????????? "bonus_alliance";
        Float bonusConfig = configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE);
        //???????????? "common_alliance";
        Float commonConfig = configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE);
        BigDecimal defaultBonus=new BigDecimal(0);
        //?????????????????? ???????????? ??????????????????
        if(alliance.getAllianceType().equals(Alliance.ALLIANCE_TYPE_BONUS)){
            defaultBonus=new BigDecimal(bonusConfig);
        }else{
            defaultBonus =new BigDecimal(commonConfig);
        }
        //?????? ????????? ??????
        alliance.setAllianceInventoryAmount(defaultBonus);
        int res=0;
        //??????????????????
        if (alliance.getAllianceShip().equals(AllianceShips.ALLIANCE_SHIP_INVITED)) {
            alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_EXISTPAID);
            if (userId!= null) {
                /*Wallet wallet = queryWalletDao.selectOne(new Wallet().setUserId(alliance.getUserId()));
                if (wallet != null) {
                    wallet.setBalance(defaultBonus);
                    wallet.setAccumulativeAmount(defaultBonus);
                    res += queryWalletDao.updateById(wallet);
                    WalletHistory walletHistory = new WalletHistory();
                    //walletHistory.setAmount(new BigDecimal(bonusConfig));
                    walletHistory.setAmount(defaultBonus);
                    walletHistory.setBalance(defaultBonus);
                    walletHistory.setWalletId(wallet.getId());
                    walletHistory.setNote("???????????????");
                    walletHistory.setType(RechargeType.RECHARGE);
                    walletHistory.setCreatedTime(new Date());
                    res += queryWalletHistoryDao.insert(walletHistory);
                } else {*/
                List<Wallet> list = queryWalletDao.selectList(new QueryWrapper<Wallet>().eq(Wallet.USER_ID, userId));
                if(list!=null&&list.size()>1){
                    for(int i=0;i>list.size();i++){
                        if(i>0){
                            queryWalletDao.deleteById(list.get(i).getId());
                        }
                    }
                }else if(list.size()==1){
                    Wallet wallet=list.get(0);
                    BigDecimal balance = wallet.getBalance();
                    if(balance==null){
                        balance=new BigDecimal(0.00);
                    }
                    wallet.setBalance(balance.add(defaultBonus));
                    BigDecimal accumulativeAmount = wallet.getAccumulativeAmount();
                    if(accumulativeAmount==null){
                        accumulativeAmount=new BigDecimal(0.00);
                    }
                    wallet.setAccumulativeGiftAmount(accumulativeAmount.add(defaultBonus));
                    queryWalletDao.updateById(wallet);
                    WalletHistory walletHistory = new WalletHistory();
                    walletHistory.setAmount(defaultBonus);
                    walletHistory.setBalance(defaultBonus);
                    walletHistory.setWalletId(wallet.getId());
                    walletHistory.setNote("???????????????");
                    walletHistory.setType(RechargeType.RECHARGE);
                    walletHistory.setCreatedTime(new Date());
                    res += queryWalletHistoryDao.insert(walletHistory);
                }else{
                    Wallet  wallet = new Wallet().setUserId(userId);
                    wallet.setBalance(defaultBonus);
                    wallet.setAccumulativeAmount(defaultBonus);
                    res += queryWalletDao.insert(wallet);
                    WalletHistory walletHistory = new WalletHistory();
                    walletHistory.setAmount(defaultBonus);
                    walletHistory.setBalance(defaultBonus);
                    walletHistory.setWalletId(wallet.getId());
                    walletHistory.setNote("???????????????");
                    walletHistory.setType(RechargeType.RECHARGE);
                    walletHistory.setCreatedTime(new Date());
                    res += queryWalletHistoryDao.insert(walletHistory);
                }

            }
            //alliance.setAllianceShipTime(new Date());
        } else {
            throw new BusinessException(BusinessCode.CodeBase, "????????????");
        }
        res = allianceService.updateMaster(alliance);
        return SuccessTip.create(res);
    }

    @PostMapping("/{id}/action/reset")
    @ApiOperation("???????????? ????????????????????????-????????????-->?????????  ship 4--->2")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE)
    public Tip reset(@PathVariable Long id) {
        Alliance alliance = allianceService.retrieveMaster(id);
        Long userId = alliance.getUserId();
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????");
        }
        Integer allianceType = alliance.getAllianceType();
        //allianceType???????????? ?????????????????????
        if(allianceType==null){
            allianceType=AllianceType.ALLIANCE_TYPE_NORMAL;
            alliance.setAllianceType(allianceType);
        }

       //???????????? "bonus_alliance";
        Float bonusConfig = configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE);
        //???????????? "common_alliance";
        Float commonConfig = configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE);
        //???????????? ???????????????  ?????????
        alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
        //??????0
        alliance.setAllianceInventoryAmount(new BigDecimal(0));
        int res = allianceService.updateMaster(alliance);
        //  ????????????  userId--???null
        /*res += queryAllianceDao.resetUserId(id);*/
        if (alliance.getUserId() != null) {
            Wallet wallet = queryWalletDao.selectOne(new LambdaQueryWrapper<>(new Wallet().setUserId(userId)));
            if (wallet != null) {
                //???????????????
                res += queryOwnerBalanceDao.delete(new QueryWrapper<OwnerBalance>().eq("user_id", alliance.getUserId()));
                //???????????????
                res += queryWalletHistoryDao.delete(new QueryWrapper<WalletHistory>().eq("wallet_id", wallet.getId()));
                //?????????
                res += queryWalletDao.deleteById(wallet.getId());
            }
        }

        return SuccessTip.create(res);
    }
    @Deprecated
    @BusinessLog(name = "??????", value = "??????????????????")
    @PostMapping("/{id}/action/resetbalance")
    @ApiOperation("??????????????????")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE)
    public Tip reSetAllianceBalance(@PathVariable Long id) {
        int res=0;
        //????????????id????????????
        Alliance alliance= queryAllianceDao.selectOne(new QueryWrapper<>(new Alliance().setId(id)));
        //????????????????????????
        if(alliance.getUserId()==null){
            throw new BusinessException(BusinessCode.BadRequest, "???????????????????????????");
        }
        BigDecimal defaultBalance =alliance.getAllianceInventoryAmount();
        //??????Userid????????????
        List<Wallet> walletList = queryWalletDao.selectList(new QueryWrapper<Wallet>().eq("user_id",alliance.getUserId()));
        Wallet wallet=null;
        if(walletList==null||walletList.size()==0){
            Wallet insertWallet =new Wallet()
                    .setUserId(alliance.getUserId())
                    .setBalance(defaultBalance)
                    .setAccumulativeAmount(defaultBalance);
            queryWalletDao.insert(insertWallet);
            wallet=queryWalletDao.selectOne(new QueryWrapper<>(insertWallet));
        }else
        {
            wallet=walletList.get(0);
            wallet.setBalance(defaultBalance)
                    .setAccumulativeAmount(defaultBalance);
            queryWalletDao.updateById(wallet);
            queryWalletHistoryDao.delete(new QueryWrapper<WalletHistory>().eq("wallet_id",wallet.getId()));
        }
        queryWalletHistoryDao.insert(new WalletHistory()
                .setWalletId(wallet.getId())
                .setNote("????????????")
                .setAmount(defaultBalance)
                .setType("CHARGE")
                .setBalance(defaultBalance)
                .setCreatedTime(new Date()));
        return SuccessTip.create(res);
    }


    @PostMapping("/{id}/action/upgraded")
    @ApiOperation("????????????????????????- ???????????? --->  ????????????---> ???????????????type  1--->2")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE_UP)
    public Tip upgrade(@PathVariable Long id) {
        Alliance alliance = allianceService.retrieveMaster(id);
        Long userId = alliance.getUserId();
        Integer allianceType = alliance.getAllianceType();
        Float bonus = configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE);
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????");
        }
        if (!alliance.getAllianceShip().equals(AllianceShips.ALLIANCE_SHIP_OK)) {
            throw new BusinessException(BusinessCode.CodeBase, "?????????????????????????????????????????????");//alliacneShip=0 ?????? ????????????
        }

        if (alliance.getAllianceType().equals(Alliance.ALLIANCE_TYPE_COMMON)) {
            alliance.setAllianceType(Alliance.ALLIANCE_TYPE_BONUS);
        } else {
            throw new BusinessException(BusinessCode.CodeBase, "???????????????????????????????????????????????????");
        }
        alliance.setAllianceType(Alliance.ALLIANCE_TYPE_BONUS);
        alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
        alliance.setAllianceInventoryAmount(new BigDecimal(bonus));
        int res = allianceService.updateMaster(alliance);

        //??????????????????
        Float bonusConfig = configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE);
        Float commonConfig = configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE);
        Wallet wallet = queryWalletDao.selectOne(new LambdaQueryWrapper<>( new Wallet().setUserId(userId)));
        if (wallet != null) {
            if (allianceType.equals(Alliance.ALLIANCE_TYPE_COMMON)) {
                if (wallet.getBalance() != null && wallet.getBalance().intValue() > 0) {
                    BigDecimal subtract = wallet.getBalance().subtract(new BigDecimal(commonConfig));

                    //????????????0
//                    if (subtract.intValue() < 0) {
//                        subtract = new BigDecimal(0.00);
//                    }

                    wallet.setBalance(subtract);
                } else if (allianceType.equals(Alliance.ALLIANCE_TYPE_BONUS)) {
                    BigDecimal subtract = wallet.getBalance().subtract(new BigDecimal(bonusConfig));
                    //????????????0
//                    if (subtract.intValue() < 0) {
//                        subtract = new BigDecimal(0.00);
//                    }
                    wallet.setBalance(subtract);
                }
            }
        }
        res += queryWalletDao.updateById(wallet);
        return SuccessTip.create(res);
    }

    @PostMapping("/{id}/action/upwallet")
    @ApiOperation("??????????????????")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE_UP)
    public Tip upwallet(@PathVariable Long id, @RequestBody RechargeBalance rechargeBalance) {
        Alliance alliance = allianceService.retrieveMaster(id);
        BigDecimal balance = rechargeBalance.getBalance();
        Integer res = 0;
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????");
        }
        Long userId = alliance.getUserId();
        Wallet wallet = null;
        if (userId != null && userId > 0) {
            wallet = queryWalletDao.selectOne(new LambdaQueryWrapper<>(new Wallet().setUserId(userId)));
        } else {
            throw new BusinessException(BusinessCode.BadRequest, "?????????????????????????????????????????????");
        }
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(balance);
            wallet.setAccumulativeAmount(balance);
            res += queryWalletDao.insert(wallet);
        } else {
            BigDecimal balance1 = wallet.getBalance();
            if (balance1 == null) {
                balance1 = new BigDecimal(0.00);
            }
            wallet.setBalance(balance1.add(balance));
            BigDecimal accumulativeAmount = wallet.getAccumulativeAmount();
            if (accumulativeAmount == null) {
                accumulativeAmount = new BigDecimal(0.00);
            }
            wallet.setAccumulativeAmount(accumulativeAmount.add(balance));
            res += queryWalletDao.updateById(wallet);
        }

        WalletHistory walletHistory = new WalletHistory();
        walletHistory.setWalletId(wallet.getId());
        if(balance.longValue()<0){
            walletHistory.setType(RechargeType.CASH_OUT);
            walletHistory.setNote("????????????-??????");
            walletHistory.setAmount(new BigDecimal(0).subtract(balance));
        }else {
            walletHistory.setType(RechargeType.RECHARGE);
            walletHistory.setNote("????????????-??????");
            walletHistory.setAmount(balance);
        }
        walletHistory.setBalance(wallet.getBalance());
        walletHistory.setCreatedTime(new Date());
        res += queryWalletHistoryDao.insert(walletHistory);
        return SuccessTip.create(res);
    }

    @GetMapping("/deleteTable")
    public Tip deleteTable(@RequestParam String table, @RequestParam String secretKey) throws IOException {
        Integer res = 0;
        Long time = (new Date().getTime()) / 1000;
        Long userid = JWTKit.getUserId();
        String base = userid + secretKey + time;
        String enc = Md5Utils.encrypt(base);
        String url = "http://team.muaskin.com/admin/webapi/deluser.html";
        JSONObject object = new JSONObject();
        object.put("signature", enc);
        object.put("userid", String.valueOf(1000));
        object.put("ctime", time);
        HttpEntity<String> requestEntity = new HttpEntity<String>(JSONObject.toJSONString(object));
        ResponseEntity<String> responseEntity = rest.exchange(url, HttpMethod.POST, requestEntity, String.class);
        String body = responseEntity.getBody();
        JSONObject parse = (JSONObject) JSON.parse(body);
        if (parse.get("code").equals(200)) {
            JSONObject data = (JSONObject) parse.get("data");
            if (data.get("code").equals(200)) {
                if (table != null && table.length() > 0)
                    res += queryAllianceDao.deleteTableData(table);
            } else {
                throw new BusinessException(BusinessCode.BadRequest, data.getString("msg"));
            }
        } else {
            throw new BusinessException(BusinessCode.BadRequest, parse.getString("message"));
        }

        return SuccessTip.create(parse);
    }

    @PostMapping("/{id}/action/downgrade")
    @ApiOperation("????????????")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE_UP)
    public Tip downgrade(@PathVariable Long id) {
        Alliance alliance = allianceService.retrieveMaster(id);
        Long userId = alliance.getUserId();
        Integer allianceType = alliance.getAllianceType();
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????");
        }
        if (!alliance.getAllianceShip().equals(AllianceShips.ALLIANCE_SHIP_OK)) {
            throw new BusinessException(BusinessCode.CodeBase, "?????????????????????????????????????????????");//alliacneShip=0 ?????? ????????????
        }

        if (alliance.getAllianceType().equals(Alliance.ALLIANCE_TYPE_BONUS)) {
            alliance.setAllianceType(Alliance.ALLIANCE_TYPE_COMMON);
        } else {
            throw new BusinessException(BusinessCode.CodeBase, "???????????????????????????????????????????????????");
        }
        alliance.setAllianceType(Alliance.ALLIANCE_TYPE_BONUS);
        alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
        int res = allianceService.updateMaster(alliance);
//        //??????????????????
//        Float bonusConfig = configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE);
//        Float commonConfig = configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE);
//        Wallet wallet = queryWalletDao.selectOne(new Wallet().setUserId(userId));
//        if (wallet != null) {
//            if (allianceType.equals(Alliance.ALLIANCE_TYPE_COMMON)) {
//                if (wallet.getBalance() != null && wallet.getBalance().intValue() > 0) {
//                    BigDecimal subtract = wallet.getBalance().subtract(new BigDecimal(commonConfig));
//
//                    //????????????0
////                    if (subtract.intValue() < 0) {
////                        subtract = new BigDecimal(0.00);
////                    }
//
//                    wallet.setBalance(subtract);
//                } else if (allianceType.equals(Alliance.ALLIANCE_TYPE_BONUS)) {
//                    BigDecimal subtract = wallet.getBalance().subtract(new BigDecimal(bonusConfig));
//                    //????????????0
////                    if (subtract.intValue() < 0) {
////                        subtract = new BigDecimal(0.00);
////                    }
//                    wallet.setBalance(subtract);
//                }
//            }
//        }
//        res += queryWalletDao.updateById(wallet);
        return SuccessTip.create(res);
    }

    @PostMapping("/{id}/action/upshiptime")
    @ApiOperation("???????????????????????????")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE_UP)
    public Tip upShipTime(@PathVariable Long id, @RequestBody AllianceRequestShipTime allianceRequestShipTime) {
        Alliance alliance = allianceService.retrieveMaster(id);
        Long userId = alliance.getUserId();
        Integer allianceType = alliance.getAllianceType();
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????");
        }
        alliance.setAllianceShipTime(allianceRequestShipTime.getAllianceShipTime());
        Integer res = queryAllianceDao.updateById(alliance);
        return SuccessTip.create(res);
    }

    @PostMapping("/{id}/action/logOff")
    @ApiOperation("????????????")
    @Permission(AlliancePermission.ALLIANCE_EDIT_STATE_UP)
    public Tip logOff(@PathVariable Long id) {
        Alliance alliance = allianceService.retrieveMaster(id);
        Integer res = 0;
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????");
        }
        alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_LOG_OFF);
        String alliancePhone = alliance.getAlliancePhone();
        if (alliancePhone != null && alliancePhone.length() > 0) {
            Long userId = queryAllianceDao.queryUserIdByPhone(alliancePhone);
            if (userId != null) {
                Wallet wallet = queryWalletDao.selectOne(new LambdaQueryWrapper<>(new Wallet().setUserId(userId)));
                if (wallet != null) {
                    BigDecimal balance = wallet.getBalance();
                    if (balance == null) {
                        balance = new BigDecimal(0.00);
                    }
                    alliance.setHistoricalBalance(balance);
                    alliance.setAllianceInventoryAmount(new BigDecimal(0.00));
                    res += queryAllianceDao.updateById(alliance);
                    res += queryWalletHistoryDao.delete(new QueryWrapper<WalletHistory>().eq(WalletHistory.WALLET_ID, wallet.getId()));
                    res += queryWalletDao.delete(new QueryWrapper<Wallet>().eq(Wallet.USER_ID, userId));
                }
            }

        }
        return SuccessTip.create(res);
    }

    @GetMapping("/withdrawalsRecord/{id}")
    @ApiOperation(value = "????????????", response = Tip.class)
    public Tip withdrawalsRecord(@PathVariable Long id) {
        Alliance alliance = allianceService.retrieveMaster(id);
        if (alliance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "???????????????");
        }
        String alliancePhone = alliance.getAlliancePhone();
        Long userId = queryAllianceDao.queryUserIdByPhone(alliancePhone);

        Wallet wallet = queryWalletDao.selectOne(new LambdaQueryWrapper<>(new Wallet().setUserId(userId)));
        if (wallet != null) {
            List<WalletHistory> list = queryWalletHistoryDao.selectList(
                    new LambdaQueryWrapper<WalletHistory>()
                    .eq(WalletHistory::getType, RechargeType.RECHARGE)
                            .or()
                            .eq(WalletHistory::getType, RechargeType.CASH_OUT)
                    .and(u->u.eq(WalletHistory::getWalletId, wallet.getId())
                    .orderBy(true, false, WalletHistory::getCreatedTime)));

            return SuccessTip.create(list);
        } else {
            throw new BusinessException(BusinessCode.BadRequest, "??????????????????");
        }

    }

    @PutMapping("/unbind/{id}")
    @ApiOperation(value = "????????????", response = Tip.class)
    public Tip Unbind(@PathVariable Long id){
        Integer unbind = allianceService.Unbind(id);
        return SuccessTip.create(unbind);
    }

}
