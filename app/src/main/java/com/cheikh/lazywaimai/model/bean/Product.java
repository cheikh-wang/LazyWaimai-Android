package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Product implements Serializable {

    @SerializedName("id")
    String _id;

    @SerializedName("business_id")
    String businessId;

    @SerializedName("category_id")
    String categoryId;

    @SerializedName("name")
    String name;

    @SerializedName("price")
    Double price;

    @SerializedName("description")
    String description;

    @SerializedName("image_path")
    String imagePath;

    @SerializedName("month_sales")
    int monthSales;

    @SerializedName("rate")
    int rate;

    @SerializedName("left_num")
    int leftNum;

    @SerializedName("created_at")
    long createdAt;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(int monthSales) {
        this.monthSales = monthSales;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(int leftNum) {
        this.leftNum = leftNum;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + _id +
                ", businessId=" + businessId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", monthSales=" + monthSales +
                ", createdAt=" + createdAt +
                '}';
    }
}
