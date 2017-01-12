package com.cheikh.lazywaimai.module.library;

import com.google.common.base.Preconditions;

import dagger.Module;
import dagger.Provides;
import com.cheikh.lazywaimai.util.Injector;

@Module(
    library = true
)
public class InjectorModule {

    private final Injector mInjector;

    public InjectorModule(Injector injector) {
        mInjector = Preconditions.checkNotNull(injector, "injector cannot be null");
    }

    @Provides
    public Injector provideMainInjector() {
        return mInjector;
    }

}
