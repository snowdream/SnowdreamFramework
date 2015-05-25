package com.github.snowdream.android.os;

import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by hui.yang on 2015/4/10.
 */
public class Handler<T> extends android.os.Handler {
    private WeakReference<T> containerWeakReference = null;
    private final Callback<T>  mCallback;


    public interface Callback<T> {
        /**
         * Callback interface you can use when instantiating a Handler to avoid
         * having to implement your own subclass of Handler.
         *
         * @param container container,such as activity,fragment,etc
         * @param msg       A {@link android.os.Message Message} object
         * @return True if no further handling is desired
         */
        public boolean handleMessage(T container, Message msg);
    }

    /**
     * Default constructor associates this handler with the {@link Looper} for the
     * current thread.
     *
     * If this thread does not have a looper, this handler won't be able to receive messages
     * so an exception is thrown.
     *
     * @param container container,such as activity,fragment,etc
     */
    public Handler(T container) {
        super();
        this.containerWeakReference = new WeakReference<T>(container);
        this.mCallback = null;
    }

    /**
     * Constructor associates this handler with the {@link Looper} for the
     * current thread and takes a callback interface in which you can handle
     * messages.
     *
     * If this thread does not have a looper, this handler won't be able to receive messages
     * so an exception is thrown.
     *
     * @param container container,such as activity,fragment,etc
     * @param callback The callback interface in which to handle messages, or null.
     */
    public Handler(T container, Callback callback) {
        mCallback = callback;
        this.containerWeakReference = new WeakReference<T>(container);
    }

    /**
     * Use the provided {@link Looper} instead of the default one.
     *
     * @param container container,such as activity,fragment,etc
     * @param looper The looper, must not be null.
     */
    public Handler(T container, Looper looper) {
        super(looper);
        this.containerWeakReference = new WeakReference<T>(container);
        this.mCallback = null;
    }

    /**
     * Use the provided {@link Looper} instead of the default one and take a callback
     * interface in which to handle messages.
     *
     * @param container container,such as activity,fragment,etc
     * @param looper The looper, must not be null.
     * @param callback The callback interface in which to handle messages, or null.
     */
    public Handler(T container, Looper looper, Callback callback) {
        super(looper, null);
        mCallback = callback;
        this.containerWeakReference = new WeakReference<T>(container);
    }

    /**
     * Handle system messages here.
     */
    @Override
    public final void dispatchMessage(Message msg) {
        if (containerWeakReference == null || containerWeakReference.get() == null) {
            return;
        }

        T tmpContainer = containerWeakReference.get();

        if (msg.getCallback() != null) {
            msg.getCallback().run();
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(tmpContainer, msg)) {
                    return;
                }
            }
            handleMessage(tmpContainer, msg);
            handleMessage(msg);
        }
    }

    /**
     * Subclasses must implement this to receive messages.
     *
     * @param container container,such as activity,fragment,etc
     * @param msg       A {@link android.os.Message Message} object
     */
    public void handleMessage(T container, Message msg) {
    }

    /**
     * Subclasses must implement this to receive messages.
     *
     * @param msg       A {@link android.os.Message Message} object
     */
    @Override
    @Deprecated
    public void handleMessage(Message msg) {
    }
}
