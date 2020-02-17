package com.jfeat.am.module.bonus.api;

import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.am.module.bonus.util.Cip;
import com.jfeat.am.module.bonus.util.SuccessCip;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("AutoSettlement")
@RequestMapping({"/api/crud/bonus/autoSettlement"})
public class AutoSettlementAllEndpoint {
    @Resource
    BonusService bonusService;
    @Resource
    QueryBonusDao queryBonusDao;
    @Resource
    SettlementCenterService settlementCenterService;

    @ApiOperation("手动批量结算订单")
    @GetMapping("/manuallySettlementAll")
    public Cip manuallySettlementAll(){
        Integer i=0;
        List<Long> ids = queryBonusDao.queryOrderId();
        if(ids!=null&&ids.size()>0){
            for(Long id:ids){
                if(settlementCenterService.settlementOrder(id)){
                    i++; }
            }
        }
        return SuccessCip.create(i);
    }
}
