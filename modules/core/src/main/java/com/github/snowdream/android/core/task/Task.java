package com.github.snowdream.android.core.task;

import com.github.snowdream.android.support.v4.app.Page;

import java.lang.ref.WeakReference;

/**
 * Created by hui.yang on 2015/4/15.
 */
public abstract class Task<Result,Progress> implements Runnable,Cancelable{
    private TaskListener<Result,Progress> mTaskListener = null;
    private WeakReference<Page> mPageReference = null;

    @SuppressWarnings("unused")
    private Task(){
        throw new AssertionError("The operation is not allowed.Use Task(TaskListener<Result,Progress> listener) instead.");
    }

    public Task(TaskListener<Result,Progress> listener){
        mTaskListener = listener;
    }

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
        mPageReference = new WeakReference<Page>(page);
        performOnStart();
        TaskManager.runOnUiThread(page,this);
    }

    public final void runOnNonUiThread(Page page) {
        mPageReference = new WeakReference<Page>(page);
        performOnStart();
        TaskManager.runOnNonUiThread(this);
    }

    /**
     * get TaskListener<Result,Progress>
     * @return TaskListener<Result,Progress>
     */
    public TaskListener<Result,Progress> getTaskListener(){
        return mTaskListener;
    }

    /**
     * Check whether the page is active.
     *
     * @return
     */
    private boolean isPageActive(){
        boolean isActive = false;

        if (mPageReference == null || mPageReference.get() == null){
            return isActive;
        }

        return  mPageReference.get().isActive();
    }

    private void performOnStart(){
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

    private void performOnFinish(){
        if (mTaskListener == null) return;

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

    private void performOnCancel(){
        if (mTaskListener == null) return;

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

    private void performOnSuccess(final Result result){
        if (mTaskListener == null) return;

        performOnFinish();
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

    private void performOnProgress(final Progress progress){
        if (mTaskListener == null) return;

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

    private void performOnError(final Throwable thr){
        if (mTaskListener == null) return;

        performOnFinish();
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

