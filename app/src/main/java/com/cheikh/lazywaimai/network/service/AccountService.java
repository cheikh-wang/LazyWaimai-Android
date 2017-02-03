package com.cheikh.lazywaimai.network.service;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import com.cheikh.lazywaimai.model.bean.Favorite;
import com.cheikh.lazywaimai.model.bean.ResultsPage;
import com.cheikh.lazywaimai.model.bean.User;


public interface AccountService {

    /**
     * 发送验证码
     * @param mobile
     * @return
     */
    @FormUrlEncoded
    @POST("users?action=send_code")
    Observable<Boolean> sendCode(@Field("mobile") String mobile);

    /**
     * 检查验证码
     * @param mobile
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST("users?action=verify_code")
    Observable<Boolean> verifyCode(@Field("mobile") String mobile, @Field("code") String code);

    /**
     * 创建帐号
     * @param mobile
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("users?action=create_user")
    Observable<Boolean> register(@Field("mobile") String mobile, @Field("password") String password);

    /**
     * 获取用户资料
     * @param id
     * @return
     */
    @GET("users/{id}")
    Observable<User> profile(@Path("id") String id);

    /**
     * 设置默认地址
     * @param userId
     * @param addressId
     * @return
     */
    @FormUrlEncoded
    @PUT("users/{id}")
    Observable<User> setLastAddress(@Path("id") String userId, @Field("last_address_id") String addressId);

    /**
     * 修改头像
     * @param userId
     * @param avatarUrl
     * @return
     */
    @FormUrlEncoded
    @PUT("users/{id}")
    Observable<User> updateAvatar(@Path("id") String userId, @Field("avatar_url") String avatarUrl);

    /**
     * 分页获取收藏
     * @param page
     * @param size
     * @return
     */
    @GET("users/favorites")
    Observable<ResultsPage<Favorite>> favorites(@Query("page") int page, @Query("size") int size);

    /**
     * 设置用户名
     * @param userId
     * @param username
     * @return
     */
    @FormUrlEncoded
    @PUT("users/{id}")
    Observable<User> setUsername(@Path("id") String userId, @Field("username") String username);

    /**
     * 设置昵称
     * @param userId
     * @param nickname
     * @return
     */
    @FormUrlEncoded
    @PUT("users/{id}")
    Observable<User> setNickname(@Path("id") String userId, @Field("nickname") String nickname);
}