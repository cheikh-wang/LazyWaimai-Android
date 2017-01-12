package com.cheikh.lazywaimai.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseAdapter;
import com.cheikh.lazywaimai.model.bean.PaymentPlatform;
import com.cheikh.lazywaimai.ui.activity.PaymentActivity;
import com.cheikh.lazywaimai.ui.adapter.holder.PlatformItemViewHolder;

/**
 * author: cheikh.wang on 17/1/12
 * email: wanghonghi@126.com
 */
public class PlatformListAdapter extends BaseAdapter<PaymentPlatform> {

    private int mSelectedPosition = -1;

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        mSelectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_platform_item;
    }

    @Override
    public PlatformItemViewHolder createViewHolder(View view, int viewType) {
        return new PlatformItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, PaymentPlatform item, int position) {
        if (holder instanceof PlatformItemViewHolder) {
            boolean isShowDivider = position != getItemCount() - 1;
            boolean isChecked = position == mSelectedPosition;
            ((PlatformItemViewHolder) holder).bind(isChecked, isShowDivider);
        }
    }
}
