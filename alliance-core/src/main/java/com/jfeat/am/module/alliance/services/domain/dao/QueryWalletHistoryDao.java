package com.jfeat.am.module.alliance.services.domain.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.module.alliance.services.domain.model.WalletHistoryRecord;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Code Generator on 2019-10-14
 */
public interface QueryWalletHistoryDao extends BaseMapper<WalletHistory> {

    List<WalletHistoryRecord> findWalletHistoryPage(Page<WalletHistoryRecord> page,
                                             @Param("search") String search,
                                                    @Param("type") String type);


}