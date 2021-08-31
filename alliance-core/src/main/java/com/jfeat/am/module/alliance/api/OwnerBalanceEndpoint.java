package com.jfeat.am.module.alliance.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;

import java.math.BigDecimal;

import com.jfeat.am.module.alliance.services.domain.service.OwnerBalanceService;
import com.jfeat.am.module.alliance.services.domain.model.OwnerBalanceRecord;
import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;


/**
 * <p>
 * api
 * </p>
 *
 * @author Code Generator
 * @since 2019-11-29
 */
@RestController

@Api("OwnerBalance")
@RequestMapping("/api/alliance/ownerBalances")
public class OwnerBalanceEndpoint {


    @Resource
    OwnerBalanceService ownerBalanceService;

    @Resource
    QueryOwnerBalanceDao queryOwnerBalanceDao;

    @Resource
    QueryWalletHistoryDao queryWalletHistoryDao;

    @Resource
    QueryWalletDao queryWalletDao;

    @GetMapping("/{id}")
    @ApiOperation(value = "查看 OwnerBalance", response = OwnerBalance.class)
    public Tip getOwnerBalance(@PathVariable Integer id) {
        return SuccessTip.create(queryOwnerBalanceDao.findOneOwnerBalance(id));
    }

    @BusinessLog(name = "线下提现", value = "提现")
    @PostMapping("/{id}")
    @ApiOperation(value = "提现", response = OwnerBalance.class)
    public Tip withdrawal(@PathVariable Integer id,@RequestBody OwnerBalanceRecord ownerBalanceRecord) {

        if(ownerBalanceRecord.getMoney().longValue()<0){
            throw new BusinessException(BusinessCode.BadRequest, "提款金额不能小于0");
        }
        OwnerBalanceRecord ownerBalance= queryOwnerBalanceDao.findOneOwnerBalance(id);
        if(ownerBalance.getBalance().subtract(ownerBalanceRecord.getMoney()).longValue()<0){
            throw new BusinessException(BusinessCode.BadRequest, "可提款金额不足");
        }

        WalletHistory walletHistory=new WalletHistory();
        walletHistory.setNote("线下提现");
        //提现金额
        walletHistory.setAmount(ownerBalanceRecord.getMoney());
        //库存金额
        walletHistory.setBalance(ownerBalance.getBalance().subtract(ownerBalanceRecord.getMoney()));
        Wallet wallet=new Wallet();
        wallet.setUserId(ownerBalance.getUserId());
        wallet=queryWalletDao.selectOne(new QueryWrapper<>(wallet));

        walletHistory.setWalletId(wallet.getId());
        walletHistory.setType("WITHDRAW");
        walletHistory.setCreatedTime(new Date());
        queryWalletHistoryDao.insert(walletHistory);

        return SuccessTip.create(queryOwnerBalanceDao.withdrawal(id,ownerBalanceRecord.getMoney()));
    }





    //@BusinessLog(name = "OwnerBalance", value = "查询列表 OwnerBalance")
    @ApiOperation(value = "OwnerBalance 列表信息", response = OwnerBalanceRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Integer"),
            @ApiImplicitParam(name = "userId", dataType = "Integer"),
            @ApiImplicitParam(name = "balance", dataType = "BigDecimal"),
            @ApiImplicitParam(name = "version", dataType = "Integer"),
            @ApiImplicitParam(name = "orderBy", dataType = "String"),
            @ApiImplicitParam(name = "sort", dataType = "String")
    })
    public Tip queryOwnerBalances(Page<OwnerBalanceRecord> page,
                                  @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                  @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                  @RequestParam(name = "search", required = false) String search,
                                  @RequestParam(name = "id", required = false) Integer id,
                                  @RequestParam(name = "userId", required = false) Long userId,
                                  @RequestParam(name = "balance", required = false) BigDecimal balance,
                                  @RequestParam(name = "version", required = false) Integer version,
                                  @RequestParam(name = "orderBy", required = false) String orderBy,
                                  @RequestParam(name = "searchMoney", required = false) BigDecimal searchMoney[],
                                  @RequestParam(name = "sort", required = false) String sort) {
        if (orderBy != null && orderBy.length() > 0) {
            if (sort != null && sort.length() > 0) {
                String pattern = "(ASC|DESC|asc|desc)";
                if (!sort.matches(pattern)) {
                    throw new BusinessException(BusinessCode.BadRequest.getCode(), "sort must be ASC or DESC");//此处异常类型根据实际情况而定
                }
            } else {
                sort = "ASC";
            }
            orderBy = "`" + orderBy + "`" + " " + sort;
        }
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        BigDecimal leftMoney = searchMoney!=null? (searchMoney.length > 0?searchMoney[0]:null) : null;
        BigDecimal rightMoney = searchMoney!=null ? (searchMoney.length==2?searchMoney[1]:(searchMoney.length==1?searchMoney[0]:null)) : null;


        OwnerBalanceRecord record = new OwnerBalanceRecord();
        record.setId(id);
        record.setUserId(userId);
        record.setBalance(balance);
        record.setVersion(version);
        page.setRecords(queryOwnerBalanceDao.findOwnerBalancePage(page, record, search, orderBy, null, null,leftMoney,rightMoney));

        return SuccessTip.create(page);
    }
}
