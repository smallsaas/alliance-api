package com.jfeat.am.module.alliance.api;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.config.services.service.ConfigFieldService;
import com.jfeat.util.Cip;
import com.jfeat.util.SuccessCip;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/openrpc/alliance/global")
@RestController
public class RPCGlobal {
    @Resource
    ConfigFieldService configFieldService;
    @GetMapping
    public Cip global(){
        String condition = configFieldService.getFieldString(AllianceFields.GLOBAL_DELAY_REGISTER);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("globalDelayRegister", Boolean.parseBoolean(condition));
        return SuccessCip.create(jsonObject);
    }
}
