package com.cheikh.lazywaimai.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShoppingCartAnimation {

    private Activity mActivity;
    private int[] mAnimationEndLocation;

    public ShoppingCartAnimation(Activity activity) {
        mActivity = activity;
        int height = DensityUtil.getScreenH(activity);
        mAnimationEndLocation = new int[] {50, height};
    }

    public void setAnimationEndLocation(int[] animationEndLocation) {
        mAnimationEndLocation = animationEndLocation;
    }

    /**
     * 启动加入到购物车的动画
     */
    public void startAnimation(View startView, final OnAnimationListener listener) {
        // 创建一个覆盖在界面上的动画图层并生成对话对象
        final View animView = createAnimView(startView);
        final ViewGroup animLayout = createAnimLayer();
        animLayout.addView(animView);
        // 设置动画对象的初始位置
        int[] animStartLoc = getAnimStartLoc(startView);
        setAnimStartLoc(animStartLoc, animView);
        //  根据起始位置和终点位置创建动画的轨迹
        int[] offset = getAnimOffset(animStartLoc, mAnimationEndLocation);
        final Animation animation = buildAnimation(offset);
        // 设置动画的执行监听器
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                if (listener != null) {
                    listener.onAnimationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 将动画图层移除
                final ViewGroup parent = (ViewGroup) animLayout.getParent();
                parent.post(new Runnable() {
                    @Override
                    public void run() {
                        parent.removeView(animLayout);
                        if (listener != null) {
                            listener.onAnimationEnd();
                        }
                    }
                });
            }
        });
        animView.startAnimation(animation);
    }

    /**
     * 创建动画层
     *
     * @return
     */
    private ViewGroup createAnimLayer() {
        final ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        LinearLayout ll = new LinearLayout(mActivity);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setBackgroundResource(android.R.color.transparent);
        decorView.addView(ll);
        return ll;
    }

    /**
     * 创建动画对象
     *
     * @param v
     * @return
     */
    private View createAnimView(View v) {
        v.buildDrawingCache();
        Bitmap animBitmap = v.getDrawingCache();
        ImageView ivAnim = new ImageView(mActivity);
        ivAnim.setImageBitmap(animBitmap);
        return ivAnim;
    }

    /**
     * 获得动画开始位置，即价格视图所在位置
     *
     * @param v
     * @return
     */
    private int[] getAnimStartLoc(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location;
    }

    /**
     * 设置动画起始位置
     *
     * @param startLoc
     * @param animView
     */
    private void setAnimStartLoc(int[] startLoc, View animView) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = startLoc[0];
        lp.topMargin = startLoc[1];
        animView.setLayoutParams(lp);
    }

    /**
     * 获得动画x，y坐标的偏移量
     *
     * @param startLoc
     * @param endLoc
     * @return
     */
    private int[] getAnimOffset(int[] startLoc, int[] endLoc) {
        int[] offset = new int[2];
        offset[0] = endLoc[0] - startLoc[0];
        offset[1] = endLoc[1] - startLoc[1];
        return offset;
    }

    /**
     * 根据偏移量创建动画
     *
     * @param offset
     * @return
     */
    private Animation buildAnimation(int[] offset) {
        AnimationSet as = new AnimationSet(false);
        TranslateAnimation translateX = new TranslateAnimation(0, offset[0], 0, 0);
        TranslateAnimation translateY = new TranslateAnimation(0, 0, 0, offset[1]);
        translateY.setInterpolator(new AccelerateInterpolator());
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0);
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        as.addAnimation(scaleAnimation);
        as.addAnimation(translateX);
        as.addAnimation(translateY);
        as.setDuration(500);
        return as;
    }

    public interface OnAnimationListener {
        void onAnimationStart();

        void onAnimationEnd();
    }
}
