package com.cheikh.lazywaimai.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.cheikh.lazywaimai.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动换行的一个布局容器,可以设置每个childView的水平间距和垂直间距
 * author：cheikh.wang on 16/8/10 11:49
 * email：wanghonghi@126.com
 */
public class FixWrapLayout extends ViewGroup {

    private List<List<View>> mAllViews = new ArrayList<>();
    private List<Integer> mLineHeight = new ArrayList<>();

    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public FixWrapLayout(Context context) {
        this(context, null);
    }

    public FixWrapLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixWrapLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FixWrapLayout);
            mHorizontalSpacing = (int) array.getDimension(R.styleable.FixWrapLayout_horizontal_spacing, 0);
            mVerticalSpacing = (int) array.getDimension(R.styleable.FixWrapLayout_vertical_spacing, 0);
            array.recycle();
        }
    }

    /**
     * 设置水平间隔
     * @param horizontalSpacing
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
        invalidate();
    }

    /**
     * 获取水平间隔
     * @return
     */
    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    /**
     * 设置垂直间隔
     * @param verticalSpacing
     */
    public void setVerticalSpacing(int verticalSpacing) {
        mVerticalSpacing = verticalSpacing;
        invalidate();
    }

    /**
     * 获取垂直间隔
     * @return
     */
    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    /**
     * 获取view的宽度，包含margin
     * @param v
     * @return
     */
    private int getChildWidth(View v) {
        LayoutParams params = v.getLayoutParams();
        if (params instanceof MarginLayoutParams) {
            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            return lp.leftMargin + lp.rightMargin + v.getMeasuredWidth();
        } else {
            return v.getMeasuredWidth();
        }
    }

    /**
     * 获取view的高度，包含margin
     * @param v
     * @return
     */
    private int getChildHeight(View v) {
        LayoutParams params = v.getLayoutParams();
        if (params instanceof MarginLayoutParams) {
            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            return lp.topMargin + lp.bottomMargin + v.getMeasuredHeight();
        } else {
            return v.getMeasuredHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
            return;
        }

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        mAllViews.clear();
        mLineHeight.clear();
        List<View> lineViews = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int childWidth = getChildWidth(child);
            int childHeight = getChildHeight(child);

            int horizontalSpacing = lineWidth != 0 ? mHorizontalSpacing : 0;
            int verticalSpacing = lineHeight != 0 ? mVerticalSpacing : 0;

            if ((lineWidth + horizontalSpacing + childWidth) <= widthSize) {
                lineViews.add(child);

                lineWidth += horizontalSpacing + childWidth;
                lineHeight = Math.max(childHeight, lineHeight);
            } else {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineViews = new ArrayList<>();
                lineViews.add(child);

                width = Math.max(widthSize, lineWidth);
                height += lineHeight + verticalSpacing;
                lineWidth = childWidth;
                lineHeight = childHeight;
            }
            if (i + 1 == childCount) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
            }
        }

        width = widthMode == MeasureSpec.EXACTLY ? widthSize : width;
        height = heightMode == MeasureSpec.EXACTLY ? heightSize : height;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;

        int rowMaxHeight;
        List<View> rowViews;
        for (int row = 0; row < mAllViews.size(); row++) {
            rowViews = mAllViews.get(row);
            rowMaxHeight = mLineHeight.get(row);

            for (int col = 0; col < rowViews.size(); col++) {
                View child = rowViews.get(col);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                left += left != 0 ? mHorizontalSpacing : 0;

                int lc = left;
                int tc = top;

                LayoutParams params = child.getLayoutParams();
                if (params instanceof MarginLayoutParams) {
                    MarginLayoutParams lp = (MarginLayoutParams) child
                            .getLayoutParams();
                    //计算childView的left,top,right,bottom
                    lc = left + lp.leftMargin;
                    tc = top + lp.topMargin;
                }

                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += getChildWidth(child);
            }
            left = 0;
            top += rowMaxHeight + mVerticalSpacing;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
