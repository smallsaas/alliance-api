package com.jfeat.am.module.alliance.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.dao.DuplicateKeyException;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Ids;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.plus.CRUDObject;

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
@RequestMapping("/api/crud/alliance/ownerBalances")
public class OwnerBalanceEndpoint {


    @Resource
    OwnerBalanceService ownerBalanceService;

    @Resource
    QueryOwnerBalanceDao queryOwnerBalanceDao;

    @BusinessLog(name = "OwnerBalance", value = "create OwnerBalance")
    @PostMapping
    @ApiOperation(value = "新建 OwnerBalance", response = OwnerBalance.class)
    public Tip createOwnerBalance(@RequestBody OwnerBalance entity) {

        Integer affected = 0;
        try {
            affected = ownerBalanceService.createMaster(entity);

        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
    }

    @BusinessLog(name = "OwnerBalance", value = "查看 OwnerBalance")
    @GetMapping("/{id}")
    @ApiOperation(value = "查看 OwnerBalance", response = OwnerBalance.class)
    public Tip getOwnerBalance(@PathVariable Long id) {
        return SuccessTip.create(ownerBalanceService.retrieveMaster(id));
    }



    @BusinessLog(name = "OwnerBalance", value = "delete OwnerBalance")
    @DeleteMapping("/{id}")
    @ApiOperation("删除 OwnerBalance")
    public Tip deleteOwnerBalance(@PathVariable Long id) {
        return SuccessTip.create(ownerBalanceService.deleteMaster(id));
    }

    @BusinessLog(name = "OwnerBalance", value = "查询列表 OwnerBalance")
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

        OwnerBalanceRecord record = new OwnerBalanceRecord();
        record.setId(id);
        record.setUserId(userId);
        record.setBonus_balance(balance);
        record.setVersion(version);
        page.setRecords(queryOwnerBalanceDao.findOwnerBalancePage(page, record, search, orderBy, null, null));

        return SuccessTip.create(page);
    }
}
