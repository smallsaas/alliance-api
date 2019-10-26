package com.jfeat.am.module.bonus.services.gen.crud.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jfeat.am.module.bonus.services.gen.crud.service.CRUDBonusProportionService;
import com.jfeat.am.module.bonus.services.gen.persistence.dao.BonusProportionMapper;
import com.jfeat.am.module.bonus.services.gen.persistence.model.BonusProportion;
import com.jfeat.crud.plus.impl.CRUDServiceOnlyImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


@Service
public class CRUDBonusProportionServiceImpl
        extends CRUDServiceOnlyImpl<BonusProportion>
        implements CRUDBonusProportionService {
    @Resource
    protected BonusProportionMapper bonusProportionMapper;

    protected BaseMapper<BonusProportion> getMasterMapper() {
        return this.bonusProportionMapper;
    }
}

