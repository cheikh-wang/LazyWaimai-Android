package com.cheikh.lazywaimai.util;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * author：cheikh.wang on 2016/10/31 15:44
 * email：wanghonghi@126.com
 */
public abstract class OnRecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private GestureDetectorCompat mGestureDetector;

    @Override
    public boolean onInterceptTouchEvent(final RecyclerView recyclerView, MotionEvent e) {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(childView);
                        onItemClick(recyclerView, holder.itemView, holder.getAdapterPosition(),
                                holder.getItemId());
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null) {
                        RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(childView);
                        onItemLongClick(recyclerView, holder.itemView, holder.getAdapterPosition(),
                                holder.getItemId());
                    }
                }
            });
        }
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    public void onItemClick(RecyclerView recyclerView, View view, int position, long id) {}

    public void onItemLongClick(RecyclerView recyclerView, View view, int position, long id) {}
}
