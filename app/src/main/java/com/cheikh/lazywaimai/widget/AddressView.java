package com.cheikh.lazywaimai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.model.bean.Address;

/**
 * author：cheikh on 16/5/13 13:41
 * email：wanghonghi@126.com
 */
public class AddressView extends FrameLayout {

    @Bind(R.id.txt_name)
    TextView nameTxt;

    @Bind(R.id.txt_phone)
    TextView mobileTxt;

    @Bind(R.id.txt_address)
    TextView addressTxt;

    @Bind(R.id.layout_address_info)
    View addressInfoLayout;

    public AddressView(Context context) {
        this(context, null);
    }

    public AddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_address_view, this);
        ButterKnife.bind(view);
    }

    public void setData(Address addressInfo) {
        if (addressInfo != null) {
            addressInfoLayout.setVisibility(View.VISIBLE);
            nameTxt.setText(addressInfo.getName());
            mobileTxt.setText(addressInfo.getPhone());
            addressTxt.setText(addressInfo.getSummary() + addressInfo.getDetail());
        } else {
            addressInfoLayout.setVisibility(View.GONE);
        }
    }
}