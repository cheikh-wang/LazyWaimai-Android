package com.cheikh.lazywaimai.widget.refresh;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

public class RefreshLayout extends FrameLayout {

    private static final float OFFSET_RADIO = 1.2f;

    private View mChildView;
    private View mScrollableView;
    private BaseRefreshHeaderView mHeaderView;

    private float mHeaderHeight;
    private float mTouchY;
    private int mTouchSlop;

    private boolean mIsOverlay;
    private int mMaxOffset;

    private boolean mIsRefreshing;

    private OnRefreshListener mOnRefreshListener;

    public RefreshLayout(Context context) {
        this(context, null, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHeaderView = new DefaultRefreshHeaderView(getContext());

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (getChildCount() > 1) {
            throw new RuntimeException("can only have one child view");
        }

        mChildView = getChildAt(0);
        if (mChildView == null) {
            throw new RuntimeException("must be have one child view");
        }

        if (isScrollableView(mChildView)) {
            mScrollableView = mChildView;
        } else {
            mScrollableView = findScrollableChildView(mChildView);
        }
        if (mScrollableView == null) {
            mScrollableView = mChildView;
        }

        initHeaderView();
    }

    /**
     * 初始化刷新的顶部视图
     */
    private void initHeaderView() {
        // 初始化配置
        HeaderConfig headerConfig = mHeaderView.getConfig();
        mIsOverlay = headerConfig.isOverlay;
        mMaxOffset = headerConfig.maxOffset;
        // 提前测量刷新视图的宽高
        measureView(mHeaderView);
        int height = mHeaderView.getMeasuredHeight();
        if (height > 0) {
            mHeaderHeight = height;
        } else {
            throw new RuntimeException("the height of the header view is 0!");
        }
        // 设置layout params
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) mHeaderHeight);
        layoutParams.gravity = Gravity.TOP;
        mHeaderView.setLayoutParams(layoutParams);

        ViewCompat.setTranslationY(mHeaderView, -mHeaderHeight);

