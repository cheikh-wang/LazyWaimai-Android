package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author：cheikh on 16/5/11 14:25
 * email：wanghonghi@126.com
 */
public class SettleResult {

    public class BookingTime {
        @SerializedName("unix_time")
        long unixTime;

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

    @SerializedName("last_address")
    Address lastAddress;

    @SerializedName("online_payment")
    boolean onlinePayment;

    @SerializedName("booking_time_list")
    List<BookingTime> bookingTimes;

    @SerializedName("cart_info")
    CartInfo cartInfo;

    @SerializedName("can_submit")
    boolean canSubmit;

    public Address getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(Address lastAddress) {
        this.lastAddress = lastAddress;
    }

    public boolean isOnlinePayment() {
        return onlinePayment;
    }

    public void setOnlinePayment(boolean onlinePayment) {
        this.onlinePayment = onlinePayment;
    }

    public List<BookingTime> getBookingTimes() {
        return bookingTimes;
    }

    public void setBookingTimes(List<BookingTime> bookingTimes) {
        this.bookingTimes = bookingTimes;
    }

    public CartInfo getCartInfo() {
        return cartInfo;
    }

    public void setCartInfo(CartInfo cartInfo) {
        this.cartInfo = cartInfo;
    }

    public boolean isCanSubmit() {
        return canSubmit;
    }

    public void setCanSubmit(boolean canSubmit) {
        this.canSubmit = canSubmit;
    }
}
