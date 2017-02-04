package com.cheikh.lazywaimai.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseActivity;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.OrderController;
import com.cheikh.lazywaimai.model.ShoppingCart;
import com.cheikh.lazywaimai.model.bean.CartInfo;
import com.cheikh.lazywaimai.model.bean.Order;
import com.cheikh.lazywaimai.model.bean.PayMethod;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.model.bean.SettleResult;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.ui.adapter.SendTimeListAdapter;
import com.cheikh.lazywaimai.util.ToastUtil;
import com.cheikh.lazywaimai.widget.AddressView;
import com.cheikh.lazywaimai.widget.MultiStateView;
import com.cheikh.lazywaimai.widget.OrderReportView;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.activity_settle)
public class SettleActivity extends BaseActivity<OrderController.OrderUiCallbacks>
        implements OrderController.OrderSettleUi {

    public static final int REQUEST_CODE_REMARK = 1000;

    @Bind(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    @Bind(R.id.view_address_info)
    AddressView mAddressInfoView;

    @Bind(R.id.txt_online_payment)
    TextView mOnlinePaymentTxt;

    @Bind(R.id.txt_offline_payment)
    TextView mOfflinePaymentTxt;

    @Bind(R.id.layout_send_time)
    View mSendTimeLayout;

    @Bind(R.id.txt_booked_time)
    TextView mBookedTimeTxt;

    @Bind(R.id.orderReportView)
    OrderReportView mOrderReportView;

    @Bind(R.id.btn_submit_order)
    Button mSubmitOrderBtn;

    @Bind(R.id.layout_remark)
    View mRemarkLayout;

    @Bind(R.id.txt_remark)
    TextView mRemarkTxt;

    private SendTimeListAdapter mSendTimeAdapter;
    private int mCheckedSendTimeIndex;

    private Drawable mOfflineDrawable;
    private Drawable mOnlineDrawable;

    private CartInfo mCartInfo;
    private SettleResult mSettleResult;
    private String mRemark;
    private SettleResult.BookingTime mBookingTime;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getOrderController();
    }

    @Override
    public String getRequestParameter() {
        return null;
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        mSendTimeAdapter = new SendTimeListAdapter();

        mOfflineDrawable = getResources().getDrawable(R.drawable.ic_check_off);
        mOnlineDrawable = getResources().getDrawable(R.drawable.ic_check_on);
        mOfflineDrawable.setBounds(0, 0, mOfflineDrawable.getMinimumWidth(),
                mOfflineDrawable.getMinimumHeight());
        mOnlineDrawable.setBounds(0, 0, mOnlineDrawable.getMinimumWidth(),
                mOnlineDrawable.getMinimumHeight());

        mMultiStateView.setState(MultiStateView.STATE_LOADING);
    }

    /**
     * 显示结算结果
     * @param settleResult
     */
    @Override
    public void onSettleFinish(SettleResult settleResult) {
        cancelLoading();
        mMultiStateView.setState(MultiStateView.STATE_CONTENT);
        mSettleResult = settleResult;
        // 收货地址
        mAddressInfoView.setData(mSettleResult.getLastAddress());
        // 付款方式
        mOnlinePaymentTxt.setCompoundDrawables(null, null,
                mSettleResult.isOnlinePayment() ? mOnlineDrawable : mOfflineDrawable, null);
        mOfflinePaymentTxt.setCompoundDrawables(null, null,
                mSettleResult.isOnlinePayment() ? mOfflineDrawable : mOnlineDrawable, null);
        // 禁用当前选择的付款方式的点击
        mOnlinePaymentTxt.setEnabled(!mSettleResult.isOnlinePayment());
        mOfflinePaymentTxt.setEnabled(mSettleResult.isOnlinePayment());
        // 可选的送达时间
        mSendTimeAdapter.setItems(mSettleResult.getBookingTimes());
        mBookedTimeTxt.setText(mBookingTime != null ? mBookingTime.getViewTime() : "");
        // 购物车信息
        mCartInfo = mSettleResult.getCartInfo();
        mOrderReportView.setupContent(mCartInfo);
        // 控制提交订单的按钮的可用性
        mSubmitOrderBtn.setEnabled(mSettleResult.isCanSubmit());
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        if (mSettleResult == null) {
            mMultiStateView.setState(MultiStateView.STATE_ERROR)
                    .setTitle(error.getMessage())
                    .setButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mMultiStateView.setState(MultiStateView.STATE_LOADING);
                            getCallbacks().settle();
                        }
                    });
        } else {
            ToastUtil.showToast(error.getMessage());
        }
    }

    @Override
    public void onOrderCreateFinish(final Order order) {
        ShoppingCart.getInstance().clearAll();
        cancelLoading();
        ToastUtil.showToast(R.string.toast_success_order_create);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (order.getPayMethod() == PayMethod.ONLINE) {
                    getCallbacks().showPayment(order);
                } else {
                    getCallbacks().showOrderDetail(order);
                }
            }
        }, 2000);
    }

    @OnClick({
            R.id.view_address_info,
            R.id.txt_online_payment,
            R.id.txt_offline_payment,
            R.id.btn_submit_order,
            R.id.layout_send_time,
            R.id.layout_remark
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_address_info:
                getCallbacks().chooseAddress();
                break;
            case R.id.txt_online_payment:
                showLoading(R.string.label_being_something);
                getCallbacks().togglePayMethod(true);
                break;
            case R.id.txt_offline_payment:
                showLoading(R.string.label_being_something);
                getCallbacks().togglePayMethod(false);
                break;
            case R.id.btn_submit_order:
                showLoading(R.string.label_being_something);
                getCallbacks().orderCreate(mCartInfo.getId(),
                        mSendTimeAdapter.getItem(mCheckedSendTimeIndex).getUnixTime(), mRemark);
                break;
            case R.id.layout_send_time:
                popupChooseBookedTime(mCheckedSendTimeIndex);
                break;
            case R.id.layout_remark:
                getCallbacks().showRemark(mRemark, REQUEST_CODE_REMARK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REMARK && resultCode == RESULT_OK) {
            mRemark = data.getStringExtra(Display.PARAM_OBJ);
            mRemarkTxt.setText(mRemark);
        }
    }

    /**
     * 弹出选择预订时间的窗口
     */
    private void popupChooseBookedTime(int checked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择送达时间");
        builder.setSingleChoiceItems(mSendTimeAdapter, checked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCheckedSendTimeIndex = which;
                mBookingTime = mSendTimeAdapter.getItem(which);
                mBookedTimeTxt.setText(mBookingTime.getViewTime());
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
