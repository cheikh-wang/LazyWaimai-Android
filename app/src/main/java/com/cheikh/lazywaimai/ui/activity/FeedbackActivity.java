package com.cheikh.lazywaimai.ui.activity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseActivity;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.MainController;
import com.cheikh.lazywaimai.model.bean.Feedback;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.util.SystemUtil;
import com.cheikh.lazywaimai.util.ToastUtil;
import butterknife.Bind;
import butterknife.OnTextChanged;

/**
 * author: cheikh.wang on 17/1/10
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.activity_feedback)
public class FeedbackActivity extends BaseActivity<MainController.MainUiCallbacks>
        implements MainController.MainFeedbackUi {

    @Bind(R.id.edit_content)
    EditText mContentEdit;

    @Bind(R.id.txt_count)
    TextView mContentCountTxt;

    @Bind(R.id.edit_contact_way)
    EditText mContactWayEdit;

    private MenuItem mSubmitMenuItem;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController();
    }

    private void updateSubmitMenuItemState(boolean enable) {
        mSubmitMenuItem.setEnabled(enable);
    }

    @OnTextChanged(R.id.edit_content)
    public void onContentTextChange(CharSequence s) {
        int length = s != null ? s.length() : 0;
        mContentCountTxt.setText(getString(R.string.label_remark_limit_count, length));
        if (mSubmitMenuItem == null) {
            invalidateOptionsMenu();
        }
        updateSubmitMenuItemState(length != 0);
    }

    @Override
    public void onFeedbackFinish() {
        cancelLoading();
        ToastUtil.showToast(R.string.toast_success_submit_feedback);
        Display display = getDisplay();
        if (display != null) {
            display.finishActivity();
        }
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        ToastUtil.showToast(error);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        mSubmitMenuItem = menu.findItem(R.id.menu_submit);
        updateSubmitMenuItemState(!TextUtils.isEmpty(mContentEdit.getText().toString()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_submit) {
            feedback();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 提交反馈
     */
    private void feedback() {
        // 隐藏软键盘
        SystemUtil.hideKeyBoard(this);

        String content = mContentEdit.getText().toString();
        String contactWay = mContactWayEdit.getText().toString();
        // 验证反馈内容是否为空
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast(R.string.toast_error_empty_feedback_content);
            return;
        }

        Feedback feedback = new Feedback();
        feedback.setContent(content);
        feedback.setContactWay(contactWay);

        showLoading(R.string.label_being_something);
        getCallbacks().feedback(feedback);
    }
}
