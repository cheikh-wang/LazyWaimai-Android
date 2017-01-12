package com.cheikh.lazywaimai.widget.section;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheikh.lazywaimai.R;

public class SectionTextItemView extends SectionItemView {

    private TextView mSubtitleTxt;

    public SectionTextItemView(Context context) {
        this(context, null);
    }

    public SectionTextItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionTextItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void renderExtensionView() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mSubtitleTxt = new TextView(getContext());
        mSubtitleTxt.setTextColor(getResources().getColor(R.color.secondary_text));
        mExtensionLayout.addView(mSubtitleTxt, lp);
    }

    public void setSubtitle(String subtitle) {
        mSubtitleTxt.setText(subtitle);
    }

    public String getSubtitle() {
        return (String) mSubtitleTxt.getText();
    }
}
