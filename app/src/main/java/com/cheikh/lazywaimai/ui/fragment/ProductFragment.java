package com.cheikh.lazywaimai.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.flipboard.bottomsheet.BottomSheetLayout;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.base.BaseFragment;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.BusinessController;
import com.cheikh.lazywaimai.model.ShoppingCart;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.model.bean.ProductCategory;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.ui.adapter.ProductCategoryListAdapter;
import com.cheikh.lazywaimai.ui.adapter.ProductItemAdapter;
import com.cheikh.lazywaimai.util.CollectionUtil;
import com.cheikh.lazywaimai.util.StringFetcher;
import com.cheikh.lazywaimai.widget.MultiStateView;
import com.cheikh.lazywaimai.widget.ShoppingCartPanel;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;

@ContentView(R.layout.fragment_product)
public class ProductFragment extends BaseFragment<BusinessController.BusinessUiCallbacks>
        implements BusinessController.ProductListUi, BaseController.SubUi {

    @Bind(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    @Bind(R.id.bottom_sheet_layout)
    BottomSheetLayout mBottomSheetLayout;

    @Bind(R.id.lv_product_category)
    ListView mCategoryListView;

    @Bind(R.id.lv_product_list)
    PinnedHeaderListView mProductListView;

    @Bind(R.id.layout_shopping_info)
    LinearLayout mShoppingInfoLayout;

    @Bind(R.id.img_cart_logo)
    ImageView mCartLogoImg;

    @Bind(R.id.txt_selected_count)
    TextView mSelectedCountTxt;

    @Bind(R.id.txt_total_price)
    TextView mTotalPriceTxt;

    @Bind(R.id.btn_settle)
    Button mSettleBtn;

    private ProductCategoryListAdapter mCategoryItemAdapter;
    private ProductItemAdapter mProductItemAdapter;

    private ShoppingCartPanel mShoppingCartPanel;

    private boolean isClickTrigger;
    private Business mBusiness;

    public static ProductFragment create(Business business) {
        ProductFragment fragment = new ProductFragment();
        if (business != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Display.PARAM_OBJ, business);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getBusinessController();
    }

    @Override
    protected void handleArguments(Bundle arguments) {
        if (arguments != null) {
            mBusiness = (Business) arguments.getSerializable(Display.PARAM_OBJ);
        }
    }

    @Override
    public Business getRequestParameter() {
        return mBusiness;
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        mShoppingCartPanel = new ShoppingCartPanel(getContext());

        // 商品分类的列表
        mCategoryItemAdapter = new ProductCategoryListAdapter();
        mCategoryListView.setAdapter(mCategoryItemAdapter);
        mCategoryListView.setSelection(0);
        mCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int productPos = 0;
                for (int index = 0; index < position; index++) {
                    // 加1是因为section也算一个位置
                    productPos += mProductItemAdapter.getCountForSection(index) + 1;
                }
                isClickTrigger = true;
                mProductListView.setSelection(productPos);
            }
        });

        // 商品的列表
        mProductItemAdapter = new ProductItemAdapter(getActivity());
        mProductItemAdapter.setAnimTargetView(mCartLogoImg);
        mProductListView.setAdapter(mProductItemAdapter);
        mProductListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isClickTrigger) {
                    isClickTrigger = false;
                } else {
                    int section = mProductItemAdapter.getSectionForPosition(firstVisibleItem);
                    mCategoryListView.setItemChecked(section, true);
                }
            }
        });

        mMultiStateView.setState(MultiStateView.STATE_LOADING);
    }

    @OnClick({R.id.btn_settle, R.id.layout_shopping_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_settle:
                getCallbacks().showSettle();
                break;
            case R.id.layout_shopping_info:
                showShoppingCartPanel();
                break;
        }
    }

    @Override
    public void onChangeItem(List<ProductCategory> items) {
        if (!CollectionUtil.isEmpty(items)) {
            mCategoryItemAdapter.setItems(items);
            mProductItemAdapter.setItems(items);
            showShoppingCartPanel();
            refreshBottomUi();
            mMultiStateView.setState(MultiStateView.STATE_CONTENT);
        } else {
            mMultiStateView.setState(MultiStateView.STATE_EMPTY)
                    .setTitle(R.string.label_empty_product_list)
                    .setButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getCallbacks().refresh();
                        }
                    });
        }
    }

    @Override
    public void onResponseError(ResponseError error) {
        mMultiStateView.setState(MultiStateView.STATE_ERROR)
                .setTitle(error.getMessage())
                .setButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCallbacks().refresh();
                    }
                });
    }

    @Override
    public void onShoppingCartChange() {
        refreshBottomUi();
        mShoppingCartPanel.refreshPanel();
        mCategoryItemAdapter.notifyDataSetChanged();
        mProductItemAdapter.notifyDataSetChanged();
    }

    /**
     * 更新底部面板
     */
    private void refreshBottomUi() {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        int totalCount = shoppingCart.getTotalQuantity();
        double totalPrice = shoppingCart.getTotalPrice();
        double distancePrice = getCallbacks().distanceSettlePrice(mBusiness);
        mCartLogoImg.setSelected(totalCount > 0);
        mSelectedCountTxt.setText(StringFetcher.getString(R.string.label_count, totalCount));
        mTotalPriceTxt.setText(StringFetcher.getString(R.string.label_price, totalPrice));
        mSettleBtn.setEnabled(getCallbacks().enableSettle(mBusiness));
        mSettleBtn.setText(distancePrice != 0 ? getString(R.string.label_distance_price, distancePrice) : getString(R.string.btn_settle));
    }

    /**
     * 显示购物车面板
     */
    private void showShoppingCartPanel() {
        int count = ShoppingCart.getInstance().getTotalQuantity();
        if (count > 0 && !mBottomSheetLayout.isSheetShowing()) {
            mBottomSheetLayout.showWithSheetView(mShoppingCartPanel);
        } else {
            mBottomSheetLayout.dismissSheet();
        }
    }
}