package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import static java.math.BigDecimal.ROUND_HALF_UP;

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
        //查询订单提成
        OrderCommissionInfo orderCommissionInfo = queryBonusDao.queryEveryOrderCommission(orderId);

        if (orderCommissionInfo != null) {
            Long userId = orderCommissionInfo.getUserId();
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
                //推荐人进货额
                BigDecimal bigDecimal = queryBonusDao.queryOrderAmountMonth(invitorUserId, orderCommissionInfo.getCreateTime());
                if (bigDecimal == null) {
                    bigDecimal = new BigDecimal(0.00);
                }
                //configFieldService 查找结算条件  每月至低可提现进货额 condition 查找数据库得出 2000
                BigDecimal condition = bigDecimal.subtract(new BigDecimal(configFieldService.getFieldFloat(AllianceFields.ALLIANCE_FIELD_WITHDRAWAL_CONDITIONS)));
                //进货额符合条件
                if (condition.compareTo(new BigDecimal(0.00)) >= 0) {

                    OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(new LambdaQueryWrapper<>(new OwnerBalance().setUserId(invitorUserId)));
                    //可提现金额
                    if (ownerBalance != null) {
                        //当前 可提现金额
                        BigDecimal bonus_balance = ownerBalance.getBalance();
                        if (bonus_balance == null) {
                            bonus_balance = new BigDecimal(0.00);
                        }
                        //根据邀请人id 订单创造时间 查找
                        List<OrderCommissionInfo> orderCommissionInfos = queryBonusDao.queryFormerOrder(invitorUserId, orderCommissionInfo.getCreateTime());
                        if (orderCommissionInfos != null && orderCommissionInfos.size() > 0) {
                            for (OrderCommissionInfo item : orderCommissionInfos) {
                                if (!item.getId().equals(orderCommissionInfo.getId()))
                                    if(item.getCommission()==null){item.setCommission(new BigDecimal(0));}
                                    bonus_balance = bonus_balance.add(item.getCommission());
                                queryBonusDao.upOrderSettlementStatus(OK, item.getId());
                                List<OrderItem> orderItems = queryOrderItemDao.selectList(new QueryWrapper<OrderItem>().eq(OrderItem.ORDER_ID, item.getId()));
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
                        List<OrderItem> orderItems = queryOrderItemDao.selectList(new QueryWrapper<OrderItem>().eq(OrderItem.ORDER_ID, orderCommissionInfo.getId()));
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
                                List<OrderItem> orderItems = queryOrderItemDao.selectList(new QueryWrapper<OrderItem>().eq(OrderItem.ORDER_ID, item.getId()));
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
                        List<OrderItem> orderItems = queryOrderItemDao.selectList(new QueryWrapper<OrderItem>().eq(OrderItem.ORDER_ID, orderCommissionInfo.getId()));
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

                    OwnerBalance ownerBalance = queryOwnerBalanceDao.selectOne(new LambdaQueryWrapper<>(new OwnerBalance().setUserId(userId)));
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
                                List<OrderItem> orderItems = queryOrderItemDao.selectList(new QueryWrapper<OrderItem>().eq(OrderItem.ORDER_ID, item.getId()));
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
                                List<OrderItem> orderItems = queryOrderItemDao.selectList(new QueryWrapper<OrderItem>().eq(OrderItem.ORDER_ID, item.getId()));
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

    //取消订单确认
    @Override
    public boolean cancelSettlementOrder(Long orderId) {
        //查出订单
        OrderCommissionInfo orderCommissionInfo = queryBonusDao.cancelQueryEveryOrderCommission(orderId);

        //查到数据 不然不处理
        if (orderCommissionInfo != null) {
            Long userId = orderCommissionInfo.getUserId();

            userId=queryOwnerBalanceDao.getInvitorUserIdByUserId(userId);
            //去掉提成金额
            queryOwnerBalanceDao.withdrawalByUserId(userId,orderCommissionInfo.getCommission());
            //删除结算记录
            queryOrderItemRewardDao.delete(new QueryWrapper<OrderItemReward>().eq("order_number",orderCommissionInfo.getOrderNumber()));

        }

        return true;
    }

    //设置所有盟友订单总额
    @Override
    public Integer setTotal(){
        /// get total
        BigDecimal total=new BigDecimal(0.00);
        {

            List<Long> userIds = queryBonusDao.queryStockholderUserId();
            if (userIds != null && userIds.size() > 0) {
                for (Long id : userIds) {
                    BigDecimal bigDecimal = queryBonusDao.queryMyTeamOrderAmount(id);
                    if (bigDecimal == null) {
                        bigDecimal = new BigDecimal(0.00);
                    }
                    total = total.add(bigDecimal);
                }
            }
        }
        Integer i = queryBonusDao.updateAllianceTotlePrice(total);
        return i;
    }

    @Override
    public BigDecimal getRatioBonusPercent(Long userId) {
        //获取进货额
        BigDecimal mySelf = queryBonusDao.queryMyTeamOrderAmount(userId);
        if(mySelf==null){
            mySelf=new BigDecimal(0.00);
        }

        /// get total
        BigDecimal total=new BigDecimal(0.00);
        {

            List<Long> userIds = queryBonusDao.queryStockholderUserId();
            if (userIds != null && userIds.size() > 0) {
                for (Long id : userIds) {
                    BigDecimal bigDecimal = queryBonusDao.queryMyTeamOrderAmount(id);
                    if (bigDecimal == null) {
                        bigDecimal = new BigDecimal(0.00);
                    }
                    total = total.add(bigDecimal);
                }
            }
        }

        if(total.intValue()==0){
            return new BigDecimal(0.0);
        }

        return mySelf.divide(total,4,ROUND_HALF_UP);
    }

    @Override
    public BigDecimal getRatioBonus(Long userId) {
        BigDecimal allBonusRatio = queryBonusDao.getAllBonusRatio();
        if(allBonusRatio==null){
            allBonusRatio=new BigDecimal(0.00);
        }

        BigDecimal percentage = getRatioBonusPercent(userId);

        return allBonusRatio.multiply(percentage);
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
        return allBonusRatio.multiply((mySelf.divide(total,2)));
    }
}
