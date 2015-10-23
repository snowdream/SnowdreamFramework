package com.github.snowdream.android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import com.squareup.leakcanary.RefWatcher;
import proguard.annotation.Keep;

import java.util.List;

/**
 * Created by hui.yang on 2015/2/7.
 */
public class Fragment extends android.support.v4.app.Fragment implements Page{
    private boolean mIsActive;
    private boolean mIsPaused;

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
        mIsActive = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsPaused = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsActive = false;

        RefWatcher refWatcher = Application.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }
}