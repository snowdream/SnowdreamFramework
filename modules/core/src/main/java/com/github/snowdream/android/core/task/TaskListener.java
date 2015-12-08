package com.github.snowdream.android.core.task;

/**
 * Created by hui.yang on 2015/4/15.
 */
public class TaskListener<Result,Progress> {
    public void onStartUI() {
    }

    public void onFinishUI() {
    }

    public void onSuccessUI(Result result) {
    }

    public void onProgressUI(Progress progress) {
    }

    public void onCancelledUI() {
    }

    public void onErrorUI(Throwable thr) {
    }

    public void onStartNonUI() {
    }

    public void onFinishNonUI() {
    }

    public void onSuccessNonUI(Result result) {
    }

    public void onProgressNonUI(Progress progress) {
    }

    public void onCancelledNonUI() {
    }

    public void onErrorNonUI(Throwable thr) {
    }
}
