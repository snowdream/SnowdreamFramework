package com.github.snowdream.android.os;

import android.os.SystemClock;

/**
 * Created by snowdream on 11/17/13.
 */
public class Os {


    /**
     * Returns the system uptime in seconds.
     *
     * @param isIncludeSleepTime including time spent in sleep.
     *
     * @return
     */
    public static final long uptime(boolean isIncludeSleepTime){
        if (isIncludeSleepTime){
            return SystemClock.elapsedRealtime();
        }else{
            return SystemClock.uptimeMillis();
        }
    }

    /**
     *  A constant defining the appropriate End-of-line marker for the operating system.
     */
    public static final String EOL = "\n";
}
