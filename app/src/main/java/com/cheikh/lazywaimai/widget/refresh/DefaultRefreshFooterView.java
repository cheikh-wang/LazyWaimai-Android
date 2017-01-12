package com.cheikh.lazywaimai.widget.refresh;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 默认的底部加载更多view
 */
public class DefaultRefreshFooterView extends BaseRefreshFooterView {

    private TextView textView;
    private ProgressBar progressBar;

    public DefaultRefreshFooterView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LinearLayout container = new LinearLayout(context);
        container.setGravity(Gravity.CENTER);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                100));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(10, 10, 10, 10);

        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
        progressBar.setLayoutParams(layoutParams);
        container.addView(progressBar);

        textView = new TextView(context);
        textView.setTextColor(Color.parseColor("#9e9e9e"));
        textView.setLayoutParams(layoutParams);
        container.addView(textView);

        addView(container);
    }

    @Override
    public void onLoadMore() {
        textView.setText("加载中");
    }

    @Override
    public void onComplete() {
        // nothing to do
    }
}