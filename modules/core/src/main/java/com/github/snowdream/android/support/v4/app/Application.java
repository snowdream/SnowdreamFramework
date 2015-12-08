package com.github.snowdream.android.support.v4.app;

import android.content.Context;
import com.github.snowdream.android.core.Snowdream;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import android.support.multidex.MultiDex;


/**
 * Created by hui.yang on 2015/2/26.
 */
public class Application extends android.app.Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        Snowdream.init(getApplicationContext());

        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        Application application = (Application) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
