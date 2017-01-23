package com.cheikh.lazywaimai.ui.adapter.wrapper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseAdapter;
import com.cheikh.lazywaimai.base.BaseWrapperAdapter;

/**
 * author: cheikh.wang on 16/12/27
 * email: wanghonghi@126.com
 * 修改至: http://blog.csdn.net/lmj623565791/article/details/51854533
 */
public class LoadMoreWrapperAdapter<T> extends BaseWrapperAdapter<T> {

    private int mLoadMoreLayoutId;
    private LoadMoreWrapperAdapter.LoadMoreHolder mLoadMoreHolder;
    private boolean mLoading;
    private boolean mEnableLoadMore;
    private LoadMoreWrapperAdapter.OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreWrapperAdapter(BaseAdapter<T> adapter) {
        super(adapter);
        mLoadMoreLayoutId = R.layout.layout_load_more_footer;
    }

    private boolean hasLoadMore() {
        return mLoadMoreLayoutId != 0;
    }

    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && position >= mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() != 0 ? mInnerAdapter.getItemCount() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return isShowLoadMore(position) ? ITEM_TYPE_LOAD_MORE : mInnerAdapter.getItemViewType(position);
    }

    @Override
    public int getViewLayoutId(int viewType) {
        return viewType == ITEM_TYPE_LOAD_MORE ? mLoadMoreLayoutId : mInnerAdapter.getViewLayoutId(viewType);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            return new LoadMoreHolder(view);
        } else {
            return mInnerAdapter.createViewHolder(view, viewType);
        }
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, T item, int position) {
        if (isShowLoadMore(position) && holder instanceof LoadMoreHolder) {
            resolveLoadMoreLogic((LoadMoreHolder) holder);
        } else {
            mInnerAdapter.bindViewHolder(holder, item, position);
        }
    }

    private void resolveLoadMoreLogic(LoadMoreWrapperAdapter.LoadMoreHolder holder) {
        mLoadMoreHolder = holder;
        if (mEnableLoadMore && !mLoading) {
            holder.loadingLayout.setVisibility(View.VISIBLE);
            startLoadMore();
        }
    }

    private void startLoadMore() {
        mLoading = true;
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void finishLoadMore() {
        if (mLoading) {
            mLoading = false;
            if (mLoadMoreHolder != null) {
                mLoadMoreHolder.loadingLayout.setVisibility(View.GONE);
            }
        }
    }

    public void setOnLoadMoreListener(LoadMoreWrapperAdapter.OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        mEnableLoadMore = enableLoadMore;
    }

    public static class LoadMoreHolder extends RecyclerView.ViewHolder {
        public View loadingLayout;
        public TextView loadingTxt;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            this.loadingLayout = itemView.findViewById(R.id.layout_loading);
            this.loadingTxt = (TextView)itemView.findViewById(R.id.txt_loading);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
