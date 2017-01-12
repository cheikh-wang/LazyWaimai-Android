package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by comet on 16/1/30.
 */
public class SendTime {
    @SerializedName("unix_time") long unixTime;
    @SerializedName("view_time")
    String viewTime;
    @SerializedName("send_time_tip")
    String sendTimeTip;

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public String getViewTime() {
        return viewTime;
    }

    public void setViewTime(String viewTime) {
        this.viewTime = viewTime;
    }

    public String getSendTimeTip() {
        return sendTimeTip;
    }

    public void setSendTimeTip(String sendTimeTip) {
        this.sendTimeTip = sendTimeTip;
    }
}
