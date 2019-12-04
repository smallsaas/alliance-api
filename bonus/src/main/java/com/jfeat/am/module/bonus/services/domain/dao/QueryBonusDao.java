package com.jfeat.am.module.bonus.services.domain.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;
import com.jfeat.am.module.bonus.services.domain.model.OrderCommissionInfo;
import com.jfeat.am.module.bonus.services.domain.model.ProductSalesRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface QueryBonusDao {

    //（算出总的订单商品总的盟友分红）
    BigDecimal getAllianceBonus(@Param("userId") Long paramLong, @Param("dateType") Integer dateType);


    //算出自己的给别的提成
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

    List<ProductSalesRecord> querySales(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("search") String search);

    List<AllianceReconciliation> queryReInformation(@Param("search") String search);

    BigDecimal queryBonusOrder(@Param("userId") Long userId);

    String queryAllianceName(@Param("userId") Long userId);

    Integer queryShip(@Param("userId") Long userId);

    Integer queryType(@Param("userId") Long userId);

    List<JSONObject> getCommissionOrder(Long id);

    List<JSONObject> getCommissionOrderMonth(Long id);

    List<JSONObject> getCommissionOrderToMonth(@Param("userId") Long id,@Param("date")Date date);

    List<JSONObject> getCommissionOrderLastMonth(Long id);
    BigDecimal getCommissionTotal(Long id);
    BigDecimal getCommissionTotalMonth(Long id);
    BigDecimal getCommissionOrderTotalLastMonth(Long id);

    BigDecimal getAllBonusReal();

    BigDecimal getAverageBonus();

    BigDecimal getAllBonusRatio();
    BigDecimal getAverageBonusMonth();

    BigDecimal getAllBonusRatioMonth();

    Integer stockholderCount();

    Float queryProportion(@Param("userId") Long userId);

    BigDecimal queryOrderAmount(@Param("userId") Long userId);

    OrderCommissionInfo queryEveryOrderCommission(@Param("orderId") Long orderId);
    List<OrderCommissionInfo> queryFormerOrder(@Param("userId") Long userId,@Param("createTime") Date createTime);
    Integer upOrderSettlementStatus(@Param("status")Integer status,@Param("orderId") Long orderId);
    Long queryInvitorUserId(@Param("userId") Long userId);
   // BigDecimal queryOrderAmountByMonth(@Param("userId") Long userId,@Param("createTime") Date createTime);
    BigDecimal queryOrderAmountMonth(@Param("userId") Long userId,@Param("createTime") Date createTime);
    BigDecimal getCommissionTotalToMonth(@Param("userId") Long userId,@Param("date") Date date);
    @Select("select ifnull(ROUND((item.price-item.cost_price)*item.quantity*JSON_EXTRACT(psp.proportion,'$.value')/100.0,2),0) from t_order_item item\n" +
            "LEFT JOIN t_product_settlement_proportion psp on psp.product_id=item.product_id and psp.type='ALLIANCE' \n" +
            "where item.id=#{itemId}")
    BigDecimal getCommissionToOneItem(@Param("itemId") Long itemId);
    BigDecimal queryMyTeamOrderAmount(@Param("userId") Long userId);
    BigDecimal queryMyTeamOrderAmountMonth(@Param("userId") Long userId);
    List<Long> queryStockholderUserId();
}


