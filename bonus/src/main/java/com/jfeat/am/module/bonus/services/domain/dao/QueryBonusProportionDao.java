package com.jfeat.am.module.bonus.services.domain.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.module.bonus.services.domain.model.BonusProportionRecord;
import com.jfeat.am.module.bonus.services.gen.persistence.model.BonusProportion;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface QueryBonusProportionDao extends BaseMapper<BonusProportion> {
  List<BonusProportionRecord> findBonusProportionPage(Page<BonusProportionRecord> paramPage, @Param("record") BonusProportionRecord paramBonusProportionRecord, @Param("search") String paramString1, @Param("orderBy") String paramString2, @Param("startTime") Date paramDate1, @Param("endTime") Date paramDate2);
  @Select("select count(id) from t_product where id=#{id}")
  Integer getProductCountById(Long id);
}

