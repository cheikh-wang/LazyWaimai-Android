package com.cheikh.lazywaimai.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseActivity;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.AddressController;
import com.cheikh.lazywaimai.model.bean.Address;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.ui.adapter.AddressListAdapter;
import com.cheikh.lazywaimai.util.ViewEventListener;
import com.cheikh.lazywaimai.util.ToastUtil;
import com.cheikh.lazywaimai.widget.MultiStateView;
import com.cheikh.lazywaimai.widget.refresh.OnRefreshListener;
import com.cheikh.lazywaimai.widget.refresh.RefreshLayout;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_ADDRESS_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_DELETE_BTN_CLICKED;
import static com.cheikh.lazywaimai.util.Constants.ClickType.CLICK_TYPE_EDIT_BTN_CLICKED;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.activity_addresses)
public class AddressesActivity extends BaseActivity<AddressController.AddressUiCallbacks>
        implements AddressController.AddressListUi, ViewEventListener<Address> {

    @Bind(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    @Bind(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.btn_create_address)
    LinearLayout mCreateAddressView;

    private boolean mIsChooseAddress;
    private AddressListAdapter mAdapter;

    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getAddressController();
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mIsChooseAddress = intent.getBooleanExtra(Display.PARAM_OBJ, false);
    }

    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCallbacks().refresh();
            }
        });

        mAdapter = new AddressListAdapter();
        mAdapter.setViewEventListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMultiStateView.setState(MultiStateView.STATE_LOADING);
    }

    @Override
    public void onStartRequest(int page) {
        if (mMultiStateView.getState() != MultiStateView.STATE_CONTENT) {
            mMultiStateView.setState(MultiStateView.STATE_LOADING);
        } else {
            mRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onFinishRequest(final List<Address> items, int page, boolean haveNextPage) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }

        if (items != null && !items.isEmpty()) {
            mAdapter.setItems(items);
            mMultiStateView.setState(MultiStateView.STATE_CONTENT);
        } else {
            mMultiStateView.setState(MultiStateView.STATE_EMPTY)
                    .setTitle(R.string.label_empty_address_list)
                    .setButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMultiStateView.setState(MultiStateView.STATE_LOADING);
                            getCallbacks().refresh();
                        }
                    });
        }
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();

        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }

        if (mMultiStateView.getState() != MultiStateView.STATE_CONTENT) {
            mMultiStateView.setState(MultiStateView.STATE_ERROR)
                    .setTitle(error.getMessage())
                    .setButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMultiStateView.setState(MultiStateView.STATE_LOADING);
                            getCallbacks().refresh();
                        }
                    });
        } else {
            ToastUtil.showToast(error.getMessage());
        }
    }

    @Override
    public void deleteFinish(Address address) {
        cancelLoading();
        mAdapter.delItem(address);
        ToastUtil.showToast(R.string.toast_success_address_delete);
    }

    @Override
    public void setDefaultFinish() {
        cancelLoading();
        Display display = getDisplay();
        if (display != null) {
            display.finishActivity();
        }
    }

    @OnClick({R.id.btn_create_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_address:
                getCallbacks().showCreateAddress();
                break;
        }
    }

    @Override
    public void onViewEvent(int actionId, Address address, int position, View view) {
        switch (actionId) {
            case CLICK_TYPE_ADDRESS_CLICKED: // 设置默认地址
                if (mIsChooseAddress) {
                    showLoading(R.string.label_being_something);
                    getCallbacks().setDefaultAddress(address);
                }
                break;
            case CLICK_TYPE_EDIT_BTN_CLICKED: // 修改
                getCallbacks().showChangeAddress(address);
                break;
            case CLICK_TYPE_DELETE_BTN_CLICKED: // 删除
                deleteAddress(address);
                break;
        }
    }

    private void deleteAddress(final Address address) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_delete_title);
        builder.setMessage(R.string.dialog_delete_message);
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showLoading(R.string.label_being_something);
                getCallbacks().delete(address);
            }
        });
        builder.create().show();
    }
}
