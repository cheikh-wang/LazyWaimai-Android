package com.cheikh.lazywaimai.ui.fragment;

import android.os.Bundle;
import com.cheikh.lazywaimai.R;
import com.cheikh.lazywaimai.base.BaseController;
import com.cheikh.lazywaimai.base.BaseFragment;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.controller.BusinessController;
import com.cheikh.lazywaimai.model.bean.Business;
import com.cheikh.lazywaimai.util.ContentView;
import com.cheikh.lazywaimai.ui.Display;

@ContentView(R.layout.fragment_business_detail)
public class BusinessDetailFragment extends BaseFragment<BusinessController.BusinessUiCallbacks>
        implements BusinessController.BusinessProfileUi {

    private Business mBusiness;

    public static BusinessDetailFragment create(Business business) {
        BusinessDetailFragment fragment = new BusinessDetailFragment();
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
}