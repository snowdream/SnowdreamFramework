package com.github.snowdream.android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hui.yang on 2015/2/7.
 */
public class FragmentActivity extends android.support.v4.app.FragmentActivity implements Page {
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
        releaseHandlers();
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
            for(Class<?> clazz = getClass() ; clazz != FragmentActivity.class ; clazz = clazz.getSuperclass()) {
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
