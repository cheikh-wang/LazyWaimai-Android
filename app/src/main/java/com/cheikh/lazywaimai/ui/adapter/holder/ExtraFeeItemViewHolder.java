package com.cheikh.lazywaimai.ui.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.bean.CartInfo;
import com.cheikh.lazywaimai.util.StringFetcher;

/**
 * author: cheikh.wang on 16/11/24
 * email: wanghonghi@126.com
 */

public class ExtraFeeItemViewHolder extends BaseViewHolder<CartInfo.ExtraFee> {

    @Bind(R.id.txt_name)
    TextView nameTxt;

    @Bind(R.id.txt_price)
    TextView priceTxt;

    @Bind(R.id.txt_desc)
    TextView descTxt;

    public ExtraFeeItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(CartInfo.ExtraFee item) {
        nameTxt.setText(item.getName());
        priceTxt.setText(StringFetcher.getString(R.string.label_price, item.getPrice()));
        if (!TextUtils.isEmpty(item.getDescription())) {
            descTxt.setText(item.getDescription());
            descTxt.setVisibility(View.VISIBLE);
        } else {
            descTxt.setVisibility(View.GONE);
        }
    }
}
