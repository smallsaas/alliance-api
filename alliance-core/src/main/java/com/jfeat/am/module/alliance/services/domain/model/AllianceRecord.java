package com.jfeat.am.module.alliance.services.domain.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.module.alliance.services.gen.persistence.model.Alliance;

import java.util.List;

/**
 * Created by Code Generator on 2019-10-14
 */
public class AllianceRecord extends Alliance{
    private JSONArray currentMonthOrder;
    private JSONArray selfProducts;

    public JSONArray getSelfProducts() {
        return selfProducts;
    }

    public void setSelfProducts(JSONArray selfProducts) {
        this.selfProducts = selfProducts;
    }

    public JSONArray getCurrentMonthOrder() {
        return currentMonthOrder;
    }

    public void setCurrentMonthOrder(JSONArray currentMonthOrder) {
        this.currentMonthOrder = currentMonthOrder;
    }

}
