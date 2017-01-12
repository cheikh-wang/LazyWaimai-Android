package com.cheikh.lazywaimai.model.bean;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    String id;

    @SerializedName("username")
    String username;

    @SerializedName("nickname")
    String nickname;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("email")
    String email;

    @SerializedName("access_token")
    String token;

    @SerializedName("avatar_url")
    String avatarUrl;

    @SerializedName("last_address_id")
    long lastAddressId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public long getLastAddressId() {
        return lastAddressId;
    }

    public void setLastAddressId(long lastAddressId) {
        this.lastAddressId = lastAddressId;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
