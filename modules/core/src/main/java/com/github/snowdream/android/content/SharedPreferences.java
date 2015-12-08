package com.github.snowdream.android.content;

/**
 * Created by snowdream on 4/2/14.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.github.snowdream.android.util.log.Log;

import java.util.Map;
import java.util.Set;

public class SharedPreferences {
    private android.content.SharedPreferences.Editor editor = null;
    private android.content.SharedPreferences sharedPreferences = null;

    public SharedPreferences(final Context context) {
        if (context == null) {
            Log.e("The context is null,please check it!");
            return;
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public SharedPreferences(final Context context, final String name, final int mode) {
        if (context == null) {
            Log.e("The context is null,please check it.");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Log.e("The name of the preference is null or not valid.");
            return;
        }

        if (mode != Context.MODE_PRIVATE && mode != Context.MODE_WORLD_READABLE
                && mode != Context.MODE_WORLD_WRITEABLE && mode != Context.MODE_MULTI_PROCESS) {
            Log.e("The mode of the preference is not valid.");
            return;
        }

        sharedPreferences = context.getSharedPreferences(name, mode);
        editor = sharedPreferences.edit();
    }

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key The name of the preference to check.
     * @return Returns true if the preference exists in the preferences,
     * otherwise false.
     */
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * Retrieve all values from the preferences. Note that you must not modify
     * the collection returned by this method, or alter any of its contents. The
     * consistency of your stored data is not guaranteed if you do.
     *
     * @return Returns a map containing a list of pairs key/value representing
     * the preferences.
     * @throws NullPointerException
     */
    public Map<String, ?> getAll() throws NullPointerException {
        return sharedPreferences.getAll();
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with this name that
     * is not a boolean.
     * @throws ClassCastException
     */
    public boolean getBoolean(String key, boolean defValue) throws ClassCastException {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Retrieve a float value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with this name that
     * is not a float.
     * @throws ClassCastException
     */
    public float getFloat(String key, float defValue) throws ClassCastException {
        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with this name that
     * is not an int.
     * @throws ClassCastException
     */
    public int getInt(String key, int defValue) throws ClassCastException {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with this name that
     * is not a String.
     * @throws ClassCastException
     */
    public String getString(String key, String defValue) throws ClassCastException {
        return sharedPreferences.getString(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defValues) throws ClassCastException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return defValues;
        }

        return sharedPreferences.getStringSet(key, defValues);
    }

    /**
     * Registers a callback to be invoked when a change happens to a preference.
     *
     * @param listener The callback that will run.
     * @see SharedPreferences#unregisterOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener)
     */
    public void registerOnSharedPreferenceChangeListener(
            android.content.SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregisters a previous callback.
     *
     * @param listener The callback that should be unregistered.
     * @see SharedPreferences#registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener)
     */
    public void unregisterOnSharedPreferenceChangeListener(
            android.content.SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Commit your preferences changes back from this Editor to the
     * SharedPreferences object it is editing. This atomically performs the
     * requested modifications, replacing whatever is currently in the
     * SharedPreferences.</BR> Note that when two editors are modifying
     * preferences at the same time, the last one to call apply wins.</BR>
     * Unlike commit(), which writes its preferences out to persistent storage
     * synchronously, apply() commits its changes to the in-memory
     * SharedPreferences immediately but starts an asynchronous commit to disk
     * and you won't be notified of any failures. If another editor on this
     * SharedPreferences does a regular commit() while a apply() is still
     * outstanding, the commit() will block until all async commits are
     * completed as well as the commit itself.</BR> As SharedPreferences
     * instances are singletons within a process, it's safe to replace any
     * instance of commit() with apply() if you were already ignoring the return
     * value.</BR> You don't need to worry about Android component lifecycles
     * and their interaction with apply() writing to disk. The framework makes
     * sure in-flight disk writes from apply() complete before switching
     * states.</BR>
     *
     * @see android.content.SharedPreferences.Editor#apply()
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void apply() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    /**
     * <p>
     * Mark in the editor to remove all values from the preferences. Once commit
     * is called, the only remaining preferences will be any that you have
     * defined in this editor.
     * </p>
     * <p>
     * Note that when committing back to the preferences, the clear is done
     * first, regardless of whether you called clear before or after put methods
     * on this editor.
     * </p>
     *
     * @return
     */
    public SharedPreferences clear() {
        editor.clear();
        return this;
    }

    /**
     * <p>
     * Commit your preferences changes back from this Editor to the
     * SharedPreferences object it is editing. This atomically performs the
     * requested modifications, replacing whatever is currently in the
     * SharedPreferences.
     * </p>
     * <p>
     * Note that when two editors are modifying preferences at the same time,
     * the last one to call commit wins.
     * </p>
     * <p>
     * If you don't care about the return value and you're using this from your
     * application's main thread, consider using apply() instead.
     * </p>
     *
     * @return Returns true if the new values were successfully written to
     * persistent storage.
     */
    public boolean commit() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            return editor.commit();
        } else {
            editor.apply();
            return true;
        }
    }

    /**
     * Set a boolean value in the preferences editor, to be written back with
     * auto commit.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can chain
     * put calls together.
     */
    public SharedPreferences putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return this;
    }

    /**
     * Set a float value in the preferences editor, to be written back with auto
     * commit.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can chain
     * put calls together.
     */
    public SharedPreferences putFloat(String key, float value) {
        editor.putFloat(key, value);
        return this;
    }

    /**
     * Set an int value in the preferences editor, to be written back with auto
     * commit.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can chain
     * put calls together.
     */
    public SharedPreferences putInt(String key, int value) {
        editor.putInt(key, value);
        return this;
    }

    /**
     * Set a long value in the preferences editor, to be written back with auto
     * commit.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can chain
     * put calls together.
     */
    public SharedPreferences putLong(String key, long value) {
        editor.putLong(key, value);
        return this;
    }

    /**
     * Set a String value in the preferences editor, to be written back with
     * auto commit.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can chain
     * put calls together.
     */
    public SharedPreferences putString(String key, String value) {
        editor.putString(key, value);
        return this;
    }

    /**
     * Set a set of String values in the preferences editor, to be written back
     * with auto commit.
     *
     * @param key    The name of the preference to modify.
     * @param values The set of new values for the preference. Passing null for
     *               this argument is equivalent to calling remove(String) with
     *               this key.
     * @return Returns a reference to the same Editor object, so you can chain
     * put calls together.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SharedPreferences putStringSet(String key, Set<String> values) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return this;
        }
        editor.putStringSet(key, values);
        return this;
    }

    /**
     * <p>
     * Mark in the editor that a preference value should be removed with auto
     * commit.
     * </p>
     * <p>
     * Note that when committing back to the preferences, all removals are done
     * first, regardless of whether you called remove before or after put
     * methods on this editor.
     * </p>
     *
     * @param key The name of the preference to remove.
     * @return Returns a reference to the same Editor object, so you can chain
     * put calls together.
     */
    public SharedPreferences remove(String key) {
        editor.remove(key);
        return this;
    }

}
