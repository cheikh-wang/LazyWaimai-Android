package com.cheikh.lazywaimai.model.bean;

/**
 * Created by comet on 16/1/30.
 */
public enum PayMethod {

    ONLINE(1, "在线支付"),
    OFFLINE(0, "货到付款");

    private int value;
    private String name;

    PayMethod(int value, String name) {
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

    public static PayMethod valueOf(int value) {
        switch (value) {
            case 1:
                return ONLINE;
            case 0:
                return OFFLINE;
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
