package com.jfeat.am.module.bonus.services.gen.crud.service.impl;
            
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jfeat.am.module.bonus.services.gen.persistence.model.OfflineWithdrawal;
import com.jfeat.am.module.bonus.services.gen.persistence.dao.OfflineWithdrawalMapper;
import com.jfeat.am.module.bonus.services.gen.crud.service.CRUDOfflineWithdrawalService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.jfeat.crud.plus.impl.CRUDServiceOnlyImpl;

/**
 * <p>
 *  implementation
 * </p>
 *CRUDOfflineWithdrawalService
 * @author Code Generator
 * @since 2019-12-03
 */

@Service
public class CRUDOfflineWithdrawalServiceImpl  extends CRUDServiceOnlyImpl<OfflineWithdrawal> implements CRUDOfflineWithdrawalService {





        @Resource
        protected OfflineWithdrawalMapper offlineWithdrawalMapper;

        @Override
        protected BaseMapper<OfflineWithdrawal> getMasterMapper() {
                return offlineWithdrawalMapper;
        }







}


