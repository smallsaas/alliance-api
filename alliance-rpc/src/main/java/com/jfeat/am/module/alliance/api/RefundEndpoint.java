package com.jfeat.am.module.alliance.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jfeat.am.module.alliance.services.domain.dao.QueryAllianceDao;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.util.Cip;
import com.jfeat.util.SuccessCip;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;


@RestController
@RequestMapping("/rpc/order/refund")
public class RefundEndpoint {
    @Resource
    QueryAllianceDao queryAllianceDao;
    @Resource
    QueryWalletDao queryWalletDao;

    @PostMapping
    public Cip refund(@RequestParam Long orderId) {
        int affect = 0;
        JSONObject object = queryAllianceDao.queryOrderMoney(orderId);
        BigDecimal totalPrice = object.getBigDecimal("totalPrice");
        Long userId = object.getLong("userId");
        if (totalPrice != null && userId != null) {
            Wallet wallet = queryWalletDao.selectOne(new LambdaQueryWrapper<>(new Wallet().setUserId(userId)));
            if (wallet != null) {
                BigDecimal balance = wallet.getBalance();
                if (balance != null) {
                    wallet.setBalance(balance.add(totalPrice));
                }
                affect += queryWalletDao.updateById(wallet);
            } else {
                throw new BusinessException(BusinessCode.BadRequest, "该用户钱包不存在");
            }
        } else {
            throw new BusinessException(BusinessCode.BadRequest, "该订单有问题");
        }
        return SuccessCip.create(affect);
    }
}
