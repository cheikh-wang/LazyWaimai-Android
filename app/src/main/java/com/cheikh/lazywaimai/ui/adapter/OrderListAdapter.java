package com.cheikh.lazywaimai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseAdapter;
import com.cheikh.lazywaimai.model.bean.Order;
import com.cheikh.lazywaimai.ui.adapter.holder.OrderItemViewHolder;

/**
 * author: cheikh.wang on 16/11/23
 * email: wanghonghi@126.com
 */
public class OrderListAdapter extends BaseAdapter<Order> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_order_list_item;
    }

    @Override
    public OrderItemViewHolder createViewHolder(View view, int viewType) {
        return new OrderItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, Order order, int position) {
        if (holder instanceof OrderItemViewHolder) {
            ((OrderItemViewHolder) holder).bind(order);
        }
    }
}
