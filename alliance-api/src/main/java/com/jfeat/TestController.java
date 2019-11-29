package com.jfeat;

import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    SettlementCenterService settlementCenterService;
    @GetMapping
    public Tip test(@RequestParam Long orderId){
        boolean b = settlementCenterService.settlementOrder(orderId);
        return SuccessTip.create(b);
    }

}
