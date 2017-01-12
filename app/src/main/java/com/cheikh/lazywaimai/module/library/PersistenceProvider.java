package com.cheikh.lazywaimai.module.library;

import com.cheikh.lazywaimai.repository.SettingManager;
import com.cheikh.lazywaimai.repository.UserManager;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module(
        library = true,
        includes = {
                ContextProvider.class,
                UtilProvider.class
        }
)
public class PersistenceProvider {

    @Provides
    @Singleton
    public UserManager provideUserManager() {
        return new UserManager();
    }

    @Provides
    @Singleton
    public SettingManager provideSettingManager() {
        return new SettingManager();
    }
}
