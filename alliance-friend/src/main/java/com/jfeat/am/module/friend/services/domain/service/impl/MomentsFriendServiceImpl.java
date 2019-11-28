package com.jfeat.am.module.friend.services.domain.service.impl;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.jfeat.am.module.friend.api.OrderStatus;
import com.jfeat.am.module.friend.api.RequestOrder;
import com.jfeat.am.module.friend.api.RequestProduct;
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

        Integer res=0;
        //之前是获取名字 现在更改为获取id
        MomentsFriendUser user = queryMomentsFriendDao.selectByUserId(requestOrder.getUserId());
       //订单项不为空 处理订单项
       if(requestOrder.getItems().size()!=0){

           //查询该用户是否为盟友
           String allianceName = queryMomentsFriendDao.queryAllianceName(user.getId());
           if (allianceName == null || allianceName.length() == 0) {
               throw new BusinessException(BusinessCode.BadRequest, "该下单人不是正式盟友");
           }
           //订单处理
           FriendOrder order = new FriendOrder();
           order.setUserId(user.getId());
           order.setPhone(requestOrder.getPhone());
           order.setDetail(requestOrder.getDetail());
           order.setContactUser(requestOrder.getName());
           order.setPaymentType(requestOrder.getPaymentType());
           //默认状态
           order.setStatus(OrderStatus.CLOSED_CONFIRMED);
           //订单类型 线下订单
           order.setType("STORE_ORDER");
           order.setCreatedDate(new Date());
           String oderNumber = IdWorker.getIdStr();
           order.setOrderNumber(oderNumber);

           //获取订单数据//循环遍历
           List<RequestProduct> productList= requestOrder.getItems();
           //总价
           BigDecimal finalPrice=new BigDecimal(0);
           for (RequestProduct product:productList) {
               Long productId = product.getId();
               //根据产品id查找barcode
               String barcode=queryMomentsFriendDao.selectBarcodeByProductId(productId);
               //设置订单总价
               //处理产品总价
               product.setTotalPrice(product.getPrice().multiply(new BigDecimal(product.getQuantity())));
               finalPrice=product.getTotalPrice().add(finalPrice);

               /*requestOrder.setTotalPrice(requestOrder.getFinalPrice().multiply(new BigDecimal(requestOrder.getQuantity())));*/
              /* requestOrder.setBarcode(barcode);*/

               //查找库存
               Integer stockBalance = queryMomentsFriendDao.queryStockBalance(productId);
               //更改后的库存量
               stockBalance = stockBalance - product.getQuantity();
               if (stockBalance < 0) { throw new BusinessException(BusinessCode.BadRequest, "商品库存不足"); }

               BigDecimal balance = queryMomentsFriendDao.queryWalletBalance(user.getId());
               if (balance == null || balance.compareTo(new BigDecimal(0.00)) <= 0) {
                   throw new BusinessException(BusinessCode.BadRequest, "该用户余额不足");
               } else {
                   balance = balance.subtract(product.getTotalPrice());
                   if (balance.compareTo(new BigDecimal(0.00)) < 0) {
                       throw new BusinessException(BusinessCode.BadRequest, "该用户余额不足");
                   } else {
                       //更新用户余额
                       queryMomentsFriendDao.upWallet(user.getId(), balance);
                   }
               }
               //更新产品库存
               queryMomentsFriendDao.upProduct(productId, stockBalance);

           }
           //插入订单
           order.setTotalPrice(finalPrice);
           queryMomentsFriendOverOrderDao.insert(order);

           for (RequestProduct product:productList) {
               //插入订单项数据
               res = queryMomentsFriendDao.insertOrderItem
                       (order.getId(), product.getBarcode(),
                               product.getName(),
                               product.getQuantity(),
                               product.getTotalPrice()
                              );
           }
       }
       //订单项为空则抛出
       else{ throw new BusinessException(BusinessCode.BadRequest, "请添加产品");}
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
