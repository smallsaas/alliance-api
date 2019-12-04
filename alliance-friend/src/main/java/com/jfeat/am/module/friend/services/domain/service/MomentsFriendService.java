package com.jfeat.am.module.friend.services.domain.service;

import com.jfeat.am.module.friend.api.OrderProductRequest;
import com.jfeat.am.module.friend.api.OrderUserRequest;
import com.jfeat.am.module.friend.api.RequestOrder;
import com.jfeat.am.module.friend.services.gen.crud.service.CRUDMomentsFriendService;

import java.rmi.ServerException;
import java.util.List;


/**
 * Created by vincent on 2017/10/19.
 */
public interface MomentsFriendService extends CRUDMomentsFriendService {
    public Integer createOrder(RequestOrder requestOrder) throws ServerException;

    public Integer closeConfirmedOrder(Long id);
    public Integer cancelOrder(Long id)throws ServerException;

    public List<OrderUserRequest> getUsers(String search);

    public List<OrderProductRequest> getProducts(String search);

}