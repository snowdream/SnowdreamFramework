package com.github.snowdream.android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import com.squareup.leakcanary.RefWatcher;
import proguard.annotation.Keep;

/**
 * Created by hui.yang on 2015/2/7.
 */
public class FragmentActivity extends android.support.v4.app.FragmentActivity  implements Page{
    private boolean mIsActive;
    private boolean mIsPaused;

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
