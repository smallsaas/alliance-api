package com.jfeat.am.module.friend.services.domain.service;

import com.jfeat.am.module.friend.api.RequestOrder;
import com.jfeat.am.module.friend.services.gen.crud.service.CRUDMomentsFriendService;

import java.rmi.ServerException;


/**
 * Created by vincent on 2017/10/19.
 */
public interface MomentsFriendService extends CRUDMomentsFriendService {
    public Integer createOrder(RequestOrder requestOrder) throws ServerException;

    public Integer closeConfirmedOrder(Long id);
}