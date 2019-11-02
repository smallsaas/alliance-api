package com.jfeat.am.module.alliance.api;


import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.service.AllianceService;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.util.Cip;
import com.jfeat.util.ErrorCip;
import com.jfeat.util.SuccessCip;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>
 * api
 * </p>
 *
 * @author Code Generator
 * @since 2019-10-14
 */
@RestController

@Api("Alliance")
@RequestMapping("/rpc/alliance/")
public class RPCAllianceRegisterEndpoint {

    @Resource
    AllianceService allianceService;

    @Resource
    QueryWalletDao queryWalletDao;

    @Resource
    ConfigFieldService configFieldService;


    @ApiOperation(value = "改变盟友状太，仅限于测试使用 0-支付过期 1-待支付/申请中 2-已确认/注册盟友 3-已支付/待绑定",response = Cip.class)
    @PostMapping("/changeship/{phone}")
    public Cip bindingAndCheckIsAlliance(@PathVariable("phone") String phone,
                                         @RequestBody ChangeShipRequest request) {
        //public static int ALLIANCE_SHIP_EXPIRED = 0;  //支付过期   （支付过期）可改为已支付
        //public static int ALLIANCE_SHIP_CREATED = 1;  //盟友申请中 （待支付）
        //public static int ALLIANCE_SHIP_OK = 2;       //确定是盟友 （正式盟友）
        //public static int ALLIANCE_SHIP_PAID = 3;     //盟友已支付 (等待绑定）
        if(request.getAllianceShip()==Alliance.ALLIANCE_SHIP_OK){
            throw new BusinessException(BusinessCode.BadRequest, "不支持测试直接设为正式盟友");
        }

        Alliance registeredAlliance = allianceService.findAllianceByPhoneNumber(phone);
        if(registeredAlliance==null){
            return ErrorCip.create(1, "找不到盟友: " + phone);
        }

        Alliance updatedAlliance = new Alliance();
        updatedAlliance.setId(registeredAlliance.getId());
        updatedAlliance.setAllianceShip(request.getAllianceShip());
        int affected = allianceService.updateMaster(updatedAlliance, false); /// false means update by Id
        if(affected>0){
            return SuccessCip.create(request);
        }

        return ErrorCip.create(1, "设置失败");
    }

    @ApiOperation(value = "对历史盟友绑定手机号，并检查是否为盟友",response = Cip.class)
    @PostMapping("/register")
    public Cip bindingAndCheckIsAlliance(@RequestHeader("X-USER-ID") Long userId, @RequestBody AllianceRegisterRequest request) {
        /**
         * 1.  接收 手机号码 及 验证码，X_USER_ID
         * 2.  手机号码有效， 检查手机号码是否已存在 盟友， 存在盟友即检查盟友的 状态是否为 已确认状态， 是否关联user_id, user_id 是否与 X_USER_ID 一致，
         *    根据盟友类型，库存额是否为 2000或10000,   t_wallet 里面的额度是与库存额一致。  条件满足， 即返回 是盟友 标识
         * 3.  手机号码有效， 但不在盟友列表中， 即此人不是盟友， 可以记录为联系人线索（考虑新建一个表，也可以暂时放弃这个电话信息），  即返回不是盟友标识
         */
        Alliance registeredAlliance = allianceService.findAllianceByPhoneNumber(request.getPhoneNumber());
        if(registeredAlliance==null){
            /// 记录盟友线索
            // TODO

            return ErrorCip.create(1, "找不到盟友注册信息");
        }

        /// 盟友已确定， 直接返回盟友类型
        AllianceRegisterResponse response = new AllianceRegisterResponse();
        response.setAllianceType(registeredAlliance.getAllianceType());

        if(registeredAlliance.getAllianceShip() == Alliance.ALLIANCE_SHIP_OK){
            return SuccessCip.create(response);
        }

        /// 检查盟友状态
        if(registeredAlliance.getAllianceShip() == Alliance.ALLIANCE_SHIP_CREATED ){
            return ErrorCip.create(2, "盟友申请状态中");
        }
        else if(registeredAlliance.getAllianceShip() == Alliance.ALLIANCE_SHIP_EXPIRED ){
            return ErrorCip.create(4, "支付超时，请重新申请");
        }else if(registeredAlliance.getAllianceShip() != Alliance.ALLIANCE_SHIP_PAID){
            return ErrorCip.create(4, "盟友状态Unknown: " + registeredAlliance.getAllianceShip());
        }

        /// 配置预存额度
        float common_alliance_inventory = configFieldService.getFieldFloat(AllianceRegisterResponse.COMMON_ALLIANCE_FIELD);
        float bonus_alliance_inventory = configFieldService.getFieldFloat(AllianceRegisterResponse.BONUS_ALLIANCE_FIELD);

        /// 查查盟友库存额
        if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_COMMON){
            if(registeredAlliance.getAllianceInventoryAmount().intValue() != (int)common_alliance_inventory){
                return ErrorCip.create(4, "盟友类型与库存额度不匹配");
            }

        }else if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_BONUS){
            if(registeredAlliance.getAllianceInventoryAmount().intValue() != (int)bonus_alliance_inventory){
                return ErrorCip.create(4, "盟友类型与库存额度不匹配");
            }
        }else{
            return ErrorCip.create(4, "盟友类型Unknown: " + registeredAlliance.getAllianceType());
        }

        if(registeredAlliance.getAllianceShip() != Alliance.ALLIANCE_SHIP_PAID){
            throw new BusinessException(BusinessCode.BadRequest, "盟友类型逻辑错误");
        }

        /// 状态正确(ALLIANCE_SHIP_PAID)，进行用户绑定
        registeredAlliance.setUserId(userId);

        //设置钱包库存额
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet = queryWalletDao.selectOne(wallet);
        if(wallet==null){
            return ErrorCip.create(4, "没有找到钱包信息，用户：" + userId);
        }
        if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_COMMON){
            if(wallet.getBalance().intValue() != common_alliance_inventory){
                return ErrorCip.create(4, "盟友初始库存额有误： " + wallet.getBalance());
            }
        }else if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_BONUS) {
            if (wallet.getBalance().intValue() != bonus_alliance_inventory) {
                return ErrorCip.create(4, "盟友初始库存额有误： " + wallet.getBalance());
            }
        }

        registeredAlliance.setAllianceShip(Alliance.ALLIANCE_SHIP_OK);

        return SuccessCip.create(response);
    }
}
