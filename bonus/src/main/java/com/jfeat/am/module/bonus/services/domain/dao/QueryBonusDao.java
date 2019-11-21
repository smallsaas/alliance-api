package com.jfeat.am.module.bonus.services.domain.dao;

import java.math.BigDecimal;
import java.util.List;

import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;
import com.jfeat.am.module.bonus.services.domain.model.ProductSalesRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface QueryBonusDao {

    //（算出总的订单商品总的盟友分红）
    BigDecimal getAllianceBonus(@Param("userId") Long paramLong, @Param("dateType") Integer dateType);


    //算出自己的推荐分红(给别人的)
    BigDecimal getTeamBonus(@Param("userId") Long paramLong, @Param("dateType") Integer dateType);


    //算出自己的团队
    @Select("select a1.user_id from t_alliance a1,t_alliance a2 where a1.invitor_alliance_id=a2.id and a2.user_id=#{userId} and a1.alliance_ship=0")
    List<Long> getTeam(@Param("userId") Long paramLong);


    //获取所有的入货总额
    @Select("select sum(alliance_inventory_amount) from t_alliance where alliance_ship=0")
    BigDecimal getTotalInventoryAmount();

    //获取自己团队的入货总额
    @Select("select sum(a1.alliance_inventory_amount) from t_alliance a1,t_alliance a2 where a1.invitor_alliance_id=a2.id and a2.user_id=#{userId} and a1.alliance_ship=0")
    BigDecimal getTeamInventoryAmount(@Param("userId") Long paramLong);

    //获取自己的入货额度
    @Select("select alliance_inventory_amount from t_alliance where user_id = #{userId} and alliance_ship=0")
    BigDecimal getInventoryAmount(@Param("userId") Long paramLong);

    //获取团队分红占比的分红比例，和股东比例
    @Select("select value/100.0 from t_config_field where field=#{type}")
    BigDecimal getAllianceOrTeamProportion(@Param("type") String type);

    @Select("select user_id from t_alliance where id=#{id} and alliance_ship=0")
    Long getAllianceUserId(@Param("id") Long id);

    Integer queryAllianceExist(@Param("userId") Long userId);

    List<ProductSalesRecord> querySales(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,@Param("search") String search);

    List<AllianceReconciliation> queryReInformation(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,@Param("search") String search);


    BigDecimal queryBonusOrder(@Param("userId") Long userId);

    String queryAllianceName(@Param("userId") Long userId);

    Integer queryShip(@Param("userId") Long userId);
    Integer queryType(@Param("userId") Long userId);


}


