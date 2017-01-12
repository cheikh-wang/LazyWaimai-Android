package com.cheikh.lazywaimai.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.bean.ShoppingEntity;
import com.cheikh.lazywaimai.util.StringFetcher;

/**
 * author: cheikh.wang on 16/11/24
 * email: wanghonghi@126.com
 */
public class ShoppingProductItemViewHolder extends BaseViewHolder<ShoppingEntity> {

    @Bind(R.id.txt_name)
    TextView nameTxt;

    @Bind(R.id.txt_quantity)
    TextView quantityTxt;

    @Bind(R.id.txt_price)
    TextView priceTxt;

    public ShoppingProductItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ShoppingEntity item) {
        nameTxt.setText(item.getName());
        quantityTxt.setText(StringFetcher.getString(R.string.label_quantity,
                item.getQuantity()));
        priceTxt.setText(StringFetcher.getString(R.string.label_price,
                item.getTotalPrice()));
    }
}
