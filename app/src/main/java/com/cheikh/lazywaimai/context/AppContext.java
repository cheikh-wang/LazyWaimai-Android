package com.cheikh.lazywaimai.context;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import java.io.File;
import javax.inject.Inject;
import dagger.ObjectGraph;
import com.cheikh.lazywaimai.module.ApplicationModule;
import com.cheikh.lazywaimai.module.library.ContextProvider;
import com.cheikh.lazywaimai.module.library.InjectorModule;
import com.cheikh.lazywaimai.module.qualifiers.ShareDirectory;
import com.cheikh.lazywaimai.network.GsonHelper;
import com.cheikh.lazywaimai.controller.MainController;
import com.cheikh.lazywaimai.util.Injector;
import com.cheikh.lazywaimai.util.PreferenceUtil;
import com.cheikh.lazywaimai.util.ToastUtil;

public class AppContext extends Application implements Injector {

    private static AppContext mInstance;

    private ObjectGraph mObjectGraph;

    @Inject
    MainController mMainController;

    @Inject
    @ShareDirectory
    File mShareLocation;

    public static AppContext getContext() {
        return mInstance;
    }

    public MainController getMainController() {
        return mMainController;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // leakcanary进程下不进行初始化
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        // LeakCanary
        LeakCanary.install(this);

        // 吐司初始化
        ToastUtil.init(this);

        // 本地存储工具类初始化
        PreferenceUtil.init(this, GsonHelper.builderGson());

        // 日志打印器初始化
        Logger.addLogAdapter(new AndroidLogAdapter());

        // 依赖注解初始化
        mObjectGraph = ObjectGraph.create(
                new ApplicationModule(),
                new ContextProvider(this),
                new InjectorModule(this)
        );
        mObjectGraph.inject(this);
    }

    @Override
    public void inject(Object object) {
        mObjectGraph.inject(object);
    }
}