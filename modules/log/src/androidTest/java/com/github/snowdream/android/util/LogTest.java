package com.github.snowdream.android.util;

import android.test.AndroidTestCase;

/**
 * Created by snowdream on 4/8/14.
 */
public class LogTest extends AndroidTestCase {
    private final static String TAG = "ANDROID_LOG";
    private final static String CUSTOM_TAG = "CUSTOM_TAG";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.setEnabled(true);
        Log.setLog2ConsoleEnabled(true);
        Log.setLog2FileEnabled(true);
        Log.setGlobalTag(TAG);
        Log.clearLogFilters();

        assertEquals(Log.isLog2ConsoleEnabled(), true);
        assertEquals(Log.isLog2FileEnabled(), true);
        assertEquals(Log.getGlobalTag(), TAG);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEnableOrDisableLog() {
        assertEquals(Log.isEnabled(), true);

        Log.setEnabled(false);

        assertEquals(Log.isEnabled(), false);

        Log.setEnabled(true);

        assertEquals(Log.isEnabled(), true);
    }

    public void testEnableOrDisableLog2Console() {
        assertEquals(Log.isLog2ConsoleEnabled(), true);

        Log.setLog2ConsoleEnabled(false);

        assertEquals(Log.isLog2ConsoleEnabled(), false);

        Log.setLog2ConsoleEnabled(true);

        assertEquals(Log.isLog2ConsoleEnabled(), true);
    }

    public void testEnableOrDisableLog2File() {
        assertEquals(Log.isLog2FileEnabled(), true);

        Log.setLog2FileEnabled(false);

        assertEquals(Log.isLog2FileEnabled(), false);

        Log.setLog2FileEnabled(true);

        assertEquals(Log.isLog2FileEnabled(), true);
    }

    public void testSimpleLog() {
        Log.d("test");
        Log.v("test");
        Log.i("test");
        Log.w("test");
        Log.e("test");
    }

    public void testSimpleLogWithCustomTAG() {
        Log.d(CUSTOM_TAG, "test");
        Log.v(CUSTOM_TAG, "test");
        Log.i(CUSTOM_TAG, "test");
        Log.w(CUSTOM_TAG, "test");
        Log.e(CUSTOM_TAG, "test");
    }

    public void testAdvanceLog() {
        Log.d("test", new Throwable("test"));
        Log.v("test", new Throwable("test"));
        Log.i("test", new Throwable("test"));
        Log.w("test", new Throwable("test"));
        Log.e("test", new Throwable("test"));
    }

    public void testAdvanceLogWithCustomTAG() {
        Log.d(CUSTOM_TAG, "test", new Throwable("test"));
        Log.v(CUSTOM_TAG, "test", new Throwable("test"));
        Log.i(CUSTOM_TAG, "test", new Throwable("test"));
        Log.w(CUSTOM_TAG, "test", new Throwable("test"));
        Log.e(CUSTOM_TAG, "test", new Throwable("test"));
    }

    public void testLog2FileWithFilePathGenerator() {
        //Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));
        Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator(getContext(),"app",".log"));
//        Log.setFilePathGenerator(new FilePathGenerator.DateFilePathGenerator("/mnt/sdcard/","app",".log"));
//        Log.setFilePathGenerator(new FilePathGenerator.LimitSizeFilePathGenerator("/mnt/sdcard/","app",".log",10240));

        Log.d("test 1");
        Log.v("test 2");
        Log.i("test 3");
        Log.w("test 4");
        Log.e("test 5");
    }

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
