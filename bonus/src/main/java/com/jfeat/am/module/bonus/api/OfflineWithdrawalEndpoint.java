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
        entity.setStatus(OfflineWithdrawalStatus.WAIT);

        Integer affected = 0;

                OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(new OwnerBalance().setUserId(userId));
                if (ownerBalance == null) {
                    throw new BusinessException(BusinessCode.BadRequest, "该账户提成不足");
                }
                BigDecimal ownerBalanceBalance = ownerBalance.getBalance();
                BigDecimal getBalance = entity.getBalance();

                //检查可提现余额
                if (getBalance == null) {
                    getBalance = new BigDecimal(0.00);
                }
                if (ownerBalanceBalance == null || ownerBalanceBalance.compareTo(new BigDecimal(0.00)) <= 0) {
                    throw new BusinessException(BusinessCode.BadRequest, "该账户提成不足");
                }
                BigDecimal subtract = ownerBalanceBalance.subtract(getBalance);
                if (subtract.compareTo(new BigDecimal(0.00)) < 0) {
                    throw new BusinessException(BusinessCode.BadRequest, "该账户提成不足");
                }
                ownerBalance.setBalance(subtract);

                affected += queryOwnerBalanceDao.updateById(ownerBalance);
                affected += offlineWithdrawalService.createMaster(entity);

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

    @BusinessLog(name = "线下提现", value = "审批通过")
    @PostMapping("/pass/{id}")
    @ApiOperation("审批通过 线下提现")
    public Tip passOfflineWithdrawal(@PathVariable Long id) {
        OfflineWithdrawal offlineWithdrawal = offlineWithdrawalService.retrieveMaster(id);
        int res = 0;
        if (offlineWithdrawal.getStatus() != OfflineWithdrawalStatus.WAIT) {
            throw new BusinessException(BusinessCode.BadRequest, "提现失败，状态不符合要求");
        }
        offlineWithdrawal.setStatus(OfflineWithdrawalStatus.OK);
        res += queryOfflineWithdrawalDao.updateById(offlineWithdrawal);

        return SuccessTip.create(res);
    }


    @BusinessLog(name = "线下提现", value = "审批不通过")
    @PostMapping("/notPass/{id}")
    @ApiOperation("审批不通过 线下提现")
    public Tip notPassOfflineWithdrawal(@PathVariable Long id) {
        OfflineWithdrawal offlineWithdrawal = offlineWithdrawalService.retrieveMaster(id);
        int res = 0;
        Long userId = offlineWithdrawal.getUserId();
        if (offlineWithdrawal.getStatus() != OfflineWithdrawalStatus.WAIT) {
            throw new BusinessException(BusinessCode.BadRequest, "状态错误，申请中的状态才能不通过");
        }


        OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(new OwnerBalance().setUserId(userId));
        if (ownerBalance == null) {
            throw new BusinessException(BusinessCode.BadRequest, "禁用失败，可能存在脏数据，用户没绑定账户，但是有提现申请");
        }
        BigDecimal ownerBalanceBalance = ownerBalance.getBalance();
        BigDecimal getBalance = offlineWithdrawal.getBalance();
        if (getBalance == null) { getBalance = new BigDecimal(0.00); }
        BigDecimal add = ownerBalanceBalance.add(getBalance);
        ownerBalance.setBalance(add);

        offlineWithdrawal.setStatus(OfflineWithdrawalStatus.NOT_OK);
        res += queryOfflineWithdrawalDao.updateById(offlineWithdrawal);
        res += queryOwnerBalanceDao.updateById(ownerBalance);
        return SuccessTip.create(res);
    }
}
