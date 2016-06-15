package com.github.snowdream.android.support.v4.app;

import android.content.Context;
import com.github.snowdream.android.core.Snowdream;
import com.github.snowdream.android.widget.Toast;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;


/**
 * Created by hui.yang on 2015/2/26.
 */
public class Application extends android.app.Application {
    static {
        initAsyncTask();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Snowdream.init(getApplicationContext());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.onConfigurationChanged(newConfig);
    }

    /**
     * release
     *
     * please call this when the main activity is onDestory();
     */
    public void release(){
        Toast.release();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //fix https://github.com/snowdream/SnowdreamFramework/issues/13
    public static void initAsyncTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }
        };
    }
}
