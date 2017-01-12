package com.cheikh.lazywaimai.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<T, VH extends BaseViewHolder<T>> extends BaseAdapter {

    protected List<T> mList;

    public BaseListAdapter() {
        mList = new ArrayList<>();
    }

    public void setItems(List<T> data) {
        if (data == null) {
            return;
        }

        mList.clear();
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(getViewLayoutId(), viewGroup, false);
            convertView.setTag(createViewHolder(convertView));
        }

        showData((VH) convertView.getTag(), position);

        return convertView;
    }

    protected abstract int getViewLayoutId();

    protected abstract VH createViewHolder(View view);

    protected abstract void showData(VH holder, int position);
}
