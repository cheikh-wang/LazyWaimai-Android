package com.cheikh.lazywaimai.ui.adapter;

import android.view.View;

import com.cheikh.lazywaimai.base.BaseListAdapter;
import com.cheikh.lazywaimai.model.bean.SettleResult;
import com.cheikh.lazywaimai.ui.adapter.holder.SendTimeItemViewHolder;

public class SendTimeListAdapter extends BaseListAdapter<SettleResult.BookingTime, SendTimeItemViewHolder> {

    @Override
    protected int getViewLayoutId() {
        return android.R.layout.simple_list_item_single_choice;
    }

    @Override
    protected SendTimeItemViewHolder createViewHolder(View view) {
        return new SendTimeItemViewHolder(view);
    }

    @Override
    protected void showData(SendTimeItemViewHolder holder, int position) {
        SettleResult.BookingTime bookingTime = getItem(position);
        holder.bind(bookingTime);
    }
}