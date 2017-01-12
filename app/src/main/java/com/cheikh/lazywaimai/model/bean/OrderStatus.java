package com.cheikh.lazywaimai.model.bean;

/**
 * Created by comet on 16/1/29.
 */
public enum OrderStatus {

    STATUS_WAIT_SUBMIT(-1, "待提交"),
    STATUS_WAIT_PAYMENT(0, "待付款"),
    STATUS_WAIT_ACCEPT(1, "待接单"),
    STATUS_WAIT_SEND(2, "待发货"),
    STATUS_WAIT_ARRIVE(3, "待送达"),
    STATUS_WAIT_CONFIRM(4, "待确认"),
    STATUS_FINISHED(5, "已完成");

    private int value;
    private String name;

    OrderStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static OrderStatus valueOf(int value) {
        switch (value) {
            case 0:
                return STATUS_WAIT_PAYMENT;
            case 1:
                return STATUS_WAIT_ACCEPT;
            case 2:
                return STATUS_WAIT_SEND;
            case 3:
                return STATUS_WAIT_ARRIVE;
            case 4:
                return STATUS_WAIT_CONFIRM;
            case 5:
                return STATUS_FINISHED;
            default:
                return STATUS_WAIT_SUBMIT;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
