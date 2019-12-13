package com.jfeat.am.module.friend.services.domain.dao;

import com.jfeat.am.module.friend.api.AllianceProduct;
import com.jfeat.am.module.friend.api.FriendOrderItem;
import com.jfeat.am.module.friend.api.OrderProductRequest;
import com.jfeat.am.module.friend.api.OrderUserRequest;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendOverOrdersRecord;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jfeat.am.module.friend.services.domain.model.MomentsFriendUser;
import org.apache.ibatis.annotations.Param;
import com.jfeat.am.module.friend.services.gen.persistence.model.MomentsFriend;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Code Generator on 2019-10-14
 */
public interface QueryMomentsFriendDao extends BaseMapper<MomentsFriend> {
    List<MomentsFriendOverOrdersRecord> findMomentsFriendPage(Page<MomentsFriendOverOrdersRecord> page, @Param("record") MomentsFriendRecord record,
                                                    @Param("search") String search, @Param("orderBy") String orderBy,
                                                    @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    Map findOrdersByPhone(@Param("phone") String id, @Param("userId") Long userId);
    List<Long> selectUserId(@Param("name") String name);
    MomentsFriendUser selectByUserId(@Param("id") Long id);

    Long insertOrder(@Param("userId")Long id, @Param("totalPrice")BigDecimal totalPrice,@Param("phone")String phone,@Param("mname")String mname,@Param("detail")String detail);
    Long selectProductId(@Param("barcode")String barcode);
    String selectBarcodeByProductId(@Param("productId")Long productId);

    Integer insertOrderItem(@Param("orderId")Long orderId,@Param("barcode")String barcode,@Param("productName")String productName,@Param("quantity")Integer quantity,@Param("finalPrice") BigDecimal finalPrice
              ,@Param("price") BigDecimal price,@Param("costPrice") BigDecimal costPrice,@Param("cover") String cover,@Param("id") Long id);

    @Select("select alliance_name from t_alliance where user_id=#{userId} and alliance_ship=0")
    String queryAllianceName(Long userId);

    @Update("update t_wallet set balance=#{balance} where user_id=#{userId}")
    Integer upWallet(@Param("userId") Long userId,@Param("balance") BigDecimal balance);

    @Select("select balance from t_wallet where user_id=#{userId}")
    BigDecimal queryWalletBalance(Long userId);

    @Select("select stock_balance from t_product where id=#{productId}")
    Integer queryStockBalance(Long productId);

    @Update("update t_product set stock_balance=#{balance} where id=#{productId}")
    Integer upProduct(@Param("productId") Long productId, @Param("balance") Integer balance);

    @Select("select * from t_product where id=#{productId}")
    AllianceProduct queryProductById(Long productId);

    Integer closeProduct(@Param("id")Long id);
    Integer cancelcloseProduct(@Param("id")Long id);

    List<FriendOrderItem> selectOrderItem(@Param("orderId")Long orderId);

    Integer upStockBalance(@Param("productId")Integer productId,@Param("num")Integer num);


}