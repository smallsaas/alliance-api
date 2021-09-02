package com.jfeat.am.module.bonus.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.common.annotation.Permission;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.am.module.alliance.services.gen.persistence.dao.AllianceMapper;
import com.jfeat.am.module.bonus.definition.BonusPermission;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;
import com.jfeat.am.module.bonus.services.domain.model.ProductSalesRecord;
import com.jfeat.am.module.bonus.services.domain.service.BonusService;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bonus/reconciliation")
public class AllianceReconciliationEndpoint {
    @Resource
    QueryBonusDao queryBonusDao;
    @Resource
    BonusService bonusService;
    @Resource
    QueryAllianceDao queryAllianceDao;


    @PostMapping("/settlementAlliance/{id}")
    @ApiOperation(value = "盟友 分红结算", response = Tip.class)
    public Tip settlementAlliance(@PathVariable Long id){
        Integer integer = bonusService.settlementAlliance(id,false);
        return SuccessTip.create(integer);
    }

    @PostMapping("/settlementAlliance")
    @ApiOperation(value = "批量 盟友 分红结算", response = Tip.class)
    public Tip settlementAllianceList(@RequestBody List<Long> ids){
        Integer integer = bonusService.settlementAllicanceBatch(ids);
        return SuccessTip.create(integer);
    }

    @PostMapping("/settlementAlliance/all")
    @ApiOperation(value = "批量  所有盟友 分红结算", response = Tip.class)
    public Tip settlementAllianceList(){
        //获取所有盟友的id
        List<Long> ids = queryAllianceDao.getAllAllianceIds();
        Integer integer = bonusService.settlementAllicanceBatch(ids);
        return SuccessTip.create(integer);
    }

    //产品销量
    @GetMapping("/sales")
    @Permission(BonusPermission.BONUS_VIEW)
    public Tip sales(Page<ProductSalesRecord> page,
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                     @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                     @RequestParam(name = "searchMoney", required = false) BigDecimal searchMoney[],
                     @RequestParam(name = "searchNumber", required = false) Integer searchNumber[],
                     @RequestParam(name = "search", required = false) String search) {

        BigDecimal leftMoney = searchMoney!=null? (searchMoney.length > 0?searchMoney[0]:null) : null;
        BigDecimal rightMoney = searchMoney!=null ? (searchMoney.length==2?searchMoney[1]:(searchMoney.length==1?searchMoney[0]:null)) : null;

        Integer leftNumber = searchNumber!=null? (searchNumber.length > 0?searchNumber[0]:null) : null;
        Integer rightNumber = searchNumber!=null ? (searchNumber.length==2?searchNumber[1]:(searchNumber.length==1?searchNumber[0]:null)) : null;

        page.setSize(pageSize);
        page.setCurrent(pageNum);
        page.setRecords(queryBonusDao.querySales(page,pageNum, pageSize, search,leftMoney,rightMoney,leftNumber,rightNumber));



        return SuccessTip.create(page);
    }

    //盟友结算
    @GetMapping("/allianceReconciliation")
    @Permission(BonusPermission.BONUS_DIVIDEND)
    public Tip allianceReconciliation(Page<AllianceReconciliation> page,
                                      @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(name = "search", required = false) String search) {
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        page.setRecords(bonusService.getAllianceReconciliation(page, search));

        return SuccessTip.create(page);
    }

    @GetMapping(value = "/text")
    public String exportExcelFile(HttpServletRequest request , HttpServletResponse response)  {
        response.setContentType("application/octet-stream");
        return "111";
    }

}
