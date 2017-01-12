package com.cheikh.lazywaimai.base;

import com.cheikh.lazywaimai.util.ViewEventListener;

public interface IViewHolder<T> {

    void setViewEventListener(ViewEventListener<T> viewEventListener);

    void setItem(T item);

    void setPosition(int position);
}
