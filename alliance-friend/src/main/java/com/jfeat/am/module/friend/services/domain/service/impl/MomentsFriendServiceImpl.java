package com.jfeat.am.module.friend.services.domain.service.impl;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.am.module.friend.api.*;
import com.jfeat.am.module.friend.services.domain.dao.QueryMomentsFriendDao;
import com.jfeat.am.module.friend.services.domain.dao.mapping.QueryMomentsFriendOverOrderDao;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendUser;
import com.jfeat.am.module.friend.services.domain.service.MomentsFriendService;
import com.jfeat.am.module.friend.services.gen.crud.service.impl.CRUDMomentsFriendServiceImpl;
import com.jfeat.am.module.friend.services.gen.persistence.model.FriendOrder;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author admin
 * @since 2017-10-16
 */
@Service("momentsFriendService")
public class MomentsFriendServiceImpl extends CRUDMomentsFriendServiceImpl implements MomentsFriendService {
    @Resource
    QueryMomentsFriendDao queryMomentsFriendDao;
    @Resource
    QueryMomentsFriendOverOrderDao queryMomentsFriendOverOrderDao;
    @Resource
    SettlementCenterService SettlementCenterService;



    @Override
    public Integer closeConfirmedOrder(Long id) {
     /*   AllianceProduct allianceProduct = queryMomentsFriendDao.queryProductById(id);
        allianceProduct.setStatus(OrderStatus.CLOSED_CONFIRMED);*/

        Integer i=queryMomentsFriendDao.closeProduct(id);
        SettlementCenterService.settlementOrder(id);


        return i;
    }

    @Override
    public Integer cancelCloseConfirmedOrder(Long id) {
        //回退钱
        SettlementCenterService.cancelSettlementOrder(id);
        //改状态 已发货 DELIVERED_CONFIRM_PENDING
        //设置 未结算 0
        Integer i=queryMomentsFriendDao.cancelcloseProduct(id);
        return i;
    }


    @Override
    @Transactional
    public Integer cancelOrder(Long id) throws ServerException {

        FriendOrder order = queryMomentsFriendOverOrderDao.selectById(id);

        Integer res=0;
            //状态 已取消
            order.setStatus(OrderStatus.CLOSED_CANCELED);
            //获取订单数据//循环遍历
            List<FriendOrderItem> friendOrderItemList= queryMomentsFriendDao.selectOrderItem(order.getId());
            //总价
            BigDecimal finalPrice=new BigDecimal(0);
            for (FriendOrderItem product:friendOrderItemList) {

                BigDecimal TotalPrice = product.getPrice().multiply(new BigDecimal(product.getQuantity()));
                finalPrice=TotalPrice.add(finalPrice);
                //还原库存
                Integer stockBalance = queryMomentsFriendDao.upStockBalance(product.getProductId(),product.getQuantity());

            }
        BigDecimal balance = queryMomentsFriendDao.queryWalletBalance(order.getUserId());
            //更新用户余额
           queryMomentsFriendDao.upWallet(order.getUserId(), order.getTotalPrice().add(balance));
           //更新订单
           queryMomentsFriendOverOrderDao.updateById(order);
        return res;
    }




}



