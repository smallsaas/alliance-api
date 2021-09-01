package com.jfeat.am.module.bonus.api;

import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
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
