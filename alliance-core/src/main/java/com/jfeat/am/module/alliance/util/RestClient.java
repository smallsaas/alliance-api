package com.jfeat.am.module.alliance.util;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class RestClient extends RestTemplate {

    private HttpHeaders headers;

    public RestClient(){
        super();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    public HttpHeaders getHeaders(){
        return this.headers;
    }

}