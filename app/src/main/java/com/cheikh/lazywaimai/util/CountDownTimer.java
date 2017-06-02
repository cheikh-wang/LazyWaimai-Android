package com.cheikh.lazywaimai.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountDownTimer {

    private static final int MSG = 1;

    private long mCountdownInterval;
    private long mStopTimeInFuture;

    private boolean mIsCountingDown;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized(this) {
                long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();
                if (millisLeft <= 0) {
                    mIsCountingDown = false;
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    onTick(millisLeft);
                    sendEmptyMessageDelayed(MSG, mCountdownInterval);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    // onTick回调可能会有延迟
                    onTick(millisLeft);
                    long delay = (mCountdownInterval + lastTickStart) - SystemClock.elapsedRealtime();
                    while (delay < 0) {
                        delay += mCountdownInterval;
                    }
                    sendEmptyMessageDelayed(MSG, delay);
                }
            }
        }
    };

    protected abstract void onTick(long millisLeft);

    protected abstract void onFinish();

    /**
     * 是否正在倒计时
     * @return
     */
    public boolean isCountingDown() {
        return mIsCountingDown;
    }

    /**
     * 取消倒计时
     */
    public final void cancel() {
        mHandler.removeMessages(MSG);
        mIsCountingDown = false;
    }

    /**
     * 开始倒计时
     * @param millisInFuture
     * @param countDownInterval
     * @return
     */
    public final synchronized CountDownTimer start(long millisInFuture, long countDownInterval) {
        mCountdownInterval = countDownInterval;
        mIsCountingDown = true;
        if (millisInFuture <= 0) {
            onFinish();
        } else {
            mStopTimeInFuture = SystemClock.elapsedRealtime() + millisInFuture;
            mHandler.sendEmptyMessage(MSG);
        }
        return this;
    }
}