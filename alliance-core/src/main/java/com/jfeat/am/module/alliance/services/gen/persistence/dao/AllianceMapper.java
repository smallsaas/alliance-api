package com.jfeat.am.module.alliance.services.gen.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author Code Generator
 * @since 2019-10-14
 */
public interface AllianceMapper extends BaseMapper<Alliance> {
    //根据盟友id 解除绑定的微信
    Integer unbind(@Param("id") Long id);

    //根据t_user的id 清空t_user绑定的手机号 和 邀请码的url
    Integer unbindUser(@Param("id") Long id);
}