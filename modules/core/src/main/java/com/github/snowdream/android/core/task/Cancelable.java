package com.github.snowdream.android.core.task;

/**
 * Something, usually a task, that can be cancelled. Cancellation is performed by the cancel method.
 * Additional methods are provided to determine if the task completed normally or was cancelled.
 *
 * Created by hui.yang on 2015/10/21.
 */
public interface Cancelable{
    /**
     * Attempt to cancel execution of this task. This attempt will fail if the task has already completed, already been cancelled, or could not be cancelled for some other reason.
     * If successful, and this task has not started when cancel is called, this task should never run.
     * If the task has already started, then the interruptIfRunning parameter determines whether the thread executing this task should be interrupted in an attempt to stop the task.
     * @param mayInterruptIfRunning  true if the thread executing this task should be interrupted; otherwise, in-progress tasks are allowed to complete
     * @return false if the task could not be cancelled, typically because is has already completed normally; true otherwise
     */
    public boolean cancel(boolean mayInterruptIfRunning);

    /**
     * Returns true if this task was cancelled before it completed normally.
     *
     * @return true if task was cancelled before it completed
     */
    public boolean isCancelled();
}
