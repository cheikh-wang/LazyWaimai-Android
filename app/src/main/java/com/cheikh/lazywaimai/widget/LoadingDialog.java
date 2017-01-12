package com.cheikh.lazywaimai.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cheikh.lazywaimai.R;

/**
 * author：cheikh on 16/5/14 14:32
 * email：wanghonghi@126.com
 */
public class LoadingDialog extends Dialog {

    private TextView titleTxt;

    public LoadingDialog(Context context) {
        super(context, R.style.Widget_LazyOrder_WaitDialog);
        initDialog();
    }

    private void initDialog() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_wait_dialog, null);
        titleTxt = (TextView) view.findViewById(R.id.title);
        setContentView(view);
    }

    @Override
    public void setTitle(CharSequence title) {
        titleTxt.setVisibility(title != null ? View.VISIBLE : View.GONE);
        titleTxt.setText(title);
    }

    @Override
    public void setTitle(@StringRes int titleResId) {
        titleTxt.setVisibility(View.VISIBLE);
        titleTxt.setText(titleResId);
    }
}
