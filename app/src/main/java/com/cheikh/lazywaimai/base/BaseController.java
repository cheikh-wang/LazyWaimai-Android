package com.cheikh.lazywaimai.base;

import android.util.Log;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import com.cheikh.lazywaimai.context.AppConfig;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.ui.Display;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
public abstract class BaseController<U extends BaseController.Ui<UC>, UC> {

    private final Set<U> mUis;
    private final Set<U> mUnmodifiableUis;

    private Display mDisplay;
    private boolean mInited;

    public BaseController() {
        mUis = new CopyOnWriteArraySet<>();
        mUnmodifiableUis = Collections.unmodifiableSet(mUis);
    }

    public final void init() {
        Preconditions.checkState(!mInited, "Already inited");
        onInited();
        mInited = true;
    }

    public final void suspend() {
        Preconditions.checkState(mInited, "Not inited");
        onSuspended();
        mInited = false;
    }

    protected void onInited() {}

    protected void onSuspended() {}

    public final boolean isInited() {
        return mInited;
    }

    protected abstract UC createUiCallbacks(U ui);

    public synchronized final void attachUi(U ui) {
        Preconditions.checkArgument(ui != null, "ui cannot be null");
        Preconditions.checkState(!mUis.contains(ui), "UI is already attached");
        mUis.add(ui);
        ui.setCallbacks(createUiCallbacks(ui));
    }

    public synchronized final void startUi(U ui) {
        Preconditions.checkArgument(ui != null, "ui cannot be null");
        Preconditions.checkState(mUis.contains(ui), "ui is not attached");
        populateUi(ui);
    }

    public synchronized final void detachUi(U ui) {
        Preconditions.checkArgument(ui != null, "ui cannot be null");
        Preconditions.checkState(mUis.contains(ui), "ui is not attached");
        ui.setCallbacks(null);
        mUis.remove(ui);
    }

    protected synchronized void populateUi(U ui) {}

    protected synchronized final void populateUis() {
        if (AppConfig.DEBUG) {
            Log.d(getClass().getSimpleName(), "populateUis");
        }
        for (U ui : mUis) {
            populateUi(ui);
        }
    }

    protected final Set<U> getUis() {
        return mUnmodifiableUis;
    }

    protected int getId(U ui) {
        return ui.hashCode();
    }

    protected synchronized U findUi(final int id) {
        for (U ui : mUis) {
            if (getId(ui) == id) {
                return ui;
            }
        }
        return null;
    }

    public void setDisplay(Display display) {
        mDisplay = display;
    }

    public final Display getDisplay() {
        return mDisplay;
    }

    public interface Ui<UC> {
        void setCallbacks(UC callbacks);

        UC getCallbacks();

        void onResponseError(ResponseError error);
    }

    public interface ListUi<T, UC> extends Ui<UC> {
        void onStartRequest(int page);

        void onFinishRequest(List<T> items, int page, boolean haveNextPage);
    }

    public interface SubUi {}
}
