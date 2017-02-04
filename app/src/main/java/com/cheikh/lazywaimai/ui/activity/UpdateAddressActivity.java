package com.cheikh.lazywaimai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import butterknife.Bind;
import butterknife.OnClick;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseActivity;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.AddressController;
import com.cheikh.lazywaimai.model.bean.Address;
import com.cheikh.lazywaimai.model.bean.Gender;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.ToastUtil;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.activity_address_update)
public class UpdateAddressActivity extends BaseActivity<AddressController.AddressUiCallbacks>
        implements AddressController.UpdateAddressUi {

    @Bind(R.id.et_input_name)
    EditText mNameEdit;

    @Bind(R.id.rb_male)
    RadioButton mMaleRadioButton;

    @Bind(R.id.rb_female)
    RadioButton mFemaleRadioButton;

    @Bind(R.id.et_input_phone)
    EditText mPhoneEdit;

    @Bind(R.id.et_poi_address)
    EditText mPoiAddressEdit;

    @Bind(R.id.et_detail_address)
    EditText mDetailAddressEdit;

    @Bind(R.id.btn_submit)
    Button mModifyOrCreateBtn;

    private Address mOriginalAddress;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getAddressController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mOriginalAddress = intent.getParcelableExtra(Display.PARAM_OBJ);
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        if (mOriginalAddress != null) {
            setTitle(R.string.title_change_address);
            mNameEdit.setText(mOriginalAddress.getName());
            mMaleRadioButton.setChecked(mOriginalAddress.getGender() == Gender.MALE);
            mFemaleRadioButton.setChecked(mOriginalAddress.getGender() == Gender.FEMALE);
            mPhoneEdit.setText(mOriginalAddress.getPhone());
            mPoiAddressEdit.setText(mOriginalAddress.getSummary());
            mDetailAddressEdit.setText(mOriginalAddress.getDetail());
            mModifyOrCreateBtn.setText(R.string.btn_confirm_update);
        } else {
            setTitle(R.string.title_create_address);
            mMaleRadioButton.setChecked(true);
            mFemaleRadioButton.setChecked(false);
            mModifyOrCreateBtn.setText(R.string.btn_confirm_create);
        }
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        ToastUtil.showToast(error.getMessage());
    }

    @Override
    public void updateFinish() {
        cancelLoading();
        ToastUtil.showToast(mOriginalAddress != null ? R.string.toast_success_address_update : R.string.toast_success_address_create);
        Display display = getDisplay();
        if (display != null) {
            display.finishActivity();
        }
    }

    @OnClick(R.id.btn_submit)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                doCreateOrChange();
                break;
        }
    }

    private void doCreateOrChange() {
        // 验证名字是否为空
        final String name = mNameEdit.getText().toString().trim();
        if (!getCallbacks().isNameValid(name)) {
            ToastUtil.showToast(R.string.toast_error_empty_name);
            return;
        }
        // 验证电话是否为空
        final String phone = mPhoneEdit.getText().toString().trim();
        if (!getCallbacks().isMobileValid(phone)) {
            ToastUtil.showToast(R.string.toast_error_empty_phone);
            return;
        }
        // 验证小区/学校/大楼是否为空
        final String summary = mPoiAddressEdit.getText().toString().trim();
        if (!getCallbacks().isSummaryValid(summary)) {
            ToastUtil.showToast(R.string.toast_error_empty_address_summary);
            return;
        }
        // 验证详细地址是否为空
        final String detail = mDetailAddressEdit.getText().toString().trim();
        if (!getCallbacks().isDetailValid(detail)) {
            ToastUtil.showToast(R.string.toast_error_empty_address_detail);
            return;
        }
        // 是否是男性
        boolean isMale = mMaleRadioButton.isChecked();

        // 开始进行新增或者修改操作
        Address address = new Address();
        address.setName(name);
        address.setGender(isMale ? Gender.MALE : Gender.FEMALE);
        address.setPhone(phone);
        address.setSummary(summary);
        address.setDetail(detail);

        showLoading(R.string.label_being_something);
        if (mOriginalAddress != null) {
            address.setId(mOriginalAddress.getId());
            getCallbacks().change(address);
        } else {
            getCallbacks().create(address);
        }
    }
}