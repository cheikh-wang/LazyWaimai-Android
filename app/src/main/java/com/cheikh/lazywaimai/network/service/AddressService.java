package com.cheikh.lazywaimai.network.service;

import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;
import com.cheikh.lazywaimai.model.bean.Address;

public interface AddressService {

    @POST("addresses")
    Observable<Address> create(@Body Address address);

    @DELETE("addresses/{id}")
    Observable<Object> delete(@Path("id") String id);

    @PATCH("addresses/{id}")
    Observable<Address> change(@Path("id") String id, @Body Address address);

    @GET("addresses")
    Observable<List<Address>> addresses();
}
