package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductCategory {

    @SerializedName("id")
    String _id;

    @SerializedName("business_id")
    String businessId;

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("products")
    List<Product> products;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "id=" + _id +
                ", businessId=" + businessId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", products=" + products +
                '}';
    }
}
