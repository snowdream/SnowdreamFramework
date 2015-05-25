package com.github.snowdream.android.core;

import android.content.Context;
import com.github.snowdream.android.util.BuildConfigUtil;
import com.github.snowdream.android.util.Log;

/**
 * Created by hui.yang on 2015/2/25.
 */
public final class Snowdream {

    //Supress default constructor for noninstantiability
    private Snowdream() {
        throw new AssertionError();
    }

    /**
     * Whether the framework has been initialized.
     */
    private static volatile boolean isInit = false;

    /**
     * The context of the application
     */
    private static Context context = null;


    /**
     *
     * call it when application init.
     *
     * @param context the context of the application.
     */
    public static final void init(Context context){
        if (!isInit) {
            synchronized (Snowdream.class) {
                if (context == null){
                    throw new NullPointerException("The context is null.");
                }

                Snowdream.context = context;
                isInit = true;

                Log.setEnabled(isDebug());
                Log.setGlobalTag("SnowdreamFramework");
            }
        }else{
            Log.i("The FrameWork has been initialized.");
        }
    }

    /**
     *
     * call it when application unInit.
     *
     */
    public static final void unInit(){
        if (isInit) {
            synchronized (Snowdream.class) {
                Snowdream.context = null;
                isInit = false;
            }
        }else{
            Log.w("The FrameWork has not been initialized.");
        }
    }

    /**
     * @return the context from the application. It may be null.
     */
    public static final Context getApplicationContext() {
        return  Snowdream.context;
    }

    /**
     * The same as BuildConfig.DEBUG
     *
     * @return  if true,in Debug mode. otherwise,in Release mode.
     */
    public static final boolean isDebug(){
        boolean isdebug = false;

        if (context != null){
            isdebug = BuildConfigUtil.isDebug(context);
        }

        return isdebug;
    }
}
