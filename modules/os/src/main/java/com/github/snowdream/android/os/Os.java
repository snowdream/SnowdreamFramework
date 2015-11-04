package com.github.snowdream.android.os;

import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;

/**
 * Created by snowdream on 11/17/13.
 */
public class Os {

    /**
     * Returns the operating system release.
     * @return
     */
    public static final String release() {
        return Build.VERSION.RELEASE + "." + Build.VERSION.INCREMENTAL;
    }

    /**
     * Returns the operating system platform. Possible value is android.
     *
     * @return
     */
    public static final String platform() {
        return "android";
    }


    /**
     * Returns the operating system CPU architecture. Possible values are 'arm', 'x86' and 'mips'.
     *
     * @return
     */
    public static final String arch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String[] abis = Build.SUPPORTED_ABIS;
            if (abis == null || abis.length == 0) return "arm";

            for (String abi : abis) {
                if (TextUtils.isEmpty(abi)) continue;

                if (abi.contains("mips")) return "mips";
                if (abi.contains("x86")) return "x86";
            }
            return "arm";
        }else{
            String abi = Build.CPU_ABI;
            if (abi.contains("mips")) return "mips";
            if (abi.contains("x86")) return "x86";
            return "arm";
        }
    }

    /**
     * Returns the system uptime in seconds.
     *
     * @param isIncludeSleepTime including time spent in sleep.
     * @return
     */
    public static final long uptime(boolean isIncludeSleepTime) {
        if (isIncludeSleepTime) {
            return SystemClock.elapsedRealtime();
        } else {
            return SystemClock.uptimeMillis();
        }
    }

    /**
     * A constant defining the appropriate End-of-line marker for the operating system.
     */
    public static final String EOL = "\n";
}
