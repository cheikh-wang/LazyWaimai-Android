package com.cheikh.lazywaimai.network.service;

import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.model.bean.Favorite;
import com.cheikh.lazywaimai.model.bean.ProductCategory;
import com.cheikh.lazywaimai.model.bean.ResultsPage;

public interface BusinessService {

    @GET("businesses")
    Observable<ResultsPage<Business>> businesses(@Query("page") int page, @Query("size") int size);

    @GET("businesses/{bid}/products")
    Observable<List<ProductCategory>> products(@Path("bid") String businessId);

    @POST("businesses/{bid}/favorite")
    Observable<Favorite> favorite(@Path("bid") String businessId);
}
