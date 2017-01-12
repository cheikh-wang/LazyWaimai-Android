package com.cheikh.lazywaimai.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;
import java.util.Date;
import butterknife.Bind;
import butterknife.OnClick;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseViewHolder;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.model.bean.Order;
import com.cheikh.lazywaimai.model.bean.OrderStatus;
import com.cheikh.lazywaimai.util.DateUtil;
import com.cheikh.lazywaimai.util.StringFetcher;
import com.cheikh.lazywaimai.widget.PicassoImageView;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_BUSINESS_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_ORDER_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_PAYMENT_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_RECEIVED_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_ORDER_AGAIN_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_EVALUATE_CLICKED;

/**
 * author: cheikh.wang on 16/11/23
 * email: wanghonghi@126.com
 */
public class OrderItemViewHolder extends BaseViewHolder<Order> {

    @Bind(R.id.view_content)
    View mContentView;

    @Bind(R.id.img_business_photo)
    PicassoImageView businessPhotoImg;

    @Bind(R.id.txt_business_name)
    TextView businessNameTxt;

    @Bind(R.id.txt_total_price)
    TextView totalPriceTxt;

    @Bind(R.id.txt_created_at)
    TextView createAtTxt;

    @Bind(R.id.txt_order_status)
    TextView orderStatusTxt;

    @Bind(R.id.btn_evaluate)
    TextView evaluateTxt;

    @Bind(R.id.btn_confirm_received)
    TextView confirmReceivedTxt;

    @Bind(R.id.btn_payment)
    TextView paymentTxt;

    @Bind(R.id.btn_order_again)
    TextView orderAgainTxt;

    public OrderItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Order order) {
        showOrderRelevantInfo(order);
        updateButtonsByStatus(order.getStatus());
    }

    /**
     * 显示订单相关信息
     * @param order
     */
    public void showOrderRelevantInfo(Order order) {
        Business business = order.getBusinessInfo();
        // 商家图片
        businessPhotoImg.loadBusinessPhoto(business);
        // 商家名称
        businessNameTxt.setText(business.getName());
        // 订单金额
        totalPriceTxt.setText(StringFetcher.getString(R.string.label_price,
                order.getTotalPrice()));
        // 下单时间
        createAtTxt.setText(DateUtil.DateToString(new Date(order.getCreatedAt() * 1000),
                DateUtil.DateStyle.YYYY_MM_DD_HH_MM
        ));
        // 订单状态
        orderStatusTxt.setText(order.getStatus().toString());
    }

    /**
     * 某些订单状态下需要显示操作按钮
     * @param status
     */
    public void updateButtonsByStatus(OrderStatus status) {
        switch (status) {
            case STATUS_WAIT_PAYMENT:
                paymentTxt.setVisibility(View.VISIBLE);
                confirmReceivedTxt.setVisibility(View.GONE);
                orderAgainTxt.setVisibility(View.VISIBLE);
                evaluateTxt.setVisibility(View.GONE);
                break;
            case STATUS_WAIT_CONFIRM:
                paymentTxt.setVisibility(View.GONE);
                confirmReceivedTxt.setVisibility(View.VISIBLE);
                orderAgainTxt.setVisibility(View.VISIBLE);
                evaluateTxt.setVisibility(View.GONE);
                break;
            case STATUS_FINISHED:
                paymentTxt.setVisibility(View.GONE);
                confirmReceivedTxt.setVisibility(View.GONE);
                orderAgainTxt.setVisibility(View.VISIBLE);
                evaluateTxt.setVisibility(View.VISIBLE);
                break;
            default:
                paymentTxt.setVisibility(View.GONE);
                confirmReceivedTxt.setVisibility(View.GONE);
                orderAgainTxt.setVisibility(View.VISIBLE);
                evaluateTxt.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.view_content, R.id.txt_business_name, R.id.btn_payment, R.id.btn_confirm_received, R.id.btn_order_again, R.id.btn_evaluate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_content:
                notifyItemAction(CLICK_TYPE_ORDER_CLICKED);
                break;
            case R.id.txt_business_name:
                notifyItemAction(CLICK_TYPE_BUSINESS_CLICKED);
                break;
            case R.id.btn_payment:
                notifyItemAction(CLICK_TYPE_PAYMENT_CLICKED);
                break;
            case R.id.btn_confirm_received:
                notifyItemAction(CLICK_TYPE_RECEIVED_CLICKED);
                break;
            case R.id.btn_order_again:
                notifyItemAction(CLICK_TYPE_ORDER_AGAIN_CLICKED);
                break;
            case R.id.btn_evaluate:
                notifyItemAction(CLICK_TYPE_EVALUATE_CLICKED);
                break;
        }
    }
}
