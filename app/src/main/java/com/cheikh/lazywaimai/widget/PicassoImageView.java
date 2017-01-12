package com.cheikh.lazywaimai.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cheikh.lazywaimai.model.bean.PaymentPlatform;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.context.AppConfig;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.model.bean.Product;
import com.cheikh.lazywaimai.model.bean.User;
import com.cheikh.lazywaimai.util.AnimationUtils;

/**
 * Picasso框架的辅助ImageView控件
 * 修改自:https://github.com/chrisbanes/philm/blob/master/app/src/main/java/app/philm/in/view/PhilmImageView.java
 */
public class PicassoImageView extends ImageView {

    private final String LOG_TAG = PicassoImageView.class.getSimpleName();

    private PicassoHandler mPicassoHandler;
    private boolean mAutoFade = true;
    private boolean mAvatarMode = false;

    public PicassoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAutoFade(boolean autoFade) {
        mAutoFade = autoFade;
    }

    public void setAvatarMode(boolean avatarMode) {
        mAvatarMode = avatarMode;
    }

    public void loadProfile(User user) {
        loadProfile(user, null);
    }

    public void loadProfile(User user, Listener listener) {
        setAvatarMode(true);
        if (!TextUtils.isEmpty(user.getAvatarUrl())) {
            setPicassoHandler(new UserProfileHandler(user, listener));
        } else {
            reset();
            setImageResourceImpl(R.drawable.ic_default_avatar);
        }
    }

    public void loadBusinessPhoto(Business business) {
        loadBusinessPhoto(business, null);
    }

    public void loadBusinessPhoto(Business business, Listener listener) {
        if (!TextUtils.isEmpty(business.getPicUrl())) {
            setPicassoHandler(new BusinessPhotoHandler(business, listener));
        } else {
            reset();
            setImageResourceImpl(R.mipmap.ic_launcher);
        }
    }

    public void loadProductPhoto(Product product) {
        loadProductPhoto(product, null);
    }

    public void loadProductPhoto(Product product, Listener listener) {
        if (!TextUtils.isEmpty(product.getImagePath())) {
            setVisibility(View.VISIBLE);
            setPicassoHandler(new ProductPhotoHandler(product, listener));
        } else {
            reset();
            setVisibility(View.GONE);
        }
    }

    public void loadPlatformIcon(PaymentPlatform platform) {
        loadPlatformIcon(platform, null);
    }

