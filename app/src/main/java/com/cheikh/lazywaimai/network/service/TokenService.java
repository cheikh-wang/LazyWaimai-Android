package com.cheikh.lazywaimai.network.service;

import java.util.Map;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;
import com.cheikh.lazywaimai.model.bean.Token;

public interface TokenService {

    @POST("oauth/access_token")
    Observable<Token> accessToken(@QueryMap Map<String, String> params);

    @POST("oauth/access_token")
    Observable<Token> refreshToken(@QueryMap Map<String, String> params);
}