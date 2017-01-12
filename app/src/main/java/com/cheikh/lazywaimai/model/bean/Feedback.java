package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * author: cheikh.wang on 17/1/12
 * email: wanghonghi@126.com
 */
public class Feedback {

    @SerializedName("id")
    String id;

    @SerializedName("content")
    String content;

    @SerializedName("contact_way")
    String contactWay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }
}
