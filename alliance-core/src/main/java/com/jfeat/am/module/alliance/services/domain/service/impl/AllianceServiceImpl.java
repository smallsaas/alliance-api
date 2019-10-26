package com.jfeat.am.module.alliance.services.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;
import com.jfeat.am.module.alliance.services.domain.service.AllianceService;
import com.jfeat.am.module.alliance.services.gen.crud.service.impl.CRUDAllianceServiceImpl;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2017-10-16
 */
@Service("allianceService")
public class AllianceServiceImpl extends CRUDAllianceServiceImpl implements AllianceService {

    @Resource
    QueryAllianceDao queryAllianceDao;
    @Override
    public Alliance findAllianceByPhoneNumber(String phoneNumber) {

        return queryAllianceDao.selectOne(new Alliance().setAlliancePhone(phoneNumber));
    }

    public List<Alliance> getAlliancesByUserId(Long id){
        Alliance entity = new Alliance();
        entity.setUserId(id);
        Alliance alliance = queryAllianceDao.selectOne(entity);
        if(alliance!=null){
            return queryAllianceDao.selectList(new EntityWrapper<Alliance>().eq("invitor_alliance_id",alliance.getId()));
        }else
            return null;
    }

    @Override
    public AllianceRecord getSelfProductById(Long id) {
        AllianceRecord alliance = queryAllianceDao.selectAllianceOneByUserId(id);
        Long userId = alliance.getUserId();

        if(userId!=null){
            List<Map> selfProductByUserId = queryAllianceDao.getSelfProductByUserId(userId);
            if(selfProductByUserId!=null){
                alliance.setSelfProducts(JSON.parseArray(JSON.toJSONString(selfProductByUserId)));
            }
        }
        return alliance;
    }
}
