package com.cheikh.lazywaimai.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.MainController;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.ActivityStack;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
public abstract class CoreActivity<UC> extends AppCompatActivity
        implements BaseController.Ui<UC> {

    private MainController mMainController;
    private Display mDisplay;
    private UC mCallbacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mDisplay = new Display(this);
        mMainController = AppContext.getContext().getMainController();

        getController().attachUi(this);
        ActivityStack.create().add(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainController.init();
        mMainController.attachDisplay(mDisplay);
        getController().startUi(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainController.suspend();
        mMainController.detachDisplay(mDisplay);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getController().detachUi(this);
        ActivityStack.create().remove(this);
        AppContext.getContext().getRefWatcher().watch(this);
    }

    protected abstract int getLayoutId();
    protected abstract BaseController getController();

    @Override
    public void setCallbacks(UC callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public UC getCallbacks() {
        return mCallbacks;
    }

    @Override
    public void onResponseError(ResponseError error) {}

    public final Display getDisplay() {
        return mDisplay;
    }

    protected final MainController getMainController() {
        return mMainController;
    }

    @Override
    public void onBackPressed() {
        getMainController().onBackButtonPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getMainController().onBackButtonPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
