package com.github.snowdream.android.support.v4.app;

import android.content.Context;
import com.github.snowdream.android.core.Snowdream;
import android.support.multidex.MultiDex;


/**
 * Created by hui.yang on 2015/2/26.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Snowdream.init(getApplicationContext());

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
