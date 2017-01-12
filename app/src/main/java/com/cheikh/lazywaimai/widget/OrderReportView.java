package com.cheikh.lazywaimai.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.model.bean.CartInfo;
import com.cheikh.lazywaimai.model.bean.ShoppingEntity;
import com.cheikh.lazywaimai.ui.adapter.DiscountInfoListAdapter;
import com.cheikh.lazywaimai.ui.adapter.ExtraFeeListAdapter;
import com.cheikh.lazywaimai.ui.adapter.ShoppingProductListAdapter;
import com.cheikh.lazywaimai.ui.fragment.BusinessNameClickHandle;
import com.cheikh.lazywaimai.util.CollectionUtil;
import com.cheikh.lazywaimai.util.StringFetcher;

public class OrderReportView extends FrameLayout {

    @Bind(R.id.txt_name)
    TextView mBusinessNameTxt;

    @Bind(R.id.img_arrow)
    ImageView mArrowImg;

    @Bind(R.id.divider)
    View mShoppingProductDivider;

    @Bind(R.id.recycler_view)
    RecyclerView mShoppingProductRecyclerView;

    @Bind(R.id.divider2)
    View mExtraFeeDivider;

    @Bind(R.id.recycler_view2)
    RecyclerView mExtraFeeRecyclerView;

    @Bind(R.id.divider3)
    View mDiscountListDivider;

    @Bind(R.id.recycler_view3)
    RecyclerView mDiscountInfoRecyclerView;

    @Bind(R.id.txt_origin_price)
    TextView mOriginPriceTxt;

    @Bind(R.id.txt_discount_price)
    TextView mDiscountPriceTxt;

    @Bind(R.id.txt_total_price)
    TextView mTotalPriceTxt;

    private ShoppingProductListAdapter mShoppingProductAdapter;
    private ExtraFeeListAdapter mExtraFeeListAdapter;
    private DiscountInfoListAdapter mDiscountInfoAdapter;

    private BusinessNameClickHandle mNameClickHandle;

    public OrderReportView(Context context) {
        this(context, null);
    }

    public OrderReportView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_order_report_view, this);
        ButterKnife.bind(view);
        initViews();
    }

    private void initViews() {
        mShoppingProductAdapter = new ShoppingProductListAdapter();
        mShoppingProductRecyclerView.setAdapter(mShoppingProductAdapter);
        mShoppingProductRecyclerView.setLayoutManager(new FixedLinearLayoutManager(getContext()));

        mExtraFeeListAdapter = new ExtraFeeListAdapter();
        mExtraFeeRecyclerView.setAdapter(mExtraFeeListAdapter);
        mExtraFeeRecyclerView.setLayoutManager(new FixedLinearLayoutManager(getContext()));

        mDiscountInfoAdapter = new DiscountInfoListAdapter();
        mDiscountInfoRecyclerView.setAdapter(mDiscountInfoAdapter);
        mDiscountInfoRecyclerView.setLayoutManager(new FixedLinearLayoutManager(getContext()));
    }

    public void setupContent(CartInfo cartInfo) {
        // 价格
        mOriginPriceTxt.setText(StringFetcher.getString(R.string.label_price, cartInfo.getOriginPrice()));
        mDiscountPriceTxt.setText(StringFetcher.getString(R.string.label_price, cartInfo.getDiscountPrice()));
        mTotalPriceTxt.setText(StringFetcher.getString(R.string.label_price, cartInfo.getTotalPrice()));
        // 商家名称
        final Business businessInfo = cartInfo.getBusiness();
        if (businessInfo != null) {
            mBusinessNameTxt.setText(businessInfo.getName());
            mBusinessNameTxt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNameClickHandle != null) {
                        mNameClickHandle.onBusinessNameClick(businessInfo);
                    }
                }
            });
        }
        // 选购的商品
        List<ShoppingEntity> shoppingProducts = cartInfo.getShoppingProducts();
        mShoppingProductAdapter.setItems(shoppingProducts);
        mShoppingProductDivider.setVisibility(CollectionUtil.isEmpty(shoppingProducts) ? View.GONE : View.VISIBLE);
        // 商家额外费用
        List<CartInfo.ExtraFee> extraFees = cartInfo.getExtraFees();
        mExtraFeeListAdapter.setItems(extraFees);
        mExtraFeeDivider.setVisibility(CollectionUtil.isEmpty(extraFees) ? View.GONE : View.VISIBLE);
        // 商家活动优惠
        List<CartInfo.DiscountInfo> discountInfos = cartInfo.getDiscountInfos();
        mDiscountInfoAdapter.setItems(discountInfos);
        mDiscountListDivider.setVisibility(CollectionUtil.isEmpty(discountInfos) ? View.GONE : View.VISIBLE);
    }

    public void setNameClickHandle(BusinessNameClickHandle nameClickHandle) {
        if (nameClickHandle != null) {
            mNameClickHandle = nameClickHandle;
            mArrowImg.setVisibility(View.VISIBLE);
        }
    }
}
