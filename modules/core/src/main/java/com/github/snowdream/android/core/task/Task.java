package com.github.snowdream.android.core.task;

import com.github.snowdream.android.support.v4.app.Page;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hui.yang on 2015/4/15.
 */
public abstract class Task<Result, Progress> implements Runnable, Cancelable {
    private TaskListener<Result, Progress> mTaskListener = null;
    private WeakReference<Page> mPageReference = null;
    private final AtomicBoolean mCancelled = new AtomicBoolean();
    private volatile Status mStatus = Status.PENDING;

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
         * Indicates that {@link AsyncTask#onPostExecute} has finished.
         */
        FINISHED,
    }
    @SuppressWarnings("unused")
    private Task() {
        throw new AssertionError("The operation is not allowed.Use Task(TaskListener<Result,Progress> listener) instead.");
    }

    public Task(TaskListener<Result, Progress> listener) {
        mTaskListener = listener;
    }

    @Override
    public abstract void run();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        mCancelled.set(true);
        try {
            if (mayInterruptIfRunning) {
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

    public final void runOnUiThread(Page page) {
        runOnThread(page,true);
    }

    public final void runOnNonUiThread(Page page) {
        runOnThread(page,false);
    }

    private final void runOnThread(Page page,boolean isRunningOnUiThread) {
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

        mPageReference = new WeakReference<Page>(page);

        performOnStart();
        if (isRunningOnUiThread){
            TaskManager.runOnUiThread(page, this);
        }else{
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
            }
        });

        if (!isPageActive()) return;

        TaskManager.runOnUiThread(mPageReference.get(), new Runnable() {
            @Override
            public void run() {
                mTaskListener.onFinishUI();
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

