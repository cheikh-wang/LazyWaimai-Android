package com.cheikh.lazywaimai.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.cheikh.lazywaimai.context.AppConfig;

public final class ToastUtil {

    private static Toast sToast;

    private ToastUtil() {
    }

    public static void init(Context context) {
        if (sToast == null) {
            sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    public static void showToast(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public static void showToast(Object object) {
        show(object, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(@StringRes int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    public static void showLongToast(Object object) {
        show(object, Toast.LENGTH_LONG);
    }

    /**
     * 只有在debug模式才会显示出来
     *
     * @param object
     */
    public static void showDebugToast(Object object) {
        if (AppConfig.DEBUG) {
            show(object, Toast.LENGTH_LONG);
        }
    }

    private static void show(@StringRes int resId, int length) {
        check();
        if (resId > 0) {
            sToast.setText(resId);
            sToast.setDuration(length);
            sToast.show();
        }
    }

    private static void show(Object object, int length) {
        check();
        if (object != null) {
            sToast.setText(object.toString());
            sToast.setDuration(length);
            sToast.show();
        }
    }

    private static void check() {
        if (sToast == null) {
            throw new IllegalStateException("you must call ToastUtil.init(context) first");
        }
    }
}
