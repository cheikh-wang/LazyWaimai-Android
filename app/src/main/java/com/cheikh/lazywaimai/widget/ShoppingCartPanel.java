package com.cheikh.lazywaimai.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.model.ShoppingCart;
import com.cheikh.lazywaimai.model.bean.ShoppingEntity;
import com.cheikh.lazywaimai.ui.adapter.ShoppingCartListAdapter;

/**
 * 购物车面板
 */
public class ShoppingCartPanel extends FrameLayout {

    @Bind(R.id.txt_clear)
    TextView mClearTxt;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ShoppingCartListAdapter mAdapter;

    public ShoppingCartPanel(Context context) {
        this(context, null);
    }

    public ShoppingCartPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_shopping_cart_panel, this);
        ButterKnife.bind(view);
        initViews();
        refreshPanel();
    }

    private void initViews() {
        mAdapter = new ShoppingCartListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void refreshPanel() {
        ShoppingCart shoppingCart = ShoppingCart.getInstance();
        List<ShoppingEntity> entities = shoppingCart.getShoppingList();
        mAdapter.setItems(entities);
    }

    @OnClick({R.id.txt_clear})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_clear:
                clearShoppingCart();
                break;
        }
    }

    /**
     * 清空购物车
     */
    private void clearShoppingCart() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_clear_shopping_cart_title)
                .setMessage(R.string.dialog_clear_shopping_cart_message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShoppingCart.getInstance().clearAll();
                        mAdapter.clearItems();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .create()
                .show();
    }
}