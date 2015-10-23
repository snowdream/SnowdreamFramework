package com.github.snowdream.android.support.v4.app;

/**
 *
 * Page,may be an activity, a fragment, or a dialog.
 *
 * Created by hui.yang on 2015/10/21.
 */
public interface Page {
    public boolean isActive();
    public boolean isPaused();
}
