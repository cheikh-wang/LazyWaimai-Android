package com.cheikh.lazywaimai.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 头部下拉刷新View基础类
 * 所有的头部下拉刷新的View必需继承这个类
 */
public abstract class BaseRefreshHeaderView extends FrameLayout {

    public BaseRefreshHeaderView(Context context) {
        super(context);
    }

    public BaseRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void onBegin();

    public abstract void onPull(float fraction);

    public abstract void onRelease();

    public abstract void onRefreshing();

    public abstract void onComplete();

    public abstract HeaderConfig getConfig();
}
