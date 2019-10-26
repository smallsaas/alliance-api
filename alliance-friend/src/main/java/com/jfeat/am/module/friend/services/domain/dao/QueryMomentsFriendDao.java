package com.jfeat.am.module.friend.services.domain.dao;

import com.jfeat.am.module.friend.services.domain.model.MomentsFriendOverOrdersRecord;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jfeat.am.module.friend.services.gen.persistence.model.Order;
import org.apache.ibatis.annotations.Param;
import com.jfeat.am.module.friend.services.gen.persistence.model.MomentsFriend;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Code Generator on 2019-10-14
 */
public interface QueryMomentsFriendDao extends BaseMapper<MomentsFriend> {
    List<MomentsFriendOverOrdersRecord> findMomentsFriendPage(Page<MomentsFriendOverOrdersRecord> page, @Param("record") MomentsFriendRecord record,
                                                    @Param("search") String search, @Param("orderBy") String orderBy,
                                                    @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    Order findOrdersByPhone(@Param("phone") String id,@Param("userId") Long userId);
    List<Long> selectUserId(@Param("name") String name);
    Long insertOrder(@Param("userId")Long id, @Param("totalPrice")BigDecimal totalPrice,@Param("phone")String phone,@Param("mname")String mname,@Param("detail")String detail);
    Long selectProductId(@Param("barcode")String barcode);
    Integer insertOrderItem(@Param("orderId")Long orderId,@Param("barcode")String barcode,@Param("productName")String productName,@Param("quantity")Integer quantity,@Param("finalPrice") BigDecimal finalPrice);
}