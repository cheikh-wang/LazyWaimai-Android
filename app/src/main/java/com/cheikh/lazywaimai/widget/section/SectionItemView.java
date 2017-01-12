package com.cheikh.lazywaimai.widget.section;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.util.DensityUtil;

public abstract class SectionItemView extends FrameLayout {

    protected View mItemView;
    protected TextView mTitleTxt;
    protected ImageView mIndicatorImg;
    protected FrameLayout mExtensionLayout;

    protected CharSequence mTitle;
    protected Drawable mIcon;
    protected boolean mIndicator;

    public SectionItemView(Context context) {
        this(context, null);
    }

    public SectionItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mItemView = LayoutInflater.from(context).inflate(R.layout.view_section_item, null, false);
        addView(mItemView);

        mTitleTxt = (TextView) mItemView.findViewById(R.id.txt_name);
        mIndicatorImg = (ImageView) mItemView.findViewById(R.id.img_indicator);
        mExtensionLayout = (FrameLayout) mItemView.findViewById(R.id.layout_extension);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SectionItemView);
        mTitle = array.getText(R.styleable.SectionItemView_siv_name);
        mIcon = array.getDrawable(R.styleable.SectionItemView_siv_icon);
        mIndicator = array.getBoolean(R.styleable.SectionItemView_siv_indicator, false);
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleTxt.setText(mTitle);
        if (mIcon != null) {
            renderIconView(mIcon);
        }
        if (mIndicator) {
            renderIndicator();
        }
        renderExtensionView();
    }

    protected void renderIconView(Drawable icon) {
        icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
        mTitleTxt.setCompoundDrawables(icon, null, null, null);
        mTitleTxt.setCompoundDrawablePadding(DensityUtil.dip2px(getContext(), 15));
    }

    protected void renderIndicator() {
        mIndicatorImg.setVisibility(View.VISIBLE);
    }

    protected abstract void renderExtensionView();

    public String getTitle() {
        return (String) mTitleTxt.getText();
    }

    public void setTitle(String title) {
        mTitleTxt.setText(title);
    }
}