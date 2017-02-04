package com.cheikh.lazywaimai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.util.ToastUtil;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.base.BaseTabActivity;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.BusinessController;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.ui.fragment.BusinessDetailFragment;
import com.cheikh.lazywaimai.ui.fragment.CommentFragment;
import com.cheikh.lazywaimai.ui.fragment.ProductFragment;
import com.cheikh.lazywaimai.util.StringFetcher;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
public class BusinessActivity extends BaseTabActivity<BusinessController.BusinessUiCallbacks>
        implements BusinessController.BusinessTabUi {

    private BusinessController.BusinessTab[] mTabs;
    private MenuItem mLikeMenuItem;

    private Business mBusiness;
    private boolean mIsLike;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getBusinessController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mBusiness = (Business) intent.getSerializableExtra(Display.PARAM_OBJ);
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        super.initializeViews(savedInstanceState);
        setTitle(mBusiness.getName());
    }

    @Override
    public Business getRequestParameter() {
        return mBusiness;
    }

    @Override
    protected String getTabTitle(int position) {
        if (mTabs != null) {
            return StringFetcher.getString(mTabs[position]);
        }
        return null;
    }

    @Override
    public void onFavoriteFinish(boolean isLike) {
        cancelLoading();
        updateLikeMenuIcon(isLike);
        ToastUtil.showToast(isLike ? R.string.toast_success_favorite_business
                : R.string.toast_success_cancel_favorite_business);
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        ToastUtil.showToast(error.getMessage());
    }

    @Override
    public void setTabs(BusinessController.BusinessTab... tabs) {
        Preconditions.checkNotNull(tabs, "tabs cannot be null");
        mTabs = tabs;

        if (getAdapter().getCount() != tabs.length) {
            ArrayList<Fragment> fragments = new ArrayList<>();
            for (BusinessController.BusinessTab tab : tabs) {
                fragments.add(createFragmentForTab(tab));
            }
            setFragments(fragments);
        }
    }

    private Fragment createFragmentForTab(BusinessController.BusinessTab tab) {
        switch (tab) {
            case PRODUCT:
                return ProductFragment.create(mBusiness);
            case COMMENT:
                return CommentFragment.create(mBusiness);
            case DETAIL:
                return BusinessDetailFragment.create(mBusiness);
        }
        return null;
    }

    private void updateLikeMenuIcon(boolean isLike) {
        mIsLike = isLike;
        mLikeMenuItem.setIcon(isLike ? R.drawable.ic_liked : R.drawable.ic_like);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_business, menu);
        mLikeMenuItem = menu.findItem(R.id.menu_like);
        updateLikeMenuIcon(mBusiness.isLike());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_call:
                getCallbacks().callBusinessPhone(mBusiness);
                return true;
            case R.id.menu_like:
                showLoading(R.string.label_being_something);
                getCallbacks().favoriteBusiness(mBusiness, !mIsLike);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
