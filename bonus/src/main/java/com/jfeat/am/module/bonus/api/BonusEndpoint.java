 package com.jfeat.am.module.bonus.api;
 
 import com.jfeat.am.module.bonus.services.domain.dao.QueryBonusDao;
 import com.jfeat.am.module.bonus.services.domain.service.BonusService;
 import com.jfeat.am.module.log.annotation.BusinessLog;
 import com.jfeat.crud.base.tips.SuccessTip;
 import com.jfeat.crud.base.tips.Tip;
 import io.swagger.annotations.Api;
 import io.swagger.annotations.ApiOperation;
 import java.math.BigDecimal;
 import java.rmi.ServerException;
 import javax.annotation.Resource;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RequestHeader;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;
 
 
 
 
 
 
 
 
 
 
 
 @RestController
 @Api("Bonus")
 @RequestMapping({"/api/crud/bonus"})
 public class BonusEndpoint
 {
   @Resource
   QueryBonusDao queryBonusDao;
   @Resource
   BonusService bonusService;
   
   @BusinessLog(name = "Bonus", value = "get Bonus")
   @GetMapping({"/totalSelfBonus"})
   @ApiOperation(value = "获取Bonus,根据X-USER-ID,获取的是盟友自己的所有分红加上团队的分红总数", response = com.jfeat.am.module.bonus.services.gen.persistence.model.BonusProportion.class)
   public Tip totalSelfBonus(@RequestHeader("X-USER-ID") Long id) throws ServerException {
     BigDecimal selfBouns = this.bonusService.getTotalSelfBonus(id);
     return SuccessTip.create(selfBouns);
   }
   
   @BusinessLog(name = "Bonus", value = "get Bonus")
   @GetMapping({"/SelfBonus"})
   @ApiOperation(value = "获取Bonus,根据X-USER-ID,获取的是盟友自己的分红", response = com.jfeat.am.module.bonus.services.gen.persistence.model.BonusProportion.class)
   public Tip SelfBonus(@RequestHeader("X-USER-ID") Long id) throws ServerException {
     BigDecimal selfBouns = this.bonusService.getSelfBonus(id);
     return SuccessTip.create(selfBouns);
   }
   
   @BusinessLog(name = "Bonus", value = "get Bonus")
   @GetMapping({"/teamBonus"})
   @ApiOperation(value = "获取Bonus,根据X-USER-ID,获取的是盟友自己的团队的分红", response = com.jfeat.am.module.bonus.services.gen.persistence.model.BonusProportion.class)
   public Tip teamBonus(@RequestHeader("X-USER-ID") Long id) throws ServerException {
     BigDecimal selfBouns = this.bonusService.getTeamBonus(id);
     return SuccessTip.create(selfBouns);
   }
 }


/* Location:              C:\Users\39250\Desktop\新建文件夹\alliance-api-1.0.0-standalone.jar!\BOOT-INF\lib\bonus-1.0.0.jar!\com\jfeat\am\module\bonus\api\BonusEndpoint.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.0.7
 */