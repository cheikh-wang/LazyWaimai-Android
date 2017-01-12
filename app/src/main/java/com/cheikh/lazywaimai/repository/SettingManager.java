package com.cheikh.lazywaimai.repository;

import com.cheikh.lazywaimai.model.bean.PaymentPlatform;
import com.cheikh.lazywaimai.model.bean.Setting;
import com.cheikh.lazywaimai.util.PreferenceUtil;
import java.util.List;

/**
 * author：cheikh.wang on 2016/11/4 11:19
 * email：wanghonghi@126.com
 */
public class SettingManager {

    private static final Class<Setting> CLAZZ = Setting.class;

    private Setting mSetting;

    public void saveOrUpdate(Setting setting) {
        if (setting == null) {
            return;
        }
        mSetting = setting;
        PreferenceUtil.set(CLAZZ.getName(), mSetting);
    }

    public Setting getSetting() {
        if (mSetting == null) {
            mSetting = PreferenceUtil.getObject(CLAZZ.getName(), CLAZZ);
        }

        return mSetting;
    }

    public List<String> getCommonRemarks() {
        if (getSetting() != null) {
            return getSetting().getCommonRemarks();
        }
        return null;
    }

    public List<PaymentPlatform> getPaymentPlatform() {
        if (getSetting() != null) {
            return getSetting().getPaymentPlatforms();
        }
        return null;
    }

    public void clear() {
        mSetting = null;
        PreferenceUtil.set(CLAZZ.getName(), "");
    }
}
