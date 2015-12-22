package com.github.snowdream.android.core.task;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.github.snowdream.android.core.Option;
import com.github.snowdream.android.support.v4.app.Page;
import com.github.snowdream.android.util.ThreadUtil;
import com.github.snowdream.android.util.TimingLogger;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hui.yang on 2015/4/15.
 */
public class Task<Result, Progress> implements Runnable, Cancelable {
    private TaskListener<Result, Progress> mTaskListener = null;
    private WeakReference<Page> mPageReference = null;
    private final AtomicBoolean mCancelled = new AtomicBoolean();
    private volatile Status mStatus = Status.PENDING;
    private TimingLogger mTimingLogger = null;

    /**
     * The Log tag to use for checking Log.isLoggable and for
     * logging the timings.
     */
    private String mTag;

    /**
     * A label to be included in every log.
     */
    private String mLabel;

    /**
     * Task name
     */
    private String mName;

    /**
     * Task Priority
     */
    private int mPriority;

    /**
     * Indicates the current status of the task. Each status will be set only once
     * during the lifetime of a task.
     */
    public enum Status {
        /**
         * Indicates that the task has not been executed yet.
         */
        PENDING,
        /**
         * Indicates that the task is running.
         */
        RUNNING,
        /**
         * Indicates that {@link Task#runOnUiThread} {@link Task#runOnNonUiThread}  or has finished.
         */
        FINISHED,
    }


    /**
     * Standard priority of application threads.
     * Use with {@link android.os.Process#setThreadPriority(int)} and
     * {@link android.os.Process#setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_DEFAULT = 0;

    /*
     * ***************************************
     * ** Keep in sync with utils/threads.h **
     * ***************************************
     */

    /**
     * Lowest available thread priority.  Only for those who really, really
     * don't want to run if anything else is happening.
     * Use with {@link android.os.Process#setThreadPriority(int)} and
     * {@link android.os.Process#setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_LOWEST = 19;

    /**
     * Standard priority background threads.  This gives your thread a slightly
     * lower than normal priority, so that it will have less chance of impacting
     * the responsiveness of the user interface.
     * Use with {@link android.os.Process#setThreadPriority(int)} and
     * {@link android.os.Process#setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_BACKGROUND = 10;

    /**
     * Standard priority of threads that are currently running a user interface
     * that the user is interacting with.  Applications can not normally
     * change to this priority; the system will automatically adjust your
     * application threads as the user moves through the UI.
     * Use with {@link android.os.Process#setThreadPriority(int)} and
     * {@link android.os.Process#setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_FOREGROUND = -2;

    /**
     * Standard priority of system display threads, involved in updating
     * the user interface.  Applications can not
     * normally change to this priority.
     * Use with {@link android.os.Process#setThreadPriority(int)} and
     * {@link android.os.Process#setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_DISPLAY = -4;

    /**
     * Standard priority of the most important display threads, for compositing
     * the screen and retrieving input events.  Applications can not normally
     * change to this priority.
     * Use with {@link android.os.Process#setThreadPriority(int)} and
     * {@link android.os.Process#setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_URGENT_DISPLAY = -8;

    /**
     * Standard priority of audio threads.  Applications can not normally
     * change to this priority.
     * Use with {@link android.os.Process#setThreadPriority(int)} and
     * {@link android.os.Process#setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_AUDIO = -16;

    /**
     * Standard priority of the most important audio threads.
     * Applications can not normally change to this priority.
     * Use with {@link android.os.Process#setThreadPriority(int)} and
     * {@link android.os.Process#setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_URGENT_AUDIO = -19;

    /**
     * Minimum increment to make a priority more favorable.
     */
    public static final int THREAD_PRIORITY_MORE_FAVORABLE = -1;

    /**
     * Minimum increment to make a priority less favorable.
     */
    public static final int THREAD_PRIORITY_LESS_FAVORABLE = +1;


    @SuppressWarnings("unused")
    private Task() {
        throw new AssertionError("The operation is not allowed.Use Task(TaskListener<Result,Progress> listener) instead.");
    }

    public Task(@NonNull TaskListener<Result, Progress> listener) {
        this("Task", "Task","Task", THREAD_PRIORITY_BACKGROUND,listener);
    }

    public Task(@NonNull String tag, @NonNull String label, @NonNull TaskListener<Result, Progress> listener) {
        this(tag, label, tag + label, THREAD_PRIORITY_BACKGROUND, listener);
    }

    public Task(@NonNull String tag, @NonNull String label, @NonNull String name, @NonNull TaskListener<Result, Progress> listener) {
        this(tag, label, name, THREAD_PRIORITY_BACKGROUND, listener);
    }

    public Task(@NonNull String tag, @NonNull String label, @NonNull String name, int priority, @NonNull TaskListener<Result, Progress> listener) {
        mTag = tag;
        mLabel = label;
        mName = name;
        mPriority = priority;
        mTaskListener = listener;
    }

