package com.jfeat.am.module.friend.services.gen.crud.service.impl;
            
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jfeat.crud.plus.FIELD;
import com.jfeat.am.module.friend.services.gen.persistence.model.MomentsFriend;
import com.jfeat.am.module.friend.services.gen.persistence.dao.MomentsFriendMapper;
import com.jfeat.am.module.friend.services.gen.crud.service.CRUDMomentsFriendService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.jfeat.crud.plus.impl.CRUDServiceOnlyImpl;

/**
 * <p>
 *  implementation
 * </p>
 *CRUDMomentsFriendService
 * @author Code Generator
 * @since 2019-10-14
 */

@Service
public class CRUDMomentsFriendServiceImpl  extends CRUDServiceOnlyImpl<MomentsFriend> implements CRUDMomentsFriendService {





        @Resource
        protected MomentsFriendMapper momentsFriendMapper;

        @Override
        protected BaseMapper<MomentsFriend> getMasterMapper() {
                return momentsFriendMapper;
        }







}


