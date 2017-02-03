package com.cheikh.lazywaimai.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseFragment;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.widget.SlidingTabLayout;

@ContentView(R.layout.fragment_viewpager)
public abstract class BaseTabFragment<UC> extends BaseFragment<UC> {

    private static final String SAVE_SELECTED_TAB = "selected_tab";

    @Bind(R.id.viewpager_tabs)
    SlidingTabLayout mSlidingTabStrip;

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    private TabPagerAdapter mAdapter;

    private int mCurrentItem;

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        mAdapter = new TabPagerAdapter(getChildFragmentManager());

        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.spacing_minor));
        mViewPager.setOffscreenPageLimit(3);

        mSlidingTabStrip.setViewPager(mViewPager);
        mSlidingTabStrip.setTabListener(new SlidingTabLayout.TabListener() {

            @Override
            public void onTabSelected(int pos) {}

            @Override
            public void onTabReSelected(int pos) {}
        });

        if (savedInstanceState != null) {
            mCurrentItem = savedInstanceState.getInt(SAVE_SELECTED_TAB);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSlidingTabStrip.getBackground().setAlpha(255);
    }

    @Override
    public void onPause() {
        mCurrentItem = mViewPager.getCurrentItem();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVE_SELECTED_TAB, mCurrentItem);
        super.onSaveInstanceState(outState);
    }

    protected ViewPager getViewPager() {
        return mViewPager;
    }

    protected void setFragments(List<Fragment> fragments) {
        mAdapter.setFragments(fragments);
        mSlidingTabStrip.notifyDataSetChanged();
        mViewPager.setCurrentItem(mCurrentItem);
    }

    protected SlidingTabLayout getSlidingTabStrip() {
        return mSlidingTabStrip;
    }

    protected TabPagerAdapter getAdapter() {
        return mAdapter;
    }

    protected abstract String getTabTitle(int position);

    protected class TabPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments;

        private TabPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
        }

        void setFragments(List<Fragment> fragments) {
            mFragments.clear();
            mFragments.addAll(fragments);
            notifyDataSetChanged();
        }

        @Override
        public final Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public final int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTabTitle(position);
        }
    }
}
