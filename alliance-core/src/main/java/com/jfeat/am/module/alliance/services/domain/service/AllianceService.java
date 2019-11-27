package com.jfeat.am.module.alliance.services.domain.service;

import com.jfeat.am.module.alliance.api.AllianceRequest;
import com.jfeat.am.module.alliance.api.RequestAlliance;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;
import com.jfeat.am.module.alliance.services.gen.crud.service.CRUDAllianceService;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;

import java.text.ParseException;
import java.util.List;

/**
 * Created by vincent on 2017/10/19.
 */
public interface AllianceService extends CRUDAllianceService{

    //根据邀请人电话查找邀请人信息
    public Alliance findAllianceByPhoneNumber(String PhoneNumber);
    public List<Alliance> getAlliancesByUserId(Long id);
    public AllianceRecord getSelfProductById(Long id);
    public Integer createAlliance(RequestAlliance requestAlliance, Long userId);

    ///
    public Alliance getAlliancesByBindingUserId(Long id);

    public Integer create(Long userId, AllianceRequest entity)throws ParseException;

    public Integer modify(Long id,AllianceRecord entity) throws ParseException;

    public Integer modifyAllianceShip(Long id);


}