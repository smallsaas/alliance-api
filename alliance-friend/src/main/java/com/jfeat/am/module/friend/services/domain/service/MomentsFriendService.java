package com.jfeat.am.module.friend.services.domain.service;

import com.jfeat.am.module.friend.services.gen.crud.service.CRUDMomentsFriendService;

import java.rmi.ServerException;


/**
 * Created by vincent on 2017/10/19.
 */
public interface MomentsFriendService extends CRUDMomentsFriendService {


    public Integer closeConfirmedOrder(Long id);

    public Integer cancelCloseConfirmedOrder(Long id);
     public Integer cancelOrder(Long id)throws ServerException;


}