package com.cheikh.lazywaimai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseAdapter;
import com.cheikh.lazywaimai.model.bean.ShoppingEntity;
import com.cheikh.lazywaimai.ui.adapter.holder.ShoppingProductItemViewHolder;

/**
 * author: cheikh.wang on 16/11/24
 * email: wanghonghi@126.com
 */
public class ShoppingProductListAdapter extends BaseAdapter<ShoppingEntity> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_shopping_product_item;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new ShoppingProductItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, ShoppingEntity entity, int position) {
        if (holder instanceof ShoppingProductItemViewHolder) {
            ((ShoppingProductItemViewHolder) holder).bind(entity);
        }
    }
}
