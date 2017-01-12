package com.cheikh.lazywaimai.controller;

import android.text.TextUtils;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.model.bean.Address;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.model.bean.User;
import com.cheikh.lazywaimai.network.RequestCallback;
import com.cheikh.lazywaimai.network.RestApiClient;
import com.cheikh.lazywaimai.ui.Display;

public class AddressController extends BaseController<AddressController.AddressUi, AddressController.AddressUiCallbacks> {

    private final RestApiClient mRestApiClient;

    @Inject
    public AddressController(RestApiClient restApiClient) {
        super();
        mRestApiClient = Preconditions.checkNotNull(restApiClient, "restApiClient cannot be null");
    }

    @Override
    protected void populateUi(AddressUi ui) {
        if (ui instanceof AddressListUi) {
            populateAddressListUi((AddressListUi) ui);
        }
    }

    private void populateAddressListUi(AddressListUi ui) {
        fetchAddressList(getId(ui));
    }

    private void fetchAddressList(final int callingId) {
        mRestApiClient.addressService()
                .addresses()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof AddressListUi) {
                            ((AddressListUi) ui).onStartRequest(0);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Address>>() {
                    @Override
                    public void onResponse(List<Address> addresses) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof AddressListUi) {
                            ((AddressListUi) ui).onFinishRequest(addresses, 0, false);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof AddressListUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    private void doCreateAddress(final int callingId, Address address) {
        mRestApiClient.addressService()
                .create(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Address>() {
                    @Override
                    public void onResponse(Address response) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof UpdateAddressUi) {
                            ((UpdateAddressUi) ui).updateFinish();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof UpdateAddressUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    private void doChangeAddress(final int callingId, Address address) {
        mRestApiClient.addressService()
                .change(address.getId(), address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Address>() {
                    @Override
                    public void onResponse(Address response) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof UpdateAddressUi) {
                            ((UpdateAddressUi) ui).updateFinish();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof UpdateAddressUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    private void doDeleteAddress(final int callingId, final Address address) {
        mRestApiClient.addressService()
                .delete(address.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Object>() {
                    @Override
                    public void onResponse(Object response) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof AddressListUi) {
                            ((AddressListUi) ui).deleteFinish(address);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof AddressListUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    private void doSetDefaultAddress(final int callingId, final Address address) {
        mRestApiClient.accountService()
                .setLastAddress(address.getUserId(), address.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<User>() {
                    @Override
                    public void onResponse(User response) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof AddressListUi) {
                            ((AddressListUi) ui).setDefaultFinish();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        AddressUi ui = findUi(callingId);
                        if (ui instanceof AddressListUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    @Override
    protected AddressUiCallbacks createUiCallbacks(final AddressUi ui) {
        return new AddressUiCallbacks() {

            @Override
            public void refresh() {
                if (ui instanceof AddressListUi) {
                    fetchAddressList(getId(ui));
                }
            }

            @Override
            public boolean isNameValid(String name) {
                return !TextUtils.isEmpty(name);
            }

            @Override
            public boolean isMobileValid(String phone) {
                return !TextUtils.isEmpty(phone);
            }

            @Override
            public boolean isSummaryValid(String summary) {
                return !TextUtils.isEmpty(summary);
            }

            @Override
            public boolean isDetailValid(String detail) {
                return !TextUtils.isEmpty(detail);
            }

            @Override
            public void create(Address address) {
                doCreateAddress(getId(ui), address);
            }

            @Override
            public void change(Address address) {
                doChangeAddress(getId(ui), address);
            }

            @Override
            public void delete(Address address) {
                doDeleteAddress(getId(ui), address);
            }

            @Override
            public void setDefaultAddress(Address address) {
                doSetDefaultAddress(getId(ui), address);
            }

            @Override
            public void showChangeAddress(Address address) {
                Display display = getDisplay();
                if (display != null) {
                    display.showChangeAddress(address);
                }
            }

            @Override
            public void showCreateAddress() {
                Display display = getDisplay();
                if (display != null) {
                    display.showCreateAddress();
                }
            }
        };
    }

    public interface AddressUiCallbacks {

        void refresh();

        boolean isNameValid(String name);

        boolean isMobileValid(String phone);

        boolean isSummaryValid(String summary);

        boolean isDetailValid(String detail);

        void delete(Address address);

        void create(Address address);

        void change(Address address);

        void setDefaultAddress(Address address);

        void showChangeAddress(Address address);

        void showCreateAddress();
    }

    /**
     * UI界面必须实现的接口,留给控制器调用
     */
    public interface AddressUi extends BaseController.Ui<AddressUiCallbacks> {}

    public interface AddressListUi extends AddressUi, ListUi<Address, AddressUiCallbacks> {
        void deleteFinish(Address address);

        void setDefaultFinish();
    }

    public interface UpdateAddressUi extends AddressUi {
        void updateFinish();
    }
}
