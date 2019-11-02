package com.jfeat.am.module.alliance.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.dao.mapping.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Royalty;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import com.jfeat.am.module.alliance.util.AllianceUtil;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import com.jfeat.util.Cip;
import com.jfeat.util.ErrorCip;
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
    QueryWalletHistoryDao queryWalletHistoryDao;

    //@BusinessLog(name = "Alliance", value = "create Alliance")
    @PostMapping
    @ApiOperation(value = "新建 Alliance", response = Alliance.class)
    public Cip createAlliance(@RequestHeader("X-USER-ID") Long userId,@RequestBody AllianceRequest entity) throws ServerException, ParseException {
        entity.setCreationTime(new Date());
        entity.setStartingCycle(new Date());
        entity.setAllianceShip(1);
        entity.setAllianceShipTime(new Date());
        if (entity.getAllianceDob() != null) {
            entity.setAge(AllianceUtil.getAgeByBirth(entity.getAllianceDob()));
        }
        List alliance_phone = queryAllianceDao.selectList(new Condition().eq("alliance_phone", entity.getAlliancePhone()));
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
        if (entity.getAllianceType().equals(2)) {
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat("common_alliance")));
            entity.setAllianceShip(2);
            Wallet walletCondition = new Wallet();
            if(userId!=null){
                walletCondition.setUserId(userId);
            }else if(entity.getUserId()!=null&&entity.getUserId()>0){
                walletCondition.setUserId(entity.getUserId());
            }
            Wallet wallet = queryWalletDao.selectOne(walletCondition);

            if(wallet==null){
                walletCondition.setBalance(new BigDecimal(configFieldService.getFieldFloat("common_alliance")));
                queryWalletDao.insert(walletCondition);
                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setCreatedTime(new Date());
                walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat("common_alliance")));
                walletHistory.setAmount(new BigDecimal(0));
                walletHistory.setGift_amount(new BigDecimal(0));
                walletHistory.setWalletId(new Long(walletCondition.getId()));
                queryWalletHistoryDao.insert(walletHistory);

            }else{

                if(wallet.getAccumulativeAmount()!=null){
                    BigDecimal common_alliance = wallet.getBalance().add(new BigDecimal(configFieldService.getFieldFloat("common_alliance")));
                    wallet.setBalance(common_alliance);
                    queryWalletDao.updateById(wallet);
                }else{
                    wallet.setAccumulativeAmount(new BigDecimal(configFieldService.getFieldFloat("common_alliance")));
                    queryWalletDao.updateById(wallet);

                }
                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat("common_alliance")));
                walletHistory.setAmount(new BigDecimal(0));
                walletHistory.setGift_amount(new BigDecimal(0));
                walletHistory.setWalletId(new Long(wallet.getId()));
                queryWalletHistoryDao.insert(walletHistory);

            }
        } else if (entity.getAllianceType().equals(1)) {
            entity.setStockholderShipTime(new Date());
            entity.setStockholderShip(1);
            entity.setAllianceShip(2);
            entity.setAllianceInventoryAmount(new BigDecimal(configFieldService.getFieldFloat("bonus_alliance")));
            Wallet walletCondition = new Wallet();
            if(userId!=null){
                walletCondition.setUserId(userId);
            }else if(entity.getUserId()!=null&&entity.getUserId()>0){
                walletCondition.setUserId(entity.getUserId());
            }
            Wallet wallet = queryWalletDao.selectOne(walletCondition);

            if(wallet==null){
                wallet=new Wallet();
                walletCondition.setAccumulativeAmount(new BigDecimal(0));
                walletCondition.setGiftBalance(new BigDecimal(0));
                walletCondition.setBalance(new BigDecimal(configFieldService.getFieldFloat("bonus_alliance")));
                queryWalletDao.insert(walletCondition);
                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat("bonus_alliance")));
                walletHistory.setAmount(new BigDecimal(0));
                walletHistory.setGift_amount(new BigDecimal(0));
                walletHistory.setWalletId(new Long(walletCondition.getId()));
                walletHistory.setType("充值");
                queryWalletHistoryDao.insert(walletHistory);

            }else{

                if(wallet.getAccumulativeAmount()!=null){
                    BigDecimal common_alliance = wallet.getBalance().add(new BigDecimal(configFieldService.getFieldFloat("common_alliance")));
                    wallet.setBalance(common_alliance);
                    queryWalletDao.updateById(wallet);
                }else{
                    wallet.setAccumulativeAmount(new BigDecimal(configFieldService.getFieldFloat("common_alliance")));
                    queryWalletDao.updateById(wallet);
                }
                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setBalance(new BigDecimal(configFieldService.getFieldFloat("bonus_alliance")));
                walletHistory.setAmount(new BigDecimal(0));
                walletHistory.setGift_amount(new BigDecimal(0));
                walletHistory.setWalletId(new Long(wallet.getId()));
                walletHistory.setType("充值");
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
        List alliance_phone = queryAllianceDao.selectList(new Condition().eq("alliance_phone", entity.getAlliancePhone()).ne("id", id));
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
            if(allianceShip.getAllianceShip()!=null&&allianceShip.getAllianceShip()==1){
                if(allianceShip.getTempAllianceExpiryTime()!=null&&new Date().getTime()<allianceShip.getTempAllianceExpiryTime().getTime()){
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
        page.setRecords(queryAllianceDao.findAlliancePage(page, record, search, orderBy, null, null));
        return SuccessCip.create(page);
    }

    @GetMapping("/getAlliancesByUserId")
    @ApiOperation(value = "根据X-USER-ID获取我的盟友列表", response = Alliance.class)
    public Cip getAlliancesByUserId(@RequestHeader("X-USER-ID") Long id) {
        List<Alliance> alliancesByUserId = allianceService.getAlliancesByUserId(id);
        return SuccessCip.create(alliancesByUserId);
    }

    @GetMapping("/getAllianceInformationByUserId")
    @ApiOperation(value = "根据X-USER-ID获取我的盟友信息,可以获取当月订单currentMonthOrder和我的盟友列表,还有分红信息（只有是股东能有分红）", response = AllianceRecord.class)
    public Cip getAllianceInformationByUserId(@RequestHeader("X-USER-ID") Long id) throws ParseException {
        Alliance entity = new Alliance();
        entity.setUserId(id);
        AllianceRecord alliance = queryAllianceDao.selectAllianceOneByUserId(id);
        List<Map> currentMonthOrderByUserId = queryAllianceDao.getCurrentMonthOrderByUserId(id);


        if (alliance != null) {
            if (currentMonthOrderByUserId != null && currentMonthOrderByUserId.size() > 0) {
                alliance.setCurrentMonthOrder(JSON.parseArray(JSON.toJSONString(queryAllianceDao.getCurrentMonthOrderByUserId(id))));
            } else {
                alliance.setCurrentMonthOrder(new JSONArray());
            }
        } else {
            return SuccessCip.create(null);
        }
        List<Alliance> alliancesByUserId = allianceService.getAlliancesByUserId(id);
        if (alliancesByUserId != null && alliancesByUserId.size() > 0) {
            alliance.setAllianceTeam(JSON.parseArray(JSON.toJSONString(alliancesByUserId)));
        } else {
            alliance.setAllianceTeam(new JSONArray());
        }
        //----------------------
        alliance.setSelfBonus(bonusService.getSelfBonus(id).add(bonusService.getTeamProportionBonus(id)));
        alliance.setTeamSelfBonus(bonusService.getTeamBonus(id));
        alliance.setTotalSelfBonus(bonusService.getSelfBonus(id).add(bonusService.getTeamProportionBonus(id)).add(bonusService.getTeamBonus(id)));
        JSONArray royalties = new JSONArray();
        Royalty ls = new Royalty();
        ls.setOrderMoney(new BigDecimal(2000.0));
        ls.setInvitorName("李四");
        ls.setCommission(new BigDecimal(400.0));
        ls.setCreateTime(new Date());
        royalties.add(ls);
        Royalty zs = new Royalty();
        zs.setOrderMoney(new BigDecimal(4000.0));
        zs.setInvitorName("张三");
        zs.setCommission(new BigDecimal(800.0));
        zs.setCreateTime(new Date());
        royalties.add(zs);
        alliance.setCommissionOrder(royalties);

        return SuccessCip.create(alliance);

    }

    @GetMapping("/getAllianceInformationByUserId/{id}")
    @ApiOperation(value = "根据盟友id获取我的盟友信息,携带自营商品selfProducts", response = Alliance.class)
    public Cip getSelfProductById(@PathVariable Long id) {

        return SuccessCip.create(allianceService.getSelfProductById(id));
    }

    @GetMapping("/setMeal")
    @ApiOperation(value = "充值套餐")
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

    @PostMapping("/createAlliance")
    @ApiOperation(value = "根据手机号，姓名，邀请码，添加盟友",response = Cip.class)
    public Cip createAlliance(@RequestBody RequestAlliance requestAlliance) {

        return SuccessCip.create(allianceService.createAlliance(requestAlliance));
    }


    @PostMapping("/bindpone")
    @ApiOperation(value = "对历史盟友绑定手机号，并检查是否为盟友",response = Cip.class)
    public Cip bindingAndCheckIsAlliance(@RequestHeader("X-USER-ID") Long userId, @RequestBody BindPhoneRequest request) {
        /**
         * 1.  接收 手机号码 及 验证码，X_USER_ID
         * 2.  手机号码有效， 检查手机号码是否已存在 盟友， 存在盟友即检查盟友的 状态是否为 已确认状态， 是否关联user_id, user_id 是否与 X_USER_ID 一致，
         *    根据盟友类型，库存额是否为 2000或10000,   t_wallet 里面的额度是与库存额一致。  条件满足， 即返回 是盟友 标识
         * 3.  手机号码有效， 但不在盟友列表中， 即此人不是盟友， 可以记录为联系人线索（考虑新建一个表，也可以暂时放弃这个电话信息），  即返回不是盟友标识
         */
        Alliance registeredAlliance = allianceService.findAllianceByPhoneNumber(request.getPhoneNumber());
        if(registeredAlliance==null){
            /// 记录盟友线索
            // TODO

            return ErrorCip.create(1, "找不到盟友注册信息");
        }

        /// 盟友已确定， 直接返回盟友类型
        BindPhoneResponse response = new BindPhoneResponse();
        response.setAllianceType(registeredAlliance.getAllianceType());

        if(registeredAlliance.getAllianceShip() == Alliance.ALLIANCE_SHIP_OK){
            return SuccessCip.create(response);
        }


        /// 检查盟友状态
        if(registeredAlliance.getAllianceShip() == Alliance.ALLIANCE_SHIP_CREATED ){
            return ErrorCip.create(2, "盟友申请状态中");
        }
        else if(registeredAlliance.getAllianceShip() == Alliance.ALLIANCE_SHIP_EXPIRED ){
            return ErrorCip.create(4, "支付超时，请重新申请");
        }else if(registeredAlliance.getAllianceShip() != Alliance.ALLIANCE_SHIP_PAID){
            return ErrorCip.create(4, "盟友状态Unknown: " + registeredAlliance.getAllianceShip());
        }

        /// 配置预存额度
        float common_alliance_inventory = configFieldService.getFieldFloat(BindPhoneResponse.COMMON_ALLIANCE_FIELD);
        float bonus_alliance_inventory = configFieldService.getFieldFloat(BindPhoneResponse.BONUS_ALLIANCE_FIELD);

        /// 查查盟友库存额
        if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_COMMON){
            if(registeredAlliance.getAllianceInventoryAmount().intValue() != (int)common_alliance_inventory){
                return ErrorCip.create(4, "盟友类型与库存额度不匹配");
            }

        }else if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_BONUS){
            if(registeredAlliance.getAllianceInventoryAmount().intValue() != (int)bonus_alliance_inventory){
                return ErrorCip.create(4, "盟友类型与库存额度不匹配");
            }
        }else{
            return ErrorCip.create(4, "盟友类型Unknown: " + registeredAlliance.getAllianceType());
        }

        if(registeredAlliance.getAllianceShip() != Alliance.ALLIANCE_SHIP_PAID){
            throw new BusinessException(BusinessCode.BadRequest, "盟友类型逻辑错误");
        }

        /// 状态正确(ALLIANCE_SHIP_PAID)，进行用户绑定
        registeredAlliance.setUserId(userId);

        //设置钱包库存额
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet = queryWalletDao.selectOne(wallet);
        if(wallet==null){
            return ErrorCip.create(4, "没有找到钱包信息，用户：" + userId);
        }
        if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_COMMON){
            if(wallet.getBalance().intValue() != common_alliance_inventory){
                return ErrorCip.create(4, "盟友初始库存额有误： " + wallet.getBalance());
            }
        }else if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_BONUS) {
            if (wallet.getBalance().intValue() != bonus_alliance_inventory) {
                return ErrorCip.create(4, "盟友初始库存额有误： " + wallet.getBalance());
            }
        }

        registeredAlliance.setAllianceShip(Alliance.ALLIANCE_SHIP_OK);

        return SuccessCip.create(response);
    }
}
