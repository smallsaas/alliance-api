package com.jfeat.am.module.friend.services.domain.service.impl;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.jfeat.am.module.bonus.services.domain.service.SettlementCenterService;
import com.jfeat.am.module.friend.api.AllianceProduct;
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
    @Transactional
    public Integer createOrder(RequestOrder requestOrder) throws ServerException {

        Integer res=0;
        //之前是获取名字 现在更改为获取id
        MomentsFriendUser user = queryMomentsFriendDao.selectByUserId(requestOrder.getUserId());

       //订单项不为空 处理订单项
       if(requestOrder.getItems()!=null&&requestOrder.getItems().size()>0){

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
           //省市区
           order.setProvince(requestOrder.getProvince());
           order.setCity(requestOrder.getCity());
           order.setDistrict(requestOrder.getDistrict());

           order.setContactUser(requestOrder.getName());
           //支付类型 默认线下支付
           order.setPaymentType("STORE");
           //默认状态 已发货
           order.setStatus(OrderStatus.DELIVERED_CONFIRM_PENDING);
           //订单类型 线下订单
           order.setType("STORE_ORDER");

           order.setCreatedDate(requestOrder.getCreateDate());
           //计算日期

           if (order.getCreatedDate()!=null ) {
               Long time=order.getCreatedDate().getTime() - new Date().getTime();
               if(time>0){
                   throw new BusinessException(BusinessCode.BadRequest, "请选择今天或之前的日期");
               }

           }

           order.setCreatedDate(requestOrder.getCreateDate());
           String oderNumber = IdWorker.getIdStr();
           order.setOrderNumber(oderNumber);

           //获取订单数据//循环遍历
           List<RequestProduct> productList= requestOrder.getItems();
           //总价
           BigDecimal finalPrice=new BigDecimal(0);
           for (RequestProduct product:productList) {

               AllianceProduct allianceProduct = queryMomentsFriendDao.queryProductById(product.getId());
               product.setPrice(allianceProduct.getPrice());
               product.setCover(allianceProduct.getCover());
               product.setCostPrice(allianceProduct.getCostPrice());

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

           //封面处理
          /* if( requestOrder.getImges()!=null&& requestOrder.getImges().size()>0){
               order.setCover(requestOrder.getImges().get(0).getUrl()) ;
           }*/
           order.setCover(requestOrder.getItems().get(0).getCover());

           //插入订单
           order.setTotalPrice(finalPrice);
           queryMomentsFriendOverOrderDao.insert(order);

           for (RequestProduct product:productList) {



               //插入订单项数据
               res = queryMomentsFriendDao.insertOrderItem
                       (order.getId(), product.getBarcode(),
                               product.getName(),
                               product.getQuantity(),
                               product.getTotalPrice(),
                               product.getPrice(),
                               product.getCostPrice(),
                               product.getCover(),
                               product.getId()
                              );
           }
       }
       //订单项为空则抛出
       else{ throw new BusinessException(BusinessCode.BadRequest, "请添加产品");}
        return res;
    }

    @Override
    public Integer closeConfirmedOrder(Long id) {
     /*   AllianceProduct allianceProduct = queryMomentsFriendDao.queryProductById(id);
        allianceProduct.setStatus(OrderStatus.CLOSED_CONFIRMED);*/

        Integer i=queryMomentsFriendDao.closeProduct(id);
        SettlementCenterService.settlementOrder(id);


        return i;
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
