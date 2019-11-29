package com.jfeat.am.module.alliance.api;


import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletDao;
import com.jfeat.am.module.alliance.services.domain.service.AllianceService;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Wallet;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.util.Cip;
import com.jfeat.util.ErrorCip;
import com.jfeat.util.SuccessCip;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


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

    @BusinessLog(name = "盟友", value = "改变盟友状态")
    @ApiOperation(value = "改变盟友状太，仅限于测试使用 0-正式盟友 1-不是盟友 2-待支付/申请中 3-已支付 4-支付过期 5-状态错误",response = Cip.class)
    @PostMapping("/changeship/{phone}")
    public Cip bindingAndCheckIsAlliance(@PathVariable("phone") String phone,
                                         @RequestBody ChangeShipRequest request) {
        if(request.getAllianceShip()==AllianceShips.ALLIANCE_SHIP_OK){
            throw new BusinessException(BusinessCode.BadRequest, "不支持测试直接设为正式盟友");
        }

        Alliance registeredAlliance = allianceService.findAllianceByPhoneNumber(phone);
        if(registeredAlliance==null){
            return ErrorCip.create(1, "输入的电话找不到盟友: " + phone);
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

    @BusinessLog(name = "盟友", value = "对历史盟友绑定手机号")
    @ApiOperation(value = "对历史盟友绑定手机号，并检查是否为盟友",response = Cip.class)
    @PostMapping("/register")
    public Cip bindingAndCheckIsAlliance(@RequestHeader("X-USER-ID") Long userId, @RequestBody AllianceRegisterRequest request) {
        /**
         * 1.  接收 手机号码 及 验证码，X_USER_ID
         * 2.  手机号码有效， 检查手机号码是否已存在 盟友， 存在盟友即检查盟友的 状态是否为 已确认状态， 是否关联user_id, user_id 是否与 X_USER_ID 一致，
         *    根据盟友类型，库存额是否为 2000或10000,   t_wallet 里面的额度是与库存额一致。  条件满足， 即返回 是盟友 标识
         * 3.  手机号码有效， 但不在盟友列表中， 即此人不是盟友， 可以记录为联系人线索（考虑新建一个表，也可以暂时放弃这个电话信息），  即返回不是盟友标识
         **/

        // 没有传送手机号码
        if(request==null || StringUtils.isEmpty(request.getPhoneNumber())){
            throw new BusinessException(BusinessCode.BadRequest, "必须传手机号码");
        }

        /*
        if(request==null || StringUtils.isEmpty(request.getPhoneNumber())){
            // 没有传手机号，直接通过userid查盟友
            Alliance registeredAlliance = allianceService.getAlliancesByBindingUserId(userId);
            if(registeredAlliance!=null) {
                if (StringUtils.isEmpty(registeredAlliance.getAlliancePhone())) {
                    return ErrorCip.create(AllianceRegisterResponse.ALLIANCE_SHIP_ERROR, "盟友已绑定，但找不到绑定手机");
                }

                ///找到了盟友， 直接返回 allianceShip 状态
                AllianceRegisterResponse resp = new AllianceRegisterResponse();
                resp.setAllianceType(registeredAlliance.getAllianceType());

                return ErrorCip.create(registeredAlliance.getAllianceShip(), "尝试登录时获取的盟友状态");
            }

            return ErrorCip.create(AllianceRegisterResponse.ALLIANCE_SHIP_NO, "找不到盟友注册信息");
        }*/

        /// 通过手机号码绑定
        Alliance registeredAlliance = allianceService.findAllianceByPhoneNumber(request.getPhoneNumber());
        if(registeredAlliance==null){
            // 没有找到手机绑定的盟友， 不是盟友

            /// 记录盟友线索
            // TODO
            return ErrorCip.create(AllianceShips.ALLIANCE_SHIP_NO, "找不到盟友信息");
        }


        /// 状态检查

        /// 配置预存额度
        float common_alliance_inventory = configFieldService.getFieldFloat(AllianceShips.COMMON_ALLIANCE_FIELD);
        float bonus_alliance_inventory = configFieldService.getFieldFloat(AllianceShips.BONUS_ALLIANCE_FIELD);

        /// 查查盟友库存额
//        if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_COMMON){
//            if(registeredAlliance.getAllianceInventoryAmount().intValue() != (int)common_alliance_inventory){
//                return ErrorCip.create(AllianceShips.ALLIANCE_SHIP_ERROR, "盟友类型与库存额度不匹配");
//            }
//
//        }else if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_BONUS){
//            if(registeredAlliance.getAllianceInventoryAmount().intValue() != (int)bonus_alliance_inventory){
//                return ErrorCip.create(AllianceShips.ALLIANCE_SHIP_ERROR, "盟友类型与库存额度不匹配");
//            }
//
//        }else{
//            return ErrorCip.create(AllianceShips.ALLIANCE_SHIP_ERROR, "盟友类型Unknown: " + registeredAlliance.getAllianceType());
//        }


        /// 盟友已确定， 直接返回盟友类型
        AllianceShips response = new AllianceShips();
        //盟友时间已存在则不设置
        if(registeredAlliance.getAllianceShipTime()==null){
            response.setAllianceType(registeredAlliance.getAllianceType());
        }


        if(registeredAlliance.getAllianceShip()==AllianceShips.ALLIANCE_SHIP_OK){
            // 最后确认是否已绑定用户，程序错误
            if(registeredAlliance.getUserId()==null || registeredAlliance.getUserId()==0){
                throw  new BusinessException(BusinessCode.CRUD_GENERAL_ERROR, "系统逻辑错误");
            }
            return SuccessCip.create(response);

        }else if(registeredAlliance.getAllianceShip() == AllianceShips.ALLIANCE_SHIP_INVITED ){

            /// 状态正确(ALLIANCE_SHIP_PAID)，进行用户绑定
            registeredAlliance.setUserId(userId);
            int affected = allianceService.updateMaster(registeredAlliance);

            if(affected>0) {
                return ErrorCip.create(registeredAlliance.getAllianceShip(), "盟友申请状态中");
            }else{
                return ErrorCip.create(AllianceShips.ALLIANCE_SHIP_ERROR, "数据库错误：确认盟友状态有误: " + registeredAlliance.getAlliancePhone());
            }
        }
        else if(registeredAlliance.getAllianceShip() == AllianceShips.ALLIANCE_SHIP_EXPIRED ){
            return ErrorCip.create(registeredAlliance.getAllianceShip(), "支付超时，请重新申请");

        }else if(registeredAlliance.getAllianceShip() == AllianceShips.ALLIANCE_SHIP_PAID){

            /// 已支付， 处理绑定
            /// 状态正确(ALLIANCE_SHIP_PAID)，进行用户绑定
            registeredAlliance.setUserId(userId);

            //设置钱包库存额
            Wallet wallet = new Wallet();
            wallet.setUserId(userId);
            Wallet originWallet = queryWalletDao.selectOne(wallet);
            if(originWallet==null){
               if(registeredAlliance.getAllianceType()==Alliance.ALLIANCE_TYPE_COMMON){
                   BigDecimal bigDecimal = new BigDecimal(common_alliance_inventory);
                   wallet.setBalance(bigDecimal);
                   wallet.setAccumulativeAmount(bigDecimal);
               }else if(registeredAlliance.getAllianceType()==Alliance.ALLIANCE_TYPE_BONUS){
                   BigDecimal bigDecimal = new BigDecimal(bonus_alliance_inventory);
                   wallet.setBalance(bigDecimal);
                   wallet.setAccumulativeAmount(bigDecimal);
               }
                wallet.insert();
                //return ErrorCip.create(AllianceShips.ALLIANCE_SHIP_ERROR, "没有找到钱包信息，用户：" + userId);
            }else{
                if(registeredAlliance.getAllianceType()==Alliance.ALLIANCE_TYPE_COMMON){
                    BigDecimal bigDecimal = new BigDecimal(common_alliance_inventory);
                    if(originWallet.getBalance()!=null){
                        originWallet.setBalance(bigDecimal.add(originWallet.getBalance()));
                    }else {
                        originWallet.setBalance(bigDecimal);
                    }
                    if(originWallet.getAccumulativeAmount()!=null){
                        originWallet.setAccumulativeAmount(bigDecimal.add(originWallet.getAccumulativeAmount()));
                    }else {
                        originWallet.setAccumulativeAmount(bigDecimal);
                    }

                }else if(registeredAlliance.getAllianceType()==Alliance.ALLIANCE_TYPE_BONUS){
                    BigDecimal bigDecimal = new BigDecimal(bonus_alliance_inventory);
                    if(originWallet.getBalance()!=null){
                        originWallet.setBalance(bigDecimal.add(originWallet.getBalance()));
                    }else {
                        originWallet.setBalance(bigDecimal);
                    }
                    if(originWallet.getAccumulativeAmount()!=null){
                        originWallet.setAccumulativeAmount(bigDecimal.add(originWallet.getAccumulativeAmount()));
                    }else {
                        originWallet.setAccumulativeAmount(bigDecimal);
                    }
                }
                queryWalletDao.updateById(originWallet);
            }

            //再次检查初始状态下的balance
//            if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_COMMON){
//                if(wallet.getBalance().intValue() != common_alliance_inventory){
//                    return ErrorCip.create(AllianceShips.ALLIANCE_SHIP_ERROR, "盟友初始库存额有误： " + wallet.getBalance());
//                }
//            }else if(registeredAlliance.getAllianceType() == Alliance.ALLIANCE_TYPE_BONUS) {
//                if (wallet.getBalance().intValue() != bonus_alliance_inventory) {
//                    return ErrorCip.create(AllianceShips.ALLIANCE_SHIP_ERROR, "盟友初始库存额有误： " + wallet.getBalance());
//                }
//            }

            // 确认为 正式盟友
            registeredAlliance.setAllianceShip(AllianceShips.ALLIANCE_SHIP_OK);
            registeredAlliance.setAllianceShipTime(new Date());
            int affected = allianceService.updateMaster(registeredAlliance);
            if(affected>0) {
                return SuccessCip.create(response);
            }else{
                return ErrorCip.create(AllianceShips.ALLIANCE_SHIP_ERROR, "数据库错误：确认盟友状态有误: " + registeredAlliance.getAlliancePhone());
            }

        }else{
            return ErrorCip.create(registeredAlliance.getAllianceShip(), "盟友状态Unknown: " + registeredAlliance.getAllianceShip());
        }
    }
}