    @CallSuper
    @Override
    public void run() {
        if (ThreadUtil.isOnNonUIThread()) {
            if (TextUtils.isEmpty(mName)) {
                Thread.currentThread().setName(mName);
            }

            android.os.Process.setThreadPriority(mPriority);
        }
    }

    @CallSuper
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        mCancelled.set(true);
        try {
            if (mayInterruptIfRunning && ThreadUtil.isOnNonUIThread()) {
                Thread t = Thread.currentThread();
                if (t != null) {
                    t.interrupt();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean isCancelled() {
        return mCancelled.get();
    }

    /**
     * Returns the current status of this task.
     *
     * @return The current status.
     */
    public final Status getStatus() {
        return mStatus;
    }

    public final Cancelable runOnUiThread(@NonNull Page page) {
        runOnThread(page, true);
        return this;
    }

    public final Cancelable runOnNonUiThread(@Nullable Page page) {
        runOnThread(page, false);
        return this;
    }

    private final void runOnThread(@Nullable Page page, boolean isRunningOnUiThread) {
        if (mStatus != Status.PENDING) {
            switch (mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task has already been executed "
                            + "(a task can be executed only once)");
            }
        }

        mStatus = Status.RUNNING;

        if (page != null) {
            mPageReference = new WeakReference<Page>(page);
        }

        performOnStart();

        mTimingLogger = new TimingLogger(mTag, mLabel);

        if (isRunningOnUiThread) {
            TaskManager.runOnUiThread(page, this);
        } else {
            TaskManager.runOnNonUiThread(this);
        }
    }

    /**
     * get TaskListener<Result,Progress>
     *
     * @return TaskListener<Result,Progress>
     */
    public TaskListener<Result, Progress> getTaskListener() {
        return mTaskListener;
    }

    /**
     * Check whether the page is active.
     *
     * @return
     */
    private boolean isPageActive() {
        boolean isActive = false;

        if (mPageReference == null || mPageReference.get() == null) {
            return isActive;
        }

        return mPageReference.get().isActive();
    }

    private void performOnStart() {
        if (mTaskListener == null) return;

        TaskManager.runOnNonUiThread(new Runnable() {
            @Override
            public void run() {
                mTaskListener.onStartNonUI();
            }
        });

        if (!isPageActive()) return;

        TaskManager.runOnUiThread(mPageReference.get(), new Runnable() {
            @Override
            public void run() {
                mTaskListener.onStartUI();
            }
        });
    }

    private void performOnFinish() {
        if (mTaskListener == null) return;

        mStatus = Status.FINISHED;

        TaskManager.runOnNonUiThread(new Runnable() {
            @Override
            public void run() {
                mTaskListener.onFinishNonUI();
                mTimingLogger.dumpToLog();
            }
        });

        if (!isPageActive()) return;

        TaskManager.runOnUiThread(mPageReference.get(), new Runnable() {
            @Override
            public void run() {
                mTaskListener.onFinishUI();
                mTimingLogger.dumpToLog();
            }
        });
    }

    protected void performOnCancel() {
        if (mTaskListener == null) return;
        if (!isCancelled()) return;

        performOnFinish();

        TaskManager.runOnNonUiThread(new Runnable() {
            @Override
            public void run() {
                mTaskListener.onCancelledNonUI();
            }
        });

        if (!isPageActive()) return;

        TaskManager.runOnUiThread(mPageReference.get(), new Runnable() {
            @Override
            public void run() {
                mTaskListener.onCancelledUI();
            }
        });
    }

    protected void performOnSuccess(final Result result) {
        if (mTaskListener == null) return;

        performOnFinish();

        if (isCancelled()) return;

        TaskManager.runOnNonUiThread(new Runnable() {
            @Override
            public void run() {
                mTaskListener.onSuccessNonUI(result);
            }
        });

        if (!isPageActive()) return;

        TaskManager.runOnUiThread(mPageReference.get(), new Runnable() {
            @Override
            public void run() {
                mTaskListener.onSuccessUI(result);
            }
        });
    }

    protected void performOnProgress(final Progress progress) {
        if (mTaskListener == null) return;

        performOnFinish();

        if (isCancelled()) return;

        TaskManager.runOnNonUiThread(new Runnable() {
            @Override
            public void run() {
                mTaskListener.onProgressNonUI(progress);
            }
        });

        if (!isPageActive()) return;

        TaskManager.runOnUiThread(mPageReference.get(), new Runnable() {
            @Override
            public void run() {
                mTaskListener.onProgressUI(progress);
            }
        });
    }

    protected void performOnError(final Throwable thr) {
        if (mTaskListener == null) return;

        performOnFinish();

        if (isCancelled()) return;

        TaskManager.runOnNonUiThread(new Runnable() {
            @Override
            public void run() {
                mTaskListener.onErrorNonUI(thr);
            }
        });

        if (!isPageActive()) return;

        TaskManager.runOnUiThread(mPageReference.get(), new Runnable() {
            @Override
            public void run() {
                mTaskListener.onErrorUI(thr);
            }
        });
    }
}

