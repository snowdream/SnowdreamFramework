package com.github.snowdream.android.util;

import android.os.Looper;

/**
 * Created by hui.yang on 2015/10/21.
 * see: http://stackoverflow.com/a/8714032
 */
public class ThreadUtil {
    private ThreadUtil() {
        throw new AssertionError();
    }

    public static boolean isOnUIThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isOnNonUIThread(){
        return Looper.myLooper() != Looper.getMainLooper();
    }
}
