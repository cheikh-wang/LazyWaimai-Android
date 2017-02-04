package com.cheikh.lazywaimai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseActivity;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.OrderController;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.widget.FixWrapLayout;
import java.util.List;
import butterknife.Bind;
import butterknife.OnTextChanged;

/**
 * author: cheikh.wang on 17/1/10
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.activity_remark)
public class RemarkActivity extends BaseActivity<OrderController.OrderUiCallbacks>
        implements OrderController.OrderRemarkUi, View.OnClickListener {

    @Bind(R.id.edit_content)
    EditText mContentEdit;

    @Bind(R.id.txt_count)
    TextView mContentCountTxt;

    @Bind(R.id.layout_common_remarks)
    FixWrapLayout mCommonRemarksLayout;

    private MenuItem mFinishMenuItem;

    private String mRemark;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getOrderController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mRemark = intent.getStringExtra(Display.PARAM_OBJ);
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(mRemark)) {
            mContentEdit.setText(mRemark);
            mContentEdit.setSelection(mRemark.length());
        }
    }

    @Override
    public void setCommonRemarks(List<String> remarks) {
        for (String remark : remarks) {
            TextView remarkTxt = (TextView) View.inflate(this, R.layout.include_common_remark, null);
            remarkTxt.setText(remark);
            remarkTxt.setTag(remark);
            remarkTxt.setOnClickListener(this);
            mCommonRemarksLayout.addView(remarkTxt);
        }
    }

    private void updateFinishMenuItemState(boolean enable) {
        if (mFinishMenuItem != null) {
            mFinishMenuItem.setEnabled(enable);
        }
    }

    @OnTextChanged(R.id.edit_content)
    public void onContentTextChange(CharSequence s) {
        int length = s != null ? s.length() : 0;
        mContentCountTxt.setText(getString(R.string.label_remark_limit_count, length));
        if (mFinishMenuItem == null) {
            invalidateOptionsMenu();
        }
        updateFinishMenuItemState(length != 0);
    }

    @Override
    public void onClick(View view) {
        String content = mContentEdit.getText().toString();

        StringBuilder sb = new StringBuilder();
        sb.append(content);
        if (!TextUtils.isEmpty(content)) {
            sb.append(" ");
        }
        sb.append((String) view.getTag());

        mContentEdit.setText(sb.toString());
        mContentEdit.setSelection(sb.length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_remark, menu);
        mFinishMenuItem = menu.findItem(R.id.menu_finish);
        updateFinishMenuItemState(!TextUtils.isEmpty(mContentEdit.getText().toString()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_finish) {
            String content = mContentEdit.getText().toString();
            Intent data = new Intent();
            data.putExtra(Display.PARAM_OBJ, content);
            setResult(RESULT_OK, data);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getRequestParameter() {
        return null;
    }
}
