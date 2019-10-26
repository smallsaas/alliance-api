package com.jfeat.am.module.bonus.services.domain.service.impl;

import com.jfeat.am.module.bonus.services.domain.service.BonusProportionService;
import com.jfeat.am.module.bonus.services.gen.crud.service.impl.CRUDBonusProportionServiceImpl;
import org.springframework.stereotype.Service;

@Service("bonusProportionService")
public class BonusProportionServiceImpl extends CRUDBonusProportionServiceImpl implements BonusProportionService {}

