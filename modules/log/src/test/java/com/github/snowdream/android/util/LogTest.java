package com.github.snowdream.android.util;

import com.github.snowdream.android.util.log.BuildConfig;
import com.github.snowdream.android.util.log.FilePathGenerator;
import com.github.snowdream.android.util.log.Log;
import com.github.snowdream.android.util.log.LogFilter;
import com.github.snowdream.android.util.log.LogFormatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by snowdream on 4/8/14.
 */
public class LogTest{
    private final static String TAG = "ANDROID_LOG";
    private final static String CUSTOM_TAG = "CUSTOM_TAG";

    @Before
    public void setUp() throws Exception {
        Log.setEnabled(true);
        Log.setLog2ConsoleEnabled(true);
        Log.setLog2FileEnabled(true);
        Log.setGlobalTag(TAG);
        Log.clearLogFilters();

        assertEquals(Log.isLog2ConsoleEnabled(), true);
        assertEquals(Log.isLog2FileEnabled(), true);
        assertEquals(Log.getGlobalTag(), TAG);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEnableOrDisableLog() {
        assertEquals(Log.isEnabled(), true);

        Log.setEnabled(false);

        assertEquals(Log.isEnabled(), false);

        Log.setEnabled(true);

        assertEquals(Log.isEnabled(), true);
    }

    @Test
    public void testEnableOrDisableLog2Console() {
        assertEquals(Log.isLog2ConsoleEnabled(), true);

        Log.setLog2ConsoleEnabled(false);

        assertEquals(Log.isLog2ConsoleEnabled(), false);

        Log.setLog2ConsoleEnabled(true);

        assertEquals(Log.isLog2ConsoleEnabled(), true);
    }

    @Test
    public void testEnableOrDisableLog2File() {
        assertEquals(Log.isLog2FileEnabled(), true);

        Log.setLog2FileEnabled(false);

        assertEquals(Log.isLog2FileEnabled(), false);

        Log.setLog2FileEnabled(true);

        assertEquals(Log.isLog2FileEnabled(), true);
    }

    @Test
    public void testSimpleLog() {
        Log.d("test");
        Log.v("test");
        Log.i("test");
        Log.w("test");
        Log.e("test");
    }

    @Test
    public void testSimpleLogWithCustomTAG() {
        Log.d(CUSTOM_TAG, "test");
        Log.v(CUSTOM_TAG, "test");
        Log.i(CUSTOM_TAG, "test");
        Log.w(CUSTOM_TAG, "test");
        Log.e(CUSTOM_TAG, "test");
    }

    @Test
    public void testAdvanceLog() {
        Log.d("test", new Throwable("test"));
        Log.v("test", new Throwable("test"));
        Log.i("test", new Throwable("test"));
        Log.w("test", new Throwable("test"));
        Log.e("test", new Throwable("test"));
    }

    @Test
    public void testAdvanceLogWithCustomTAG() {
        Log.d(CUSTOM_TAG, "test", new Throwable("test"));
        Log.v(CUSTOM_TAG, "test", new Throwable("test"));
        Log.i(CUSTOM_TAG, "test", new Throwable("test"));
        Log.w(CUSTOM_TAG, "test", new Throwable("test"));
        Log.e(CUSTOM_TAG, "test", new Throwable("test"));
    }

    @Test
    public void testLog2FileWithFilePathGenerator() {
        //Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));
//        Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator(RuntimeEnvironment.application,"app",".log"));
//        Log.setFilePathGenerator(new FilePathGenerator.DateFilePathGenerator("/mnt/sdcard/","app",".log"));
//        Log.setFilePathGenerator(new FilePathGenerator.LimitSizeFilePathGenerator("/mnt/sdcard/","app",".log",10240));

        Log.d("test 1");
        Log.v("test 2");
        Log.i("test 3");
        Log.w("test 4");
        Log.e("test 5");
    }

    @Test
    public void testLog2FileWithLogFilter() {
        Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));

        Log.addLogFilter(new LogFilter.LevelFilter(Log.LEVEL.DEBUG));
//        Log.addLogFilter(new LogFilter.TagFilter(TAG));
//        Log.addLogFilter(new LogFilter.ContentFilter(CUSTOM_TAG));


        Log.d("test 1");
        Log.v("test 2");
        Log.i("test 3");
        Log.w("test 4");
        Log.e("test 5");
    }

    @Test
    public void testLog2FileWithLogFormatter() {
        Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));

        Log.addLogFilter(new LogFilter.LevelFilter(Log.LEVEL.DEBUG));

        Log.setLogFormatter(new LogFormatter.EclipseFormatter());
//        Log.setLogFormatter(new LogFormatter.IDEAFormatter());

        Log.d("test 1");
        Log.v("test 2");
        Log.i("test 3");
        Log.w("test 4");
        Log.e("test 5");
    }
}
