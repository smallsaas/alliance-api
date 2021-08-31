package com.jfeat.am.module.alliance.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jfeat.am.common.annotation.Permission;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.domain.model.WalletHistoryRecord;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/walletHistory")
public class walletHistoryEndpoint {

    @Resource
    QueryWalletHistoryDao queryWalletHistoryDao;

    @GetMapping()
    @ApiOperation(value = "查看 钱包历史记录", response = Alliance.class)
    public Tip getAlliance(Page<WalletHistoryRecord> page,
                           @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @RequestParam(name = "search", required = false) String search,
                           @RequestParam(name = "searchMoney", required = false) BigDecimal searchMoney[],
                           @RequestParam(name = "type", required = false) String type
                            ) {
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        BigDecimal leftMoney = searchMoney!=null? (searchMoney.length > 0?searchMoney[0]:null) : null;
        BigDecimal rightMoney = searchMoney!=null ? (searchMoney.length==2?searchMoney[1]:(searchMoney.length==1?searchMoney[0]:null)) : null;


        List<WalletHistoryRecord> walletHistoryRecord=queryWalletHistoryDao.findWalletHistoryPage(page,search,type,leftMoney,rightMoney);
        for (WalletHistoryRecord walletHistory:walletHistoryRecord) {
            if(walletHistory.getType()!=null && walletHistory.getType().equals("WITHDRAW")){
                walletHistory.setOwnBlance(walletHistory.getBalance());
                walletHistory.setBalance(null);
            }
        }
        page.setRecords(walletHistoryRecord);
        return SuccessTip.create(page);
    }

}
