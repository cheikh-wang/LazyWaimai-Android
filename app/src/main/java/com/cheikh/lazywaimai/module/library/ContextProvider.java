package com.cheikh.lazywaimai.module.library;

import android.app.AlarmManager;
import android.content.Context;
import android.content.res.AssetManager;

import com.google.common.base.Preconditions;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.cheikh.lazywaimai.module.qualifiers.ApplicationContext;
import com.cheikh.lazywaimai.module.qualifiers.FilesDirectory;

@Module(
    library = true
)
public class ContextProvider {

    private final Context mApplicationContext;

    public ContextProvider(Context context) {
        mApplicationContext = Preconditions.checkNotNull(context, "context cannot be null");
    }

    @Provides @ApplicationContext
    public Context provideApplicationContext() {
        return mApplicationContext;
    }

    @Provides @FilesDirectory
    public File providePrivateFileDirectory() {
        return mApplicationContext.getFilesDir();
    }

    @Provides @Singleton
    public AssetManager provideAssetManager() {
        return mApplicationContext.getAssets();
    }

    @Provides @Singleton
    public AlarmManager provideAlarmManager() {
        return (AlarmManager) mApplicationContext.getSystemService(Context.ALARM_SERVICE);
    }
}
