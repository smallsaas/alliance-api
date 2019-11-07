package com.jfeat.am.module.bonus.api;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.bonus.util.Cip;
import com.jfeat.am.module.bonus.util.SuccessCip;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import io.swagger.annotations.Api;

import java.math.BigDecimal;
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

    @GetMapping("/selfBonus")
    @ApiOperation("获取自己的分红，可以传盟友id，也可以Header的X-USER-ID，dateType--->1当天，2当月，3当季，不传时算总的")
    public Cip getSelfBonus(@RequestHeader(required = false, name = "X-USER-ID") Long userId, @RequestParam(required = false, name = "id") Long id,@RequestParam(name = "dateType",required = false) Integer dateType) {
        if (userId != null) {
            BigDecimal selfBonus = bonusService.getSelfBonus(userId,dateType).add(bonusService.getTeamProportionBonus(userId,dateType));
            JSONObject object = new JSONObject();
            object.put("selfBonus", selfBonus);
            return SuccessCip.create(object);
        } else if (id != null) {
            Long allianceUserId = queryBonusDao.getAllianceUserId(id);
            if (allianceUserId != null) {
                BigDecimal selfBonus = bonusService.getSelfBonus(allianceUserId,dateType).add(bonusService.getTeamProportionBonus(allianceUserId,dateType));
                JSONObject object = new JSONObject();
                object.put("selfBonus", selfBonus);
                return SuccessCip.create(object);
            }
        } else {
            throw new BusinessException(BusinessCode.BadRequest,"参数有误，请重试");
        }
        JSONObject object = new JSONObject();
        object.put("selfBonus", 0);
        return SuccessCip.create(object);
    }

    @GetMapping("/teamBonus")
    @ApiOperation("获取自己团队分红，可以传盟友id，也可以Header的X-USER-ID,dateType--->1当天，2当月，3当季，不传时算总的")
    public Cip getTeamBonus(@RequestHeader(required = false, name = "X-USER-ID") Long userId, @RequestParam(required = false, name = "id") Long id,@RequestParam(name = "dateType",required = false) Integer dateType) {
        if (userId != null) {
            BigDecimal selfBonus = bonusService.getTeamBonus(userId,dateType);
            JSONObject object = new JSONObject();
            object.put("teamBonus", selfBonus);
            return SuccessCip.create(object);
        } else if (id != null) {
            Long allianceUserId = queryBonusDao.getAllianceUserId(id);
            if (allianceUserId != null) {
                BigDecimal selfBonus = bonusService.getTeamBonus(allianceUserId,dateType);
                JSONObject object = new JSONObject();
                object.put("teamBonus", selfBonus);
                return SuccessCip.create(object);
            }
        } else {
            JSONObject object = new JSONObject();
            object.put("teamBonus", 0);
            return SuccessCip.create(object);
        }
        JSONObject object = new JSONObject();
        object.put("teamBonus", 0);
        return SuccessCip.create(object);
    }

    @ApiOperation("获取自己总的分红，可以传盟友id，也可以Header的X-USER-ID,dateType--->1当天，2当月，3当季，不传时算总的")
    @GetMapping("/totalSelfBonus")
    public Cip getTotalSelfBonus(@RequestHeader(required = false, name = "X-USER-ID") Long userId, @RequestParam(required = false, name = "id") Long id,@RequestParam(required = false,name = "dateType") Integer dateType) {
        if (userId != null) {
            BigDecimal selfBonus = bonusService.getSelfBonus(userId,dateType).add(bonusService.getTeamProportionBonus(userId,dateType)).add(bonusService.getTeamBonus(userId,dateType));
            JSONObject object = new JSONObject();
            object.put("totalSelfBonus", selfBonus);
            return SuccessCip.create(object);
        } else if (id != null) {
            Long allianceUserId = queryBonusDao.getAllianceUserId(id);
            if (allianceUserId != null) {
                BigDecimal selfBonus = bonusService.getSelfBonus(allianceUserId,dateType).add(bonusService.getTeamProportionBonus(allianceUserId,dateType)).add(bonusService.getTeamBonus(allianceUserId,dateType));
                JSONObject object = new JSONObject();
                object.put("totalSelfBonus", selfBonus);
                return SuccessCip.create(object);
            }
        } else {
            JSONObject object = new JSONObject();
            object.put("totalSelfBonus", object);
            return SuccessCip.create(object);
        }
        JSONObject object = new JSONObject();
        object.put("totalSelfBonus", object);
        return SuccessCip.create(object);
    }
}

