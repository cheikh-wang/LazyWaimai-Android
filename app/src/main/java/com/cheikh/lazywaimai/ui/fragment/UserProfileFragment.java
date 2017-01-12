package com.cheikh.lazywaimai.ui.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.base.BaseFragment;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.UserController;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.model.bean.User;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.util.FileUtil;
import com.cheikh.lazywaimai.util.SDCardUtil;
import com.cheikh.lazywaimai.util.StringUtil;
import com.cheikh.lazywaimai.util.ToastUtil;
import com.cheikh.lazywaimai.widget.PicassoImageView;
import com.cheikh.lazywaimai.widget.section.SectionExtensionItemView;
import com.cheikh.lazywaimai.widget.section.SectionTextItemView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
@ContentView(R.layout.fragment_user_profile)
public class UserProfileFragment extends BaseFragment<UserController.UserUiCallbacks>
        implements UserController.UserProfileUi {

    @Bind(R.id.item_avatar)
    SectionExtensionItemView mAvatarItem;

    @Bind(R.id.img_avatar)
    PicassoImageView mAvatarImg;

    @Bind(R.id.item_nickname)
    SectionTextItemView mNicknameItem;

    @Bind(R.id.item_username)
    SectionTextItemView mUsernameItem;

    @Bind(R.id.item_mobile)
    SectionTextItemView mMobileItem;

    @Bind(R.id.item_email)
    SectionTextItemView mEmailItem;

    @Bind(R.id.item_password)
    SectionTextItemView mPasswordItem;

    private TakePhoto mTakePhoto;
    private CropOptions mCropOptions;
    private User mUser;

    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_user_profile);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showUserInfo(User user) {
        mUser = user;
        mAvatarImg.loadProfile(user);
        mMobileItem.setSubtitle(user.getMobile());
        mNicknameItem.setSubtitle(StringUtil.isNotEmpty(user.getNickname()) ? user.getNickname() : "无");
        mUsernameItem.setSubtitle(StringUtil.isNotEmpty(user.getUsername()) ? user.getUsername() : "无");
        mEmailItem.setSubtitle(StringUtil.isNotEmpty(user.getEmail()) ? user.getEmail() : "无");
    }

    @Override
    public void uploadAvatarFinish() {
        cancelLoading();
        ToastUtil.showToast(R.string.toast_success_upload_avatar);
    }

    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();
        ToastUtil.showToast(error.getMessage());
    }

    @OnClick({R.id.item_avatar, R.id.item_nickname, R.id.item_username, R.id.item_mobile, R.id.item_email, R.id.item_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_avatar:
                selectUpdateAvatarMethod();
                break;
            case R.id.item_nickname:
                wantToSetNickname();
                break;
            case R.id.item_username:
                wantToSetUsername();
                break;
            case R.id.item_mobile:
                ToastUtil.showToast("还未开发");
                break;
            case R.id.item_email:
                ToastUtil.showToast("还未开发");
                break;
            case R.id.item_password:
                ToastUtil.showToast("还未开发");
                break;
        }
    }

    /**
     * 显示选择更换头像的方式
     */
    private void selectUpdateAvatarMethod() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_update_avatar_title)
                .setItems(R.array.update_avatar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri originUri = getOriginUri();
                        if (originUri != null) {
                            if (which == 0) {
                                getTakePhoto().onPickFromCaptureWithCrop(originUri, getCropOptions());
                            } else {
                                getTakePhoto().onPickFromGalleryWithCrop(originUri, getCropOptions());
                            }
                        }
                    }
                })
                .show();
    }

    /**
     * 设置昵称
     */
    private void wantToSetNickname() {
        Display display = getDisplay();
        if (display != null) {
            display.showSetNicknameFragment();
        }
    }

    /**
     * 设置用户名
     */
    private void wantToSetUsername() {
        if (mUser != null && TextUtils.isEmpty(mUser.getUsername())) {
            Display display = getDisplay();
            if (display != null) {
                display.showSetUsernameFragment();
            }
        } else {
            ToastUtil.showToast(R.string.toast_error_already_set_username);
        }
    }

    /**
     * 获取TakePhoto实例
     */
    private TakePhoto getTakePhoto(){
        if (mTakePhoto == null){
            mTakePhoto = new TakePhotoImpl(this, new TakePhoto.TakeResultListener() {

                @Override
                public void takeSuccess(String imagePath) {
                    // 开始上传图片
                    showLoading(R.string.label_being_something);
                    getCallbacks().uploadAvatar(new File(imagePath));
                }

                @Override
                public void takeFail(String msg) {
                    ToastUtil.showToast(msg);
                }

                @Override
                public void takeCancel() {
                }
            });
        }
        return mTakePhoto;
    }

    /**
     * 获取裁剪的选项
     */
    private CropOptions getCropOptions() {
        if (mCropOptions == null) {
            mCropOptions = new CropOptions.Builder()
                    .setAspectX(1)
                    .setAspectY(1)
                    .create();
        }
        return mCropOptions;
    }

    /**
     * 获取裁剪后的图片的存储位置
     */
    @SuppressLint("SimpleDateFormat")
    private Uri getOriginUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "lazy_crop_" + timeStamp + ".jpg";
        if (SDCardUtil.isAvailable()) {
            File cropFile = FileUtil.createImageFile(getContext(), fileName);
            return Uri.fromFile(cropFile);
        } else {
            ToastUtil.showToast(R.string.toast_error_have_not_sd_card_to_store_photo);
            return null;
        }
    }
}
