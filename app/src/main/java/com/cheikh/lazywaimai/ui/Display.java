package com.cheikh.lazywaimai.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.cheikh.lazywaimai.model.bean.Order;
import com.cheikh.lazywaimai.ui.activity.FavoritesActivity;
import com.cheikh.lazywaimai.ui.activity.FeedbackActivity;
import com.cheikh.lazywaimai.ui.activity.RemarkActivity;
import com.cheikh.lazywaimai.ui.fragment.SetNicknameFragment;
import com.cheikh.lazywaimai.ui.fragment.SetUsernameFragment;
import com.cheikh.lazywaimai.ui.fragment.UserProfileFragment;
import com.google.common.base.Preconditions;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.model.bean.Address;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.ui.activity.AddressesActivity;
import com.cheikh.lazywaimai.ui.activity.BusinessActivity;
import com.cheikh.lazywaimai.ui.activity.LoginActivity;
import com.cheikh.lazywaimai.ui.activity.MainActivity;
import com.cheikh.lazywaimai.ui.activity.OrderDetailActivity;
import com.cheikh.lazywaimai.ui.activity.PaymentActivity;
import com.cheikh.lazywaimai.ui.activity.RegisterActivity;
import com.cheikh.lazywaimai.ui.activity.SettingActivity;
import com.cheikh.lazywaimai.ui.activity.SettleActivity;
import com.cheikh.lazywaimai.ui.activity.UpdateAddressActivity;
import com.cheikh.lazywaimai.ui.activity.UserProfileActivity;
import com.cheikh.lazywaimai.ui.fragment.RegisterFirstStepFragment;
import com.cheikh.lazywaimai.ui.fragment.RegisterSecondStepFragment;
import com.cheikh.lazywaimai.ui.fragment.RegisterThirdStepFragment;
import com.cheikh.lazywaimai.util.MainTab;
import com.cheikh.lazywaimai.util.RegisterStep;

public class Display {

    public static final String PARAM_ID = "_id";
    public static final String PARAM_OBJ = "_obj";

    private final AppCompatActivity mActivity;

    public Display(AppCompatActivity activity) {
        mActivity = Preconditions.checkNotNull(activity, "activity cannot be null");
    }

