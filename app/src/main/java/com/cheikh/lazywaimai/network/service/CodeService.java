package com.cheikh.lazywaimai.network.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by comet on 15/8/24.
 */
public interface CodeService {

    @FormUrlEncoded
    @POST("codes")
    Call<Object> create(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("codes/login")
    Call<Object> check(@Field("mobile") String mobile, @Field("code") String code);

}