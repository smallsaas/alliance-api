package com.jfeat.am.module.bonus.api;


import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.module.alliance.api.RechargeType;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import com.jfeat.am.module.bonus.services.domain.dao.QueryOfflineWithdrawalDao;
import com.jfeat.am.module.bonus.services.domain.model.OfflineWithdrawalRecord;
import com.jfeat.am.module.bonus.services.domain.service.OfflineWithdrawalService;
import com.jfeat.am.module.bonus.services.gen.persistence.model.OfflineWithdrawal;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>
 * api
 * </p>
 *
 * @author Code Generator
 * @since 2019-12-03
 */
@RestController
@Api("OfflineWithdrawal")
@RequestMapping("/api/crud/bonus/offlineWithdrawals")
public class OfflineWithdrawalEndpoint {


    @Resource
    OfflineWithdrawalService offlineWithdrawalService;

    @Resource
    QueryOfflineWithdrawalDao queryOfflineWithdrawalDao;
    @Resource
    QueryWalletDao queryWalletDao;
    @Resource
    QueryWalletHistoryDao queryWalletHistoryDao;
    @Resource
    QueryOwnerBalanceDao queryOwnerBalanceDao;


    @BusinessLog(name = "线下提现", value = "新建 线下提现")
    @PostMapping
    @ApiOperation(value = "新建线下提现", response = OfflineWithdrawal.class)
    public Tip createOfflineWithdrawal(@RequestHeader("X-USER-ID") Long userId, @RequestBody OfflineWithdrawal entity) {
        entity.setUserId(userId);
        entity.setCreateTime(new Date());
        Integer affected = 0;
        try {
            affected = offlineWithdrawalService.createMaster(entity);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }
        return SuccessTip.create(affected);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查看 OfflineWithdrawal", response = OfflineWithdrawal.class)
    public Tip getOfflineWithdrawal(@PathVariable Long id) {
        OfflineWithdrawal offlineWithdrawal = offlineWithdrawalService.retrieveMaster(id);
        if (offlineWithdrawal != null) {
            Long userId = offlineWithdrawal.getUserId();
            if (userId != null) {
                String allianceName = queryOfflineWithdrawalDao.queryAllianceName(userId);
                if (allianceName != null) {
                    offlineWithdrawal.setAllianceName(allianceName);
                }

            }
        }
        return SuccessTip.create(offlineWithdrawal);
    }
    @BusinessLog(name = "线下提现", value = "更新 线下提现")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改 OfflineWithdrawal", response = OfflineWithdrawal.class)
    public Tip updateOfflineWithdrawal(@PathVariable Long id, @RequestBody OfflineWithdrawal entity) {
        entity.setId(id);
        return SuccessTip.create(offlineWithdrawalService.updateMaster(entity));
    }
    @BusinessLog(name = "线下提现", value = "删除 线下提现")
    @DeleteMapping("/{id}")
    @ApiOperation("删除 OfflineWithdrawal")
    public Tip deleteOfflineWithdrawal(@PathVariable Long id) {
        return SuccessTip.create(offlineWithdrawalService.deleteMaster(id));
    }
    /*@BusinessLog(name = "线下提现", value = "查询列表 线下提现")*/
    @ApiOperation(value = "线下提现 列表信息", response = OfflineWithdrawalRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Integer"),
            @ApiImplicitParam(name = "balance", dataType = "BigDecimal"),
            @ApiImplicitParam(name = "createTime", dataType = "Date"),
            @ApiImplicitParam(name = "status", dataType = "Integer"),
            @ApiImplicitParam(name = "userId", dataType = "Integer"),
            @ApiImplicitParam(name = "note", dataType = "String"),
            @ApiImplicitParam(name = "orderBy", dataType = "String"),
            @ApiImplicitParam(name = "sort", dataType = "String")
    })
    public Tip queryOfflineWithdrawals(Page<OfflineWithdrawalRecord> page,
                                       @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                       @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                       @RequestParam(name = "search", required = false) String search,
                                       @RequestParam(name = "id", required = false) Long id,
                                       @RequestParam(name = "balance", required = false) BigDecimal balance,
                                       @RequestParam(name = "createTime", required = false) Date createTime,
                                       @RequestParam(name = "status", required = false) Integer status,
                                       @RequestParam(name = "userId", required = false) Long userId,
                                       @RequestParam(name = "note", required = false) String note,
                                       @RequestParam(name = "orderBy", required = false) String orderBy,
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

        OfflineWithdrawalRecord record = new OfflineWithdrawalRecord();
        record.setId(id);
        record.setBalance(balance);
        record.setCreateTime(createTime);
        record.setStatus(status);
        record.setUserId(userId);
        record.setNote(note);
        page.setRecords(queryOfflineWithdrawalDao.findOfflineWithdrawalPage(page, record, search, orderBy, null, null));
        return SuccessTip.create(page);
    }

    @BusinessLog(name = "线下提现", value = "删除线下提现")
    @PostMapping("/pass/{id}")
    @ApiOperation("审批通过 线下提现")
    public Tip passOfflineWithdrawal(@PathVariable Long id) {
        OfflineWithdrawal offlineWithdrawal = offlineWithdrawalService.retrieveMaster(id);
        int res=0;
        if(offlineWithdrawal.getStatus()!=0){
            throw new BusinessException(BusinessCode.BadRequest,"提现失败，状态不符合要求");
        }
        if(offlineWithdrawal!=null){
            if(offlineWithdrawal.getStatus().equals(OfflineWithdrawalStatus.WAIT)){

                Long userId = offlineWithdrawal.getUserId();
                if(userId!=null){
                    OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(new OwnerBalance().setUserId(userId));
                    if(ownerBalance==null){
                        throw new BusinessException(BusinessCode.BadRequest,"该账户提成不足");
                    }
                    BigDecimal balance = ownerBalance.getBalance();
                    BigDecimal balance1 = offlineWithdrawal.getBalance();
                    if(balance1==null){
                        balance1=new BigDecimal(0.00);
                    }
                    if(balance==null||balance.compareTo(new BigDecimal(0.00))<=0){
                        throw new BusinessException(BusinessCode.BadRequest,"该账户提成不足");
                    }
                    BigDecimal subtract = balance.subtract(balance1);
                    if(subtract.compareTo(new BigDecimal(0.00))<0){
                        throw new BusinessException(BusinessCode.BadRequest,"该账户提成不足");
                    }
                    ownerBalance.setBalance(subtract);
                    offlineWithdrawal.setStatus(OfflineWithdrawalStatus.OK);
                    res+=queryOfflineWithdrawalDao.updateById(offlineWithdrawal);
                    res+=queryOwnerBalanceDao.updateById(ownerBalance);
                    Wallet wallet = queryWalletDao.selectOne(new Wallet().setUserId(userId));
                    if(wallet!=null){
                        BigDecimal balance2 = wallet.getBalance();
                        if(balance2==null){
                            balance2=new BigDecimal(0.00);
                        }
                        BigDecimal add = balance2.add(balance1);
                        wallet.setBalance(add);
                        res+=queryWalletDao.updateById(wallet);
                        WalletHistory walletHistory = new WalletHistory().setNote("提成线下提现（转入）钱包").setType(RechargeType.CASH_OUT).setBalance(ownerBalance.getBalance()).setCreatedTime(new Date()).setAmount(balance1).setWalletId(wallet.getId());
                        res+=queryWalletHistoryDao.insert(walletHistory);
                    }
                }else {
                    throw new BusinessException(BusinessCode.BadRequest,"申请失败，请联系管理员");
                }

            }
        }
        return SuccessTip.create(res);
    }
}
