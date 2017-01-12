package com.cheikh.lazywaimai.model.event;

/**
 * author：cheikh.wang on 16/7/18 14:21
 * email：wanghonghi@126.com
 */
public class OrderStatusChangedEvent extends BaseArgumentEvent<String> {

    public OrderStatusChangedEvent(int callingId, String orderId) {
        super(callingId, orderId);
    }

    public String getOrderId() {
        return arg;
    }
}