package com.github.snowdream.android.util;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yanghui.yangh on 2016-07-11.
 */
public class SDCardUtil {
    private SDCardUtil() {
        throw new AssertionError("No constructor allowed here!");
    }

    public static
    @Nullable
    File getAvailableStorageDirectory(@NonNull Context mContext) {
        String externalPath = getStoragePath(mContext, true);
        if (!TextUtils.isEmpty(externalPath)) {
            return new File(externalPath);
        }

        String internalPath = getStoragePath(mContext, false);
        if (!TextUtils.isEmpty(internalPath)) {
            return new File(internalPath);
        }

        return null;
    }

    public static
    @Nullable
    File getExternalStorageDirectory(@NonNull Context mContext) {
        String path = getStoragePath(mContext, true);
        if (TextUtils.isEmpty(path)) return null;

        return new File(path);
    }

    public static
    @Nullable
    File getInternalStorageDirectory(@NonNull Context mContext) {
        String path = getStoragePath(mContext, false);
        if (TextUtils.isEmpty(path)) return null;

        return new File(path);
    }

    private static
    @Nullable
    String getStoragePath(@NonNull Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getVolumeState = StorageManager.class.getMethod(
                    "getVolumeState", String.class);
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                String state = (String) getVolumeState.invoke(mStorageManager,
                        getPath.invoke(storageVolumeElement));

                if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) continue;

                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
