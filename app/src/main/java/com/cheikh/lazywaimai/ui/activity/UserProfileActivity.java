package com.cheikh.lazywaimai.ui.activity;

import android.content.Intent;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseActivity;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.UserController;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.ContentView;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.activity_user_profile)
public class UserProfileActivity extends BaseActivity<UserController.UserUiCallbacks>
        implements UserController.UserUi {

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        if (!display.hasMainFragment()) {
            display.showUserProfileFragment();
        }
    }
}
