package com.cheikh.lazywaimai.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.base.BaseFragment;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.UserController;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.StringUtil;
import com.cheikh.lazywaimai.util.SystemUtil;
import com.cheikh.lazywaimai.util.ToastUtil;

/**
 * author：cheikh on 16/5/14 20:45
 * email：wanghonghi@126.com
 */
@ContentView(R.layout.fragment_register_third_step)
public class RegisterThirdStepFragment extends BaseFragment<UserController.UserUiCallbacks>
        implements UserController.RegisterThirdStepUi {

    @Bind(R.id.edit_password)
    EditText mPasswordEdit;

    @Bind(R.id.btn_clear_password)
    ImageView mClearPasswordBtn;

    @Bind(R.id.edit_password_again)
    EditText mPasswordAgainEdit;

    @Bind(R.id.btn_clear_password_again)
    ImageView mClearPasswordAgainBtn;

    @Bind(R.id.btn_register)
    Button mRegisterBtn;

    private String mMobile;

    public static RegisterThirdStepFragment create(String mobile) {
        RegisterThirdStepFragment fragment = new RegisterThirdStepFragment();
        if (!TextUtils.isEmpty(mobile)) {
            Bundle bundle = new Bundle();
            bundle.putString(Display.PARAM_OBJ, mobile);
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected void handleArguments(Bundle arguments) {
        if (arguments != null) {
            mMobile = arguments.getString(Display.PARAM_OBJ);
        }
    }

    @Override
    public void userCreateFinish() {
        cancelLoading();
        mRegisterBtn.setEnabled(true);
        ToastUtil.showToast(R.string.toast_success_register);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Display display = getDisplay();
                if (display != null) {
                    display.finishActivity();
                }
            }
        }, 1500);
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        mRegisterBtn.setEnabled(true);
        ToastUtil.showToast(error.getMessage());
    }

    @OnTextChanged(R.id.edit_password)
    public void onPasswordTextChange(CharSequence s) {
        int visible = StringUtil.isEmpty(s.toString()) ? View.GONE : View.VISIBLE;
        mPasswordEdit.setVisibility(visible);
    }

    @OnTextChanged(R.id.edit_password_again)
    public void onPasswordAgainTextChange(CharSequence s) {
        int visible = StringUtil.isEmpty(s.toString()) ? View.GONE : View.VISIBLE;
        mPasswordAgainEdit.setVisibility(visible);
    }

    @OnClick({R.id.btn_clear_password, R.id.btn_clear_password_again, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear_password:
                mPasswordEdit.setText("");
                break;
            case R.id.btn_clear_password_again:
                mPasswordAgainEdit.setText("");
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    /**
     * 执行创建用户的操作
     */
    private void register() {
        // 隐藏软键盘
        SystemUtil.hideKeyBoard(getContext());

        // 验证密码是否为空
        final String password = mPasswordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(R.string.toast_error_empty_password);
            return;
        }
        // 验证确认密码是否为空
        final String passwordAgain = mPasswordAgainEdit.getText().toString().trim();
        if (TextUtils.isEmpty(passwordAgain)) {
            ToastUtil.showToast(R.string.toast_error_empty_password_confirm);
            return;
        }
        // 验证两次的密码输入是否一致
        if (!password.equals(passwordAgain)) {
            ToastUtil.showToast(R.string.toast_error_password_not_consistent);
            return;
        }

        showLoading(R.string.label_being_something);
        // 避免重复点击
        mRegisterBtn.setEnabled(false);
        // 发起发送验证码的API请求
        getCallbacks().createUser(mMobile, password);
    }
}