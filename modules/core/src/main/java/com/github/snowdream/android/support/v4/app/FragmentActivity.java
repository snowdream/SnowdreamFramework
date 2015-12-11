package com.github.snowdream.android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import com.squareup.leakcanary.RefWatcher;
import proguard.annotation.Keep;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hui.yang on 2015/2/7.
 */
public class FragmentActivity extends android.support.v4.app.FragmentActivity  implements Page{
    private AtomicBoolean mIsActive = new AtomicBoolean(true);
    private AtomicBoolean mIsPaused = new AtomicBoolean(false);

    /**
     * @return the context from the activity
     */
    public final Context getContext() {
        return getBaseContext();
    }

    /**
     * The same as finish.
     *
     * @see android.support.v4.app.FragmentActivity#finish
     */
    public final void finishActivity() {
        finish();
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
