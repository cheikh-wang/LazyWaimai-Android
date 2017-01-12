package com.cheikh.lazywaimai.base;

import com.cheikh.lazywaimai.util.ViewEventListener;
import java.util.List;

public interface IAdapter<T> {

    void setItems(List<T> items);

    void addItem(T item);

    void delItem(T item);

    void addItems(List<T> items);

    void clearItems();

    T getItem(int position);

    ViewEventListener<T> getViewEventListener();

    void setViewEventListener(ViewEventListener<T> viewEventListener);
}