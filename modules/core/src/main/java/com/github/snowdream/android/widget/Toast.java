package com.github.snowdream.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Toast
 *
 * Created by yanghui.yangh on 2016/6/15.
 */
public class Toast {
    private static android.widget.Toast mToast = null;
    private static ToastBean mToastBean = null;
    private static ToastBean mToastBeanPortrait = null;
    private static ToastBean mToastBeanLandscape = null;

    /**
     * @hide
     */
    @IntDef({ONE_FIRST, ONE_LAST, MANY_IN_ORDER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Policy {
    }

    /**
     * @hide
     */
    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }


    /**
     * If one toast is showing, then the next toast will be skipped.
     */
    public static final int ONE_FIRST = 0;

    /**
     * If one toast is showing, then it will be calcelled, and the next toast will be shown.
     */
    public static final int ONE_LAST = 1;

    /**
     * All the toasts will be shown in order. It is the default policy for android,but not prefect.
     */
    public static final int MANY_IN_ORDER = 2;

    private static int mPolicy = ONE_LAST;

    /**
     * Show the view or text notification for a short period of time.  This time could be
     * user-definable.  This is the default.
     *
     * @see ToastBean#setDuration
     */
    public static final int LENGTH_SHORT = 0;

    /**
     * Show the view or text notification for a long period of time.  This time could be
     * user-definable.
     *
     * @see ToastBean#setDuration
     */
    public static final int LENGTH_LONG = 1;


    //when activity or fragment change Configuration
    public static void onConfigurationChanged(Configuration newConfig) {
        if (!isShown()) return;
        cancel();
        if (mToastBeanPortrait != null && mToastBeanLandscape != null){
            show(mToastBeanPortrait,mToastBeanLandscape);
        }
    }

    //release
    public static void release(){
        cancel();
        mPolicy = ONE_LAST;
    }

    //cancel
    public static void cancel(){
        if (mToast != null){
            mToast.cancel();
        }
        mToast = null;
        mToastBean = null;
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application} or {@link
     *                 android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     */
    public static void show(Context context, CharSequence text, @Duration int duration) {
        ToastBean bean = new ToastBean(context, text, duration);
        show(bean);
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application} or {@link
     *                 android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static void show(Context context, @StringRes int resId, @Duration int duration) {
        ToastBean bean = new ToastBean(context, resId, duration);
        show(bean);
    }

    /**
     * Make a toast with custom view
     *
     * @param context  The context to use.  Usually your {@link android.app.Application} or {@link
     *                 android.app.Activity} object.
     * @param view     The view to show.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     */
    public static void show(Context context, View view, @Duration int duration) {
        ToastBean bean = new ToastBean(context, view, duration);
        show(bean);
    }

    /**
     * Make a toast with toastBean
     *
     * @param portrait The ToastBean to show for portrait.
     * @param landscape The ToastBean to show for landscape.
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static void show(@NonNull ToastBean portrait,@NonNull ToastBean landscape){
        mToastBeanLandscape = landscape;
        mToastBeanPortrait = portrait;

        Context context = portrait.mContext;
        ToastBean bean;
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            bean = mToastBeanPortrait;
        }else{
            bean = mToastBeanPortrait;
        }

        show(bean);
    }


    /**
     * Make a toast with toastBean
     *
     * @param bean The ToastBean to show.
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static void show(@NonNull ToastBean bean){
        if (!isShown()) {
            mToastBean = bean;
            mToast = createOrUpdateToastFromToastBean(null, mToastBean);
            mToast.show();
        } else {
            switch (mPolicy) {
                case ONE_FIRST:
                    break;
                case MANY_IN_ORDER:
                    mToastBean = bean;
                    mToast = createOrUpdateToastFromToastBean(null, mToastBean);
                    mToast.show();
                    break;
                case ONE_LAST:
                default:
                    if (mToastBean.isStandard() && bean.isStandard()) {
                        if (mToastBean.equals(bean)) {
                            if (!TextUtils.isEmpty(bean.text)) {
                                mToast.setText(bean.text);
                            } else if (bean.resId != -1) {
                                mToast.setText(bean.resId);
                            }
                        } else {
                            createOrUpdateToastFromToastBean(mToast, mToastBean);
                        }
                    }else {
                        createOrUpdateToastFromToastBean(mToast, mToastBean);
                        mToast.show();
                    }
                    break;
            }
        }
    }

    /**
     * create or update toast from toast bean
     *
     * @param toast Toast
     * @param bean  Toast Bean
     */
    private static android.widget.Toast createOrUpdateToastFromToastBean(android.widget.Toast toast, @NonNull ToastBean bean) {
        int duration;
        if (bean.mDuration == android.widget.Toast.LENGTH_SHORT) {
            duration = android.widget.Toast.LENGTH_SHORT;
        } else {
            duration = android.widget.Toast.LENGTH_LONG;
        }

        if (toast == null) { //create toast
            if (!TextUtils.isEmpty(bean.text)) {
                toast = android.widget.Toast.makeText(bean.mContext, bean.text, duration);
                bean.mView = toast.getView();
            } else if (bean.resId != -1) {
                toast = android.widget.Toast.makeText(bean.mContext, bean.resId, duration);
                bean.mView = toast.getView();
            } else {
                toast = new android.widget.Toast(bean.mContext);
                toast.setView(bean.mView);
            }
        } else {   //update toast
            if (!TextUtils.isEmpty(bean.text)) {
                mToast.setText(bean.text);
            } else if (bean.resId != -1) {
                mToast.setText(bean.resId);
            } else if (bean.mView != null) {
                mToast.setView(bean.mView);
            }
        }

        toast.setGravity(bean.mGravity, bean.mX, bean.mY);
        toast.setMargin(bean.mHorizontalMargin, bean.mHorizontalMargin);

        return toast;
    }


    /**
     * Check whether the toast is showing now.
     */
    private static boolean isShown() {
        boolean isHidden = (mToastBean == null || mToast == null || mToast.getView() == null || mToast.getView().getWindowVisibility() != View.VISIBLE);

        return !isHidden;
    }

    /**
     * Return the policy.
     *
     * @see #setPolicy
     */
    @Policy
    public static int getPolicy() {
        return mPolicy;
    }

    /**
     * Set which policy to show the toast
     *
     * @see #ONE_FIRST
     * @see #ONE_LAST
     * @see #MANY_IN_ORDER
     */
    public static void setPolicy(@Policy int mPolicy) {
        Toast.mPolicy = mPolicy;
    }

    public static class ToastBean {
        final Context mContext;
        int mDuration = Toast.LENGTH_SHORT;
        View mView;
        CharSequence text = null;
        int resId = -1;
        int mGravity;
        int mX, mY;
        float mHorizontalMargin;
        float mVerticalMargin;

        private ToastBean() {
            throw new AssertionError("No constructor allowed here!");
        }

        public ToastBean(@NonNull Context context, @StringRes CharSequence text, @Duration int duration) {
            this.mContext = context;
            this.text = text;
            this.mDuration = duration;
        }

        public ToastBean(@NonNull Context context, @StringRes int resId, @Duration int duration) {
            this.mContext = context;
            this.resId = resId;
            this.text = context.getResources().getText(resId);
            this.mDuration = duration;
        }

        public ToastBean(@NonNull Context context, View view, @Duration int duration) {
            this.mContext = context;
            this.mView = view;
            this.mDuration = duration;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;

            if (!(obj instanceof ToastBean)) return false;

            ToastBean bean = (ToastBean) obj;

            if (this.text != bean.text) {
                if ((this.text == null) || (bean.text == null)) return false;

                if (!this.text.equals(bean.text)) {
                    return false;
                }
            }

            if ((this.text != null) || (bean.text == null)) {
                return false;
            } else if ((this.text == null) || (bean.text != null)) {
                return false;
            } else if ((this.text == null) || (bean.text == null)) {
                if (this.mView != bean.mView) return false;
            } else {
                //Nothing to do.
            }

            if (this.mGravity != bean.mGravity ||
                    this.mX != bean.mX || this.mY != bean.mY ||
                    this.mHorizontalMargin != bean.mHorizontalMargin ||
                    this.mVerticalMargin != bean.mVerticalMargin) {
                return false;
            }

            return true;
        }

        /**
         * Check whether the toast is standard which just contains a text view.
         */
        public boolean isStandard() {
            return !TextUtils.isEmpty(text) || resId != -1;
        }

        /**
         * Set the view to show.
         *
         * @see #getView
         */
        public void setView(View view) {
            mView = view;
        }

        /**
         * Return the view.
         *
         * @see #setView
         */
        public View getView() {
            return mView;
        }

        /**
         * Set how long to show the view for.
         *
         * @see #LENGTH_SHORT
         * @see #LENGTH_LONG
         */
        public void setDuration(@Duration int duration) {
            mDuration = duration;
        }

        /**
         * Return the duration.
         *
         * @see #setDuration
         */
        @Duration
        public int getDuration() {
            return mDuration;
        }

        /**
         * Set the margins of the view.
         *
         * @param horizontalMargin The horizontal margin, in percentage of the container width,
         *                         between the container's edges and the notification
         * @param verticalMargin   The vertical margin, in percentage of the container height,
         *                         between the container's edges and the notification
         */
        public void setMargin(float horizontalMargin, float verticalMargin) {
            mHorizontalMargin = horizontalMargin;
            mVerticalMargin = verticalMargin;
        }

        /**
         * Return the horizontal margin.
         */
        public float getHorizontalMargin() {
            return mHorizontalMargin;
        }

        /**
         * Return the vertical margin.
         */
        public float getVerticalMargin() {
            return mVerticalMargin;
        }

        /**
         * Set the location at which the notification should appear on the screen.
         *
         * @see android.view.Gravity
         * @see #getGravity
         */
        public void setGravity(int gravity, int xOffset, int yOffset) {
            mGravity = gravity;
            mX = xOffset;
            mY = yOffset;
        }

        /**
         * Get the location at which the notification should appear on the screen.
         *
         * @see android.view.Gravity
         * @see #getGravity
         */
        public int getGravity() {
            return mGravity;
        }

        /**
         * Return the X offset in pixels to apply to the gravity's location.
         */
        public int getXOffset() {
            return mX;
        }

        /**
         * Return the Y offset in pixels to apply to the gravity's location.
         */
        public int getYOffset() {
            return mY;
        }
    }
}
