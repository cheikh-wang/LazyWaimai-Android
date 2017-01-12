package com.cheikh.lazywaimai.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import cn.bingoogolapple.badgeview.BGABadgeFrameLayout;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.ShoppingCart;
import com.cheikh.lazywaimai.model.bean.ProductCategory;

/**
 * author: cheikh.wang on 16/11/23
 * email: wanghonghi@126.com
 */
public class ProductCategoryItemViewHolder extends BaseViewHolder<ProductCategory> {

    @Bind(R.id.txt_name)
    TextView mNameTxt;

    @Bind(R.id.badge_view)
    BGABadgeFrameLayout mBadgeView;

    public ProductCategoryItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ProductCategory category) {
        mNameTxt.setText(category.getName());

        int count = ShoppingCart.getInstance().getQuantityForCategory(category);
        if (count > 0) {
            mBadgeView.showTextBadge(String.valueOf(count));
        } else {
            mBadgeView.hiddenBadge();
        }
    }
}
