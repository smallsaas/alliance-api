package com.jfeat.am.module.bonus.services.domain.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface QueryBonusDao {
    @Select({"select proportion from t_product_settlement_proportion where product_id=#{productId} and type=#{type}"})
    String getProductSettled(@Param("productId") Long paramLong, @Param("type") String paramString);

    @Select({"select sum((p.price-p.cost_price)*oi.quantity*(b.bonus_proportion/100)) from t_order o,t_order_item oi,t_product p,t_bonus_proportion b            where o.user_id=3 and oi.order_id=o.id and oi.product_id=p.id and b.product_id=oi.product_id and b.type='BONUSALLIANCE'"})
    BigDecimal getSelfBonusByUserId(@Param("userId") Long paramLong);

    @Select({"select count(id) from t_product where id=#{id}"})
    Integer getProductCountById(@Param("id") Long paramLong);

    @Select({"select user_id from t_alliance where invitor_alliance_id=(select id from t_alliance where user_id=#{userId})"})
    List<Long> getInviters(@Param("userId") Long paramLong);
}


