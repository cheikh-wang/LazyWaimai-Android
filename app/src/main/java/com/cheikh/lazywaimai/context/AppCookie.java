package com.cheikh.lazywaimai.context;

import com.cheikh.lazywaimai.model.bean.User;
import com.cheikh.lazywaimai.util.Constants;
import com.cheikh.lazywaimai.util.PreferenceUtil;

/**
 * author：cheikh.wang on 16/8/3 19:50
 * email：wanghonghi@126.com
 */
public class AppCookie {

    public static boolean isLoggin() {
        return getUserInfo() != null && getAccessToken() != null;
    }

    /**
     * 保存用户信息
     * @param user
     */
    public static void saveUserInfo(User user) {
        PreferenceUtil.set(Constants.Persistence.USER_INFO, user);
    }

    /**
     * 获取用户信息
     * @return
     */
    public static User getUserInfo() {
        return PreferenceUtil.getObject(Constants.Persistence.USER_INFO, User.class);
    }

    /**
     * 保存最后一次登录的手机号
     * @param phone
     */
    public static void saveLastPhone(String phone) {
        PreferenceUtil.set(Constants.Persistence.LAST_LOGIN_PHONE, phone);
    }

    /**
     * 获取最后一次登录的手机号
     * @return
     */
    public static String getLastPhone() {
        return PreferenceUtil.getString(Constants.Persistence.LAST_LOGIN_PHONE, null);
    }

    /**
     * 保存AccessToken
     * @param token
     */
    public static void saveAccessToken(String token) {
        PreferenceUtil.set(Constants.Persistence.ACCESS_TOKEN, token);
    }

    /**
     * 获取AccessToken
     * @return
     */
    public static String getAccessToken() {
        return PreferenceUtil.getString(Constants.Persistence.ACCESS_TOKEN, null);
    }

    /**
     * 保存RefreshToken
     * @param token
     */
    public static void saveRefreshToken(String token) {
        PreferenceUtil.set(Constants.Persistence.REFRESH_TOKEN, token);
    }

    /**
     * 获取RefreshToken
     * @return
     */
    public static String getRefreshToken() {
        return PreferenceUtil.getString(Constants.Persistence.REFRESH_TOKEN, null);
    }
}
