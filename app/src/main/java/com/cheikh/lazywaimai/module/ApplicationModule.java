package com.cheikh.lazywaimai.module;

import dagger.Module;
import com.cheikh.lazywaimai.context.AppContext;
import com.cheikh.lazywaimai.module.library.InjectorModule;
import com.cheikh.lazywaimai.module.library.NetworkProvider;
import com.cheikh.lazywaimai.module.library.PersistenceProvider;
import com.cheikh.lazywaimai.module.library.UtilProvider;

@Module(
        injects = {
                AppContext.class,
        },
        includes = {
                UtilProvider.class,
                NetworkProvider.class,
                PersistenceProvider.class,
                InjectorModule.class,
        }
)
public class ApplicationModule {
}