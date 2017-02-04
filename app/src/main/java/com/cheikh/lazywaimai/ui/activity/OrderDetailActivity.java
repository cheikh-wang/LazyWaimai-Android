package com.cheikh.lazywaimai.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.util.Date;
import butterknife.Bind;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseActivity;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.OrderController;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.model.bean.Order;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.ui.fragment.BusinessNameClickHandle;
import com.cheikh.lazywaimai.util.DateUtil;
import com.cheikh.lazywaimai.util.MainTab;
import com.cheikh.lazywaimai.widget.MultiStateView;
import com.cheikh.lazywaimai.widget.OrderReportView;
import com.cheikh.lazywaimai.widget.refresh.OnRefreshListener;
import com.cheikh.lazywaimai.widget.refresh.RefreshLayout;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity<OrderController.OrderUiCallbacks>
        implements OrderController.OrderDetailUi, BusinessNameClickHandle {

    @Bind(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.orderReportView)
    OrderReportView mOrderReportView;

    @Bind(R.id.txt_deliver_time)
    TextView mDeliverTimeTxt;

    @Bind(R.id.txt_deliver_name)
    TextView mDeliverNameTxt;

    @Bind(R.id.txt_deliver_phone)
    TextView mDeliverPhoneTxt;

    @Bind(R.id.txt_deliver_address)
    TextView mDeliverAddressTxt;

    @Bind(R.id.txt_order_num)
    TextView mOrderNumTxt;

    @Bind(R.id.txt_remark)
    TextView mOrderRemarkTxt;

    @Bind(R.id.txt_created_at)
    TextView mCreateAtTxt;

    @Bind(R.id.txt_pay_method)
    TextView mPayMethodTxt;

    private String mOrderId;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getOrderController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mOrderId = intent.getStringExtra(Display.PARAM_ID);
    }

    @Override
    public String getRequestParameter() {
        return mOrderId;
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCallbacks().refresh();
            }
        });
        mOrderReportView.setNameClickHandle(this);
        mMultiStateView.setState(MultiStateView.STATE_LOADING);
    }

    @Override
    public void showOrderInfo(Order order) {
        mMultiStateView.setState(MultiStateView.STATE_CONTENT);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        // 商品信息
        mOrderReportView.setupContent(order.getCartInfo());
        // 配送信息
        if (order.getBookedTime() == 0) {
            mDeliverTimeTxt.setText("立即送出");
        } else {
            mDeliverTimeTxt.setText(DateUtil.DateToString(new Date(order.getBookedTime() * 1000),
                    DateUtil.DateStyle.YYYY_MM_DD_HH_MM));
        }
        mDeliverNameTxt.setText(order.getConsignee());
        mDeliverPhoneTxt.setText(order.getPhone());
        mDeliverAddressTxt.setText(order.getAddress());
        // 订单信息
        mOrderNumTxt.setText(String.valueOf(order.getOrderNum()));
        mOrderRemarkTxt.setText(!TextUtils.isEmpty(order.getRemark()) ? order.getRemark() : "无");
        mCreateAtTxt.setText(DateUtil.DateToString(new Date(order.getCreatedAt()), DateUtil.DateStyle.YYYY_MM_DD_HH_MM));
        mPayMethodTxt.setText(order.getPayMethod().getName());
    }

    @Override
    public void onResponseError(ResponseError error) {
        mMultiStateView.setState(MultiStateView.STATE_ERROR)
                .setTitle(error.getMessage())
                .setButton(null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMultiStateView.setState(MultiStateView.STATE_LOADING);
                        getCallbacks().refresh();
                    }
                });
    }

    @Override
    public void onBusinessNameClick(Business business) {
        if (business != null) {
            getCallbacks().showBusiness(business);
        }
    }

    @Override
    public void onBackPressed() {
        Display display = getDisplay();
        if (display != null) {
            display.showMain(MainTab.ORDERS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Display display = getDisplay();
                if (display != null) {
                    display.showMain(MainTab.ORDERS);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
