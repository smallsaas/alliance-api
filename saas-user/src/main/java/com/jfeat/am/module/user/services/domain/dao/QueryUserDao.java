package com.jfeat.am.module.user.services.domain.dao;

import com.jfeat.am.module.user.services.domain.model.UserRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.jfeat.am.module.user.services.gen.persistence.model.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Code Generator on 2019-11-05
 */
public interface QueryUserDao extends BaseMapper<User> {
    List<UserRecord> findUserPage(Page<UserRecord> page, @Param("record") UserRecord record,
                                            @Param("search") String search, @Param("orderBy") String orderBy,
                                            @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}