package com.cheikh.lazywaimai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseAdapter;
import com.cheikh.lazywaimai.model.bean.CartInfo;
import com.cheikh.lazywaimai.ui.adapter.holder.ExtraFeeItemViewHolder;

/**
 * author: cheikh.wang on 16/11/24
 * email: wanghonghi@126.com
 */
public class ExtraFeeListAdapter extends BaseAdapter<CartInfo.ExtraFee> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_extra_fee_item;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        return new ExtraFeeItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, CartInfo.ExtraFee extraFee, int position) {
        if (holder instanceof ExtraFeeItemViewHolder) {
            ((ExtraFeeItemViewHolder) holder).bind(extraFee);
        }
    }
}
