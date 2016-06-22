package com.github.snowdream.android.widget;

/**
 * Created by yanghui.yangh on 2016/6/22.
 */
public interface PriorityView extends Comparable<PriorityView> {
    public static enum ACTION {COVER, SKIP, SHOW}

    public static final int ACTION_SHOW= 0;
    public static final int ACTION_COVER = 1;
    public static final int ACTION_SKIP = 2;


    public void show();

    public void hide();

    public void onShowed();

    public ACTION getAction();
}

