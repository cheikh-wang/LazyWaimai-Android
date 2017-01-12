package com.cheikh.lazywaimai.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.model.bean.Favorite;
import com.cheikh.lazywaimai.util.StringFetcher;
import com.cheikh.lazywaimai.widget.PicassoImageView;
import butterknife.Bind;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_BUSINESS_CLICKED;

/**
 * author: cheikh.wang on 17/01/11
 * email: wanghonghi@126.com
 */
public class FavoriteItemViewHolder extends BaseViewHolder<Favorite> {

    @Bind(R.id.img_photo)
    PicassoImageView mPhotoImg;

    @Bind(R.id.txt_name)
    TextView mNameTxt;

    @Bind(R.id.txt_month_sales)
    TextView mMonthSalesTxt;

    @Bind(R.id.txt_content)
    TextView mMultiContentTxt;

    public FavoriteItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Favorite favorite) {
        Business business = favorite.getBusiness();
        if (business != null) {
            mPhotoImg.loadBusinessPhoto(business);
            mNameTxt.setText(business.getName());
            mMonthSalesTxt.setText(StringFetcher.getString(R.string.label_month_sales,
                    business.getMonthSales()));
            mMultiContentTxt.setText(StringFetcher.getString(R.string.label_business_multi_content,
                    String.valueOf(business.getMinPrice()),
                    String.valueOf(business.getShippingFee()),
                    String.valueOf(business.getShippingTime())));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemAction(CLICK_TYPE_BUSINESS_CLICKED);
                }
            });
        }
    }
}
