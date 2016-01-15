package com.github.snowdream.android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.squareup.leakcanary.RefWatcher;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hui.yang on 2015/2/7.
 */
public class Fragment extends android.support.v4.app.Fragment implements Page {
    private AtomicBoolean mIsActive = new AtomicBoolean(true);
    private AtomicBoolean mIsPaused = new AtomicBoolean(false);

    /**
     * @return the context from the application
     */
    public final Context getApplicationContext() {
        android.support.v4.app.FragmentActivity activity = getActivity();

        if (activity == null) {
            throw new IllegalStateException("Fragment " + this
                    + " not attached to Activity");
        }

        return activity.getApplicationContext();
    }

    /**
     * @return the context from the activity
     */
    public final Context getContext() {
        android.support.v4.app.FragmentActivity activity = getActivity();

        if (activity == null) {
            throw new IllegalStateException("Fragment " + this
                    + " not attached to Activity");
        }

        return activity.getBaseContext();
    }

    /**
     * The same as press the back key.
     *
     * @see android.support.v4.app.FragmentActivity#onBackPressed
     */
    public final void finishFragment() {
        android.support.v4.app.FragmentActivity activity = getActivity();

        if (activity == null) {
            throw new IllegalStateException("Fragment " + this
                    + " not attached to Activity");
        }

        activity.onBackPressed();
    }

    /**
     * close several fragment by step
     *
     * @param step the number of the fragments which will be finished.
     */
    public final void finishFragmentByStep(int step) {
        android.support.v4.app.FragmentActivity activity = getActivity();

        if (activity == null) {
            throw new IllegalStateException("Fragment " + this
                    + " not attached to Activity");
        }

        List<android.support.v4.app.Fragment> list = getFragmentManager().getFragments();
        if (list == null || list.size() < step) {
            throw new IllegalStateException("There is not enough Fragment to finish.");
        }

        for (int i = 0; i < step; i++) {
            activity.onBackPressed();
        }
    }

    /**
     * Return the com.github.snowdream.android.support.v4.app.FragmentActivity this fragment is currently associated with.
     * <p>
     * Warning: it may return null.
     */
    public final FragmentActivity getFragmentActivity() {
        android.support.v4.app.FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof FragmentActivity) {
            return (FragmentActivity) activity;
        }

        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsActive.set(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsPaused.set(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsPaused.set(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsActive.set(false);
        releaseHandlers();

        RefWatcher refWatcher = Application.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public boolean isActive() {
        return mIsActive.get();
    }

    @Override
    public boolean isPaused() {
        return mIsPaused.get();
    }

    private void releaseHandlers() {
        try {
            for(Class<?> clazz = getClass() ; clazz != Fragment.class ; clazz = clazz.getSuperclass()) {
                Field[] fields = clazz.getDeclaredFields();
                if (fields == null || fields.length <= 0) {
                    continue;
                }
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (!Handler.class.isAssignableFrom(field.getType())) continue;

                    Handler handler = (Handler) field.get(this);
                    if (handler != null && handler.getLooper() == Looper.getMainLooper()) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}