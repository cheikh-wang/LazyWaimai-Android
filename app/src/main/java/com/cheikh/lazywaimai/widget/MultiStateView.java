package com.cheikh.lazywaimai.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import com.cheikh.lazywaimai.R;

/**
 * 一个提供多种状态切换显示的 layout 组件
 * author：cheikh.wang on 16/7/12 11:34
 * email：wanghonghi@126.com
 * 修改自:https://github.com/Kennyc1012/MultiStateView
 */
public class MultiStateView extends FrameLayout {

    public static final int STATE_CONTENT = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_UNAUTH = 4;

    public static final int DEFAULT_STATE = STATE_LOADING;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_CONTENT,
            STATE_LOADING,
            STATE_ERROR,
            STATE_EMPTY,
            STATE_UNAUTH
    })
    public @interface State {
    }

    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mUnauthView;

    private LayoutInflater mInflater;

    @State
    private int mState;


    public MultiStateView(Context context) {
        this(context, null);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateView);
        // 加载中视图布局
        int loadingViewResId = a.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1);
        if (loadingViewResId > -1) {
            mLoadingView = mInflater.inflate(loadingViewResId, this, false);
            addView(mLoadingView);
        }
        // 错误视图布局
        int errorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_errorView, -1);
        if (errorViewResId > -1) {
            mErrorView = mInflater.inflate(errorViewResId, this, false);
            addView(mErrorView);
        }
        // 空视图布局
        int emptyViewResId = a.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1);
        if (emptyViewResId > -1) {
            mEmptyView = mInflater.inflate(emptyViewResId, this, false);
            addView(mEmptyView);
        }
        // 未登录视图布局
        int unauthViewResId = a.getResourceId(R.styleable.MultiStateView_msv_unauthView, -1);
        if (unauthViewResId > -1) {
            mUnauthView = mInflater.inflate(unauthViewResId, this, false);
            addView(mUnauthView);
        }
        // 默认状态.由于mViewState变量使用了注解限制,所以不能直接赋值
        int viewState = a.getInt(R.styleable.MultiStateView_msv_viewState, DEFAULT_STATE);
        switch (viewState) {
            case STATE_CONTENT:
                mState = STATE_CONTENT;
                break;
            case STATE_LOADING:
                mState = STATE_LOADING;
                break;
            case STATE_ERROR:
                mState = STATE_ERROR;
                break;
            case STATE_EMPTY:
                mState = STATE_EMPTY;
                break;
            default:
                // nothing to do
                break;
        }

        a.recycle();

        initDefaultViews();
    }

    /**
     * 初始化各个状态对应的默认视图
     */
    private void initDefaultViews() {
        if (mLoadingView == null) {
            mLoadingView = mInflater.inflate(R.layout.layout_loading_hint, this, false);
            addView(mLoadingView);
        }
        if (mErrorView == null) {
            mErrorView = mInflater.inflate(R.layout.layout_error_hint, this, false);
            addView(mErrorView);
        }
        if (mEmptyView == null) {
            mEmptyView = mInflater.inflate(R.layout.layout_empty_hint, this, false);
            addView(mEmptyView);
        }
        if (mUnauthView == null) {
            mUnauthView = mInflater.inflate(R.layout.layout_unauth_hint, this, false);
            addView(mUnauthView);
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (isValidContentView(child)) {
            mContentView = child;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) throw new IllegalArgumentException("Content view is not defined");
        setView();
    }

    /**
     * 检测是否是Content View
     * @param view
     * @return
     */
    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }

        return view != mLoadingView && view != mErrorView
                && view != mEmptyView && view != mUnauthView;
    }

    @Nullable
    public View getView(@State int state) {
        switch (state) {
            case STATE_CONTENT:
                return mContentView;
            case STATE_LOADING:
                return mLoadingView;
            case STATE_ERROR:
                return mErrorView;
            case STATE_EMPTY:
                return mEmptyView;
            default:
                return null;
        }
    }

    /**
     * 切换显示的状态视图的逻辑
     */
    private void setView() {
        switch (mState) {
            case STATE_CONTENT:
                mContentView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mUnauthView.setVisibility(View.GONE);
                break;
            case STATE_LOADING:
                mContentView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mUnauthView.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                mContentView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mUnauthView.setVisibility(View.GONE);
                break;
            case STATE_EMPTY:
                mContentView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                mUnauthView.setVisibility(View.GONE);
                break;
            case STATE_UNAUTH:
                mContentView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mUnauthView.setVisibility(View.VISIBLE);
                break;
            default:
                // nothing to do
                break;
        }
    }


    ///////////////////////////////////////////////
    ////          以下是供外部操作的公有方法        ////
    ///////////////////////////////////////////////
    /**
     * 设置指定状态的视图
     * @param view
     * @param state
     * @param switchToState
     */
    public MultiStateView setViewForState(View view, @State int state, boolean switchToState) {
        switch (state) {
            case STATE_CONTENT:
                if (mContentView != null) {
                    removeView(mContentView);
                }
                mContentView = view;
                addView(mContentView);
                break;
            case STATE_LOADING:
                if (mLoadingView != null) {
                    removeView(mLoadingView);
                }
                mLoadingView = view;
                addView(mLoadingView);
                break;
            case STATE_ERROR:
                if (mErrorView != null) {
                    removeView(mErrorView);
                }
                mErrorView = view;
                addView(mErrorView);
                break;
            case STATE_EMPTY:
                if (mEmptyView != null) {
                    removeView(mEmptyView);
                }
                mEmptyView = view;
                addView(mEmptyView);
                break;
            case STATE_UNAUTH:
                if (mUnauthView != null) {
                    removeView(mUnauthView);
                }
                mUnauthView = view;
                addView(mUnauthView);
                break;
            default:
                // nothing to do
                break;
        }
        setView();
        // 是否立即切换状态
        if (switchToState) {
            setState(state);
        }

        return this;
    }

    /**
     * 设置指定状态的视图
     * @param view
     * @param state
     */
    public MultiStateView setViewForState(View view, @State int state) {
        return setViewForState(view, state, false);
    }

    /**
     * 设置指定状态的视图
     * @param layoutRes
     * @param state
     * @param switchToState
     */
    public MultiStateView setViewForState(@LayoutRes int layoutRes, @State int state, boolean switchToState) {
        View view = mInflater.inflate(layoutRes, this, false);
        return setViewForState(view, state, switchToState);
    }

    /**
     * 设置指定状态的视图
     * @param layoutRes
     * @param state
     */
    public MultiStateView setViewForState(@LayoutRes int layoutRes, @State int state) {
        return setViewForState(layoutRes, state, false);
    }

    /**
     * 获取当前的状态
     * @return
     */
    @State
    public int getState() {
        return mState;
    }

    /**
     * 切换显示的状态
     * @param state
     * @return
     */
    public MultiStateView setState(@State int state) {
        if (state != mState) {
            mState = state;
            setView();
        }

        return this;
    }

    /**
     * 设置当前状态视图的图标
     * @param iconResId
     * @return
     */
    public MultiStateView setIcon(@DrawableRes int iconResId) {
        View view = getView(mState);
        if (view != null) {
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            if (icon != null) {
                icon.setImageResource(iconResId);
            } else {
                throw new IllegalArgumentException("view must have an id of named icon");
            }
        }

        return this;
    }

    /**
     * 设置当前状态视图的标题
     * @param titleResId
     * @return
     */
    public MultiStateView setTitle(@StringRes int titleResId) {
        return setTitle(getContext().getString(titleResId));
    }

    /**
     * 设置当前状态视图的标题
     * @param title
     * @return
     */
    public MultiStateView setTitle(String title) {
        View view = getView(mState);
        if (view != null) {
            TextView titleTxt = (TextView) view.findViewById(R.id.title);
            if (titleTxt != null) {
                titleTxt.setVisibility(View.VISIBLE);
                titleTxt.setText(title);
            } else {
                throw new IllegalArgumentException(view.getClass().getSimpleName() + " must have an id of named title");
            }
        }

        return this;
    }

    /**
     * 设置当前状态视图的子标题
     * @param subtitleResId
     * @return
     */
    public MultiStateView setSubtitle(@StringRes int subtitleResId) {
        return setSubtitle(getContext().getString(subtitleResId));
    }

    /**
     * 设置当前状态视图的子标题
     * @param subtitle
     * @return
     */
    public MultiStateView setSubtitle(String subtitle) {
        View view = getView(mState);
        if (view != null) {
            TextView titleTxt = (TextView) view.findViewById(R.id.subtitle);
            if (titleTxt != null) {
                titleTxt.setVisibility(View.VISIBLE);
                titleTxt.setText(subtitle);
            } else {
                throw new IllegalArgumentException(view.getClass().getSimpleName() + " must have an id of named subtitle");
            }
        }

        return this;
    }

    /**
     * 设置当前状态视图的按钮点击事件
     * @param listener
     * @return
     */
    public MultiStateView setButton(OnClickListener listener) {
        return setButton(null, listener);
    }

    /**
     * 设置当前状态视图的按钮文字和点击事件
     * @param textResId
     * @param listener
     * @return
     */
    public MultiStateView setButton(@StringRes int textResId, OnClickListener listener) {
        return setButton(getContext().getString(textResId), listener);
    }

    /**
     * 设置当前状态视图的按钮文字和点击事件
     * @param text
     * @param listener
     * @return
     */
    public MultiStateView setButton(String text, OnClickListener listener) {
        View view = getView(mState);
        if (view != null) {
            View button = view.findViewById(R.id.button);
            // 这个button只能是TextView或者Button控件
            if (button != null && (button instanceof Button || button instanceof TextView)) {
                button.setVisibility(VISIBLE);
                if (text != null && !text.equals("")) {
                    if (button instanceof Button) {
                        ((Button) button).setText(text);
                    } else {
                        ((TextView) button).setText(text);
                    }
                }
                if (listener != null) {
                    button.setOnClickListener(listener);
                }
            } else {
                throw new IllegalArgumentException(view.getClass().getSimpleName() + " view must have an id of named button");
            }
        }

        return this;
    }
}