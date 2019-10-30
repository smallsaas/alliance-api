package com.jfeat.am.module.alliance.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.Condition;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Royalty;
import com.jfeat.am.module.alliance.util.AllianceUtil;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.crud.base.tips.SuccessTip;
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
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;

import javax.annotation.Resource;
import java.rmi.ServerException;
import java.text.ParseException;
import java.util.ArrayList;
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

    //@BusinessLog(name = "Alliance", value = "create Alliance")
    @PostMapping
    @ApiOperation(value = "新建 Alliance", response = Alliance.class)
    public Cip createAlliance(@RequestBody AllianceRequest entity) throws ServerException, ParseException {
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
            entity.setAllianceInventoryAmount(new BigDecimal(2000));
        } else if (entity.getAllianceType().equals(1)) {
            entity.setAllianceInventoryAmount(new BigDecimal(10000));
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

}
