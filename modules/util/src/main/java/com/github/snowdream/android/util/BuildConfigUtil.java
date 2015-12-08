/*
 * Copyright (C) 2014 Snowdream Mobile <yanghui1986527@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.snowdream.android.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.github.snowdream.android.util.log.Log;

import java.lang.reflect.Field;

/**
 * Created by hui.yang on 2014/11/9.
 */
public final class BuildConfigUtil {
    public static final String DEBUG = "DEBUG";
    public static final String APPLICATION_ID = "APPLICATION_ID";
    public static final String BUILD_TYPE = "BUILD_TYPE";
    public static final String FLAVOR = "FLAVOR";
    public static final String VERSION_CODE = "VERSION_CODE";
    public static final String VERSION_NAME = "VERSION_NAME";

    /**
     * @deprecated Use {@link #APPLICATION_ID}
     */
    @Deprecated
    public static final String PACKAGE_NAME = "PACKAGE_NAME";

    private BuildConfigUtil() {
        throw new AssertionError("No constructor allowed here!");
    }

    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
     * are used at the project level to set custom fields.
     *
     * @param context   Used to find the correct file
     * @param fieldName The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    public static <T> T getBuildConfigValue(@NonNull Context context, @NonNull String fieldName) {
        if (context == null) {
            Log.w("The Context is null.");
            return null;
        }

        if (TextUtils.isEmpty(fieldName)) {
            Log.w("The fileName is null or empty.");
            return null;
        }

        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            Field field = clazz.getField(fieldName);
            return (T) field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BuildConfig.Debug
     *
     * @param context
     * @return
     */
    public static Boolean isDebug(@NonNull Context context) {
        return getBuildConfigValue(context, DEBUG);
    }

    /**
     * BuildConfig.APPLICATION_ID
     *
     * @param context
     * @return
     */
    public static String getApplicationId(Context context) {
        return getBuildConfigValue(context, APPLICATION_ID);
    }

    /**
     * BuildConfig.PACKAGE_NAME
     *
     * @param context
     * @return
     */
    @Deprecated
    public static String getPackageName(@NonNull Context context) {
        return getBuildConfigValue(context, PACKAGE_NAME);
    }

    /**
     * BuildConfig.BUILD_TYPE
     *
     * @param context
     * @return
     */
    public static String getBuildType(@NonNull Context context) {
        return getBuildConfigValue(context, BUILD_TYPE);
    }

    /**
     * BuildConfig.FLAVOR
     *
     * @param context
     * @return
     */
    public static String getFlavor(@NonNull Context context) {
        return getBuildConfigValue(context, FLAVOR);
    }

    /**
     * BuildConfig.VersionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(@NonNull Context context) {
        return getBuildConfigValue(context, VERSION_NAME);
    }

    /**
     * BuildConfig.VERSION_CODE
     *
     * @param context
     * @return
     */
    public static Integer getVersionCode(@NonNull Context context) {
        return getBuildConfigValue(context, VERSION_CODE);
    }
}
