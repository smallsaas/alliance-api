package com.jfeat.am.module.friend.services.domain.model;

import com.jfeat.am.module.friend.services.gen.persistence.model.MomentsFriend;
import com.jfeat.am.module.friend.services.gen.persistence.model.Order;


public class MomentsFriendOverOrdersRecord extends MomentsFriendRecord {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
