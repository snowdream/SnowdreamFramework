package com.github.snowdream.android.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by yanghui.yangh on 2016-07-14.
 */
public class SimUtil {
    private static SoftReference<DualSimInfo> reference = null;

    private SimUtil() {
        throw new AssertionError("No constructor allowed here!");
    }

    /**
     * Whether the phone use DualSim
     *
     * @param context
     * @return
     */
    public static boolean isDualSim(@NonNull Context context) {
        return (getMtkDualSimInfo(context, true) != null) || (getQualcommDualSimInfo(context, true) != null);
    }


    /**
     * Get DualSimInfo,if any.
     *
     * @param context
     * @return
     */
    @Nullable
    public static DualSimInfo getDualSimInfo(@NonNull Context context) {
        if (isDualSim(context)) {
            return getDualSimInfoFromCache();
        }

        return null;
    }


    /**
     * Whether the phone use MTK DualSim
     *
     * @param context
     * @param isCache
     * @return
     */
    @Nullable
    public static DualSimInfo getMtkDualSimInfo(@NonNull Context context, boolean isCache) {
        if (isCache && getDualSimInfoFromCache() != null) {
            return getDualSimInfoFromCache();
        }

        DualSimInfo dualSimInfo = new DualSimInfo();
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            dualSimInfo.simId_1 = (Integer) fields1.get(null);
            Field fields2 = c.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            dualSimInfo.simId_2 = (Integer) fields2.get(null);
            Method m = TelephonyManager.class.getDeclaredMethod(
                    "getSubscriberIdGemini", int.class);
            dualSimInfo.imsi_1 = (String) m.invoke(tm,
                    dualSimInfo.simId_1);
            dualSimInfo.imsi_2 = (String) m.invoke(tm,
                    dualSimInfo.simId_2);

            Method m1 = TelephonyManager.class.getDeclaredMethod(
                    "getDeviceIdGemini", int.class);
            dualSimInfo.imei_1 = (String) m1.invoke(tm,
                    dualSimInfo.simId_1);
            dualSimInfo.imei_2 = (String) m1.invoke(tm,
                    dualSimInfo.simId_2);

            Method mx = TelephonyManager.class.getDeclaredMethod(
                    "getPhoneTypeGemini", int.class);
            dualSimInfo.phoneType_1 = (Integer) mx.invoke(tm,
                    dualSimInfo.simId_1);
            dualSimInfo.phoneType_2 = (Integer) mx.invoke(tm,
                    dualSimInfo.simId_2);
            dualSimInfo.platformType = DualSimInfo.PLATORM_MTK;

            if (TextUtils.isEmpty(dualSimInfo.imsi_1)
                    && (!TextUtils.isEmpty(dualSimInfo.imsi_2))) {
                dualSimInfo.defaultImsi = dualSimInfo.imsi_2;
            }
            if (TextUtils.isEmpty(dualSimInfo.imsi_2)
                    && (!TextUtils.isEmpty(dualSimInfo.imsi_1))) {
                dualSimInfo.defaultImsi = dualSimInfo.imsi_1;
            }

            if (isCache) {
                reference = new SoftReference<DualSimInfo>(dualSimInfo);
            }
        } catch (Exception e) {
            dualSimInfo = null;
        }
        return dualSimInfo;
    }

    /**
     * Whether the phone use Qualcomm DualSim
     *
     * @param context
     * @param isCache
     * @return
     */
    @Nullable
    public static DualSimInfo getQualcommDualSimInfo(@NonNull Context context, boolean isCache) {
        if (isCache && getDualSimInfoFromCache() != null) {
            return getDualSimInfoFromCache();
        }

        DualSimInfo dualSimInfo = new DualSimInfo();
        dualSimInfo.simId_1 = 0;
        dualSimInfo.simId_2 = 1;
        try {
            Class<?> cx = Class
                    .forName("android.telephony.MSimTelephonyManager");
            Object obj = context.getSystemService("phone_msim");

            Method md = cx.getMethod("getDeviceId", int.class);
            Method ms = cx.getMethod("getSubscriberId", int.class);

            dualSimInfo.imei_1 = (String) md.invoke(obj,
                    dualSimInfo.simId_1);
            dualSimInfo.imei_2 = (String) md.invoke(obj,
                    dualSimInfo.simId_2);
            dualSimInfo.imsi_1 = (String) ms.invoke(obj,
                    dualSimInfo.simId_1);
            dualSimInfo.imsi_2 = (String) ms.invoke(obj,
                    dualSimInfo.simId_2);
            dualSimInfo.platformType = DualSimInfo.PLATORM_QUALCOMM;


            if (TextUtils.isEmpty(dualSimInfo.imsi_1)
                    && (!TextUtils.isEmpty(dualSimInfo.imsi_2))) {
                dualSimInfo.defaultImsi = dualSimInfo.imsi_2;
            }
            if (TextUtils.isEmpty(dualSimInfo.imsi_2)
                    && (!TextUtils.isEmpty(dualSimInfo.imsi_1))) {
                dualSimInfo.defaultImsi = dualSimInfo.imsi_1;
            }

            if (isCache) {
                reference = new SoftReference<DualSimInfo>(dualSimInfo);
            }
        } catch (Exception e) {
            dualSimInfo = null;
        }

        return dualSimInfo;
    }

    private static DualSimInfo getDualSimInfoFromCache() {
        if (reference != null && reference.get() != null) {
            return reference.get();
        }

        return null;
    }
}
