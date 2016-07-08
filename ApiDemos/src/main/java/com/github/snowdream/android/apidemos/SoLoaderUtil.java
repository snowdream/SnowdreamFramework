package com.github.snowdream.android.apidemos;

import android.content.Context;
import android.support.annotation.NonNull;
import com.github.snowdream.android.core.SoLoader;

/**
 * Created by yanghui.yangh on 2016-07-08.
 */
public class SoLoaderUtil {
    //init
    public static void init(@NonNull Context context){
        SoLoader.loadLibrary(context,"hello-jni");
    }


    /* A native method that is implemented by the
    * 'hello-jni' native library, which is packaged
    * with this application.
    */
    public static native String stringFromJNI();
}
