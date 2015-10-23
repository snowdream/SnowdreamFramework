package com.github.snowdream.android.core.task;

import android.os.Handler;
import android.os.Looper;
import com.github.snowdream.android.support.v4.app.Page;
import com.github.snowdream.android.util.Log;
import com.github.snowdream.android.util.ThreadUtil;

/**
 * Created by hui.yang on 2015/10/21.
 */
public class TaskManager {
    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    private TaskManager() {
        throw new AssertionError();
    }

    public static final void runOnUiThread(Page page,Runnable action) {
        if (page == null  || !page.isActive()){
            Log.w("Page is null or not active.");
            return;
        }

        if (ThreadUtil.isOnNonUIThread()) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }

    public static final void runOnNonUiThread(Runnable action) {
        if (ThreadUtil.isOnUIThread()) {
            TaskExecutorManager.getExecutor().execute(action);
        } else {
            action.run();
        }
    }
}
