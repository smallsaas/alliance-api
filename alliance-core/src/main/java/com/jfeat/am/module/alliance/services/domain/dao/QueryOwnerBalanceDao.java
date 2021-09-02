package com.jfeat.am.module.alliance.services.domain.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.module.alliance.services.domain.model.OwnerBalanceRecord;

import org.apache.ibatis.annotations.Param;
import com.jfeat.am.module.alliance.services.gen.persistence.model.OwnerBalance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Code Generator on 2019-11-29
 */
public interface QueryOwnerBalanceDao extends BaseMapper<OwnerBalance> {
    List<OwnerBalanceRecord> findOwnerBalancePage(Page<OwnerBalanceRecord> page, @Param("record") OwnerBalanceRecord record,
                                                  @Param("search") String search, @Param("orderBy") String orderBy,
                                                  @Param("startTime") Date startTime, @Param("endTime") Date endTime,
                                                  @Param("leftMoney") BigDecimal leftMoney,
                                                  @Param("rightMoney") BigDecimal rightMoney
    );

    OwnerBalanceRecord findOneOwnerBalance (@Param("id") Integer id);
    Integer withdrawal(@Param("id") Integer id,@Param("money") BigDecimal money);
    Integer withdrawalByUserId(@Param("id") Long id,@Param("money") BigDecimal money);

    Long getInvitorUserIdByUserId(@Param("userId") Long userId);
}