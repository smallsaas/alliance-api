package com.jfeat.am.module.bonus.services.domain.dao;

import com.jfeat.am.module.bonus.services.domain.model.OfflineWithdrawalRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.jfeat.am.module.bonus.services.gen.persistence.model.OfflineWithdrawal;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by Code Generator on 2019-12-03
 */
public interface QueryOfflineWithdrawalDao extends BaseMapper<OfflineWithdrawal> {
    List<OfflineWithdrawalRecord> findOfflineWithdrawalPage(Page<OfflineWithdrawalRecord> page, @Param("record") OfflineWithdrawalRecord record,
                                            @Param("search") String search, @Param("orderBy") String orderBy,
                                            @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    @Select("select alliance_name from t_alliance where user_id=#{userId}")
    String queryAllianceName(@Param("userId") Long userId);
}