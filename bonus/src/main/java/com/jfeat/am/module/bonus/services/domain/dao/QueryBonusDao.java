package com.jfeat.am.module.bonus.services.domain.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.module.bonus.services.domain.model.AllianceReconciliation;
import com.jfeat.am.module.bonus.services.domain.model.OrderCommissionInfo;
import com.jfeat.am.module.bonus.services.domain.model.ProductSalesRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
//    查询所有正式盟友的userId
    @Select("select user_id from t_alliance where id=#{id} and alliance_ship=0")
    Long getAllianceUserId(@Param("id") Long id);
    //查询盟友是否存在
    Integer queryAllianceExist(@Param("userId") Long userId);
    //销量查询
    List<ProductSalesRecord> querySales(
            Page<ProductSalesRecord> page,
            @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize
            , @Param("search") String search
                                        ,@Param("leftMoney") BigDecimal leftMoney,
                                        @Param("rightMoney") BigDecimal rightMoney,
                                        @Param("leftNumber") Integer leftNumber, @Param("rightNumber") Integer rightNumber);
    //查询正式盟友的信息
    List<AllianceReconciliation> queryReInformation(
            Page<AllianceReconciliation> page,
            @Param("search") String search);
    //查询订单分红
    BigDecimal queryBonusOrder(@Param("userId") Long userId);
    //查询盟友名称
    String queryAllianceName(@Param("userId") Long userId);
    //查询盟友状态
    Integer queryShip(@Param("userId") Long userId);
    //查询盟友类型
    Integer queryType(@Param("userId") Long userId);
    //查询订单提成信息
    List<JSONObject> getCommissionOrder(Long id);
    //查询当月订单提成信息
    List<JSONObject> getCommissionOrderMonth(Long id);
    //根据月份查询订单提成信息
    List<JSONObject> getCommissionOrderToMonth(@Param("userId") Long id,@Param("date")Date date);
    //查询上个月订单提成信息
    List<JSONObject> getCommissionOrderLastMonth(Long id);
    //查询总提成
    BigDecimal getCommissionTotal(Long id);
    //查询当月提成
    BigDecimal getCommissionTotalMonth(Long id);
    //查询上月提成
    BigDecimal getCommissionOrderTotalLastMonth(Long id);
    //查询总分红
    BigDecimal getAllBonusReal();
    //查询平均分红
    BigDecimal getAverageBonus();
    //查询占比分红
    BigDecimal getAllBonusRatio();
    //查询当月平均分红
    BigDecimal getAverageBonusMonth();
    //查询当月占比分红
    BigDecimal getAllBonusRatioMonth();
    //查询总的分红盟友个数
    Integer stockholderCount();
    //查询比例（废弃）
    Float queryProportion(@Param("userId") Long userId);
    //查询订单总额（废弃）
    BigDecimal queryOrderAmount(@Param("userId") Long userId);
    //查询每条订单提成
    OrderCommissionInfo queryEveryOrderCommission(@Param("orderId") Long orderId);
    //查询每条订单提成
    OrderCommissionInfo cancelQueryEveryOrderCommission(@Param("orderId") Long orderId);
    //根据时间查询未结算和结算失败的订单
    List<OrderCommissionInfo> queryFormerOrder(@Param("userId") Long userId,@Param("createTime") Date createTime);
    //更新订单状态
    Integer upOrderSettlementStatus(@Param("status")Integer status,@Param("orderId") Long orderId);
    //查询邀请人userId
    Long queryInvitorUserId(@Param("userId") Long userId);
   // BigDecimal queryOrderAmountByMonth(@Param("userId") Long userId,@Param("createTime") Date createTime);
    //查询当月订单额度
    BigDecimal queryOrderAmountMonth(@Param("userId") Long userId,@Param("createTime") Date createTime);
    //根据时间查询提成
    BigDecimal getCommissionTotalToMonth(@Param("userId") Long userId,@Param("date") Date date);
    //查询一个订单Item的提成
    @Select("select ifnull(ROUND((item.price-item.cost_price)*item.quantity*JSON_EXTRACT(psp.proportion,'$.value')/100.0,2),0) from t_order_item item\n" +
            "LEFT JOIN t_product_settlement_proportion psp on psp.product_id=item.product_id and psp.type='ALLIANCE' \n" +
            "where item.id=#{itemId}")
    BigDecimal getCommissionToOneItem(@Param("itemId") Long itemId);
    //查询我团队的入货总额
    BigDecimal queryMyTeamOrderAmount(@Param("userId") Long userId);
    //查询我团队当月的入货总额
    BigDecimal queryMyTeamOrderAmountMonth(@Param("userId") Long userId);
    //查询所有分红盟友的userId
    List<Long> queryStockholderUserId();

    //查询所有未结算订单
    @Select("select id from t_order where settlement_status!=1")
    List<Long> queryOrderId();


    //更新所有盟友总额
    @Update("UPDATE `st_statistics_record` SET `record_value` = #{value} WHERE(`identifier` = 'all_alliance_totle');")
    Integer updateAllianceTotlePrice(BigDecimal value);
}


