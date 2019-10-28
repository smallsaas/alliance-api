package com.jfeat.am.module.friend.services.domain.model;

import com.jfeat.am.module.friend.services.gen.persistence.model.MomentsFriend;

import java.util.Map;


public class MomentsFriendOverOrdersRecord extends MomentsFriendRecord {
    private Map order;

    public Map getOrder() {
        return order;
    }

    public void setOrder(Map order) {
        this.order = order;
    }
}
