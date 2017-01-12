package com.cheikh.lazywaimai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseAdapter;
import com.cheikh.lazywaimai.model.bean.Address;
import com.cheikh.lazywaimai.ui.adapter.holder.AddressItemViewHolder;

/**
 * author: cheikh.wang on 16/11/24
 * email: wanghonghi@126.com
 */
public class AddressListAdapter extends BaseAdapter<Address> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_address_item;
    }

    @Override
    public AddressItemViewHolder createViewHolder(View view, int viewType) {
        return new AddressItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, Address address, int position) {
        if (holder instanceof AddressItemViewHolder) {
            ((AddressItemViewHolder) holder).bind(address);
        }
    }
}