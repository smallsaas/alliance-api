package com.jfeat.am.module.bonus.services.domain.dao;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
public interface QueryBonusDao {

    //（算出订单商品总的盟友分红）
    @Select("select sum((p.price-p.cost_price)*oi.quantity*b.bonus_proportion/(100.0)) from t_order o,t_order_item oi,t_product p ,t_bonus_proportion b,t_alliance a where\n" +
            "o.user_id=#{userId} and oi.order_id= o.id and p.id=oi.product_id and a.user_id=o.user_id and a.alliance_type=1 and b.product_id=p.id and b.type = 'STOCKHOLDER';\n")
    BigDecimal getAllianceBonus(@Param("userId") Long paramLong);



    //算出自己的推荐分红(给别人的)
    @Select("select sum((p.price-p.cost_price)*oi.quantity*b.bonus_proportion/(100.0)) from t_order o,t_order_item oi,t_product p ,t_bonus_proportion b,t_alliance a where\n" +
            "o.user_id=#{userId} and oi.order_id= o.id and p.id=oi.product_id and a.user_id=o.user_id  and b.product_id=p.id and b.type = 'RECOMMEND';\n")
    BigDecimal getTeamBonus(@Param("userId") Long paramLong);


    //算出自己的团队
    @Select("select a1.user_id from t_alliance a1,t_alliance a2 where a1.invitor_alliance_id=a2.id and a2.user_id=#{userId}")
    List<Long> getTeam(@Param("userId") Long paramLong);

    //获取所有的入货总额
    @Select("select sum(alliance_inventory_amount) from t_alliance")
    BigDecimal getTotalInventoryAmount();

    //获取自己团队的入货总额
    @Select("select sum(a1.alliance_inventory_amount) from t_alliance a1,t_alliance a2 where a1.invitor_alliance_id=a2.id and a2.user_id=#{userId}")
    BigDecimal getTeamInventoryAmount(@Param("userId") Long paramLong);

    //获取自己的入货额度
    @Select("select alliance_inventory_amount from t_alliance where user_id = #{userId}")
    BigDecimal getInventoryAmount(@Param("userId") Long paramLong);

    //获取团队分红占比的分红比例，和股东比例
    @Select("select proportion/100.0 from t_alliance_bonus where type=#{type}")
    BigDecimal getAllianceOrTeamProportion(@Param("type")String type);

    @Select("select user_id from t_alliance where id=#{id}")
    Long getAllianceUserId(@Param("id") Long id);


}


