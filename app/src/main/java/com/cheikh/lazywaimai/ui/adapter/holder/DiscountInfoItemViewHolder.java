package com.cheikh.lazywaimai.ui.adapter.holder;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.bean.CartInfo;
import com.cheikh.lazywaimai.util.StringFetcher;

/**
 * author: cheikh.wang on 16/11/24
 * email: wanghonghi@126.com
 */

public class DiscountInfoItemViewHolder extends BaseViewHolder<CartInfo.DiscountInfo> {

    @BindView(R.id.txt_icon)
    TextView iconTxt;

    @BindView(R.id.txt_name)
    TextView nameTxt;

    @BindView(R.id.txt_price)
    TextView priceTxt;

    @BindView(R.id.txt_desc)
    TextView descTxt;

    public DiscountInfoItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(CartInfo.DiscountInfo item) {
        iconTxt.setText(item.getIconName());
        iconTxt.setBackgroundColor(Color.parseColor(item.getIconColor()));
        nameTxt.setText(item.getName());
        priceTxt.setText(StringFetcher.getString(R.string.label_discount_price, item.getPrice()));
        if (!TextUtils.isEmpty(item.getDescription())) {
            descTxt.setText(item.getDescription());
            descTxt.setVisibility(View.VISIBLE);
        } else {
            descTxt.setVisibility(View.GONE);
        }
    }
}
