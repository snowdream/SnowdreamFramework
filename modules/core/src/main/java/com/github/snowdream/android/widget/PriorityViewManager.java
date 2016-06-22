package com.github.snowdream.android.widget;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanghui.yangh on 2016/6/22.
 */
public class PriorityViewManager {
    private static List<PriorityView> mViews = new ArrayList<PriorityView>();

    public boolean show(@NonNull PriorityView view) {
        boolean isShow = false;

        if (mViews.isEmpty()) {
            view.show();
            isShow = true;
        } else {
            PriorityView current = mViews.get(mViews.size() - 1);

            if (view.compareTo(current) >= 0) {
                switch (view.getAction()) {
                    case SHOW:
                        current.hide();
                    case COVER:
                        isShow = true;
                        view.show();
                        break;
                    case SKIP:
                        isShow = false;
                        break;
                }

            }
        }


        if (isShow) {
            mViews.add(view);
            view.onShowed();
        }

        return isShow;
    }

    public void hide(@NonNull PriorityView view) {
        if (mViews.isEmpty()) return;

        mViews.remove(view);
        view.hide();
    }

    public void release() {
        hideAll();
    }

    public void hideAll() {
        if (mViews.isEmpty()) return;

        for (PriorityView view : mViews) {
            view.hide();
        }

        mViews.clear();
    }
}
