package com.cheikh.lazywaimai.widget.refresh;

/**
 * 下拉刷新和加载更多的监听器
 */
public abstract class OnRefreshListener {

    public abstract void onRefresh();

    public boolean enableRefresh() {
        return true;
    }

    public void onFinish() {}
}
