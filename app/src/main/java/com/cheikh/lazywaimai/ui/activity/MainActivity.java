package com.cheikh.lazywaimai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import butterknife.Bind;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseActivity;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.MainController;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.ui.fragment.OrdersFragment;
import com.cheikh.lazywaimai.ui.fragment.ShopFragment;
import com.cheikh.lazywaimai.ui.fragment.UserCenterFragment;
import com.cheikh.lazywaimai.util.ActivityStack;
import com.cheikh.lazywaimai.util.DoubleExitUtil;
import com.cheikh.lazywaimai.util.MainTab;

/**
 * author：cheikh on 16/5/9 15:02
 * email：wanghonghi@126.com
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity<MainController.MainUiCallbacks>
        implements MainController.MainHomeUi {

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Bind(R.id.viewpager_tab)
    SmartTabLayout mViewpagerTab;

    private DoubleExitUtil mDoubleClickExit;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController();
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final int[] tabIcons = {R.drawable.tab_ic_home, R.drawable.tab_ic_orders, R.drawable.tab_ic_me};
        final int[] tabTitles = {R.string.tab_home, R.string.tab_orders, R.string.tab_me};
        FragmentPagerItems pages = FragmentPagerItems.with(this)
                .add(R.string.tab_home, ShopFragment.class)
                .add(R.string.tab_orders, OrdersFragment.class)
                .add(R.string.tab_me, UserCenterFragment.class)
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                pages);

        mViewPager.setOffscreenPageLimit(pages.size());
        mViewPager.setAdapter(adapter);
        mViewpagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                View view = inflater.inflate(R.layout.layout_navigation_bottom_item, container, false);
                ImageView iconView = (ImageView) view.findViewById(R.id.img_icon);
                iconView.setBackgroundResource(tabIcons[position % tabIcons.length]);
                TextView titleView = (TextView) view.findViewById(R.id.txt_title);
                titleView.setText(tabTitles[position % tabTitles.length]);
                return view;
            }
        });
        mViewpagerTab.setViewPager(mViewPager);
        mDoubleClickExit = new DoubleExitUtil(this);
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        if (intent != null && intent.hasExtra(Display.PARAM_OBJ)) {
            MainTab tab = (MainTab) intent.getSerializableExtra(Display.PARAM_OBJ);
            switch (tab) {
                default:
                case SHOP:
                    mViewPager.setCurrentItem(0);
                    break;
                case ORDERS:
                    mViewPager.setCurrentItem(1);
                    break;
                case PERSON:
                    mViewPager.setCurrentItem(2);
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            boolean exit = mDoubleClickExit.onKeyDown(keyCode, event);
            if (exit) {
                ActivityStack.create().appExit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
