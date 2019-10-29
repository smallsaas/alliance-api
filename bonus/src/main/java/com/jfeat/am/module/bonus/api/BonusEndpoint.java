package com.jfeat.am.module.bonus.api;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.bonus.util.Cip;
import com.jfeat.am.module.bonus.util.SuccessCip;
import io.swagger.annotations.Api;

import java.math.BigDecimal;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

@RestController
@Api("Bonus")
@RequestMapping({"/rpc/bonus"})
public class BonusEndpoint {
    @Resource
    BonusService bonusService;
    @Resource
    QueryBonusDao queryBonusDao;

    @GetMapping("/selfBonus")
    public Cip getSelfBonus(@RequestHeader(required = false,name = "X-USER-ID") Long userId, @RequestParam(required = false,name = "id") Long id) {
        if(userId!=null){
            BigDecimal selfBonus = bonusService.getSelfBonus(userId).add(bonusService.getTeamProportionBonus(userId));
            JSONObject object=new JSONObject();
            object.put("selfBonus",selfBonus);
            return SuccessCip.create(object);
        }else if(id!=null){
            Long allianceUserId = queryBonusDao.getAllianceUserId(id);
            if(allianceUserId!=null){
                BigDecimal selfBonus = bonusService.getSelfBonus(allianceUserId).add(bonusService.getTeamProportionBonus(allianceUserId));
                JSONObject object=new JSONObject();
                object.put("selfBonus",selfBonus);
                return SuccessCip.create(object);
            }

        }else {
            JSONObject object=new JSONObject();
            object.put("selfBonus",0);
            return   SuccessCip.create(object);
        }

        JSONObject object=new JSONObject();
        object.put("selfBonus",0);
        return   SuccessCip.create(object);

    }
    @GetMapping("/teamBonus")
    public Cip getTeamBonus(@RequestHeader(required = false,name = "X-USER-ID") Long userId, @RequestParam(required = false,name = "id") Long id) {
        if(userId!=null){
            BigDecimal selfBonus = bonusService.getTeamBonus(userId);
            JSONObject object=new JSONObject();
            object.put("teamBonus",selfBonus);
            return SuccessCip.create(object);
        }else if(id!=null){
            Long allianceUserId = queryBonusDao.getAllianceUserId(id);
            if(allianceUserId!=null){
                BigDecimal selfBonus = bonusService.getTeamBonus(allianceUserId);
                JSONObject object=new JSONObject();
                object.put("teamBonus",selfBonus);
                return SuccessCip.create(object);
            }

        }else {
            JSONObject object=new JSONObject();
            object.put("teamBonus",0);
            return   SuccessCip.create(object);
        }

        JSONObject object=new JSONObject();
        object.put("teamBonus",0);
        return SuccessCip.create(object);
    }
    @GetMapping("/totalSelfBonus")
    public Cip getTotalSelfBonus(@RequestHeader(required = false,name = "X-USER-ID") Long userId, @RequestParam(required = false,name = "id") Long id) {
        if(userId!=null){
            BigDecimal selfBonus = bonusService.getSelfBonus(userId).add(bonusService.getTeamProportionBonus(userId)).add(bonusService.getTeamBonus(userId));
            JSONObject object=new JSONObject();
            object.put("totalSelfBonus",selfBonus);
            return SuccessCip.create(object);
        }else if(id!=null){
            Long allianceUserId = queryBonusDao.getAllianceUserId(id);
            if(allianceUserId!=null){
                BigDecimal selfBonus = bonusService.getSelfBonus(allianceUserId).add(bonusService.getTeamProportionBonus(allianceUserId)).add(bonusService.getTeamBonus(allianceUserId));
                JSONObject object=new JSONObject();
                object.put("totalSelfBonus",selfBonus);
                return SuccessCip.create(object);
            }

        }else {
            JSONObject object=new JSONObject();
            object.put("totalSelfBonus",object);
            return   SuccessCip.create(object);
        }
        JSONObject object=new JSONObject();
        object.put("totalSelfBonus",object);
        return   SuccessCip.create(object);

    }
}

