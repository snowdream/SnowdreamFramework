package com.github.snowdream.android.core.task;

/**
 * Created by hui.yang on 2015/4/15.
 */
public final class TaskListener<Progress, Output> {


    public void onStart() {
    }

    public void onFinish() {
    }

    public void onSuccess(Output output) {
    }

    public void onProgress(Progress progress) {
    }

    public void onCancel() {
    }

    public void onError(Throwable thr) {
    }


}
