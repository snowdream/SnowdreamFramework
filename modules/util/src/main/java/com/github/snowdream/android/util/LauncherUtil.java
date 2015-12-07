package com.github.snowdream.android.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by hui.yang on 2015/11/19.
 * see: http://blog.zanlabs.com/2015/03/14/android-shortcut-summary/
 * see: https://gist.github.com/waylife/437a3d98a84f245b9582
 */
public class LauncherUtil {
    private static String mBufferedValue = null;

    private LauncherUtil() {
        throw new AssertionError("No constructor allowed here!");
    }

    /**
     * get the current Launcher's Package Name
     */
    public static String getCurrentLauncherPackageName(@NonNull Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res == null || res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }

    /**
     * default permission is "com.android.launcher.permission.READ_SETTINGS"<br/>
     * {@link #getAuthorityFromPermission(Context, String)}<br/>
     *
     * @param context
     */
    public static String getAuthorityFromPermissionDefault(@NonNull Context context) {
        if (TextUtils.isEmpty(mBufferedValue))//we get value buffered
            mBufferedValue = getAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS");
        return mBufferedValue;
    }

    /**
     * be cautious to use this, it will cost about 500ms 此函数为费时函数，大概占用500ms左右的时间<br/>
     * android系统桌面的基本信息由一个launcher.db的Sqlite数据库管理，里面有三张表<br/>
     * 其中一张表就是favorites。这个db文件一般放在data/data/com.android.launcher(launcher2)文件的databases下<br/>
     * 但是对于不同的rom会放在不同的地方<br/>
     * 例如MIUI放在data/data/com.miui.home/databases下面<br/>
     * htc放在data/data/com.htc.launcher/databases下面<br/
     *
     * @param context
     * @param permission 读取设置的权限  READ_SETTINGS_PERMISSION
     * @return
     */
    public static String getAuthorityFromPermission(@NonNull Context context, @NonNull String permission) {
        if (TextUtils.isEmpty(permission)) {
            return "";
        }

        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs == null) {
                return "";
            }
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission) || permission.equals(provider.writePermission)) {
                            return provider.authority;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
