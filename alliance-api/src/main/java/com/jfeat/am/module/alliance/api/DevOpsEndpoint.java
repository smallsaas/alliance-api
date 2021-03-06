package com.jfeat.am.module.alliance.api;

import com.jfeat.am.core.jwt.JWTKit;
import com.jfeat.am.module.alliance.services.domain.service.DevService;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController

@Api("Dev")
@RequestMapping("/api/crud/dev")
public class DevOpsEndpoint {

    @Resource
    DevService devService;

    //删除订单
    @DeleteMapping("/delete/order")
    public Tip deleteOrder(@RequestParam(name = "type", required = true) String type){
        Integer integer = devService.deleteOrder(type);

        return SuccessTip.create(integer);
    }
    //删除退货
    @DeleteMapping("/delete/orderRefunds")
    public Tip deleteOrderRefunds(){
        Integer integer = devService.deleteOrderRefunds();

        return SuccessTip.create(integer);
    }
    
    //删除可提现额
    @DeleteMapping("/delete/ownerBalance")
    public Tip deleteOwnerBalance(){
        Integer integer = devService.deleteOwnerBalance();

        return SuccessTip.create(integer);
    }

    //删除线下提现记录
    @DeleteMapping("/delete/offlineWithdrawal")
    public Tip deleteOfflineWithdrawal(){
        Integer integer = devService.deleteOfflineWithdrawal();

        return SuccessTip.create(integer);
    }


    //删除盟友
    @DeleteMapping("/delete/alliance")
    public Tip deleteAlliance(){
        Integer integer = devService.deleteAlliance();

        return SuccessTip.create(integer);
    }

}
