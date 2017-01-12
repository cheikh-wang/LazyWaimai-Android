package com.cheikh.lazywaimai.network;

import android.util.Log;

import rx.Subscriber;
import com.cheikh.lazywaimai.model.bean.ResponseError;

/**
 * author: cheikh.wang on 17/1/3
 * email: wanghonghi@126.com
 */
public abstract class RequestCallback<T> extends Subscriber<T> {

    private static final String TAG = "RequestCallback";

    @Override
    public final void onNext(T t) {
        onResponse(t);
    }

    @Override
    public final void onError(Throwable throwable) {
        if (throwable instanceof ResponseError) {
            onFailure((ResponseError) throwable);
        } else {
            Log.e(TAG, "throwable isn't instance of ResponseError");
        }
    }

    @Override
    public void onStart() {

    }

    public void onResponse(T response) {}

    public void onFailure(ResponseError error) {}

    @Override
    public void onCompleted() {}
}