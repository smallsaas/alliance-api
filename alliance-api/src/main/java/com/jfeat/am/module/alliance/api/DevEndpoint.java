package com.jfeat.am.module.alliance.api;

import com.jfeat.am.core.jwt.JWTKit;
import com.jfeat.am.module.alliance.services.domain.service.DevService;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController

@Api("Dev")
@RequestMapping("/api/pub/dev")
public class DevEndpoint {
    @GetMapping
    public Tip crudDev(){
        // get header HOST
        var request = JWTKit.getRequest();
        String host = JWTKit.getRequest().getHeader("Host");
        String uriString = JWTKit.getRequest().getRequestURI();
        String urlString = JWTKit.getRequest().getRequestURL().toString();
        var url = JWTKit.getRequest().getRequestURL();
        String schema = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String remoteHost = request.getRemoteUser();

        return SuccessTip.create(0);
    }
}
