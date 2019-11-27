package com.jfeat.am.module.friend.services.domain.service.impl;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.jfeat.am.module.friend.api.OrderStatus;
import com.jfeat.am.module.friend.api.RequestOrder;
import com.jfeat.am.module.friend.services.domain.dao.QueryMomentsFriendDao;
import com.jfeat.am.module.friend.services.domain.dao.mapping.QueryMomentsFriendOverOrderDao;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendUser;
import com.jfeat.am.module.friend.services.domain.service.MomentsFriendService;
import com.jfeat.am.module.friend.services.gen.crud.service.impl.CRUDMomentsFriendServiceImpl;
import com.jfeat.am.module.friend.services.gen.persistence.model.FriendOrder;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.Date;
import java.util.List;

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

    @Override
    @Transactional
    public Integer createOrder(RequestOrder requestOrder) throws ServerException {
        /*List<Long> userIds = queryMomentsFriendDao.selectUserId(requestOrder.getById());
        if (userIds == null || userIds.size() == 0) {
            throw new ServerException("该下单人不存在");
        }
        if (userIds.size() > 1) {
            throw new ServerException("该下单人姓名相同的有好几个");
        }*/
        MomentsFriendUser user = queryMomentsFriendDao.selectByUserId(requestOrder.getUserId());

       /* Long productId = queryMomentsFriendDao.selectProductId(requestOrder.getBarcode());
        if (productId == null) {
            throw new ServerException("该表形码" + requestOrder.getBarcode() + "的商品不存在");
        }*/
        Long productId = requestOrder.getProductId();
        //根据产品id查找barcode
        String barcode=queryMomentsFriendDao.selectBarcodeByProductId(productId);

        requestOrder.setTotalPrice(requestOrder.getFinalPrice().multiply(new BigDecimal(requestOrder.getQuantity())));
        FriendOrder order = new FriendOrder();
        //order.setUserId(userIds.get(0));
        requestOrder.setBarcode(barcode);

        order.setUserId(user.getId());
        order.setPhone(requestOrder.getPhone());
        order.setDetail(requestOrder.getDetail());
        order.setContactUser(requestOrder.getName());
        order.setStatus(OrderStatus.CLOSED_CONFIRMED);
        order.setType("STORE_ORDER");
        order.setTotalPrice(requestOrder.getTotalPrice());
        order.setCreatedDate(new Date());
        String oderNumber = IdWorker.getIdStr();
        order.setOrderNumber(oderNumber);
        queryMomentsFriendOverOrderDao.insert(order);
        Integer res = queryMomentsFriendDao.insertOrderItem(order.getId(), requestOrder.getBarcode(), requestOrder.getProductName(), requestOrder.getQuantity(), requestOrder.getFinalPrice());
        //String allianceName = queryMomentsFriendDao.queryAllianceName(userIds.get(0));
        String allianceName = queryMomentsFriendDao.queryAllianceName(user.getId());
        if (allianceName == null || allianceName.length() == 0) {
            throw new BusinessException(BusinessCode.BadRequest, "该下单人不是正式盟友");
        } else {
            Integer stockBalance = queryMomentsFriendDao.queryStockBalance(productId);
            stockBalance = stockBalance - requestOrder.getQuantity();
            if (stockBalance >= 0) {
                queryMomentsFriendDao.upProduct(productId, stockBalance);
            } else {
                throw new BusinessException(BusinessCode.BadRequest, "该商品库存不足");
            }
           // BigDecimal balance = queryMomentsFriendDao.queryWalletBalance(userIds.get(0));
            BigDecimal balance = queryMomentsFriendDao.queryWalletBalance(user.getId());
            if (balance == null || balance.compareTo(new BigDecimal(0.00)) <= 0) {
                throw new BusinessException(BusinessCode.BadRequest, "该用户余额不足");
            } else {
                balance = balance.subtract(requestOrder.getTotalPrice());
                if (balance.compareTo(new BigDecimal(0.00)) < 0) {
                    throw new BusinessException(BusinessCode.BadRequest, "该用户余额不足");
                } else {
//                    queryMomentsFriendDao.upWallet(userIds.get(0), balance);
                    queryMomentsFriendDao.upWallet(user.getId(), balance);
                }
            }
        }

        return res;
    }

}

/**
 * 生成订单编号
 *
 * @return
 */
/*
class OrderNumber extends Thread{

    private static long orderNum = 0l;
    private static String date ;

    public static synchronized String getOrderNo() {
        String str = new SimpleDateFormat("yyMMddHHmm").format(new Date());
        if(date==null||!date.equals(str)){
            date = str;
            orderNum  = 0l;
        }
        orderNum ++;
        long orderNo = Long.parseLong((date)) * 10000;
        orderNo += orderNum;;
        return orderNo+"";
    }
}
*/
