package com.cheikh.lazywaimai.ui.fragment;

import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.base.BaseListFragment;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.BusinessController;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.ui.adapter.BusinessListAdapter;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_BUSINESS_CLICKED;

/**
 * author: cheikh.wang on 16/11/23
 * email: wanghonghi@126.com
 */
public class ShopFragment extends BaseListFragment<Business, BusinessController.BusinessUiCallbacks>
        implements BusinessController.BusinessListUi {

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getBusinessController();
    }

    @Override
    public Business getRequestParameter() {
        return null;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_shop);
    }

    @Override
    protected boolean isShowBack() {
        return false;
    }

    @Override
    protected BusinessListAdapter getAdapter() {
        return new BusinessListAdapter();
    }

    @Override
    protected void refreshPage() {
        getCallbacks().refresh();
    }

    @Override
    protected void nextPage() {
        getCallbacks().nextPage();
    }

    @Override
    protected void onItemClick(int actionId, Business business) {
        switch (actionId) {
            case CLICK_TYPE_BUSINESS_CLICKED:
                getCallbacks().showBusiness(business);
                break;
        }
    }
}
