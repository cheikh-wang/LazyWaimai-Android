package com.cheikh.lazywaimai.model.bean;

/**
 * author：cheikh on 16/5/20 20:34
 * email：wanghonghi@126.com
 */
public enum Gender {
    MALE(0, "先生"),
    FEMALE(1, "女士");

    private int value;
    private String name;

    Gender(int value, String name) {
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

    public static Gender valueOf(int value) {
        switch (value) {
            case 0:
                return MALE;
            case 1:
                return FEMALE;
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