    public void loadPlatformIcon(PaymentPlatform platform, Listener listener) {
        if (!TextUtils.isEmpty(platform.getIconUrl())) {
            setVisibility(View.VISIBLE);
            setPicassoHandler(new PlatformIconHandler(platform, listener));
        } else {
            reset();
            setVisibility(View.GONE);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed && canLoadImage() && mPicassoHandler != null && !mPicassoHandler.isStarted()) {
            loadUrlImmediate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Picasso.with(getContext()).cancelRequest(mPicassoTarget);

        super.onDetachedFromWindow();
    }

    private boolean canLoadImage() {
        return getWidth() != 0 && getHeight() != 0;
    }

    private void loadUrlImmediate() {
        Preconditions.checkNotNull(mPicassoHandler, "mPicassoHandler cannot be null");

        final String url = mPicassoHandler.getUrl(this);

        if (url != null) {
            mPicassoHandler.markAsStarted();

            RequestCreator request = Picasso.with(getContext()).load(url);
            if (mPicassoHandler.shouldDisplayPlaceholder()
                    && mPicassoHandler.getPlaceholderDrawable() != 0) {
                request = request.placeholder(mPicassoHandler.getPlaceholderDrawable());
            }
            if (mPicassoHandler.centerCrop()) {
                request = request.resize(getWidth(), getHeight()).centerCrop();
            } else {
                request = request.resize(getWidth(), getHeight()).centerInside();
            }
            request.into(mPicassoTarget);

            if (AppConfig.DEBUG) {
                Log.d(LOG_TAG, "Loading " + url);
            }
        }
    }

    private void reset() {
        setPicassoHandler(null);
        setImageDrawable(null);
    }

    private void setPicassoHandler(PicassoHandler handler) {
        if (mPicassoHandler != null && mPicassoHandler.isStarted()
                && !mPicassoHandler.isFinished()) {
            Picasso.with(getContext()).cancelRequest(mPicassoTarget);
        }

        if (handler != null && Objects.equal(handler, mPicassoHandler)) {
            handler.setDisplayPlaceholder(false);
        }

        mPicassoHandler = handler;

        if (handler != null && canLoadImage()) {
            loadUrlImmediate();
        }
    }

    private static abstract class PicassoHandler<T> {

        private final T mObject;
        private final Listener mCallback;
        private boolean mIsStarted, mIsFinished;
        private boolean mDisplayPlaceholder = true;

        PicassoHandler(T object, Listener callback) {
            mObject = Preconditions.checkNotNull(object, "object cannot be null");
            mCallback = callback;
        }

        final String getUrl(ImageView imageView) {
            return buildUrl(mObject, imageView);
        }

        protected abstract String buildUrl(T object, ImageView imageView);

        void markAsStarted() {
            mIsStarted = true;
        }

        void markAsFinished() {
            mIsFinished = true;
        }

        boolean isStarted() {
            return mIsStarted;
        }

        boolean isFinished() {
            return mIsFinished;
        }

        int getPlaceholderDrawable() {
            return 0;
        }

        void setDisplayPlaceholder(boolean displayPlaceholder) {
            mDisplayPlaceholder = displayPlaceholder;
        }

        boolean shouldDisplayPlaceholder() {
            return mDisplayPlaceholder;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PicassoHandler that = (PicassoHandler) o;
            return Objects.equal(mObject, that.mObject);
        }

        boolean centerCrop() {
            return true;
        }

        @Override
        public int hashCode() {
            return mObject != null ? mObject.hashCode() : 0;
        }
    }

    private class UserProfileHandler extends PicassoHandler<User> {

        UserProfileHandler(User user, Listener callback) {
            super(user, callback);
        }

        @Override
        protected String buildUrl(User user, ImageView imageView) {
            return user.getAvatarUrl();
        }
    }

    private class BusinessPhotoHandler extends PicassoHandler<Business> {

        BusinessPhotoHandler(Business business, Listener callback) {
            super(business, callback);
        }

        @Override
        protected String buildUrl(Business business, ImageView imageView) {
            return business.getPicUrl();
        }
    }

    private class ProductPhotoHandler extends PicassoHandler<Product> {

        ProductPhotoHandler(Product product, Listener callback) {
            super(product, callback);
        }

        @Override
        protected String buildUrl(Product product, ImageView imageView) {
            return product.getImagePath();
        }
    }

    private class PlatformIconHandler extends PicassoHandler<PaymentPlatform> {

        PlatformIconHandler(PaymentPlatform platform, Listener callback) {
            super(platform, callback);
        }

        @Override
        protected String buildUrl(PaymentPlatform platform, ImageView imageView) {
            return platform.getIconUrl();
        }
    }

    private final Target mPicassoTarget = new Target() {

        @Override
        public void onPrepareLoad(Drawable drawable) {
            if (mPicassoHandler == null || mPicassoHandler.shouldDisplayPlaceholder()) {
                setImageDrawableImpl(drawable);
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            setImageBitmapFromNetwork(bitmap, loadedFrom);

            if (mPicassoHandler != null) {
                if (mPicassoHandler.mCallback != null) {
                    mPicassoHandler.mCallback.onSuccess(PicassoImageView.this, bitmap);
                }
                mPicassoHandler.markAsFinished();
            }
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            if (mPicassoHandler != null) {
                if (mPicassoHandler.mCallback != null) {
                    mPicassoHandler.mCallback.onError(PicassoImageView.this);
                }
                mPicassoHandler.markAsFinished();
            }
        }
    };

    void setImageBitmapFromNetwork(final Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
        final boolean fade = mAutoFade && loadedFrom != Picasso.LoadedFrom.MEMORY;
        final Drawable currentDrawable = getDrawable();

        if (fade) {
            if (currentDrawable == null || mPicassoHandler.getPlaceholderDrawable() != 0) {
                // If we have no current drawable, or it is a placeholder drawable. Just fade in
                setVisibility(View.INVISIBLE);
                setImageBitmapImpl(bitmap);
                AnimationUtils.Fade.show(this);
            } else {
                AnimationUtils.startCrossFade(this, currentDrawable,
                        new BitmapDrawable(getResources(), bitmap));
            }
        } else {
            setImageBitmapImpl(bitmap);
        }
    }

    void setImageBitmapImpl(final Bitmap bitmap) {
        if (mAvatarMode) {
            setImageDrawable(new RoundedAvatarDrawable(bitmap));
        } else {
            setImageBitmap(bitmap);
        }
    }

    void setImageDrawableImpl(final Drawable drawable) {
        if (mAvatarMode && drawable instanceof BitmapDrawable) {
            setImageBitmapImpl(((BitmapDrawable) drawable).getBitmap());
        } else {
            setImageDrawable(drawable);
        }
    }

    void setImageResourceImpl(int resId) {
        if (mAvatarMode) {
            BitmapDrawable d = (BitmapDrawable) getResources().getDrawable(resId);
            setImageDrawable(new RoundedAvatarDrawable(d.getBitmap()));
        } else {
            setImageResource(resId);
        }
    }

    public interface Listener {

        void onSuccess(PicassoImageView imageView, Bitmap bitmap);

        void onError(PicassoImageView imageView);
    }
}
