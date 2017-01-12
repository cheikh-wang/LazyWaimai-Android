package com.cheikh.lazywaimai.widget.section;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class SectionExtensionItemView extends SectionItemView {

    private View mChildView;

    public SectionExtensionItemView(Context context) {
        this(context, null);
    }

    public SectionExtensionItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionExtensionItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);

        if (isValidChildView(child)) {
            mChildView = child;
        }
    }

    private boolean isValidChildView(View view) {
        if (mChildView != null && mChildView != view) {
            return false;
        }

        return view != mItemView;
    }

    @Override
    protected void renderExtensionView() {
        if (mChildView == null) {
            throw new RuntimeException("must be have one child view");
        }

        ViewGroup parent = (ViewGroup) mChildView.getParent();
        parent.removeView(mChildView);
        mExtensionLayout.addView(mChildView);
    }
}
