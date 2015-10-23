package com.github.snowdream.android.core.task;

import com.github.snowdream.android.support.v4.app.Page;

/**
 * Created by hui.yang on 2015/4/15.
 */
public abstract class Task implements Runnable,Cancelable{
    @Override
    public abstract void run();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    public final void runOnUiThread(Page page) {
        TaskManager.runOnUiThread(page,this);
    }

    public final void runOnNonUiThread(Page page) {
        TaskManager.runOnNonUiThread(this);
    }
}
