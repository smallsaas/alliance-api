package com.jfeat.am.module.alliance.api;


import com.baomidou.mybatisplus.plugins.Page;
import com.jfeat.am.common.annotation.Permission;
import com.jfeat.am.module.alliance.services.domain.dao.QueryWalletHistoryDao;
import com.jfeat.am.module.alliance.services.domain.definition.AlliancePermission;
import com.jfeat.am.module.alliance.services.domain.model.AllianceRecord;
import com.jfeat.am.module.alliance.services.domain.model.WalletHistoryRecord;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
                           @RequestParam(name = "search", required = false) String search
                            ) {
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        page.setRecords(queryWalletHistoryDao.findWalletHistoryPage(page,search));
        return SuccessTip.create(page);
    }

}
