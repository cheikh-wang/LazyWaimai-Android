package com.cheikh.lazywaimai.ui.activity;

import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.base.BaseListActivity;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.UserController;
import com.cheikh.lazywaimai.model.bean.Favorite;
import com.cheikh.lazywaimai.ui.adapter.FavoriteListAdapter;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_BUSINESS_CLICKED;

/**
 * author: cheikh.wang on 17/01/11
 * email: wanghonghi@126.com
 */
public class FavoritesActivity extends BaseListActivity<Favorite, UserController.UserUiCallbacks>
        implements UserController.UserFavoriteListUi {

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected FavoriteListAdapter getAdapter() {
        return new FavoriteListAdapter();
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
    protected void onItemClick(int actionId, Favorite favorite) {
        switch (actionId) {
            case CLICK_TYPE_BUSINESS_CLICKED:
                getCallbacks().showBusiness(favorite.getBusiness());
                break;
        }
    }
}
