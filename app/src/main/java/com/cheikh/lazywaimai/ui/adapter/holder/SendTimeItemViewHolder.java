package com.cheikh.lazywaimai.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.bean.SettleResult;

/**
 * author: cheikh.wang on 16/11/23
 * email: wanghonghi@126.com
 */
public class SendTimeItemViewHolder extends BaseViewHolder<SettleResult.BookingTime> {

    @Bind(android.R.id.text1)
    TextView mTitleTxt;

    public SendTimeItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(SettleResult.BookingTime data) {
        mTitleTxt.setText(data.getViewTime());
    }
}
