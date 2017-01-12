package com.cheikh.lazywaimai.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import com.cheikh.lazywaimai.R;

/***
 * 双击退出
 */
public class DoubleExitUtil {

    private final Activity mActivity;

    private boolean isOnKeyBacking;
    private Handler mHandler;
    private Toast mBackToast;

    public DoubleExitUtil(Activity activity) {
        mActivity = activity;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Activity onKeyDown事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOnKeyBacking) {
                mHandler.removeCallbacks(onBackTimeRunnable);
                if (mBackToast != null) {
                    mBackToast.cancel();
                }
                return true;
            } else {
                isOnKeyBacking = true;
                if (mBackToast == null) {
                    mBackToast = Toast.makeText(mActivity, R.string.toast_hint_double_click_exit, Toast.LENGTH_LONG);
                }
                mBackToast.show();
                mHandler.postDelayed(onBackTimeRunnable, 2000);
                return false;
            }
        }
        return false;
    }

    private Runnable onBackTimeRunnable = new Runnable() {

        @Override
        public void run() {
            isOnKeyBacking = false;
            if (mBackToast != null) {
                mBackToast.cancel();
            }
        }
    };
}
