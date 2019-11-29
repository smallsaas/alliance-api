package com.jfeat.am.module.alliance.services.gen.crud.service.impl;
            
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jfeat.crud.plus.FIELD;
import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;
import com.jfeat.am.module.alliance.services.gen.persistence.dao.OwnerBalanceMapper;
import com.jfeat.am.module.alliance.services.gen.crud.service.CRUDOwnerBalanceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import javax.annotation.Resource;
import com.jfeat.crud.plus.impl.CRUDServiceOnlyImpl;

/**
 * <p>
 *  implementation
 * </p>
 *CRUDOwnerBalanceService
 * @author Code Generator
 * @since 2019-11-29
 */

@Service
public class CRUDOwnerBalanceServiceImpl  extends CRUDServiceOnlyImpl<OwnerBalance> implements CRUDOwnerBalanceService {





        @Resource
        protected OwnerBalanceMapper ownerBalanceMapper;

        @Override
        protected BaseMapper<OwnerBalance> getMasterMapper() {
                return ownerBalanceMapper;
        }







}


