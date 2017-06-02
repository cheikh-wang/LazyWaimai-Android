package com.cheikh.lazywaimai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.util.CountDownTimer;

public class CountDownTimerView extends Button {

    private CountDownTimer countDownTimer;
    private OnCountDownListener mListener;

    public CountDownTimerView(Context context) {
        super(context);
    }

    public CountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void startCountDown(long millisInFuture) {
        setEnabled(false);
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer() {
                @Override
                protected void onTick(long millisLeft) {
                    int secondsLeft = (int) Math.ceil((double) millisLeft / 1000.0);
                    setText(getResources().getString(R.string.btn_send_code_again, secondsLeft));
                }

                @Override
                protected void onFinish() {
                    setText(R.string.btn_again_send_code);
                    if (mListener != null) {
                        setEnabled(mListener.onCountDownFinishState());
                    } else {
                        setEnabled(true);
                    }
                }
            };
        }
        countDownTimer.start(millisInFuture, 1000);
    }

    public void cancelCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        mListener = null;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && countDownTimer != null && countDownTimer.isCountingDown()) {
            return;
        }
        super.setEnabled(enabled);
    }

    public void setOnCountDownListener(OnCountDownListener listener) {
        mListener = listener;
    }

    public interface OnCountDownListener {
        boolean onCountDownFinishState();
    }
}