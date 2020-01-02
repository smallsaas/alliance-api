package com.jfeat.am.module.bonus.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusProportionDao;
import com.jfeat.am.module.bonus.services.domain.model.BonusProportionRecord;
import com.jfeat.am.module.bonus.services.domain.service.BonusProportionService;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.am.module.bonus.services.gen.persistence.model.BonusProportion;
import com.jfeat.am.module.bonus.util.Cip;
import com.jfeat.am.module.bonus.util.SuccessCip;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.request.Ids;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.rmi.ServerException;
import javax.annotation.Resource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api("BonusProportion")
@RequestMapping({"/api/crud/bonus/bonusProportions"})
public class BonusProportionEndpoint {
    @Resource
    BonusProportionService bonusProportionService;
    @Resource
    QueryBonusProportionDao queryBonusProportionDao;
    @Resource
    QueryBonusDao queryBonusDao;
    @Resource
    SettlementCenterService settlementCenterService;

    @BusinessLog(name = "BonusProportion", value = "create BonusProportion")
    @PostMapping
    @ApiOperation(value = "新建 BonusProportion", response = BonusProportion.class)
    public Tip createBonusProportion(@RequestBody BonusProportion entity) throws ServerException {
        Long productId = entity.getProductId();
        Integer productCountById = this.queryBonusProportionDao.getProductCountById(productId);
        if (productCountById.intValue() == 0) {
            throw new ServerException("该商品不存在");
        }
        Integer affected = Integer.valueOf(0);
        try {
            affected = this.bonusProportionService.createMaster(entity);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
    }


    @BusinessLog(name = "BonusProportion", value = "查看 BonusProportion")
    @GetMapping({"/{id}"})
    @ApiOperation(value = "查看 BonusProportion", response = BonusProportion.class)
    public Tip getBonusProportion(@PathVariable Long id) {
        return SuccessTip.create(this.bonusProportionService.retrieveMaster(id.longValue()));
    }


    @BusinessLog(name = "BonusProportion", value = "update BonusProportion")
    @PutMapping({"/{id}"})
    @ApiOperation(value = "修改 BonusProportion", response = BonusProportion.class)
    public Tip updateBonusProportion(@PathVariable Long id, @RequestBody BonusProportion entity) {
        entity.setId(id);
        return SuccessTip.create(this.bonusProportionService.updateMaster(entity));
    }


    @BusinessLog(name = "BonusProportion", value = "delete BonusProportion")
    @DeleteMapping({"/{id}"})
    @ApiOperation("删除 BonusProportion")
    public Tip deleteBonusProportion(@PathVariable Long id) {
        return SuccessTip.create(this.bonusProportionService.deleteMaster(id.longValue()));
    }


    @BusinessLog(name = "BonusProportion", value = "查询列表 BonusProportion")
    @ApiOperation(value = "BonusProportion 列表信息", response = BonusProportionRecord.class)
    @GetMapping
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum", dataType = "Integer"), @ApiImplicitParam(name = "pageSize", dataType = "Integer"), @ApiImplicitParam(name = "search", dataType = "String"), @ApiImplicitParam(name = "id", dataType = "Long"), @ApiImplicitParam(name = "productId", dataType = "Long"), @ApiImplicitParam(name = "bonusProportion", dataType = "BigDecimal"), @ApiImplicitParam(name = "type", dataType = "String"), @ApiImplicitParam(name = "name", dataType = "String"), @ApiImplicitParam(name = "level", dataType = "Integer"), @ApiImplicitParam(name = "orderBy", dataType = "String"), @ApiImplicitParam(name = "sort", dataType = "String")})
    public Tip queryBonusProportions(Page<BonusProportionRecord> page, @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum, @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize, @RequestParam(name = "search", required = false) String search, @RequestParam(name = "id", required = false) Long id, @RequestParam(name = "productId", required = false) Long productId, @RequestParam(name = "bonusProportion", required = false) BigDecimal bonusProportion, @RequestParam(name = "type", required = false) String type, @RequestParam(name = "name", required = false) String name, @RequestParam(name = "level", required = false) Integer level, @RequestParam(name = "orderBy", required = false) String orderBy, @RequestParam(name = "sort", required = false) String sort) {
        if (orderBy != null && orderBy.length() > 0) {
            if (sort != null && sort.length() > 0) {
                String pattern = "(ASC|DESC|asc|desc)";
                if (!sort.matches(pattern)) {
                    throw new BusinessException(BusinessCode.BadRequest.getCode(), "sort must be ASC or DESC");
                }
            } else {
                sort = "ASC";
            }
            orderBy = "`" + orderBy + "` " + sort;
        }
        page.setCurrent(pageNum.intValue());
        page.setSize(pageSize.intValue());

        BonusProportionRecord record = new BonusProportionRecord();
        record.setId(id);
        record.setProductId(productId);
        record.setBonusProportion(bonusProportion);
        record.setType(type);
        record.setName(name);
        record.setLevel(level);
        page.setRecords(this.queryBonusProportionDao.findBonusProportionPage(page, record, search, orderBy, null, null));

        return SuccessTip.create(page);
    }

    @ApiOperation("批量结算订单")
    @PostMapping("/settlementAll")
    public Cip settlementAll(@RequestBody Ids ids){
        Integer i=0;
        if(ids!=null&&ids.getIds().size()>0){
            for(Long id:ids.getIds()){
                if(settlementCenterService.settlementOrder(id)){
                    i++; } } }
        return SuccessCip.create(i);
    }

}


