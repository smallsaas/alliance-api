package com.jfeat.am.module.bonus.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.common.annotation.Permission;
import com.jfeat.am.module.bonus.definition.BonusPermission;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;
import com.jfeat.am.module.bonus.services.domain.model.ProductSalesRecord;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
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


    //产品销量
    @GetMapping("/sales")
    @Permission(BonusPermission.BONUS_VIEW)
    public Tip sales(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                     @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                     @RequestParam(name = "search", required = false) String search) {
        Page<ProductSalesRecord> page=new Page<>();
        page.setRecords(queryBonusDao.querySales(pageNum, pageSize, search));
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        return SuccessTip.create(page);
    }

    @GetMapping("/allianceReconciliation")
    @Permission(BonusPermission.BONUS_DIVIDEND)
    public Tip allianceReconciliation(@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(name = "search", required = false) String search) {
        Page<AllianceReconciliation> page=new Page<>();
        page.setRecords(bonusService.getAllianceReconciliation(pageNum, pageSize, search));
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        return SuccessTip.create(page);
    }
}
