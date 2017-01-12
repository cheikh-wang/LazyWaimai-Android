package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * author: cheikh.wang on 17/1/11
 * email: wanghonghi@126.com
 */
public class Favorite {

    @SerializedName("id")
    String id;

    @SerializedName("business_info")
    Business business;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