        addView(mHeaderView);
    }

    /**
     * 测量视图的宽高
     * @param view
     */
    private void measureView(View view) {
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }

    /**
     * 是否是可滚动的View
     * @return
     */
    private boolean isScrollableView(View view) {
        return view instanceof AbsListView || view instanceof ScrollView
                || view instanceof RecyclerView;
    }

    /**
     * 查找可滚动的子View
     * @param root
     * @return
     */
    private View findScrollableChildView(View root) {
        if (root == null) {
            return null;
        }

        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            View view = null;
            for (int i = 0; i < group.getChildCount(); i++) {
                view = group.getChildAt(i);
                if (isScrollableView(view)) {
                    return view;
                } else {
                    view = findScrollableChildView(view);
                    if (view != null) {
                        return view;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = ev.getY() - mTouchY;
                if (!mIsRefreshing && dy > mTouchSlop && !canChildScrollUp()) {
                    boolean enablePull = false;
                    if (mOnRefreshListener != null) {
                        enablePull = mOnRefreshListener.enableRefresh();
                    }
                    if (enablePull) {
                        mHeaderView.onBegin();
                    }
                    return enablePull;
                }
                break;
            default:
                // nothing to do
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 能否继续往上滑动（是否到达顶部）
     */
    public boolean canChildScrollUp() {
        if (Build.VERSION.SDK_INT < 14) {
            if (mScrollableView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mScrollableView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mScrollableView, -1) || mScrollableView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mScrollableView, -1);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = e.getY() - mTouchY;
                dy = Math.max(0, dy);
                // 按照比例缩减滑动的有效距离
                float offsetY = dy / OFFSET_RADIO;
                offsetY = Math.min(offsetY, mMaxOffset);
                // 下拉刷新视图的可见度
                float fraction = offsetY / mHeaderHeight;
                fraction = fraction < 1 ? fraction : 1;

                // 逐渐显示下拉刷新的视图
                ViewCompat.setTranslationY(mHeaderView, (int) (offsetY - mHeaderHeight));

                // 如果不是覆盖模式,则将列表视图向下平移以留出空位来显示下拉刷新的视图
                if (!mIsOverlay) {
                    LayoutParams lp = (LayoutParams) mChildView.getLayoutParams();
                    lp.topMargin = (int) offsetY;
                    mChildView.requestLayout();
                }
                if (offsetY >= mHeaderHeight) {
                    mHeaderView.onRelease();
                } else {
                    mHeaderView.onPull(fraction);
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsOverlay) {
                    if (ViewCompat.getTranslationY(mHeaderView) >= 0) {
                        viewAnimateTranslationY(mHeaderView, 0);
                        startRefresh();
                    } else {
                        viewAnimateTranslationY(mHeaderView, -mHeaderHeight);
                    }
                } else {
                    if (ViewCompat.getTranslationY(mHeaderView) >= 0) {
                        viewAnimateTranslationY(mHeaderView, 0, mChildView);
                        startRefresh();
                    } else {
                        viewAnimateTranslationY(mHeaderView, -mHeaderHeight, mChildView);
                    }
                }
                return true;
            default:
                // nothing to do
                break;
        }

        return super.onTouchEvent(e);
    }

    /**
     * 使用动画的方式将视图在 Y 轴上平移
     * @param v
     * @param y
     */
    private void viewAnimateTranslationY(final View v, final float y) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(v);
        viewPropertyAnimatorCompat.setDuration(250);
        viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimatorCompat.translationY(y);
        viewPropertyAnimatorCompat.start();
    }

    /**
     * 使用动画的方式将视图在 Y 轴上平移
     * @param v
     * @param y
     */
    private void viewAnimateTranslationY(final View v, final float y, final View childView) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(v);
        viewPropertyAnimatorCompat.setDuration(250);
        viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimatorCompat.translationY(y);
        viewPropertyAnimatorCompat.start();
        viewPropertyAnimatorCompat.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(View view) {
                float translationY = ViewCompat.getTranslationY(v);
                LayoutParams lp = (LayoutParams) childView.getLayoutParams();
                lp.topMargin = (int) (mHeaderHeight + translationY);
                childView.requestLayout();
            }
        });
    }

    /**
     * 开始刷新
     */
    private void startRefresh() {
        mIsRefreshing = true;
        mHeaderView.onRefreshing();
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    /**
     * 是否正在刷新
     * @return
     */
    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    /**
     * 设置是否是刷新状态
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (mIsRefreshing == refreshing) {
            return;
        }
        mIsRefreshing = refreshing;
        if (refreshing) {
            if (mIsOverlay) {
                viewAnimateTranslationY(mHeaderView, 0);
            } else {
                viewAnimateTranslationY(mHeaderView, 0, mChildView);
            }
            mHeaderView.onRefreshing();
        } else {
            if (mIsOverlay) {
                viewAnimateTranslationY(mHeaderView, -mHeaderHeight);
            } else {
                viewAnimateTranslationY(mHeaderView, -mHeaderHeight, mChildView);
            }
            mHeaderView.onComplete();
        }
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        if (mIsRefreshing) {
            return;
        }
        if (mIsOverlay) {
            viewAnimateTranslationY(mHeaderView, 0);
        } else {
            viewAnimateTranslationY(mHeaderView, 0, mChildView);
        }
        startRefresh();
    }

    /**
     * 设置自定义的下拉刷新视图
     * @param headerView
     */
    public void setRefreshHeaderView(BaseRefreshHeaderView headerView) {
        mHeaderView = headerView;
    }

    /**
     * 获取下拉刷新视图
     */
    public BaseRefreshHeaderView getRefreshHeaderView() {
        return mHeaderView;
    }

    /**
     * 设置下拉刷新视图是否为覆盖模式
     * @param isOverlay
     */
    public void setIsOverlay(boolean isOverlay) {
        mIsOverlay = isOverlay;
    }

    /**
     * 设置刷新的监听器
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }
}
