package com.cheikh.lazywaimai.network.service;

import com.cheikh.lazywaimai.model.bean.Feedback;
import com.cheikh.lazywaimai.model.bean.Setting;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface CommonService {

    /**
     * 单个上传文件或者图片
     * @param part
     * @return
     */
    @Multipart
    @POST("files")
    Observable<String> singleFileUpload(@Part MultipartBody.Part part);

    /**
     * 获取应用配置
     * @return
     */
    @GET("settings")
    Observable<Setting> settings();

    /**
     * 提交意见反馈
     * @return
     */
    @POST("feedbacks")
    Observable<Feedback> feedback(@Body Feedback feedback);
}