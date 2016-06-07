package com.github.snowdream.android.core;

import android.content.Context;

import com.github.snowdream.android.util.BuildConfigUtil;
import com.github.snowdream.android.util.log.Log;

/**
 * Created by hui.yang on 2015/2/25.
 */
public final class Snowdream {
    public static final String TAG = "SnowdreamFramework";

    private Snowdream() {
        throw new AssertionError("The operation is not allowed.");
    }

    /**
     * Whether the framework has been initialized.
     */
    private static volatile boolean isInit = false;

    /**
     * The Context of the application
     */
    private static Context mContext = null;

    /**
     * Whether the app is in Debug Mode
     */
    private static boolean mIsdebug = false;


    /**
     * call it when application init.
     *
     * @param context the Context of the application.
     */
    public static final void init(Context context) {
        if (!isInit) {
            synchronized (Snowdream.class) {
                if (context == null) {
                    throw new NullPointerException("The mContext is null.");
                }

                Snowdream.mContext = context;
                isInit = true;

                mIsdebug = BuildConfigUtil.isDebug(mContext);

                Log.setEnabled(mIsdebug);
                Log.setGlobalTag(TAG);

                welcome();
            }
        } else {
            Log.i("The FrameWork has been initialized.");
        }
    }

    private static final void welcome(){
        Log.i("**************************************************************************************************************************");
        Log.i("*                                                   SnowdreamFramework                                                   *");
        Log.i("*                                                                                                                        *");
        Log.i("*                                         An Android Framework Build With Gradle                                         *");
        Log.i("**************************************************************************************************************************");
    }

    /**
     * call it when application unInit.
     */
    public static final void unInit() {
        if (isInit) {
            synchronized (Snowdream.class) {
                Snowdream.mContext = null;
                isInit = false;
            }
        } else {
            Log.w("The FrameWork has not been initialized.");
        }
    }

    /**
     * @return the mContext from the application. It may be null.
     */
    public static final Context getApplicationContext() {
        return Snowdream.mContext;
    }

    /**
     * The same as BuildConfig.DEBUG
     *
     * @return if true,in Debug mode. otherwise,in Release mode.
     */
    public static final boolean isDebug() {
        return mIsdebug;
    }
}
