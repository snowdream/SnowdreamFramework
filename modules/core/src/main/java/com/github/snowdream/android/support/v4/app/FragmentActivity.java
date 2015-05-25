package com.github.snowdream.android.support.v4.app;

import android.content.Context;
import proguard.annotation.Keep;

/**
 * Created by hui.yang on 2015/2/7.
 */
public class FragmentActivity extends android.support.v4.app.FragmentActivity {
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

}
