package com.cheikh.lazywaimai.widget.refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.cheikh.lazywaimai.R;

/**
 * 默认的头部下拉刷新view
 */
public class DefaultRefreshHeaderView extends BaseRefreshHeaderView {

    @Bind(R.id.pull_sun)
    ImageView sunImg;

    @Bind(R.id.pull_rider)
    ImageView riderImg;

    @Bind(R.id.pull_wheel_left)
    ImageView leftWheelImg;

    @Bind(R.id.pull_wheel_right)
    ImageView rightWheelImg;

    @Bind(R.id.pull_backImg_left)
    ImageView leftBackImg;

    @Bind(R.id.pull_backImg_right)
    ImageView rightBackImg;

    private Animation sunRotation;
    private Animation riderShake;
    private Animation wheelRotation;
    private Animation leftBackTranslate;
    private Animation rightBackTranslate;

    public DefaultRefreshHeaderView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_refresh_header, this);
        initAnimations();
    }

    private void initAnimations() {
        sunRotation = new RotateAnimation(0, 179,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sunRotation.setInterpolator(new LinearInterpolator());
        sunRotation.setRepeatCount(Animation.INFINITE);
        sunRotation.setDuration(600);

        riderShake = new TranslateAnimation(0, 0, 0, 5);
        riderShake.setRepeatMode(Animation.REVERSE);
        riderShake.setRepeatCount(Animation.INFINITE);
        riderShake.setInterpolator(new DecelerateInterpolator());
        riderShake.setDuration(225);

        wheelRotation = new RotateAnimation(0, 359,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        wheelRotation.setInterpolator(new LinearInterpolator());
        wheelRotation.setRepeatCount(Animation.INFINITE);
        wheelRotation.setDuration(300);

        leftBackTranslate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        leftBackTranslate.setRepeatMode(Animation.RESTART);
        leftBackTranslate.setRepeatCount(Animation.INFINITE);
        leftBackTranslate.setInterpolator(new LinearInterpolator());
        leftBackTranslate.setDuration(2000);

        rightBackTranslate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        rightBackTranslate.setRepeatMode(Animation.RESTART);
        rightBackTranslate.setRepeatCount(Animation.INFINITE);
        rightBackTranslate.setInterpolator(new LinearInterpolator());
        rightBackTranslate.setDuration(2000);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.bind(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }

    @Override
    public HeaderConfig getConfig() {
        HeaderConfig config = new HeaderConfig();
        config.isOverlay = false;
        config.maxOffset = 300;
        return config;
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onPull(float fraction) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onRefreshing() {
        sunImg.startAnimation(sunRotation);
        riderImg.startAnimation(riderShake);
        leftWheelImg.startAnimation(wheelRotation);
        rightWheelImg.startAnimation(wheelRotation);
        leftBackImg.startAnimation(leftBackTranslate);
        rightBackImg.startAnimation(rightBackTranslate);
    }

    @Override
    public void onComplete() {
        sunImg.clearAnimation();
        riderImg.clearAnimation();
        leftWheelImg.clearAnimation();
        rightWheelImg.clearAnimation();
        leftBackImg.clearAnimation();
        rightBackImg.clearAnimation();
    }
}