package com.cheikh.lazywaimai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseAdapter;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.ui.adapter.holder.BusinessItemViewHolder;

/**
 * author: cheikh.wang on 16/11/23
 * email: wanghonghi@126.com
 */
public class BusinessListAdapter extends BaseAdapter<Business> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_business_item;
    }

    @Override
    public BusinessItemViewHolder createViewHolder(View view, int viewType) {
        return new BusinessItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, Business business, int position) {
        if (holder instanceof BusinessItemViewHolder) {
            ((BusinessItemViewHolder) holder).bind(business);
        }
    }
}
