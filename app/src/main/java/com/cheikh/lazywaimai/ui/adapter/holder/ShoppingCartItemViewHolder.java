package com.cheikh.lazywaimai.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.ShoppingCart;
import com.cheikh.lazywaimai.model.bean.Product;
import com.cheikh.lazywaimai.model.bean.ShoppingEntity;
import com.cheikh.lazywaimai.util.StringFetcher;
import com.cheikh.lazywaimai.widget.ShoppingCountView;

/**
 * author: cheikh.wang on 16/11/24
 * email: wanghonghi@126.com
 */

public class ShoppingCartItemViewHolder extends BaseViewHolder<ShoppingEntity> {

    @BindView(R.id.txt_name)
    TextView mNameTxt;

    @BindView(R.id.txt_price)
    TextView mPriceTxt;

    @BindView(R.id.shopping_count_view)
    ShoppingCountView mShoppingCountView;

    public ShoppingCartItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ShoppingEntity entity) {
        mNameTxt.setText(entity.getName());
        mPriceTxt.setText(StringFetcher.getString(R.string.label_price, entity.getTotalPrice()));

        final Product finalProduct = entity.getProduct();
        int quantity = ShoppingCart.getInstance().getQuantityForProduct(finalProduct);
        mShoppingCountView.setShoppingCount(quantity);
        mShoppingCountView.setOnShoppingClickListener(new ShoppingCountView.ShoppingClickListener() {
            @Override
            public void onAddClick(int num) {
                if (!ShoppingCart.getInstance().push(finalProduct)) {
                    // 添加失败则恢复数量
                    int oldQuantity = ShoppingCart.getInstance().getQuantityForProduct(finalProduct);
                    mShoppingCountView.setShoppingCount(oldQuantity);
                }
            }

            @Override
            public void onMinusClick(int num) {
                if (!ShoppingCart.getInstance().pop(finalProduct)) {
                    // 减少失败则恢复数量
                    int oldQuantity = ShoppingCart.getInstance().getQuantityForProduct(finalProduct);
                    mShoppingCountView.setShoppingCount(oldQuantity);
                }
            }
        });
    }
}
