package com.cheikh.lazywaimai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseAdapter;
import com.cheikh.lazywaimai.model.bean.CartInfo;
import com.cheikh.lazywaimai.ui.adapter.holder.DiscountInfoItemViewHolder;

/**
 * author: cheikh.wang on 16/11/24
 * email: wanghonghi@126.com
 */
public class DiscountInfoListAdapter extends BaseAdapter<CartInfo.DiscountInfo> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_discount_info_item;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new DiscountInfoItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, CartInfo.DiscountInfo discountInfo, int position) {
        if (holder instanceof DiscountInfoItemViewHolder) {
            ((DiscountInfoItemViewHolder) holder).bind(discountInfo);
        }
    }
}
