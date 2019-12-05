package com.jfeat.am.module.alliance.services.domain.dao;

import com.jfeat.am.module.alliance.services.domain.model.OwnerBalanceRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
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
                                            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    OwnerBalanceRecord findOneOwnerBalance (@Param("id") Integer id);
    Integer withdrawal(@Param("id") Integer id,@Param("money") BigDecimal money);
    Integer withdrawalByUserId(@Param("id") Long id,@Param("money") BigDecimal money);

    Long getInvitorUserIdByUserId(@Param("userId") Long userId);
}