package com.cheikh.lazywaimai.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.base.BaseFragment;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.UserController;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.util.StringUtil;
import com.cheikh.lazywaimai.util.SystemUtil;
import com.cheikh.lazywaimai.util.ToastUtil;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * author: cheikh.wang on 17/1/12
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.fragment_set_nickname)
public class SetNicknameFragment extends BaseFragment<UserController.UserUiCallbacks>
        implements UserController.UserNicknameUi {

    @Bind(R.id.edit_content)
    EditText mContentEdit;

    @Bind(R.id.btn_clear)
    ImageView mClearBtn;

    @Bind(R.id.btn_save)
    TextView mSaveBtn;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_set_nickname);
    }

    @OnTextChanged(R.id.edit_content)
    public void onContentTextChange(CharSequence s) {
        int visible = StringUtil.isEmpty(s.toString()) ? View.GONE : View.VISIBLE;
        mClearBtn.setVisibility(visible);
    }

    @Override
    public void onSetNicknameFinish() {
        cancelLoading();
        ToastUtil.showToast(R.string.toast_success_set_nickname);
        // 关闭当前页面
        Display display = getDisplay();
        if (display != null) {
            display.popTopFragmentBackStack();
        }
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        ToastUtil.showToast(error);
    }

    @OnClick({R.id.btn_clear, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                mContentEdit.setText("");
                break;
            case R.id.btn_save:
                saveUserName();
                break;
        }
    }

    /**
     * 执行设置昵称的操作
     */
    private void saveUserName() {
        // 隐藏软键盘
        SystemUtil.hideKeyBoard(getContext());

        // 验证昵称是否为空
        final String nickname = mContentEdit.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            ToastUtil.showToast(R.string.toast_error_empty_nickname);
            return;
        }

        // 禁用保存按钮,避免重复点击
        mSaveBtn.setEnabled(false);
        // 显示提示对话框
        showLoading(R.string.label_being_something);
        // 发起设置昵称的网络请求
        getCallbacks().setNickname(nickname);
    }
}
