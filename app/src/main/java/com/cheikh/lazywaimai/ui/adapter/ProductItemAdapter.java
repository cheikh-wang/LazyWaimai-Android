package com.cheikh.lazywaimai.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.model.ShoppingCart;
import com.cheikh.lazywaimai.model.bean.Product;
import com.cheikh.lazywaimai.model.bean.ProductCategory;
import com.cheikh.lazywaimai.util.CollectionUtil;
import com.cheikh.lazywaimai.util.StringFetcher;
import com.cheikh.lazywaimai.widget.PicassoImageView;
import com.cheikh.lazywaimai.widget.ProperRatingBar;
import com.cheikh.lazywaimai.widget.ShoppingCountView;
import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;

public class ProductItemAdapter extends SectionedBaseAdapter {

    private LayoutInflater mInflater;
    private List<ProductCategory> mCategories;

    private View mAnimTargetView;

    public ProductItemAdapter(Activity activity) {
        mInflater = LayoutInflater.from(activity);
    }

    public void setAnimTargetView(View animTargetView) {
        mAnimTargetView = animTargetView;
    }

    public void setItems(List<ProductCategory> categories) {
        mCategories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getCountForSection(int section) {
        if (mCategories != null) {
            List<Product> products = mCategories.get(section).getProducts();
            if (!CollectionUtil.isEmpty(products)) {
                return products.size();
            }
        }
        return 0;
    }

    @Override
    public int getSectionCount() {
        return mCategories != null ? mCategories.size() : 0;
    }

    @Override
    public Product getItem(int section, int position) {
        List<Product> products = mCategories.get(section).getProducts();
        return products.get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup viewGroup) {
        final ItemViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_product_item, null);
            holder = new ItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        Product product = getItem(section, position);
        holder.photoImg.loadProductPhoto(product);
        holder.nameTxt.setText(product.getName());
        holder.priceTxt.setText(StringFetcher.getString(R.string.label_price, product.getPrice()));
        holder.monthSalesTxt.setText(StringFetcher.getString(R.string.label_month_sales, product.getMonthSales()));
        holder.rateRatingBar.setRating(product.getRate());
        if (!TextUtils.isEmpty(product.getDescription())) {
            holder.descriptionTxt.setVisibility(View.VISIBLE);
            holder.descriptionTxt.setText(product.getDescription());
        } else {
            holder.descriptionTxt.setVisibility(View.GONE);
        }
        if (product.getLeftNum() > 0) {
            final Product finalProduct = product;
            int quantity = ShoppingCart.getInstance().getQuantityForProduct(finalProduct);
            holder.shoppingCountView.setShoppingCount(quantity);
            holder.shoppingCountView.setAnimTargetView(mAnimTargetView);
            holder.shoppingCountView.setOnShoppingClickListener(new ShoppingCountView.ShoppingClickListener() {
                @Override
                public void onAddClick(int num) {
                    if (!ShoppingCart.getInstance().push(finalProduct)) {
                        // 添加失败则恢复数量
                        int oldQuantity = ShoppingCart.getInstance().getQuantityForProduct(finalProduct);
                        holder.shoppingCountView.setShoppingCount(oldQuantity);
                        showClearDialog();
                    }
                }

                @Override
                public void onMinusClick(int num) {
                    if (!ShoppingCart.getInstance().pop(finalProduct)) {
                        // 减少失败则恢复数量
                        int oldQuantity = ShoppingCart.getInstance().getQuantityForProduct(finalProduct);
                        holder.shoppingCountView.setShoppingCount(oldQuantity);
                    }
                }
            });
            holder.shoppingCountView.setVisibility(View.VISIBLE);
            holder.leftNumTxt.setVisibility(View.GONE);
        } else {
            holder.leftNumTxt.setText(StringFetcher.getString(R.string.label_sold_out));
            holder.leftNumTxt.setVisibility(View.VISIBLE);
            holder.shoppingCountView.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getSectionHeaderView(int position, View convertView, ViewGroup viewGroup) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_product_header, viewGroup, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        ProductCategory productCategory = mCategories.get(position);
        holder.titleTxt.setText(productCategory.getName());
        if (!TextUtils.isEmpty(productCategory.getDescription())) {
            holder.descText.setText(productCategory.getDescription());
            holder.descText.setVisibility(View.VISIBLE);
        } else {
            holder.descText.setVisibility(View.GONE);
        }

        return convertView;
    }

    ////////////////////////////////////////////
    ///            view holder               ///
    ////////////////////////////////////////////

    public static class HeaderViewHolder {
        TextView titleTxt;
        TextView descText;

        HeaderViewHolder(View headerView) {
            titleTxt = (TextView) headerView.findViewById(R.id.txt_title);
            descText = (TextView) headerView.findViewById(R.id.txt_desc);
        }
    }

    public static class ItemViewHolder {
        PicassoImageView photoImg;
        TextView nameTxt;
        TextView priceTxt;
        TextView descriptionTxt;
        TextView monthSalesTxt;
        ProperRatingBar rateRatingBar;
        TextView leftNumTxt;
        ShoppingCountView shoppingCountView;

        ItemViewHolder(View itemView) {
            photoImg = (PicassoImageView) itemView.findViewById(R.id.img_product_photo);
            nameTxt = (TextView) itemView.findViewById(R.id.txt_product_name);
            priceTxt = (TextView) itemView.findViewById(R.id.txt_product_price);
            descriptionTxt = (TextView) itemView.findViewById(R.id.txt_product_description);
            monthSalesTxt = (TextView) itemView.findViewById(R.id.txt_product_month_sales);
            rateRatingBar = (ProperRatingBar) itemView.findViewById(R.id.rating_product_rate);
            leftNumTxt = (TextView) itemView.findViewById(R.id.txt_product_left_num);
            shoppingCountView = (ShoppingCountView) itemView.findViewById(R.id.shopping_count_view);
        }
    }

    private void showClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mInflater.getContext());
        builder.setTitle(R.string.dialog_shopping_cart_business_conflict_title);
        builder.setMessage(R.string.dialog_shopping_cart_business_conflict_message);
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setPositiveButton(R.string.dialog_clear, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShoppingCart.getInstance().clearAll();
            }
        });
        builder.create().show();
    }
}