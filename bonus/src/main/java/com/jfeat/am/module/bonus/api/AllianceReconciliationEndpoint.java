package com.jfeat.am.module.bonus.api;

import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/bonus/reconciliation")
public class AllianceReconciliationEndpoint {
    @Resource
    QueryBonusDao queryBonusDao;
    @Resource
    BonusService bonusService;
    @GetMapping("/sales")
    public Tip sales(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                     @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        return SuccessTip.create(queryBonusDao.querySales(pageNum,pageSize));
    }
    @GetMapping("/allianceReconciliation")
    public Tip allianceReconciliation(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                     @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        return SuccessTip.create(bonusService.getAllianceReconciliation(pageNum,pageSize));
    }
}
