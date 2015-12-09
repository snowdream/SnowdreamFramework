package com.github.snowdream.android.util;

import android.test.AndroidTestCase;
import com.github.snowdream.android.core.task.Task;
import com.github.snowdream.android.core.task.TaskListener;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.with;

/**
 * Created by snowdream on 4/8/14.
 */
public class ThreadUtilTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Awaitility.setDefaultTimeout(Duration.FIVE_SECONDS);
        Awaitility.setDefaultPollInterval(new Duration(5, TimeUnit.MILLISECONDS));
        Awaitility.setDefaultPollDelay(new Duration(0, TimeUnit.MILLISECONDS));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOnUIThread() {
        final AtomicBoolean[] atomic = {new AtomicBoolean(false)};
        final Boolean[] isOnUIThread = new Boolean[1];

        new Task<Boolean, Void>("core", "ThreadUtilTest", new TaskListener<Boolean, Void>() {
            @Override
            public void onSuccessNonUI(Boolean aBoolean) {
                super.onSuccessNonUI(aBoolean);
                atomic[0].set(true);
                isOnUIThread[0] = aBoolean;
            }
        }) {
            @Override
            public void run() {
                boolean isOnUIThread = ThreadUtil.isOnUIThread();
                performOnSuccess(isOnUIThread);
            }
        }.runOnUiThread(null);

        with().await().untilTrue(atomic[0]);

        assertTrue(isOnUIThread[0]);
//        assertTrue(!isOnNonUIThread[0]);
    }

    public void testOnNonUIThread() {
        final AtomicBoolean[] atomic = {new AtomicBoolean(false)};
        final Boolean[] isOnNonUIThread = new Boolean[1];

        new Task<Boolean, Void>("core", "ThreadUtilTest", new TaskListener<Boolean, Void>() {
            @Override
            public void onSuccessNonUI(Boolean aBoolean) {
                super.onSuccessNonUI(aBoolean);
                atomic[0].set(true);
                isOnNonUIThread[0] = aBoolean;
            }
        }) {
            @Override
            public void run() {
                boolean isOnNonUIThread = ThreadUtil.isOnNonUIThread();
                performOnSuccess(isOnNonUIThread);
            }
        }.runOnNonUiThread(null);

        with().await().untilTrue(atomic[0]);

        assertTrue(isOnNonUIThread[0]);
//        assertTrue(!isOnNonUIThread[0]);
    }

    public void testOnNonUIThread1000() {
        for (int i = 0; i < 1000; i++){
            ThreadUtil.isOnNonUIThread();
        }
    }

    public void testOnUIThread1000() {
        for (int i = 0; i < 1000; i++){
            ThreadUtil.isOnUIThread();
        }
    }
}
