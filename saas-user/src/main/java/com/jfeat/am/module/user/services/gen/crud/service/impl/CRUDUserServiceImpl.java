package com.jfeat.am.module.user.services.gen.crud.service.impl;
            
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jfeat.am.module.user.services.gen.persistence.model.User;
import com.jfeat.am.module.user.services.gen.persistence.dao.UserMapper;
import com.jfeat.am.module.user.services.gen.crud.service.CRUDUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import com.jfeat.crud.plus.impl.CRUDServiceOnlyImpl;

/**
 * <p>
 *  implementation
 * </p>
 *CRUDUserService
 * @author Code Generator
 * @since 2019-11-05
 */

@Service
public class CRUDUserServiceImpl  extends CRUDServiceOnlyImpl<User> implements CRUDUserService {





        @Resource
        protected UserMapper userMapper;

        @Override
        protected BaseMapper<User> getMasterMapper() {
                return userMapper;
        }







}


