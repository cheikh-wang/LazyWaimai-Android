package com.cheikh.lazywaimai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseAdapter;
import com.cheikh.lazywaimai.model.bean.Favorite;
import com.cheikh.lazywaimai.ui.adapter.holder.FavoriteItemViewHolder;

/**
 * author: cheikh.wang on 17/01/11
 * email: wanghonghi@126.com
 */
public class FavoriteListAdapter extends BaseAdapter<Favorite> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_favorite_item;
    }

    @Override
    public FavoriteItemViewHolder createViewHolder(View view, int viewType) {
        return new FavoriteItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, Favorite item, int position) {
        if (holder instanceof FavoriteItemViewHolder) {
            ((FavoriteItemViewHolder) holder).bind(item);
        }
    }
}
