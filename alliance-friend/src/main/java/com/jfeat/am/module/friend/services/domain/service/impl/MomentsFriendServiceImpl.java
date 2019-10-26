package com.jfeat.am.module.friend.services.domain.service.impl;

import com.jfeat.am.module.friend.api.RequestOrder;
import com.jfeat.am.module.friend.services.domain.dao.QueryMomentsFriendDao;
import com.jfeat.am.module.friend.services.domain.dao.mapping.QueryMomentsFriendOverOrderDao;
import com.jfeat.am.module.friend.services.domain.service.MomentsFriendService;
import com.jfeat.am.module.friend.services.gen.crud.service.impl.CRUDMomentsFriendServiceImpl;
import com.jfeat.am.module.friend.services.gen.persistence.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.rmi.ServerException;
import java.util.List;

/**
 * <p>
 *  服务实现类
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
        List<Long> userIds = queryMomentsFriendDao.selectUserId(requestOrder.getById());
        if (userIds==null||userIds.size()==0) {
            throw new ServerException("该下单人不存在");

        }
        if(userIds.size()>1){
            throw new ServerException("该下单人有好几个");
        }
        Long productId = queryMomentsFriendDao.selectProductId(requestOrder.getBarcode());
//        if(productId==null){
//            throw new ServerException("该表形码"+requestOrder.getBarcode()+"的商品不存在");
//        }
        Order order=new Order();
        order.setUserId(userIds.get(0));
        order.setTotalPrice(requestOrder.getTotalPrice());
        order.setPhone(requestOrder.getPhone());
        order.setDetail(requestOrder.getDetail());
        order.setContactUser(requestOrder.getName());
        queryMomentsFriendOverOrderDao.insert(order);
//        Long orderId = queryMomentsFriendDao.insertOrder(userIds.get(0), requestOrder.getTotalPrice(),
//                requestOrder.getPhone(), requestOrder.getName(), requestOrder.getDetail());

        Integer integer = queryMomentsFriendDao.insertOrderItem(order.getId(), requestOrder.getBarcode(), requestOrder.getProductName(), requestOrder.getQuantity(), requestOrder.getFinalPrice());


        return integer;
    }
}
