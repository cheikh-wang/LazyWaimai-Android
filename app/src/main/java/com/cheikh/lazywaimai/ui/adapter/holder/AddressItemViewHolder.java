package com.cheikh.lazywaimai.ui.adapter.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.bean.Address;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_ADDRESS_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_DELETE_BTN_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_EDIT_BTN_CLICKED;

/**
 * author: cheikh.wang on 16/11/24
 * email: wanghonghi@126.com
 */
public class AddressItemViewHolder extends BaseViewHolder<Address> {

    @Bind(R.id.txt_name)
    TextView mNameTxt;

    @Bind(R.id.txt_gender)
    TextView mGenderTxt;

    @Bind(R.id.txt_mobile)
    TextView mMobileTxt;

    @Bind(R.id.txt_address)
    TextView mAddressTxt;

    @Bind(R.id.btn_delete)
    ImageButton mDeleteBtn;

    @Bind(R.id.btn_edit)
    ImageButton mEditBtn;

    public AddressItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Address address) {
        mNameTxt.setText(address.getName());
        mGenderTxt.setText(address.getGender().toString());
        mMobileTxt.setText(address.getPhone());
        mAddressTxt.setText(getString(R.string.label_address, address.getSummary(),
                address.getDetail()));
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemAction(CLICK_TYPE_DELETE_BTN_CLICKED);
            }
        });
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemAction(CLICK_TYPE_EDIT_BTN_CLICKED);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemAction(CLICK_TYPE_ADDRESS_CLICKED);
            }
        });
    }
}
