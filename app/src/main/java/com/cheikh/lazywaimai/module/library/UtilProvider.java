package com.cheikh.lazywaimai.module.library;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    includes = ContextProvider.class,
    library = true
)
public class UtilProvider {

    @Provides @Singleton
    public Bus provideEventBus() {
        return new Bus();
    }
}
