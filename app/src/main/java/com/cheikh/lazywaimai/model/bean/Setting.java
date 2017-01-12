package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * author: cheikh.wang on 17/1/12
 * email: wanghonghi@126.com
 */
public class Setting {

    @SerializedName("common_remarks")
    List<String> commonRemarks;

    @SerializedName("payment_platforms")
    List<PaymentPlatform> paymentPlatforms;

    public List<String> getCommonRemarks() {
        return commonRemarks;
    }

    public void setCommonRemarks(List<String> commonRemarks) {
        this.commonRemarks = commonRemarks;
    }

    public List<PaymentPlatform> getPaymentPlatforms() {
        return paymentPlatforms;
    }

    public void setPaymentPlatforms(List<PaymentPlatform> paymentPlatforms) {
        this.paymentPlatforms = paymentPlatforms;
    }
}
