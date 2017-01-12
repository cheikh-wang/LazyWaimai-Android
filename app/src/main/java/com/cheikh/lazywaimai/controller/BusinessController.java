package com.cheikh.lazywaimai.controller;

import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.model.bean.Favorite;
import com.cheikh.lazywaimai.util.StringFetcher;
import com.google.common.base.Preconditions;
import com.squareup.otto.Subscribe;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.context.AppCookie;
import com.cheikh.lazywaimai.model.ShoppingCart;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.model.bean.ProductCategory;
import com.cheikh.lazywaimai.model.bean.ResponseError;
import com.cheikh.lazywaimai.model.bean.ResultsPage;
import com.cheikh.lazywaimai.model.event.ShoppingCartChangeEvent;
import com.cheikh.lazywaimai.network.RequestCallback;
import com.cheikh.lazywaimai.network.RestApiClient;
import com.cheikh.lazywaimai.ui.Display;
import com.cheikh.lazywaimai.util.EventUtil;

import static com.cheikh.lazywaimai.util.Constants.HttpCode.HTTP_UNAUTHORIZED;

public class BusinessController extends BaseController<BusinessController.BusinessUi,
        BusinessController.BusinessUiCallbacks> {

    private static final int PAGE_SIZE = 10;

    private final RestApiClient mRestApiClient;

    private int mPageIndex;

    @Inject
    public BusinessController(RestApiClient restApiClient) {
        super();
        mRestApiClient = Preconditions.checkNotNull(restApiClient, "restApiClient cannot be null");
    }

    @Subscribe
    public void onShoppingCartChanged(ShoppingCartChangeEvent event) {
        for (BusinessUi ui : getUis()) {
            if (ui instanceof ProductListUi) {
                ((ProductListUi) ui).onShoppingCartChange();
                break;
            }
        }
    }

    @Override
    protected void onInited() {
        super.onInited();
        EventUtil.register(this);
    }

    @Override
    protected void onSuspended() {
        EventUtil.unregister(this);
        super.onSuspended();
    }

    @Override
    protected void populateUi(BusinessUi ui) {
        if (ui instanceof BusinessListUi) {
            populateBusinessListUi((BusinessListUi) ui);
        } else if (ui instanceof BusinessTabUi) {
            populateBusinessTabUi((BusinessTabUi) ui);
        } else if (ui instanceof ProductListUi) {
            populateProductListUi((ProductListUi) ui);
        }
    }

    private void populateBusinessListUi(BusinessListUi ui) {
        mPageIndex = 1;
        doFetchBusinesses(getId(ui), mPageIndex, PAGE_SIZE);
    }

    private void populateBusinessTabUi(BusinessTabUi ui) {
        ui.setTabs(BusinessTab.PRODUCT, BusinessTab.COMMENT, BusinessTab.DETAIL);
    }

    private void populateProductListUi(ProductListUi ui) {
        doFetchProducts(getId(ui), ui.getRequestParameter());
    }

    /**
     * 分页获取商家列表数据
     * @param callingId
     * @param page
     */
    private void doFetchBusinesses(final int callingId, final int page, int size) {
        mRestApiClient.businessService()
                .businesses(page, size)
                .map(new Func1<ResultsPage<Business>, List<Business>>() {
                    @Override
                    public List<Business> call(ResultsPage<Business> results) {
                        return results.results;
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        BusinessUi ui = findUi(callingId);
                        if (ui instanceof BusinessListUi) {
                            ((BusinessListUi) ui).onStartRequest(page);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Business>>() {
                    @Override
                    public void onResponse(List<Business> businesses) {
                        BusinessUi ui = findUi(callingId);
                        if (ui instanceof BusinessListUi) {
                            boolean haveNextPage = businesses != null && businesses.size() == PAGE_SIZE;
                            ((BusinessListUi) ui).onFinishRequest(businesses, page, haveNextPage);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        BusinessUi ui = findUi(callingId);
                        if (ui instanceof BusinessListUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    /**
     * 获取指定商家下的所有商品数据
     * @param callingId
     * @param business
     */
    private void doFetchProducts(final int callingId, Business business) {
        mRestApiClient.businessService()
                .products(business.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<ProductCategory>>() {
                    @Override
                    public void onResponse(List<ProductCategory> businesses) {
                        BusinessUi ui = findUi(callingId);
                        if (ui instanceof ProductListUi) {
                            ((ProductListUi) ui).onChangeItem(businesses);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        BusinessUi ui = findUi(callingId);
                        if (ui instanceof ProductListUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    /**
     * 收藏\取消收藏店铺
     * @param callingId
     * @param business
     */
    private void doFavoriteBusiness(final int callingId, Business business, final boolean isLike) {
        if (!AppCookie.isLoggin()) {
            BusinessUi ui = findUi(callingId);
            if (ui instanceof BusinessTabUi) {
                ResponseError error = new ResponseError(HTTP_UNAUTHORIZED,
                        StringFetcher.getString(R.string.toast_error_not_login));
                ui.onResponseError(error);
            }
        } else {
            mRestApiClient.businessService()
                    .favorite(business.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RequestCallback<Favorite>() {
                        @Override
                        public void onResponse(Favorite favorite) {
                            BusinessUi ui = findUi(callingId);
                            if (ui instanceof BusinessTabUi) {
                                ((BusinessTabUi) ui).onFavoriteFinish(isLike);
                            }
                        }

                        @Override
                        public void onFailure(ResponseError error) {
                            BusinessUi ui = findUi(callingId);
                            if (ui instanceof BusinessTabUi) {
                                ui.onResponseError(error);
                            }
                        }
                    });
        }
    }

    @Override
    protected BusinessUiCallbacks createUiCallbacks(final BusinessUi ui) {
        return new BusinessUiCallbacks() {

            @Override
            public void refresh() {
                if (ui instanceof BusinessListUi) {
                    mPageIndex = 1;
                    doFetchBusinesses(getId(ui), mPageIndex, PAGE_SIZE);
                } else if (ui instanceof ProductListUi) {
                    doFetchProducts(getId(ui), ui.getRequestParameter());
                }
            }

            @Override
            public void nextPage() {
                if (ui instanceof BusinessListUi) {
                    ++mPageIndex;
                    doFetchBusinesses(getId(ui), mPageIndex, PAGE_SIZE);
                }
            }

            @Override
            public void favoriteBusiness(Business business, boolean isLike) {
                doFavoriteBusiness(getId(ui), business, isLike);
            }

            @Override
            public boolean enableSettle(Business business) {
                ShoppingCart shoppingCart = ShoppingCart.getInstance();
                return (shoppingCart.getTotalPrice() > business.getMinPrice())
                        && (shoppingCart.getTotalQuantity() > 0);
            }

            @Override
            public double distanceSettlePrice(Business business) {
                ShoppingCart shoppingCart = ShoppingCart.getInstance();
                double distancePrice = business.getMinPrice() - shoppingCart.getTotalPrice();
                return distancePrice > 0 ? distancePrice : 0;
            }

            @Override
            public void callBusinessPhone(Business business) {
                Display display = getDisplay();
                if (display != null) {
                    display.callPhone(business.getPhone());
                }
            }

            @Override
            public void showBusiness(Business business) {
                Preconditions.checkNotNull(business, "business cannot be null");

                Display display = getDisplay();
                if (display != null) {
                    display.showBusiness(business);
                }
            }

            @Override
            public void showSettle() {
                Display display = getDisplay();
                if (display != null) {
                    if (AppCookie.isLoggin()) {
                        display.showSettle();
                    } else {
                        display.showLogin();
                    }
                }
            }
        };
    }

    public enum BusinessTab {
        PRODUCT, COMMENT, DETAIL
    }

    public interface BusinessUiCallbacks {
        void refresh();

        void nextPage();

        void favoriteBusiness(Business business, boolean isLike);

        boolean enableSettle(Business business);

        double distanceSettlePrice(Business business);

        void callBusinessPhone(Business business);

        void showSettle();

        void showBusiness(Business business);
    }

    public interface BusinessUi extends BaseController.Ui<BusinessUiCallbacks> {
        Business getRequestParameter();
    }

    public interface BusinessListUi extends BusinessUi, BaseController.ListUi<Business, BusinessUiCallbacks> {}

    public interface BusinessTabUi extends BusinessUi {
        void setTabs(BusinessTab... tabs);

        void onFavoriteFinish(boolean isLike);
    }

    public interface ProductListUi extends BaseController.SubUi, BusinessUi {
        void onChangeItem(List<ProductCategory> items);

        void onShoppingCartChange();
    }

    public interface CommentListUi extends BaseController.SubUi, BusinessUi {
    }

    public interface BusinessProfileUi extends BaseController.SubUi, BusinessUi {
    }
}
