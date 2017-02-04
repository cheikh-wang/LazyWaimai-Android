package com.cheikh.lazywaimai.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseActivity;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.context.AppCookie;
import com.cheikh.lazywaimai.controller.UserController;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.SystemUtil;
import com.cheikh.lazywaimai.util.ToastUtil;
import com.cheikh.lazywaimai.widget.section.SectionTextItemView;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity<UserController.UserUiCallbacks>
    implements UserController.SettingUi {

        @Bind(R.id.btn_check_update)
        SectionTextItemView mCheckUpdateBtn;

        @Bind(R.id.btn_feedback)
        SectionTextItemView mFeedbackBtn;

        @Bind(R.id.layout_logout)
        View mLogoutLayout;

        @Bind(R.id.btn_logout)
        TextView mLogoutBtn;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        String versionName = SystemUtil.getAppVersionName(this);
        mCheckUpdateBtn.setSubtitle(getString(R.string.label_current_version_name, versionName));
        mLogoutLayout.setVisibility(AppCookie.isLoggin() ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.btn_check_update, R.id.btn_feedback, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_check_update:
                // TODO 还未开发
                ToastUtil.showToast("还未开发");
                break;
            case R.id.btn_feedback:
                getCallbacks().showFeedback();
                break;
            case R.id.btn_logout:
                handleLogoutClick();
                break;
        }
    }

    @Override
    public void logoutFinish() {
        ToastUtil.showToast(R.string.toast_success_logout);
        Display display = getDisplay();
        if (display != null) {
            display.finishActivity();
        }
    }

    private void handleLogoutClick() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_logout_title)
                .setMessage(R.string.dialog_logout_message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCallbacks().logout();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .create()
                .show();
    }
}
