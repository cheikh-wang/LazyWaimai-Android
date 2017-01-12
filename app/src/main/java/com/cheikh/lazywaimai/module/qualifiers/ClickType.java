package com.cheikh.lazywaimai.module.qualifiers;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import com.cheikh.lazywaimai.util.Constants;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({
        Constants.ClickType.CLICK_TYPE_DELETE_BTN_CLICKED,
        Constants.ClickType.CLICK_TYPE_EDIT_BTN_CLICKED,
        Constants.ClickType.CLICK_TYPE_BUSINESS_CLICKED,
        Constants.ClickType.CLICK_TYPE_ORDER_CLICKED,
        Constants.ClickType.CLICK_TYPE_ADDRESS_CLICKED,
        Constants.ClickType.CLICK_TYPE_PRODUCT_CATEGORY_CLICKED,
        Constants.ClickType.CLICK_TYPE_SHOPPING_CART_CLICKED
})
public @interface ClickType {
}