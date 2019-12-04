package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.jfeat.am.module.alliance.api.AllianceFields;
import com.jfeat.am.module.alliance.services.domain.dao.QueryOwnerBalanceDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;
import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
import com.jfeat.am.module.bonus.services.domain.model.OrderCommissionInfo;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import com.jfeat.am.module.order.services.domain.dao.QueryOrderItemDao;
import com.jfeat.am.module.order.services.domain.dao.QueryOrderItemRewardDao;
import com.jfeat.am.module.order.services.gen.persistence.model.OrderItem;
import com.jfeat.am.module.order.services.gen.persistence.model.OrderItemReward;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("settlementCenterService")
public class SettlementCenterServiceImpl implements SettlementCenterService {
    private Integer OK = 1;//结算成功
    private Integer NOT_OK = 2;//结算失败

    @Resource
    QueryBonusDao queryBonusDao;

    @Resource
    QueryOwnerBalanceDao queryOwnerBalanceDao;
    @Resource
    QueryOrderItemDao queryOrderItemDao;
    @Resource
    QueryOrderItemRewardDao queryOrderItemRewardDao;
    @Resource
    ConfigFieldService configFieldService;

    @Override
    @Transactional
    public boolean settlementOrder(Long orderId) {
        OrderCommissionInfo orderCommissionInfo = queryBonusDao.queryEveryOrderCommission(orderId);
        Long userId = orderCommissionInfo.getUserId();
        if (orderCommissionInfo != null) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(new Date());
            c2.setTime(orderCommissionInfo.getCreateTime());
            if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {//判断订单日期是否当前月
                return true;
            }
//推荐人
            Long invitorUserId = queryBonusDao.queryInvitorUserId(orderCommissionInfo.getUserId());
            if (invitorUserId != null) {
                BigDecimal bigDecimal = queryBonusDao.queryOrderAmountMonth(invitorUserId, orderCommissionInfo.getCreateTime());
                if (bigDecimal == null) {
                    bigDecimal = new BigDecimal(0.00);
                }
                BigDecimal condition = bigDecimal.subtract(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_WITHDRAWAL_CONDITIONS)));
                if (condition.compareTo(new BigDecimal(0.00)) >= 0) {
                    OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(new OwnerBalance().setUserId(invitorUserId));
                    if (ownerBalance != null) {
                        BigDecimal bonus_balance = ownerBalance.getBalance();
                        if (bonus_balance == null) {
                            bonus_balance = new BigDecimal(0.00);
                        }
                        List<OrderCommissionInfo> orderCommissionInfos = queryBonusDao.queryFormerOrder(invitorUserId, orderCommissionInfo.getCreateTime());
                        if (orderCommissionInfos != null && orderCommissionInfos.size() > 0) {
                            for (OrderCommissionInfo item : orderCommissionInfos) {
                                if (!item.getId().equals(orderCommissionInfo.getId()))
                                    bonus_balance = bonus_balance.add(item.getCommission());
                                queryBonusDao.upOrderSettlementStatus(OK, item.getId());
                                List<OrderItem> orderItems = queryOrderItemDao.selectList(new Condition().eq(OrderItem.ORDER_ID, item.getId()));
                                if (orderItems != null && orderItems.size() > 0) {
                                    for (OrderItem orderItem : orderItems) {
                                        OrderItemReward orderItemReward = new OrderItemReward();
                                        orderItemReward.setCreatedTime(new Date());
                                        orderItemReward.setOrderCreatedTime(item.getCreateTime());
                                        orderItemReward.setOrderId(item.getId().intValue());
                                        orderItemReward.setOrderNumber(item.getOrderNumber());
                                        orderItemReward.setOrderItemId(orderItem.getId().intValue());
                                        orderItemReward.setOwnerId(item.getUserId().intValue());
                                        orderItemReward.setOrderTotalPrice(item.getTotalPrice());
                                        orderItemReward.setState("成功");
                                        orderItemReward.setReward(queryBonusDao.getCommissionToOneItem(orderItem.getId()));
                                        orderItemReward.setWithdrawnTime(new Date());
                                        queryOrderItemRewardDao.insert(orderItemReward);
                                    }
                                }

                            }

                        }
                        ownerBalance.setBalance(bonus_balance.add(orderCommissionInfo.getCommission()));
                        queryBonusDao.upOrderSettlementStatus(OK, orderCommissionInfo.getId());
                        queryOwnerBalanceDao.updateById(ownerBalance);
                        List<OrderItem> orderItems = queryOrderItemDao.selectList(new Condition().eq(OrderItem.ORDER_ID, orderCommissionInfo.getId()));
                        if (orderItems != null && orderItems.size() > 0) {
                            for (OrderItem orderItem : orderItems) {
                                OrderItemReward orderItemReward = new OrderItemReward();
                                orderItemReward.setCreatedTime(new Date());
                                orderItemReward.setOrderCreatedTime(orderCommissionInfo.getCreateTime());
                                orderItemReward.setOrderId(orderCommissionInfo.getId().intValue());
                                orderItemReward.setOrderNumber(orderCommissionInfo.getOrderNumber());
                                orderItemReward.setOrderItemId(orderItem.getId().intValue());
                                orderItemReward.setOwnerId(orderCommissionInfo.getUserId().intValue());
                                orderItemReward.setOrderTotalPrice(orderCommissionInfo.getTotalPrice());
                                orderItemReward.setState("成功");
                                orderItemReward.setReward(queryBonusDao.getCommissionToOneItem(orderItem.getId()));
                                orderItemReward.setWithdrawnTime(new Date());
                                queryOrderItemRewardDao.insert(orderItemReward);
                            }
                        }
                    } else {
                        ownerBalance = new OwnerBalance();
                        ownerBalance.setUserId(invitorUserId);
                        List<OrderCommissionInfo> orderCommissionInfos = queryBonusDao.queryFormerOrder(invitorUserId, orderCommissionInfo.getCreateTime());
                        BigDecimal bonus_balance = new BigDecimal(0.00);
                        if (orderCommissionInfos != null && orderCommissionInfos.size() > 0) {
                            for (OrderCommissionInfo item : orderCommissionInfos) {
                                if (!item.getId().equals(orderCommissionInfo.getId()))
                                    bonus_balance = bonus_balance.add(item.getCommission());
                                queryBonusDao.upOrderSettlementStatus(OK, item.getId());
                                List<OrderItem> orderItems = queryOrderItemDao.selectList(new Condition().eq(OrderItem.ORDER_ID, item.getId()));
                                if (orderItems != null && orderItems.size() > 0) {
                                    for (OrderItem orderItem : orderItems) {
                                        OrderItemReward orderItemReward = new OrderItemReward();
                                        orderItemReward.setCreatedTime(new Date());
                                        orderItemReward.setOrderCreatedTime(item.getCreateTime());
                                        orderItemReward.setOrderId(item.getId().intValue());
                                        orderItemReward.setOrderNumber(item.getOrderNumber());
                                        orderItemReward.setOrderItemId(orderItem.getId().intValue());
                                        orderItemReward.setOwnerId(item.getUserId().intValue());
                                        orderItemReward.setOrderTotalPrice(item.getTotalPrice());
                                        orderItemReward.setState("成功");
                                        orderItemReward.setReward(queryBonusDao.getCommissionToOneItem(orderItem.getId()));
                                        orderItemReward.setWithdrawnTime(new Date());
                                        queryOrderItemRewardDao.insert(orderItemReward);
                                    }
                                }
                            }

                        }
                        ownerBalance.setBalance(bonus_balance.add(orderCommissionInfo.getCommission()));
                        queryBonusDao.upOrderSettlementStatus(OK, orderCommissionInfo.getId());
                        queryOwnerBalanceDao.insert(ownerBalance);
                        List<OrderItem> orderItems = queryOrderItemDao.selectList(new Condition().eq(OrderItem.ORDER_ID, orderCommissionInfo.getId()));
                        if (orderItems != null && orderItems.size() > 0) {
                            for (OrderItem orderItem : orderItems) {
                                OrderItemReward orderItemReward = new OrderItemReward();
                                orderItemReward.setCreatedTime(new Date());
                                orderItemReward.setOrderCreatedTime(orderCommissionInfo.getCreateTime());
                                orderItemReward.setOrderId(orderCommissionInfo.getId().intValue());
                                orderItemReward.setOrderNumber(orderCommissionInfo.getOrderNumber());
                                orderItemReward.setOrderItemId(orderItem.getId().intValue());
                                orderItemReward.setOwnerId(orderCommissionInfo.getUserId().intValue());
                                orderItemReward.setOrderTotalPrice(orderCommissionInfo.getTotalPrice());
                                orderItemReward.setState("成功");
                                orderItemReward.setReward(queryBonusDao.getCommissionToOneItem(orderItem.getId()));
                                orderItemReward.setWithdrawnTime(new Date());
                                queryOrderItemRewardDao.insert(orderItemReward);
                            }
                        }
                    }
                } else {
                    queryBonusDao.upOrderSettlementStatus(NOT_OK, orderCommissionInfo.getId());
                }


            } else {
                queryBonusDao.upOrderSettlementStatus(NOT_OK, orderCommissionInfo.getId());
            }

