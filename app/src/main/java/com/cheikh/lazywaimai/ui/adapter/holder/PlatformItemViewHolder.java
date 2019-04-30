package com.cheikh.lazywaimai.ui.adapter.holder;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.bean.PaymentPlatform;
import com.cheikh.lazywaimai.widget.PicassoImageView;
import butterknife.BindView;
import butterknife.OnClick;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_PAYMENT_PLATFORM_CLICKED;

/**
 * author: cheikh.wang on 17/1/12
 * email: wanghonghi@126.com
 */
public class PlatformItemViewHolder extends BaseViewHolder<PaymentPlatform> {

    @BindView(R.id.list_item)
    View listItem;

    @BindView(R.id.img_icon)
    PicassoImageView platformIcon;

    @BindView(R.id.txt_name)
    TextView platformName;

    @BindView(R.id.radio)
    RadioButton radioBtn;

    @BindView(R.id.divider)
    View divider;

    public PlatformItemViewHolder(View view) {
        super(view);
    }

    public void bind(boolean isSelected, boolean isShowDivider) {
        platformIcon.loadPlatformIcon(mItem);
        platformName.setText(mItem.getName());
        radioBtn.setChecked(isSelected);
        divider.setVisibility(isShowDivider ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.list_item)
    public void onClick() {
        notifyItemAction(CLICK_TYPE_PAYMENT_PLATFORM_CLICKED);
    }
}
