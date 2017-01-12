package com.cheikh.lazywaimai.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountDownTimer {

    private static final int MSG = 1;

    private long mCountdownInterval;
    private boolean mIsCountingDown;
    private long mMillisInFuture;
    private long mStopTimeInFuture;
    private Handler mHandler = new CountDownHandler();

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
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
        mIsCountingDown = true;
        if (mMillisInFuture <= 0) {
            onFinish();
        } else {
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
            mHandler.sendEmptyMessage(MSG);
        }
        return this;
    }

    public void reset(long millisInFuture) {
        if (mMillisInFuture <= 0) {
            cancel();
            onFinish();
        }
        mMillisInFuture = millisInFuture;
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
    }

    private class CountDownHandler extends Handler {

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
                    onTick(millisLeft);
                    long delay = (mCountdownInterval + lastTickStart) - SystemClock.elapsedRealtime();
                    while (delay < 0) {
                        delay += mCountdownInterval;
                    }
                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    }
}