//当前人
            if (userId != null) {
                BigDecimal bigDecimal = queryBonusDao.queryOrderAmountMonth(userId, orderCommissionInfo.getCreateTime());
                if (bigDecimal == null) {
                    bigDecimal = new BigDecimal(0.00);
                }
                BigDecimal condition = bigDecimal.subtract(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_WITHDRAWAL_CONDITIONS)));
                if (condition.compareTo(new BigDecimal(0.00)) >= 0) {
                    OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(new OwnerBalance().setUserId(userId));
                    if (ownerBalance != null) {
                        BigDecimal bonus_balance = ownerBalance.getBalance();
                        if (bonus_balance == null) {
                            bonus_balance = new BigDecimal(0.00);
                        }
                        List<OrderCommissionInfo> orderCommissionInfos2 = queryBonusDao.queryFormerOrder(userId, orderCommissionInfo.getCreateTime());
                        if (orderCommissionInfos2 != null && orderCommissionInfos2.size() > 0) {
                            for (OrderCommissionInfo item : orderCommissionInfos2) {
                                if (!item.getId().equals(orderCommissionInfo.getId())) {
                                    bonus_balance = bonus_balance.add(item.getCommission());
                                }
                                queryBonusDao.upOrderSettlementStatus(OK, item.getId());
                                List<OrderItem> orderItems = queryOrderItemDao.selectList(new Condition().eq(OrderItem.ORDER_ID, item.getId()));
                                if (orderItems != null && orderItems.size() > 0) {
                                    for (OrderItem orderItem : orderItems) {
                                        OrderItemReward orderItemReward = new OrderItemReward();
                                        orderItemReward.setCreatedTime(new Date());
                                        orderItemReward.setOrderCreatedTime(item.getCreateTime());
                                        orderItemReward.setOrderId(item.getId().intValue());
                                        orderItemReward.setOrderNumber(item.getOrderNumber());
                                        orderItemReward.setOrderItemId(orderItem.getId().intValue());
                                        orderItemReward.setOwnerId(item.getUserId().intValue());
                                        orderItemReward.setOrderTotalPrice(item.getTotalPrice());
                                        orderItemReward.setState("成功");
                                        orderItemReward.setReward(queryBonusDao.getCommissionToOneItem(orderItem.getId()));
                                        orderItemReward.setWithdrawnTime(new Date());
                                        queryOrderItemRewardDao.insert(orderItemReward);
                                    }
                                }
                            }

                        }
                        ownerBalance.setBalance(bonus_balance);
                        queryOwnerBalanceDao.updateById(ownerBalance);
                    } else {
                        ownerBalance = new OwnerBalance();
                        ownerBalance.setUserId(userId);
                        List<OrderCommissionInfo> orderCommissionInfos2 = queryBonusDao.queryFormerOrder(userId, orderCommissionInfo.getCreateTime());
                        BigDecimal bonus_balance = new BigDecimal(0.00);
                        if (orderCommissionInfos2 != null && orderCommissionInfos2.size() > 0) {
                            for (OrderCommissionInfo item : orderCommissionInfos2) {
                                if (!item.getId().equals(orderCommissionInfo.getId()))
                                    bonus_balance = bonus_balance.add(item.getCommission());
                                queryBonusDao.upOrderSettlementStatus(OK, item.getId());
                                List<OrderItem> orderItems = queryOrderItemDao.selectList(new Condition().eq(OrderItem.ORDER_ID, item.getId()));
                                if (orderItems != null && orderItems.size() > 0) {
                                    for (OrderItem orderItem : orderItems) {
                                        OrderItemReward orderItemReward = new OrderItemReward();
                                        orderItemReward.setCreatedTime(new Date());
                                        orderItemReward.setOrderCreatedTime(item.getCreateTime());
                                        orderItemReward.setOrderId(item.getId().intValue());
                                        orderItemReward.setOrderNumber(item.getOrderNumber());
                                        orderItemReward.setOrderItemId(orderItem.getId().intValue());
                                        orderItemReward.setOwnerId(item.getUserId().intValue());
                                        orderItemReward.setOrderTotalPrice(item.getTotalPrice());
                                        orderItemReward.setState("成功");
                                        orderItemReward.setReward(queryBonusDao.getCommissionToOneItem(orderItem.getId()));
                                        orderItemReward.setWithdrawnTime(new Date());
                                        queryOrderItemRewardDao.insert(orderItemReward);
                                    }
                                }
                            }

                        }
                        ownerBalance.setBalance(bonus_balance);
                        queryOwnerBalanceDao.insert(ownerBalance);
                    }
                }
            }


        }
        return true;
    }

    @Override
    public BigDecimal getRatioBonus(Long userId) {
        List<Long> userIds = queryBonusDao.queryStockholderUserId();
        BigDecimal allBonusRatio = queryBonusDao.getAllBonusRatio();
        if(allBonusRatio==null){
            allBonusRatio=new BigDecimal(0.00);
        }
        BigDecimal mySelf = queryBonusDao.queryMyTeamOrderAmount(userId);
        if(mySelf==null){
            mySelf=new BigDecimal(0.00);
        }
        BigDecimal total=new BigDecimal(0.00);
        if(userIds!=null&&userIds.size()>0){
            for(Long id:userIds){
                BigDecimal bigDecimal = queryBonusDao.queryMyTeamOrderAmount(id);
                if(bigDecimal==null){
                    bigDecimal=new BigDecimal(0.00);
                }
                total=total.add(bigDecimal);
            }
        }
        return allBonusRatio.multiply((mySelf.divide(total)));
    }

    @Override
    public BigDecimal getRatioBonusMonth(Long userId) {
        List<Long> userIds = queryBonusDao.queryStockholderUserId();
        BigDecimal allBonusRatio = queryBonusDao.getAllBonusRatioMonth();
        if(allBonusRatio==null){
            allBonusRatio=new BigDecimal(0.00);
        }
        BigDecimal mySelf = queryBonusDao.queryMyTeamOrderAmountMonth(userId);
        if(mySelf==null){
            mySelf=new BigDecimal(0.00);
        }
        BigDecimal total=new BigDecimal(0.00);
        if(userIds!=null&&userIds.size()>0){
            for(Long id:userIds){
                BigDecimal bigDecimal = queryBonusDao.queryMyTeamOrderAmountMonth(id);
                if(bigDecimal==null){
                    bigDecimal=new BigDecimal(0.00);
                }
                total=total.add(bigDecimal);
            }
        }
        if(total.intValue()==0){
            return new BigDecimal(0.00);
        }
        return allBonusRatio.multiply((mySelf.divide(total)));
    }
}