    /**
     * 设置app bar上的主标题
     * @param title
     */
    public void setActionBarTitle(CharSequence title) {
        ActionBar ab = mActivity.getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }
    }

    /**
     * 设置app bar上的子标题
     * @param title
     */
    public void setActionBarSubtitle(CharSequence title) {
        ActionBar ab = mActivity.getSupportActionBar();
        if (ab != null) {
            ab.setSubtitle(title);
        }
    }

    /**
     * 显示向上导航的按钮
     * @param isShow
     */
    public void showUpNavigation(boolean isShow) {
        final ActionBar ab = mActivity.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(isShow);
            ab.setHomeButtonEnabled(isShow);
            if (isShow) {
                ab.setHomeAsUpIndicator(R.drawable.ic_back);
            }
        }
    }

    /**
     * 判断当前activity是否已经嵌套了fragment
     * @return
     */
    public boolean hasMainFragment() {
        return getBackStackFragmentCount() > 0;
    }

    /**
     * 获取回退栈里fragment的数量
     * @return
     */
    public int getBackStackFragmentCount() {
        return mActivity.getSupportFragmentManager().getBackStackEntryCount();
    }

    /**
     * 弹出回退栈里最顶上的fragment
     * 如果栈里只有一个fragment的话,则在弹出的同时并结束掉当前的activity
     * @return
     */
    public boolean popTopFragmentBackStack() {
        final FragmentManager fm = mActivity.getSupportFragmentManager();
        final int backStackCount = fm.getBackStackEntryCount();
        if (backStackCount > 1) {
            fm.popBackStack();
            return true;
        }

        return false;
    }

    /**
     * 弹出回退栈里的所有fragment
     * @return
     */
    public boolean popEntireFragmentBackStack() {
        final FragmentManager fm = mActivity.getSupportFragmentManager();
        final int backStackCount = fm.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            fm.popBackStack();
        }
        return backStackCount > 0;
    }

    /**
     * 回退
     * @return
     */
    public void navigateUp() {
        final Intent intent = NavUtils.getParentActivityIntent(mActivity);
        if (intent != null) {
            NavUtils.navigateUpTo(mActivity, intent);
        } else {
            finishActivity();
        }
    }

    public void finishActivity() {
        mActivity.finish();
    }

    /**
     * 拨打电话
     * @param phone
     */
    public void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        mActivity.startActivity(intent);
    }

    public void showMain(MainTab mainTab) {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra(PARAM_OBJ, mainTab);
        mActivity.startActivity(intent);
    }

    public void showBusiness(Business business) {
        Intent intent = new Intent(mActivity, BusinessActivity.class);
        intent.putExtra(PARAM_OBJ, business);
        mActivity.startActivity(intent);
    }

    public void showLogin() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        mActivity.startActivity(intent);
    }

    public void showRegister() {
        Intent intent = new Intent(mActivity, RegisterActivity.class);
        mActivity.startActivity(intent);
    }

    public void showUserProfile() {
        Intent intent = new Intent(mActivity, UserProfileActivity.class);
        mActivity.startActivity(intent);
    }

    public void showRemark(String remark, int requestCode) {
        Intent intent = new Intent(mActivity, RemarkActivity.class);
        intent.putExtra(PARAM_OBJ, remark);
        mActivity.startActivityForResult(intent, requestCode);
    }

    public void showPayment(Order order) {
        Intent intent = new Intent(mActivity, PaymentActivity.class);
        intent.putExtra(PARAM_OBJ, order);
        mActivity.startActivity(intent);
    }

    public void showOrderDetail(Order order) {
        Intent intent = new Intent(mActivity, OrderDetailActivity.class);
        intent.putExtra(PARAM_ID, order.getId());
        mActivity.startActivity(intent);
    }

    public void showAddresses() {
        Intent intent = new Intent(mActivity, AddressesActivity.class);
        mActivity.startActivity(intent);
    }

    public void showFavorites() {
        Intent intent = new Intent(mActivity, FavoritesActivity.class);
        mActivity.startActivity(intent);
    }

    public void showChooseAddress() {
        Intent intent = new Intent(mActivity, AddressesActivity.class);
        intent.putExtra(PARAM_OBJ, true);
        mActivity.startActivity(intent);
    }

    public void showCreateAddress() {
        Intent intent = new Intent(mActivity, UpdateAddressActivity.class);
        mActivity.startActivity(intent);
    }

    public void showChangeAddress(Address address) {
        Intent intent = new Intent(mActivity, UpdateAddressActivity.class);
        intent.putExtra(PARAM_OBJ, address);
        mActivity.startActivity(intent);
    }

    public void showSettle() {
        Intent intent = new Intent(mActivity, SettleActivity.class);
        mActivity.startActivity(intent);
    }

    public void showSetting() {
        Intent intent = new Intent(mActivity, SettingActivity.class);
        mActivity.startActivity(intent);
    }

    public void showFeedback() {
        Intent intent = new Intent(mActivity, FeedbackActivity.class);
        mActivity.startActivity(intent);
    }

    public void showFragment(Fragment fragment) {
        mActivity.getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(hasMainFragment() ? R.anim.right_in : 0, R.anim.left_out,
                        R.anim.left_in, R.anim.right_out)
                .replace(R.id.fragment_main, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void showUserProfileFragment() {
        showFragment(new UserProfileFragment());
    }

    public void showSetUsernameFragment() {
        showFragment(new SetUsernameFragment());
    }

    public void showSetNicknameFragment() {
        showFragment(new SetNicknameFragment());
    }

    public void showRegisterStep(RegisterStep step, String mobile) {
        switch (step) {
            case STEP_FIRST:
                showFragment(new RegisterFirstStepFragment());
                break;
            case STEP_SECOND:
                showFragment(RegisterSecondStepFragment.create(mobile));
                break;
            case STEP_THIRD:
                showFragment(RegisterThirdStepFragment.create(mobile));
                break;
        }
    }
}
