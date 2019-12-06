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
import java.math.BigDecimal;

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
                     @RequestParam(name = "searchMoney", required = false) BigDecimal searchMoney[],
                     @RequestParam(name = "searchNumber", required = false) Integer searchNumber[],
                     @RequestParam(name = "search", required = false) String search) {
        Page<ProductSalesRecord> page=new Page<>();
        BigDecimal leftMoney = searchMoney!=null? (searchMoney.length > 0?searchMoney[0]:null) : null;
        BigDecimal rightMoney = searchMoney!=null ? (searchMoney.length==2?searchMoney[1]:(searchMoney.length==1?searchMoney[0]:null)) : null;

        Integer leftNumber = searchNumber!=null? (searchNumber.length > 0?searchNumber[0]:null) : null;
        Integer rightNumber = searchNumber!=null ? (searchNumber.length==2?searchNumber[1]:(searchNumber.length==1?searchNumber[0]:null)) : null;


        page.setRecords(queryBonusDao.querySales(pageNum, pageSize, search,leftMoney,rightMoney,leftNumber,rightNumber));
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
