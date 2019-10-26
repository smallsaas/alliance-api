package com.jfeat.am.module.friend.services.domain.dao.mapping;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendOverOrdersRecord;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendRecord;
import com.jfeat.am.module.friend.services.gen.persistence.model.MomentsFriend;
import com.jfeat.am.module.friend.services.gen.persistence.model.Order;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Code Generator on 2019-10-14
 */
public interface QueryMomentsFriendOverOrderDao extends BaseMapper<Order> {
}