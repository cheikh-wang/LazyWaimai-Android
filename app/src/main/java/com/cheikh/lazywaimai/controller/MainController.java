package com.cheikh.lazywaimai.controller;

import com.cheikh.lazywaimai.model.bean.Feedback;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.model.bean.Setting;
import com.cheikh.lazywaimai.network.RequestCallback;
import com.cheikh.lazywaimai.network.RestApiClient;
import com.cheikh.lazywaimai.repository.SettingManager;
import com.google.common.base.Preconditions;
import javax.inject.Inject;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.ui.Display;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: cheikh.wang on 17/1/5
 * email: wanghonghi@126.com
 */
public class MainController extends BaseController<MainController.MainUi, MainController.MainUiCallbacks> {

    private final UserController mUserController;
    private final AddressController mAddressController;
    private final BusinessController mBusinessController;
    private final OrderController mOrderController;

    private final RestApiClient mRestApiClient;
    private final SettingManager mSettingManager;

    @Inject
    public MainController(UserController userController, AddressController addressController,
                          BusinessController businessController, OrderController orderController,
                          RestApiClient restApiClient, SettingManager settingManager) {
        super();
        mUserController = Preconditions.checkNotNull(userController, "userController cannot be null");
        mAddressController = Preconditions.checkNotNull(addressController, "addressController cannot be null");
        mBusinessController = Preconditions.checkNotNull(businessController, "businessController cannot be null");
        mOrderController = Preconditions.checkNotNull(orderController, "orderController cannot be null");
        mRestApiClient = Preconditions.checkNotNull(restApiClient, "restApiClient cannot be null");
        mSettingManager = Preconditions.checkNotNull(settingManager, "settingManager cannot be null");
    }

    @Override
    protected void onInited() {
        super.onInited();
        mUserController.init();
        mAddressController.init();
        mBusinessController.init();
        mOrderController.init();
    }

    @Override
    protected void onSuspended() {
        mUserController.suspend();
        mAddressController.suspend();
        mBusinessController.suspend();
        mOrderController.suspend();
        super.onSuspended();
    }

    public void attachDisplay(Display display) {
        Preconditions.checkNotNull(display, "display is null");
        Preconditions.checkState(getDisplay() == null, "we currently have a display");
        setDisplay(display);
    }

    public void detachDisplay(Display display) {
        Preconditions.checkNotNull(display, "display is null");
        Preconditions.checkState(getDisplay() == display, "display is not attached");
        setDisplay(null);
    }

    @Override
    public void setDisplay(Display display) {
        super.setDisplay(display);
        mUserController.setDisplay(display);
        mAddressController.setDisplay(display);
        mBusinessController.setDisplay(display);
        mOrderController.setDisplay(display);
    }

    @Override
    protected void populateUi(MainUi ui) {
        if (ui instanceof MainHomeUi) {
            populateMainHomeUi((MainHomeUi) ui);
        }
    }

    private void populateMainHomeUi(MainHomeUi ui) {
        doFetchSetting(getId(ui));
    }

    /**
     * 获取应用配置
     * @param callingId
     */
    private void doFetchSetting(final int callingId) {
        mRestApiClient.commonService()
                .settings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Setting>() {
                    @Override
                    public void onResponse(Setting setting) {
                        mSettingManager.saveOrUpdate(setting);
                    }
                });
    }

    /**
     * 提交意见反馈
     * @param callingId
     */
    private void doFeedback(final int callingId, Feedback feedback) {
        mRestApiClient.commonService()
                .feedback(feedback)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Feedback>() {
                    @Override
                    public void onResponse(Feedback result) {
                        MainUi ui = findUi(callingId);
                        if (ui instanceof MainFeedbackUi) {
                            ((MainFeedbackUi) ui).onFeedbackFinish();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        MainUi ui = findUi(callingId);
                        if (ui instanceof MainFeedbackUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    /**
     * 当返回键被按下时
     */
    public void onBackButtonPressed() {
        Display display = getDisplay();
        if (display != null) {
            if (!display.popTopFragmentBackStack()) {
                display.navigateUp();
            }
        }
    }

    @Override
    protected MainUiCallbacks createUiCallbacks(final MainUi ui) {
        return new MainUiCallbacks() {

            @Override
            public void feedback(Feedback feedback) {
                doFeedback(getId(ui), feedback);
            }
        };
    }

    public final UserController getUserController() {
        return mUserController;
    }

    public final BusinessController getBusinessController() {
        return mBusinessController;
    }

    public final AddressController getAddressController() {
        return mAddressController;
    }

    public final OrderController getOrderController() {
        return mOrderController;
    }

    public interface MainUi extends BaseController.Ui<MainUiCallbacks> {
    }

    public interface MainHomeUi extends MainUi {}

    public interface MainFeedbackUi extends MainUi {
        void onFeedbackFinish();
    }

    public interface MainUiCallbacks {

        void feedback(Feedback feedback);
    }
}
