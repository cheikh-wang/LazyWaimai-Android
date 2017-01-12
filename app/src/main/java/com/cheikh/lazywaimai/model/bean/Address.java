package com.cheikh.lazywaimai.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Address implements Parcelable {

    @SerializedName("id")
    String id;

    @SerializedName("summary")
    String summary;

    @SerializedName("detail")
    String detail;

    @SerializedName("name")
    String name;

    @SerializedName("phone")
    String phone;

    @SerializedName("user_id")
    String userId;

    @SerializedName("gender")
    Gender gender;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.summary);
        dest.writeString(this.detail);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.userId);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
    }

    public Address() {
    }

    protected Address(Parcel in) {
        this.id = in.readString();
        this.summary = in.readString();
        this.detail = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.userId = in.readString();
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
