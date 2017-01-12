package com.cheikh.lazywaimai.ui.fragment;

import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.base.BaseListFragment;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.OrderController;
import com.cheikh.lazywaimai.model.bean.Order;
import com.cheikh.lazywaimai.model.bean.Product;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.ui.adapter.OrderListAdapter;

import java.util.List;

import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_ORDER_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_BUSINESS_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_PAYMENT_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_RECEIVED_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_ORDER_AGAIN_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_EVALUATE_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.HttpCode.HTTP_UNAUTHORIZED;

public class OrdersFragment extends BaseListFragment<Order, OrderController.OrderUiCallbacks>
        implements OrderController.OrderListUi {

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getOrderController();
    }

    @Override
    public String getRequestParameter() {
        return null;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_orders);
    }

    @Override
    protected boolean isShowBack() {
        return false;
    }

    @Override
    protected OrderListAdapter getAdapter() {
        return new OrderListAdapter();
    }

    @Override
    protected boolean isDisplayError(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            return true;
        }
        return super.isDisplayError(error);
    }

    @Override
    protected int getErrorIcon(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            return R.drawable.ic_unauth;
        }
        return super.getErrorIcon(error);
    }

    @Override
    protected String getErrorTitle(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            return error.getMessage();
        }
        return super.getErrorTitle(error);
    }

    @Override
    protected String getErrorButton(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            return getString(R.string.btn_go_login);
        }
        return super.getErrorTitle(error);
    }

    protected void onRetryClick(ResponseError error) {
        if (error.getStatus() == HTTP_UNAUTHORIZED) {
            getCallbacks().showLogin();
        }
        super.onRetryClick(error);
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
    protected void onItemClick(int actionId, Order order) {
        switch (actionId) {
            case CLICK_TYPE_ORDER_CLICKED:
                getCallbacks().showOrderDetail(order);
                break;
            case CLICK_TYPE_BUSINESS_CLICKED:
                getCallbacks().showBusiness(order.getBusinessInfo());
                break;
            case CLICK_TYPE_PAYMENT_CLICKED:
                getCallbacks().showPayment(order);
                break;
            case CLICK_TYPE_RECEIVED_CLICKED:
                getCallbacks().confirmReceived();
                break;
            case CLICK_TYPE_ORDER_AGAIN_CLICKED:
                getCallbacks().orderAgain(order);
                break;
            case CLICK_TYPE_EVALUATE_CLICKED:
                getCallbacks().showEvaluate();
                break;
        }
    }
}