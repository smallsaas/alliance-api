package com.jfeat.am.module.friend.api;


import com.jfeat.am.module.friend.services.domain.dao.QueryMomentsFriendDao;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendOverOrdersRecord;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendRecord;
import com.jfeat.am.module.friend.services.domain.service.MomentsFriendService;
import com.jfeat.am.module.friend.services.gen.persistence.model.MomentsFriend;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.util.Cip;
import com.jfeat.util.SuccessCip;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.plus.CRUDObject;

import java.math.BigDecimal;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * api
 * </p>
 *
 * @author Code Generator
 * @since 2019-10-14
 */
@RestController

@Api("MomentsFriend")
@RequestMapping("/rpc/friend/momentsFriends")
public class RPCMomentsFriendEndpoint {


    @Resource
    MomentsFriendService momentsFriendService;

    @Resource
    QueryMomentsFriendDao queryMomentsFriendDao;

    //@BusinessLog(name = "MomentsFriend", value = "create MomentsFriend")
    @PostMapping
    @ApiOperation(value = "新建 MomentsFriend", response = MomentsFriend.class)
    public Tip createMomentsFriend(@RequestBody MomentsFriend entity, @RequestHeader(name = "X-USER-ID", required = false) Long userId) {
        entity.setCreateTime(new Date());
        if (userId != null) {
            entity.setAllianceUserId(userId);
        }
        Integer affected = 0;
        try {
            affected = momentsFriendService.createMaster(entity);

        } catch (DuplicateKeyException e) {
            throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
    }

    //@BusinessLog(name = "MomentsFriend", value = "查看 MomentsFriend")
    @GetMapping("/{id}")
    @ApiOperation(value = "查看 MomentsFriend", response = MomentsFriend.class)
    public Cip getMomentsFriend(@PathVariable Long id) {
        return SuccessCip.create(momentsFriendService.retrieveMaster(id));
    }

    //@BusinessLog(name = "MomentsFriend", value = "update MomentsFriend")
    @PutMapping("/{id}")
    @ApiOperation(value = "修改 MomentsFriend", response = MomentsFriend.class)
    public Cip updateMomentsFriend(@PathVariable Long id, @RequestBody MomentsFriend entity) {
        entity.setId(id);
        return SuccessCip.create(momentsFriendService.updateMaster(entity));
    }

    //@BusinessLog(name = "MomentsFriend", value = "delete MomentsFriend")
    @DeleteMapping("/{id}")
    @ApiOperation("删除 MomentsFriend")
    public Cip deleteMomentsFriend(@PathVariable Long id) {
        return SuccessCip.create(momentsFriendService.deleteMaster(id));
    }

    //@BusinessLog(name = "MomentsFriend", value = "delete MomentsFriend")
    @ApiOperation(value = "根据请求头X-USER-ID 获取MomentsFriend 列表信息和订单，X-USER不传时获取所有的朋友列表", response = MomentsFriendRecord.class)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer"),
            @ApiImplicitParam(name = "search", dataType = "String"),
            @ApiImplicitParam(name = "id", dataType = "Long"),
            @ApiImplicitParam(name = "allianceId", dataType = "Long"),
            @ApiImplicitParam(name = "contactPhone", dataType = "String"),
            @ApiImplicitParam(name = "name", dataType = "String"),
            @ApiImplicitParam(name = "createTime", dataType = "Date"),
            @ApiImplicitParam(name = "allianceUserId", dataType = "Long"),
            @ApiImplicitParam(name = "pcdProvince", dataType = "String"),
            @ApiImplicitParam(name = "pcdCity", dataType = "String"),
            @ApiImplicitParam(name = "pcdDistinct", dataType = "String"),
            @ApiImplicitParam(name = "postCode", dataType = "String"),
            @ApiImplicitParam(name = "address", dataType = "String"),
            @ApiImplicitParam(name = "avator", dataType = "String"),
            @ApiImplicitParam(name = "nick", dataType = "String"),
            @ApiImplicitParam(name = "remark", dataType = "String"),
            @ApiImplicitParam(name = "orderBy", dataType = "String"),
            @ApiImplicitParam(name = "sort", dataType = "String")
    })
    public Cip queryMomentsFriends(Page<MomentsFriendOverOrdersRecord> page,
                                   @RequestHeader(name = "X-USER-ID", required = false) Long userId,
                                   @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                   @RequestParam(name = "search", required = false) String search,
                                   @RequestParam(name = "id", required = false) Long id,
                                   @RequestParam(name = "allianceId", required = false) Long allianceId,
                                   @RequestParam(name = "contactPhone", required = false) String contactPhone,
                                   @RequestParam(name = "name", required = false) String name,
                                   @RequestParam(name = "createTime", required = false) Date createTime,
                                   @RequestParam(name = "allianceUserId", required = false) Long allianceUserId,
                                   @RequestParam(name = "pcdProvince", required = false) String pcdProvince,
                                   @RequestParam(name = "pcdCity", required = false) String pcdCity,
                                   @RequestParam(name = "pcdDistinct", required = false) String pcdDistinct,
                                   @RequestParam(name = "postCode", required = false) String postCode,
                                   @RequestParam(name = "address", required = false) String address,
                                   @RequestParam(name = "avator", required = false) String avator,
                                   @RequestParam(name = "nick", required = false) String nick,
                                   @RequestParam(name = "remark", required = false) String remark,
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

        MomentsFriendRecord record = new MomentsFriendRecord();
        record.setId(id);
        //
        record.setAllianceId(allianceId);
        record.setContactPhone(contactPhone);
        record.setName(name);
        record.setCreateTime(createTime);
        record.setAllianceUserId(allianceUserId);
        record.setPcdProvince(pcdProvince);
        record.setPcdCity(pcdCity);
        record.setPcdDistinct(pcdDistinct);
        record.setPostCode(postCode);
        record.setAddress(address);
        record.setAvator(avator);
        record.setNick(nick);
        record.setRemark(remark);
        record.setAllianceUserId(userId);
        List<MomentsFriendOverOrdersRecord> momentsFriendPage = queryMomentsFriendDao.findMomentsFriendPage(page, record, search, orderBy, null, null);
        if (momentsFriendPage != null && momentsFriendPage.size() > 0) {
            for (MomentsFriendOverOrdersRecord momentsFriendOverOrdersRecord : momentsFriendPage) {
                if (momentsFriendOverOrdersRecord != null) {
                    if (momentsFriendOverOrdersRecord.getContactPhone() != null) {
                        Map order = queryMomentsFriendDao.findOrdersByPhone(momentsFriendOverOrdersRecord.getContactPhone(), record.getAllianceUserId());
                        momentsFriendOverOrdersRecord.setOrder(order);
                    }
                }
            }
        }


        page.setRecords(momentsFriendPage);

        return SuccessCip.create(page);
    }
}
