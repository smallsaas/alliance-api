package com.jfeat.am.module.alliance.services.domain.dao;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import org.apache.ibatis.annotations.Select;

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
    List<Map> getCurrentMonthOrderByUserId(@Param("id") Long id);
    AllianceRecord selectAllianceOneByUserId(@Param("id")Long id);
    public List<Map> getSelfProductByUserId(@Param("id")Long id);
    @Select("select CONCAT('充值',value) as title,value from t_config_field where group_id=1")
    List<JSONObject> getSetMeal();
    Long selectUserIdByInvitationCode(@Param("invitationCode") String invitationCode);


}