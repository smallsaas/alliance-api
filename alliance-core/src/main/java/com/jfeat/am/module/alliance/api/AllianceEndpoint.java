package com.jfeat.am.module.alliance.api;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.dao.mapping.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import com.jfeat.am.module.alliance.util.AllianceUtil;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.dao.DuplicateKeyException;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;

import java.math.BigDecimal;

import com.jfeat.am.module.alliance.services.domain.service.AllianceService;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;

import javax.annotation.Resource;
import java.rmi.ServerException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;



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
    private final Integer ALLIANCE_TYPE_BONUS=1;
    private final Integer ALLIANCE_TYPE_COMMON=2;

    private Long millisecond=86400000L;//24 * 60 * 60 * 1000 毫秒
    //@BusinessLog(name = "Alliance", value = "create Alliance")
    @PostMapping
    @ApiOperation(value = "新建 Alliance", response = Alliance.class)
    public Tip createAlliance(@RequestHeader(required = false,name = "X-USER-ID") Long userId,@RequestBody AllianceRequest entity) throws ServerException, ParseException {
        entity.setCreationTime(new Date());
        entity.setStartingCycle(new Date());
        entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
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
        if (entity.getAllianceType().equals(Alliance.ALLIANCE_TYPE_COMMON)) {
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
            entity.setTempAllianceExpiryTime(new Date((new Date().getTime()+configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_TEMP_ALLIANCE_EXPIRY_TIME)*millisecond)));

            entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
        } else if (entity.getAllianceType().equals(Alliance.ALLIANCE_TYPE_BONUS)) {
//            entity.setAllianceShipTime(new Date());
            entity.setTempAllianceExpiryTime(new Date((new Date().getTime()+configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_TEMP_ALLIANCE_EXPIRY_TIME)* millisecond)));
            entity.setStockholderShip(1);
            entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));

        }
        Integer affected = 0;
        try {
            affected = allianceService.createMaster(entity);

        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
    }

    //@BusinessLog(name = "Alliance", value = "查看 Alliance")
    @GetMapping("/{id}")
    @ApiOperation(value = "查看 Alliance", response = Alliance.class)
    public Tip getAlliance(@PathVariable Long id) {
        return SuccessTip.create(queryAllianceDao.allianceDetails(id));
    }


    //@BusinessLog(name = "Alliance", value = "update Alliance")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改 Alliance", response = Alliance.class)
    public Tip updateAlliance(@PathVariable Long id, @RequestBody AllianceRequest entity) throws ServerException, ParseException {
        entity.setId(id);
        if(entity.getAllianceDob()!=null){
            entity.setAge( AllianceUtil.getAgeByBirth(entity.getAllianceDob()));
        }
        List alliance_phone = queryAllianceDao.selectList(new Condition().eq(Alliance.ALLIANCE_PHONE, entity.getAlliancePhone()).ne(Alliance.ID, id));
        if (alliance_phone.size() > 0) {
            throw new ServerException("该手机号以被注册盟友，不能重复");
        }
        //根据邀请人电话查找邀请人信息
        Alliance alliance = null;
        if (entity.getInvitorPhoneNumber() != null && entity.getInvitorPhoneNumber().length() > 0) {

            alliance = allianceService.findAllianceByPhoneNumber(entity.getInvitorPhoneNumber());

        }
        if (alliance != null) {
            Alliance allianceShip = allianceService.retrieveMaster(id);
//            if(allianceShip.getAllianceShip()!=null&&allianceShip.getAllianceShip()==1){
//                if(allianceShip.getTempAllianceExpiryTime()!=null&&new Date().getTime()<allianceShip.getTempAllianceExpiryTime().getTime()){
//                    throw new ServerException("临时盟友不能修改邀请人，请将邀请人手机号去掉");
//                }
//            }
            entity.setInvitorAllianceId(alliance.getId());
        }
        if (entity.getAllianceType().equals(Alliance.ALLIANCE_TYPE_COMMON)) {
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
            entity.setTempAllianceExpiryTime(new Date((new Date().getTime()+configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_TEMP_ALLIANCE_EXPIRY_TIME)* millisecond)));
//            entity.setAllianceShip(1);

        } else if (entity.getAllianceType().equals(Alliance.ALLIANCE_TYPE_BONUS)) {
            entity.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
//            entity.setAllianceShipTime(new Date());
            entity.setTempAllianceExpiryTime(new Date((new Date().getTime()+configFieldService.getFieldInteger(AllianceFields.ALLIANCE_FIELD_TEMP_ALLIANCE_EXPIRY_TIME)* millisecond)));
//            entity.setStockholderShip(1);
//            entity.setAllianceShip(2);
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));



        }
        Alliance user = queryAllianceDao.selectById(id);
        entity.setUserId(user.getUserId());
        return SuccessTip.create(allianceService.updateMaster(entity));
    }

    //@BusinessLog(name = "Alliance", value = "delete Alliance")
    @DeleteMapping("/{id}")
    @ApiOperation("删除 Alliance")
    public Tip deleteAlliance(@PathVariable Long id) {
        return SuccessTip.create(allianceService.deleteMaster(id));
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
            @ApiImplicitParam(name = "balance", dataType = "BigDecimal"),
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
                              @RequestParam(name = "allianceShipTime", required = false) Date allianceShipTime,
                              @RequestParam(name = "stockholderShipTime", required = false) Date stockholderShipTime,
                              @RequestParam(name = "startingCycle", required = false) Date startingCycle,
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
        record.setStartingCycle(startingCycle);
        record.setBalance(balance);
        record.setAllianceRank(allianceRank);
        record.setInvitorAllianceId(invitorAllianceId);
        record.setAllianceType(allianceType);
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
        page.setRecords(queryAllianceDao.findAlliancePage(page, record, search, orderBy, null, null));

        return SuccessTip.create(page);
    }

    @GetMapping("/getAlliancesByUserId")
    @ApiOperation(value = "根据请求头X-USER-ID获取我的盟友列表", response = Alliance.class)
    public Tip getAlliancesByUserId(@RequestHeader("X-USER-ID") Long id) {

        return SuccessTip.create(allianceService.getAlliancesByUserId(id));
    }

    @GetMapping("/getAllianceInformationByUserId")
    @ApiOperation(value = "根据请求头X-USER-ID获得个人的盟友信息，currentMonthOrder是当月订单", response = Alliance.class)
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
        return SuccessTip.create(alliance);
    }

    @GetMapping("/getAllianceInformationByUserId/{id}")
    @ApiOperation(value = "根据盟友id获取我的盟友信息,携带自营商品", response = Alliance.class)
    public Tip getSelfProductById(@PathVariable Long id) {

        return SuccessTip.create(allianceService.getSelfProductById(id));
    }
    @PutMapping("/updateAllianceShip/{id}")
    @ApiOperation("修改盟友确认支付状态")
    @Transactional
    public Tip modifyAllianceShip(@PathVariable Long id){
        Alliance alliance = allianceService.retrieveMaster(id);
        if(alliance==null){
            throw new BusinessException(BusinessCode.BadRequest,"该盟友不存在");
        }
        alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_PAID);
        alliance.setAllianceShipTime(new Date());
        allianceService.updateMaster(alliance);

        Long userId = alliance.getUserId();
        if(userId==null){
            userId=1L;
        }
        Wallet walletCondition = new Wallet();

        List<Wallet> wallets = queryWalletDao.selectList(new Condition().eq(Wallet.USER_ID,userId));
        Wallet wallet=null;
        if(wallets!=null&&wallets.size()>0){
             wallet = wallets.get(0);
        }else {
            wallet=new Wallet();
            if(userId!=null){
                wallet.setUserId(userId);
            }
        }

        String tmp=null;
        if(alliance.getAllianceType().equals(ALLIANCE_TYPE_BONUS)){
            tmp=AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE;
        }else if(alliance.getAllianceType().equals(ALLIANCE_TYPE_COMMON)){
            tmp=AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE;
        }else {
            throw new BusinessException(BusinessCode.CodeBase,"该盟友状态可能有误");
        }

        if(wallet.getId()==null){
            ///设置初始累计额度
            walletCondition.setAccumulativeAmount(new BigDecimal(configFieldService.getFieldFloat(tmp)));
            walletCondition.setGiftBalance(new BigDecimal(0));
            walletCondition.setBalance(new BigDecimal(configFieldService.getFieldFloat(tmp)));
            walletCondition.setUserId(userId);
            queryWalletDao.insert(walletCondition);
            WalletHistory walletHistory = new WalletHistory();
            walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
            walletHistory.setAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
            walletHistory.setGift_amount(new BigDecimal(0));
            walletHistory.setWalletId(new Long(walletCondition.getId()));
            walletHistory.setType(RechargeType.RECHARGE);
            queryWalletHistoryDao.insert(walletHistory);

        }else{

            if(wallet.getAccumulativeAmount()!=null){
                BigDecimal common_alliance = wallet.getBalance().add(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                wallet.setBalance(common_alliance);
                queryWalletDao.updateById(wallet);
            }else{
                wallet.setAccumulativeAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_COMMON_ALLIANCE)));
                queryWalletDao.updateById(wallet);
            }
            WalletHistory walletHistory = new WalletHistory();
            walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
            walletHistory.setAmount(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_BONUS_ALLIANCE)));
            walletHistory.setGift_amount(new BigDecimal(0));
            walletHistory.setWalletId(new Long(wallet.getId()));
            walletHistory.setType(RechargeType.RECHARGE);
            queryWalletHistoryDao.insert(walletHistory);

        }
        return SuccessTip.create(1);
    }

    @PostMapping("/{id}/action/setpaid")
    @ApiOperation("修改盟友支付状态-设置为已支付   ")
    @Transactional
    public Tip paid(@PathVariable Long id){
        Alliance alliance = allianceService.retrieveMaster(id);
        if(alliance==null){
            throw new BusinessException(BusinessCode.BadRequest,"该盟友不存在");
        }

        if(alliance.getAllianceShip().equals(AllianceShips.ALLIANCE_SHIP_INVITED)){
            alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_PAID);
            alliance.setAllianceShipTime(new Date());
        }else {
            throw new BusinessException(BusinessCode.CodeBase,"状态错误");
        }

        int res = allianceService.updateMaster(alliance);
        return SuccessTip.create(res);
    }

    @PostMapping("/{id}/action/reset")
    @ApiOperation("修改盟友支付状态-支付过期-->待支付  ship 4--->2")
    @Transactional
    public Tip reset(@PathVariable Long id){
        Alliance alliance = allianceService.retrieveMaster(id);
        if(alliance==null){
            throw new BusinessException(BusinessCode.BadRequest,"该盟友不存在");
        }

        alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);
        int res = allianceService.updateMaster(alliance);

        return SuccessTip.create(res);
    }

    @PostMapping("/{id}/action/upgraded")
    @ApiOperation("修改盟友支付状态- 升级盟友 --->  普通盟友---> 分红盟友。type  1--->2")
    @Transactional
    public Tip upgrade(@PathVariable Long id){
        Alliance alliance = allianceService.retrieveMaster(id);
        if(alliance==null){
            throw new BusinessException(BusinessCode.BadRequest,"该盟友不存在");
        }
        if(!alliance.getAllianceShip().equals(AllianceShips.ALLIANCE_SHIP_OK)){
            throw new BusinessException(BusinessCode.CodeBase,"非正式盟友，无法执行升级操作！");//alliacneShip=0 才能 升级盟友
        }

        if(!alliance.getAllianceType().equals(Alliance.ALLIANCE_TYPE_COMMON)){
            alliance.setAllianceType(Alliance.ALLIANCE_TYPE_BONUS);
        }else {
            throw new BusinessException(BusinessCode.CodeBase,"非普通盟友身份，无法执行升级操作！");
        }
        alliance.setAllianceType(Alliance.ALLIANCE_TYPE_BONUS);
        alliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_INVITED);


        int res = allianceService.updateMaster(alliance);

        return SuccessTip.create(res);
    }



}
