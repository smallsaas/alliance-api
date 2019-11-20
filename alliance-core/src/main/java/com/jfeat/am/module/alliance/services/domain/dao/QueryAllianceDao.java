package com.jfeat.am.module.alliance.services.domain.dao;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Code Generator on 2019-10-14
 */
public interface QueryAllianceDao extends BaseMapper<Alliance> {
    List<AllianceRecord> findAlliancePage(Page<AllianceRecord> page, @Param("record") AllianceRecord record,
                                          @Param("search") String search, @Param("orderBy") String orderBy,
                                          @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<AllianceRecord> findAlliancePageShip(Page<AllianceRecord> page, @Param("record") AllianceRecord record,
                                              @Param("search") String search, @Param("orderBy") String orderBy,
                                              @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<Map> getCurrentMonthOrderByUserId(@Param("id") Long id);

    AllianceRecord selectAllianceOneByUserId(@Param("id") Long id);

    public List<Map> getSelfProductByUserId(@Param("id") Long id);

    @Select("select CONCAT('充值',value) as title,value from t_config_field where group_id=1")
    List<JSONObject> getSetMeal();

    Long selectUserIdByInvitationCode(@Param("invitationCode") String invitationCode);


    AllianceRecord allianceDetails(@Param("id") Long id);

    @Select("select alliance_phone from t_alliance where alliance_phone=#{phone} ")
    String queryPhone(@Param("phone") String phone);


    List<AllianceRecord> myTeam(Page<AllianceRecord> page,
                                @Param("id") Long id,
                                @Param("record") AllianceRecord record,
                                @Param("search") String search);

    @Select("select total_price as totalPrice,user_id as userId from t_order where id=#{orderId}")
    public JSONObject queryOrderMoney(@Param("orderId") Long orderId);

    //一周内加入的盟友
    @Select("select b.* from t_alliance a INNER JOIN t_alliance b ON b.invitor_alliance_id=a.id where a.user_id=#{userId} and DATE_SUB(CURDATE(), INTERVAL 7 DAY)<b.alliance_ship_time")
    public List<Alliance> queryWeekAlliance(@Param("userId")Long userId);

    //一周内发货订单
    @Select("select * from t_order where user_id = 11 and status='DELIVERED_CONFIRM_PENDING' and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= created_date\n")
    public List<JSONObject> queryWeekOrderDeliver(@Param("userId") Long userId);

    //一周内盟友下单
    @Select("select a.alliance_name as allianceName,o.* from t_order o,(select b.user_id,b.alliance_name from t_alliance a INNER JOIN t_alliance b ON b.invitor_alliance_id=a.id where a.user_id=#{userId}\n" +
            "and b.alliance_ship=0) as a where o.user_id = a.user_id and  DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= o.created_date and o.status='PAID_CONFIRM_PENDING'")
    public List<JSONObject> queryWeekOrder(@Param("userId") Long userId);

    //重置userID
    @Update("update t_alliance set user_id=null where id=#{id}")
    Integer resetUserId(@Param("id")Long id);

    //团队下单奖励

    //团队盟友升级



}