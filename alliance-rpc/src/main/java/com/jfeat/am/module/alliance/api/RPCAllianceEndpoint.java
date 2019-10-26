package com.jfeat.am.module.alliance.api;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jfeat.am.module.alliance.api.AllianceRequest;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
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
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.plus.CRUDObject;

import java.math.BigDecimal;

import com.jfeat.am.module.alliance.services.domain.service.AllianceService;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;

import javax.annotation.Resource;
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
@RequestMapping("/rest/rpc/alliance/alliances")
public class RPCAllianceEndpoint {


    @Resource
    AllianceService allianceService;

    @Resource
    QueryAllianceDao queryAllianceDao;

    //@BusinessLog(name = "Alliance", value = "create Alliance")
    @PostMapping
    @ApiOperation(value = "新建 Alliance", response = Alliance.class)
    public Cip createAlliance(@RequestBody Alliance entity) {

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
    public Cip updateAlliance(@PathVariable Long id, @RequestBody AllianceRequest entity) {
        entity.setId(id);
        //根据邀请人电话查找邀请人信息
        Alliance alliance = null;
        if (entity.getInvitorPhoneNumber() != null) {
            allianceService.findAllianceByPhoneNumber(entity.getInvitorPhoneNumber());
        }
        if (alliance != null) {
            entity.setInvitorAllianceId(alliance.getId());
        }

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
    @ApiOperation(value = "根据X-USER-ID获取我的盟友信息,可以获取当月订单currentMonthOrder", response = Alliance.class)
    public Cip getAllianceInformationByUserId(@RequestHeader("X-USER-ID") Long id) {
        Alliance entity = new Alliance();
        entity.setUserId(id);
        AllianceRecord alliance = queryAllianceDao.selectAllianceOneByUserId(id);
        List<Map> currentMonthOrderByUserId = queryAllianceDao.getCurrentMonthOrderByUserId(id);
        if (alliance != null) {
            if (currentMonthOrderByUserId != null && currentMonthOrderByUserId.size() > 0) {
                alliance.setCurrentMonthOrder(JSON.parseArray(JSON.toJSONString(queryAllianceDao.getCurrentMonthOrderByUserId(id))));
            }
        } else {
            return SuccessCip.create(null);
        }
        return SuccessCip.create(alliance);
    }

    @GetMapping("/getAllianceInformationByUserId/{id}")
    @ApiOperation(value = "根据盟友id获取我的盟友信息,携带自营商品selfProducts", response = Alliance.class)
    public Cip getSelfProductById(@PathVariable Long id) {

        return SuccessCip.create(allianceService.getSelfProductById(id));
    }
}
