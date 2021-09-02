package com.jfeat.am.module.alliance.services.domain.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.module.alliance.services.domain.model.WalletHistoryRecord;
import com.jfeat.am.module.alliance.services.gen.persistence.model.WalletHistory;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Code Generator on 2019-10-14
 */
public interface QueryWalletHistoryDao extends BaseMapper<WalletHistory> {

    List<WalletHistoryRecord> findWalletHistoryPage(Page<WalletHistoryRecord> page,
                                                    @Param("search") String search,
                                                    @Param("type") String type,
                                                    @Param("leftMoney") BigDecimal leftMoney,
                                                    @Param("rightMoney") BigDecimal rightMoney
                                                    );


}