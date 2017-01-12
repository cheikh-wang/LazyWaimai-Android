package com.cheikh.lazywaimai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.util.CountDownTimer;

public class CountDownTimerView extends Button {

    private CountDownTimer countDownTimer;
    private OnCountDownListener onCountDownListener;

    public CountDownTimerView(Context context) {
        super(context);
    }

    public CountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void countDown(long millisInFuture) {
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
                    if (onCountDownListener != null) {
                        setEnabled(onCountDownListener.onCountDownFinishState());
                    } else {
                        setEnabled(true);
                    }
                }
            };
        }
        countDownTimer.start(millisInFuture, 1000);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && countDownTimer != null && countDownTimer.isCountingDown()) {
            return;
        }
        super.setEnabled(enabled);
    }

    public void setOnCountDownListener(OnCountDownListener listener) {
        onCountDownListener = listener;
    }

    public interface OnCountDownListener {
        boolean onCountDownFinishState();
    }
}