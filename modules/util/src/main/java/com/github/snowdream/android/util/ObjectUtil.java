package com.github.snowdream.android.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Collection;
import java.util.Map;

/**
 * see: http://hualong.iteye.com/blog/1938284
 * see: http://blog.csdn.net/elwy_cn/article/details/21181097
 * Created by yanghui.yangh on 2015/12/30.
 */
public class ObjectUtil {

    private ObjectUtil() {
        throw new AssertionError("No constructor allowed here!");
    }

    private static boolean isNull(@Nullable Object object) {
        return object == null;
    }

    private static boolean isNotNull(@Nullable Object object) {
        return object != null;
    }

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isNullOrEmpty(@Nullable CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * Returns true if the collection is null or 0-length.
     * @param collection the collection to be examined
     * @return true if collection is null or zero length
     */
    public static boolean isNullOrEmpty(@Nullable Collection collection) {
        return isNull(collection) || collection.isEmpty();
    }

    /**
     * Returns true if the map is null or 0-length.
     * @param map the map to be examined
     * @return true if map is null or zero length
     */
    public static boolean isNullOrEmpty(@Nullable Map map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * Returns true if the objects is null or 0-length.
     * @param objects the objects to be examined
     * @return true if objects is null or zero length
     */
    public static boolean isNullOrEmpty(@Nullable Object[] objects) {
        return isNull(objects) || objects.length == 0;
    }

    /**
     * Returns true if the object is null
     * @param object the objects to be examined
     * @return true if object is null
     */
    public static boolean isNullOrEmpty(@Nullable Object object) {
        return isNull(object);
    }
}
