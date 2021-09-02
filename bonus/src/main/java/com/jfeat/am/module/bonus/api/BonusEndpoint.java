package com.jfeat.am.module.bonus.api;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.request.Ids;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import io.swagger.annotations.Api;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("|Bonus|分红")
@RequestMapping({"/rpc/bonus"})
public class BonusEndpoint {
    @Resource
    BonusService bonusService;
    @Resource
    QueryBonusDao queryBonusDao;
    @Resource
    SettlementCenterService settlementCenterService;

    @GetMapping("/selfBonus")
    @ApiOperation("获取自己的分红，可以传盟友id，也可以Header的X-USER-ID，dateType--->1当天，2当月，3当季，不传时算总的")
    public Tip getSelfBonus(@RequestHeader(required = false, name = "X-USER-ID") Long userId, @RequestParam(required = false, name = "id") Long id,@RequestParam(name = "dateType",required = false) Integer dateType) {
        if (userId != null) {
            Integer allianceExist = queryBonusDao.queryAllianceExist(userId);
            if(allianceExist==0){
                throw new BusinessException(BusinessCode.BadRequest,BonusError.ALLIANCE_NOT_EXIST);
            }
            BigDecimal selfBonus = bonusService.getSelfBonus(userId,dateType).add(bonusService.getTeamProportionBonus(userId,dateType));
            JSONObject object = new JSONObject();
            selfBonus = selfBonus.setScale(2, BigDecimal.ROUND_HALF_UP);
            object.put("selfBonus", selfBonus);
            return SuccessTip.create(object);
        } else if (id != null) {
            Long allianceUserId = queryBonusDao.getAllianceUserId(id);
            if (allianceUserId != null) {
                BigDecimal selfBonus = bonusService.getSelfBonus(allianceUserId,dateType).add(bonusService.getTeamProportionBonus(allianceUserId,dateType));
                JSONObject object = new JSONObject();
                selfBonus = selfBonus.setScale(2, BigDecimal.ROUND_HALF_UP);
                object.put("selfBonus", selfBonus);
                return SuccessTip.create(object);
            }
        } else {
            throw new BusinessException(BusinessCode.BadRequest,"参数有误，请重试");
        }
        JSONObject object = new JSONObject();
        object.put("selfBonus", 0);
        return SuccessTip.create(object);
    }

    @GetMapping("/teamBonus")
    @ApiOperation("获取自己团队分红，可以传盟友id，也可以Header的X-USER-ID,dateType--->1当天，2当月，3当季，不传时算总的")
    public Tip getTeamBonus(@RequestHeader(required = false, name = "X-USER-ID") Long userId, @RequestParam(required = false, name = "id") Long id,@RequestParam(name = "dateType",required = false) Integer dateType) {
        if (userId != null) {
            Integer allianceExist = queryBonusDao.queryAllianceExist(userId);
            if(allianceExist==0){
                throw new BusinessException(BusinessCode.BadRequest,BonusError.ALLIANCE_NOT_EXIST);
            }
            BigDecimal selfBonus = bonusService.getTeamBonus(userId,dateType);
            JSONObject object = new JSONObject();
            selfBonus = selfBonus.setScale(2, BigDecimal.ROUND_HALF_UP);
            object.put("teamBonus", selfBonus);
            return SuccessTip.create(object);
        } else if (id != null) {
            Long allianceUserId = queryBonusDao.getAllianceUserId(id);
            if (allianceUserId != null) {
                BigDecimal selfBonus = bonusService.getTeamBonus(allianceUserId,dateType);
                JSONObject object = new JSONObject();
                selfBonus = selfBonus.setScale(2, BigDecimal.ROUND_HALF_UP);
                object.put("teamBonus", selfBonus);
                return SuccessTip.create(object);
            }
        } else {
            JSONObject object = new JSONObject();
            object.put("teamBonus", 0);
            return SuccessTip.create(object);
        }
        JSONObject object = new JSONObject();
        object.put("teamBonus", 0);
        return SuccessTip.create(object);
    }

    @ApiOperation("获取自己总的分红，可以传盟友id，也可以Header的X-USER-ID,dateType--->1当天，2当月，3当季，不传时算总的")
    @GetMapping("/totalSelfBonus")
    public Tip getTotalSelfBonus(@RequestHeader(required = false, name = "X-USER-ID") Long userId, @RequestParam(required = false, name = "id") Long id,@RequestParam(required = false,name = "dateType") Integer dateType) {
        if (userId != null) {
            Integer allianceExist = queryBonusDao.queryAllianceExist(userId);
            if(allianceExist==0){
                throw new BusinessException(BusinessCode.BadRequest,BonusError.ALLIANCE_NOT_EXIST);
            }
            BigDecimal selfBonus = bonusService.getSelfBonus(userId,dateType).add(bonusService.getTeamProportionBonus(userId,dateType)).add(bonusService.getTeamBonus(userId,dateType));
            JSONObject object = new JSONObject();
            selfBonus = selfBonus.setScale(2, BigDecimal.ROUND_HALF_UP);
            object.put("totalSelfBonus", selfBonus);
            return SuccessTip.create(object);
        } else if (id != null) {
            Long allianceUserId = queryBonusDao.getAllianceUserId(id);
            if (allianceUserId != null) {
                BigDecimal selfBonus = bonusService.getSelfBonus(allianceUserId,dateType).add(bonusService.getTeamProportionBonus(allianceUserId,dateType)).add(bonusService.getTeamBonus(allianceUserId,dateType));
                JSONObject object = new JSONObject();
                selfBonus = selfBonus.setScale(2, BigDecimal.ROUND_HALF_UP);
                object.put("totalSelfBonus", selfBonus);
                return SuccessTip.create(object);
            }
        } else {
            JSONObject object = new JSONObject();
            object.put("totalSelfBonus", object);
            return SuccessTip.create(object);
        }
        JSONObject object = new JSONObject();
        object.put("totalSelfBonus", object);
        return SuccessTip.create(object);
    }


    @ApiOperation("手动计算并设置所有分红盟友总订单")
    @GetMapping("/setTotal")
    public Integer setAllianceTotal(){
        // 设置所有分红盟友总订单
        Integer i = settlementCenterService.setTotal();
        return i;
    }



    @ApiOperation("手动批量结算订单")
    @GetMapping("/manuallySettlementAll")
    public Tip manuallySettlementAll(){
        Integer i=0;
        List<Long> ids = queryBonusDao.queryOrderId();
        if(ids!=null&&ids.size()>0){
            for(Long id:ids){
                if(settlementCenterService.settlementOrder(id)){
                    i++; }
            }
        }
        return SuccessTip.create(i);
    }





}

