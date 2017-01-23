package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Order implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("user_id")
    String userId;

    @SerializedName("business_id")
    String businessId;

    @SerializedName("business_info")
    Business businessInfo;

    @SerializedName("cart_info")
    CartInfo cartInfo;

    @SerializedName("status")
    OrderStatus status;

    @SerializedName("origin_price")
    double originPrice;

    @SerializedName("discount_price")
    double discountPrice;

    @SerializedName("total_price")
    double totalPrice;

    @SerializedName("consignee")
    String consignee;

    @SerializedName("phone")
    String phone;

    @SerializedName("address")
    String address;

    @SerializedName("pay_method")
    PayMethod payMethod;

    @SerializedName("remark")
    String remark;

    @SerializedName("order_num")
    String orderNum;

    @SerializedName("booked_at")
    long bookedTime;

    @SerializedName("created_at")
    long createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Business getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(Business businessInfo) {
        this.businessInfo = businessInfo;
    }

    public CartInfo getCartInfo() {
        return cartInfo;
    }

    public void setCartInfo(CartInfo cartInfo) {
        this.cartInfo = cartInfo;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(double originPrice) {
        this.originPrice = originPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }

    public String getRemark() {
        return remark;
    }

    public void setmRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public long getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(long bookedTime) {
        this.bookedTime = bookedTime;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }


}
