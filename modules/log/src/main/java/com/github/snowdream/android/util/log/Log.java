/*
 * Copyright (C) 2013 Snowdream Mobile <yanghui1986527@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.snowdream.android.util.log;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Wrapper API for sending log output
 *
 * 1.enable/disable log
 * <pre>
 * Log.setEnabled(true);
 * Log.setEnabled(false);
 * </pre>
 *
 * 2.enable/disable log to console
 * <pre>
 * Log.setLog2ConsoleEnabled(true);
 * Log.setLog2ConsoleEnabled(false);
 * </pre>
 *
 * 3.enable/disable log to file
 * <pre>
 * Log.setLog2FileEnabled(true);
 * Log.setLog2FileEnabled(false);
 * </pre>
 *
 * 4.set the Global Tag for the log
 * <pre>
 * Log.setGlobalTag("Android");
 * </pre>
 *
 * 5.log simple
 * <pre>
 * Log.d("test");
 * Log.v("test");
 * Log.i("test");
 * Log.w("test");
 * Log.e("test");
 * Log.json(JSON_CONTENT);
 * Log.xml(XML_CONTENT);
 * </pre>
 *
 * 6.log simple -- set custom tag
 * <pre>
 * Log.d("TAG","test");
 * Log.v("TAG","test");
 * Log.i("TAG","test");
 * Log.w("TAG","test");
 * Log.e("TAG","test");
 * Log.json(JSON_CONTENT);
 * Log.xml(XML_CONTENT);
 * </pre>
 *
 * 7.log advance
 * <pre>
 * Log.d("test",new Throwable("test"));
 * Log.v("test",new Throwable("test"));
 * Log.i("test",new Throwable("test"));
 * Log.w("test",new Throwable("test"));
 * Log.e("test",new Throwable("test"));
 * </pre>
 *
 * 8.log advance -- set custom tag
 * <pre>
 * Log.d("TAG","test",new Throwable("test"));
 * Log.v("TAG","test",new Throwable("test"));
 * Log.i("TAG","test",new Throwable("test"));
 * Log.w("TAG","test",new Throwable("test"));
 * Log.e("TAG","test",new Throwable("test"));
 * </pre>
 *
 * 9.Log to File<BR> log into one file With FilePathGenerator
 * <pre>
 * Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));
 * //  Log.setFilePathGenerator(new FilePathGenerator.DateFilePathGenerator("/mnt/sdcard/","app",".log"));
 * // Log.setFilePathGenerator(new FilePathGenerator.LimitSizeFilePathGenerator("/mnt/sdcard/","app",".log",10240));
 *
 * Log.d("test 1");
 * Log.v("test 2");
 * Log.i("test 3");
 * Log.w("test 4");
 * Log.e("test 5");
 * Log.json(JSON_CONTENT);
 * Log.xml(XML_CONTENT);
 * </pre>
 *
 * log into one file With LogFilter
 * <pre>
 * Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));
 *
 * Log.addLogFilter(new LogFilter.LevelFilter(Log.LEVEL.DEBUG));
 * // Log.addLogFilter(new LogFilter.TagFilter(TAG));
 * // Log.addLogFilter(new LogFilter.ContentFilter(CUSTOM_TAG));
 *
 * Log.d("test 1");
 * Log.v("test 2");
 * Log.i("test 3");
 * Log.w("test 4");
 * Log.e("test 5");
 * Log.json(JSON_CONTENT);
 * Log.xml(XML_CONTENT);
 * </pre>
 *
 *
 * log into one file With LogFormatter
 * <pre>
 *  Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));
 *
 * Log.addLogFilter(new LogFilter.LevelFilter(Log.LEVEL.DEBUG));
 *
 *  Log.setLogFormatter(new LogFormatter.EclipseFormatter());
 *  //        Log.setLogFormatter(new LogFormatter.IDEAFormatter());
 *
 * Log.d("test 1");
 * Log.v("test 2");
 * Log.i("test 3");
 * Log.w("test 4");
 * Log.e("test 5");
 * Log.json(JSON_CONTENT);
 * Log.xml(XML_CONTENT);
 * </pre>
 */
public final class Log {

    /**
     * ALL
     */
    public static final int LOG_ALL_TO_FILE = 3;
    /**
     * ERROR
     */
    public static final int LOG_ERROR_TO_FILE = 2;
    /**
     * None
     */
    public static final int LOG_NONE_TO_FILE = 0;
    /**
     * WARN
     */
    public static final int LOG_WARN_TO_FILE = 1;

    /**
     * The GLOBAL_TAG of the Application
     */
    public static String GLOBAL_TAG = "";
    /**
     * Whether to enable the log
     */
    protected static boolean isEnabled = true;

    /**
     * Whether to enable log to the console
     */
    protected static boolean isLog2ConsoleEnabled = true;

    /**
     * Whether to enable log to the file
     */
    protected static boolean isLog2FileEnabled = false;

    /**
     * Whether to show thread info
     */
    private static boolean isShowThreadInfo = false;

    /**
     * Whether to show method info
     */
    private static boolean isShowMethodInfo = false;

    private static int methodCount = 3;

    private static int methodOffset = 0;


    /**
     * Which will be logged into the file
     */
    protected static int policy = LOG_NONE_TO_FILE;

    private static FilePathGenerator generator = null;

    private static LogFormatter formatter = null;

    private static List<LogFilter> filters = null;

    /**
     * It is used for json pretty print
     */
    private static final int JSON_INDENT = 4;

    /**
     * It is used for xml pretty print
     */
    private static final int XML_INDENT = 2;

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 3;

    //Supress default constructor for noninstantiability
    private Log() {
        throw new AssertionError();
    }

    private static void log(LEVEL level, String tag, String msg, Throwable tr) {
        if (!isEnabled) {
            return;
        }

        String curTag = getCurrentTag(tag);

        if (isShowThreadInfo) {
            logThreadInfo(level, curTag);
        }

        if (isShowMethodInfo) {
            logStackTraceInfo(level, curTag);
        }

        if (isLog2ConsoleEnabled) {
            log2Console(level, curTag, msg, tr);
        }

        if (isLog2FileEnabled) {
            log2File(level, curTag, msg, tr);
        }
    }

    /**
     *  log StackTrace Info 2 Console
     *
     * @param level
     * @param curTag
     */
    private static void logStackTraceInfo(LEVEL level, String curTag) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        String space = "";

        int stackOffset = getStackOffset(trace) + methodOffset;

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(space)
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            space += "...";
            String msg = builder.toString();

            if (isLog2ConsoleEnabled) {
                log2Console(level, curTag, msg, null);
            }

            if (isLog2FileEnabled) {
                log2File(level, curTag, msg, null);
            }
        }

    }

    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private static int getStackOffset(StackTraceElement[] trace) {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(Log.class.getName())) {
                return --i;
            }
        }
        return -1;
    }

    /**
     * log Thread Info 2 Console
     *
     * @param level
     * @param curTag
     */
    private static void logThreadInfo(LEVEL level, String curTag) {
        String msg = "Thread: " + Thread.currentThread().getName();

        if (isLog2ConsoleEnabled) {
            log2Console(level, curTag, msg, null);
        }

        if (isLog2FileEnabled) {
            log2File(level, curTag, msg, null);
        }
    }

    /**
     * Get the final tag from the tag.
     */
    private static String getCurrentTag(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            return tag;
        }

        if (!TextUtils.isEmpty(GLOBAL_TAG)) {
            return GLOBAL_TAG;
        }

        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stacks.length >= 4) {
            return stacks[3].getClassName();
        }

        return null;
    }

    /**
     * write the log messages to the console.
     */
    protected static void log2Console(LEVEL level, String tag, String msg, Throwable thr) {
        switch (level) {
            case VERBOSE:
                if (thr == null) {
                    android.util.Log.v(tag, msg);
                } else {
                    android.util.Log.v(tag, msg, thr);
                }
                break;
            case DEBUG:
                if (thr == null) {
                    android.util.Log.d(tag, msg);
                } else {
                    android.util.Log.d(tag, msg, thr);
                }
                break;
            case INFO:
                if (thr == null) {
                    android.util.Log.i(tag, msg);
                } else {
                    android.util.Log.i(tag, msg, thr);
                }
                break;
            case WARN:
                if (thr == null) {
                    android.util.Log.w(tag, msg);
                } else if (TextUtils.isEmpty(msg)) {
                    android.util.Log.w(tag, thr);
                } else {
                    android.util.Log.w(tag, msg, thr);
                }
                break;
            case ERROR:
                if (thr == null) {
                    android.util.Log.e(tag, msg);
                } else {
                    android.util.Log.e(tag, msg, thr);
                }
                break;
            case ASSERT:
                if (thr == null) {
                    android.util.Log.wtf(tag, msg);
                } else if (TextUtils.isEmpty(msg)) {
                    android.util.Log.wtf(tag, thr);
                } else {
                    android.util.Log.wtf(tag, msg, thr);
                }
                break;
            default:
                break;
        }
    }

    /**
     * write the log messages to the file.
     */
    private static void log2File(LEVEL level, String tag, String msg, Throwable tr) {
        if (generator == null) {
            generator = new FilePathGenerator.DefaultFilePathGenerator("", "", "");
        }

        if (formatter == null) {
            formatter = new LogFormatter.EclipseFormatter();
        }

        boolean isFilter = false;

        if (filters != null) {
            for (LogFilter f : filters) {
                if (f.filter(level, tag, msg)) {
                    isFilter = true;
                    break;
                }
            }
        }

        if (!isFilter && !TextUtils.isEmpty(generator.getPath())) {
            Log2File.log2file(generator.getPath(), formatter.format(level, tag, msg, tr));
        }
    }

    /**
     * Get the ExecutorService
     *
     * @return the ExecutorService
     */
    public static ExecutorService getExecutor() {
        return Log2File.getExecutor();
    }

    /**
     * Set the ExecutorService
     *
     * @param executor the ExecutorService
     */
    public static void setExecutor(ExecutorService executor) {
        Log2File.setExecutor(executor);
    }

    /**
     * get the log file path
     *
     * @return path
     */
    public static String getPath() {
        if (generator == null) {
            return null;
        }

        return generator.getPath();
    }

    /**
     * set the path of the log file
     *
     * use {@link #setFilePathGenerator} instead. This method will be removed in the near future.
     */
    @Deprecated
    public static void setPath(String path) {
        generator = new FilePathGenerator.DefaultFilePathGenerator(path, null, null);
    }

    /**
     * get the FilePathGenerator
     *
     * @return the FilePathGenerator
     */
    public static FilePathGenerator getFilePathGenerator() {
        return generator;
    }

    /**
     * set the FilePathGenerator
     *
     * @param generator the FilePathGenerator
     */
    public static void setFilePathGenerator(FilePathGenerator generator) {
        Log.generator = generator;
    }

    /**
     * get the log formatter
     */
    public static LogFormatter getLogFormatter() {
        return formatter;
    }

    /**
     * set the log formatter
     */
    public static void setLogFormatter(LogFormatter formatter) {
        Log.formatter = formatter;
    }

    /**
     * add log filter
     *
     * each one kind
     *
     * @return true if added successfully,else the filter is null or any filter with the same kind
     * has been added.
     */
    public static boolean addLogFilter(LogFilter filter) {
        boolean ret = true;

        if (filter == null) {
            ret = false;
            return ret;
        }

        if (filters == null) {
            filters = new ArrayList<LogFilter>();
        }

        for (LogFilter f : filters) {
            if (filter.getClass().getName().equals(f.getClass().getName())) {
                ret = false;
                break;
            }
        }

        if (ret) {
            filters.add(filter);
        }

        return ret;
    }

    /**
     * get the log filters
     */
    public static List<LogFilter> getLogFilters() {
        return filters;
    }

    /**
     * remove the log filter
     */
    public static void removeLogFilter(LogFilter filter) {
        if (filter == null || filters == null || filters.isEmpty()) {
            return;
        }

        if (filters.contains(filter)) {
            filters.remove(filter);
        }
    }

    /**
     * remove all the log filters which have been added before.
     */
    public static void clearLogFilters() {
        if (filters == null || filters.isEmpty()) {
            return;
        }

        filters.clear();
    }

    /**
     * get the policy of the log
     *
     * use {@link LogFilter} instead. This method will be removed in the near future.
     *
     * @return the policy of the log
     */
    @Deprecated
    public static int getPolicy() {
        return policy;
    }

    /**
     * set the policy of the log
     *
     * use {@link LogFilter} instead. This method will be removed in the near future.
     *
     * @param policy the policy of the log
     */
    @Deprecated
    public static void setPolicy(int policy) {
        Log.policy = policy;
    }

    /**
     * is the log enabled
     */
    public static boolean isEnabled() {
        return isEnabled;
    }

    /**
     * enable or disable the log, the default value is true.
     *
     * @param enabled whether to enable the log
     */
    public static void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    /**
     * is the Log2Console  enabled
     */
    public static boolean isLog2ConsoleEnabled() {
        return isLog2ConsoleEnabled;
    }

    /**
     * enable or disable writing the log to the console. the default value is true.
     *
     * @param enabled whether to enable the log
     */
    public static void setLog2ConsoleEnabled(boolean enabled) {
        isLog2ConsoleEnabled = enabled;
    }

    /**
     * is the Log2Console  enabled
     */
    public static boolean isLog2FileEnabled() {
        return isLog2FileEnabled;
    }

    /**
     * enable or disable writing the log to the file. the default value is false.
     *
     * @param enabled whether to enable the log
     */
    public static void setLog2FileEnabled(boolean enabled) {
        isLog2FileEnabled = enabled;
    }

    public static boolean isShowThreadInfo() {
        return isShowThreadInfo;
    }

    public static void setIsShowThreadInfo(boolean isShowThreadInfo) {
        Log.isShowThreadInfo = isShowThreadInfo;
    }

    public static int getMethodCount() {
        return methodCount;
    }

    public static void setMethodCount(int methodCount) {
        Log.methodCount = methodCount;
    }

    public static int getMethodOffset() {
        return methodOffset;
    }

    public static void setMethodOffset(int methodOffset) {
        Log.methodOffset = methodOffset;
    }

    /**
     * Checks to see whether or not a log for the specified tag is loggable at the specified level.
     * The default level of any tag is set to INFO. This means that any level above and including
     * INFO will be logged. Before you make any calls to a logging method you should check to see if
     * your tag should be logged.
     *
     * @param tag   The tag to check
     * @param level The level to check
     * @return Whether or not that this is allowed to be logged.
     */
    public static boolean isLoggable(String tag, int level) {
        return android.util.Log.isLoggable(tag, level);
    }

    /**
     * Low-level logging call.
     *
     * @param priority The priority/type of this log message
     * @param tag      Used to identify the source of a log message. It usually identifies the class
     *                 or activity where the log call occurs.
     * @param msg      The message you would like logged.
     * @return The number of bytes written.
     */
    public static int println(int priority, String tag, String msg) {
        return android.util.Log.println(priority, tag, msg);
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }

    /**
     * Get the Tag of the application
     *
     * This method will be removed in the near future. use {@link #getGlobalTag}
     */
    @Deprecated
    public static String getTag() {
        return GLOBAL_TAG;
    }

    /**
     * Set the Tag of the application use {@link #getGlobalTag} This method will be removed in the
     * near future.
     *
     * @param tag the Tag of the application
     */
    @Deprecated
    public static void setTag(String tag) {
        GLOBAL_TAG = tag;
    }

    /**
     * Get the Tag of the application
     */
    public static String getGlobalTag() {
        return GLOBAL_TAG;
    }

    /**
     * Set the Tag of the application
     *
     * @param tag the Tag of the application
     */
    public static void setGlobalTag(String tag) {
        GLOBAL_TAG = tag;
    }

    /**
     * set the log file path
     *
     * The log file path will be: logDirPath + logFileBaseName + Formated time +logFileSuffix use
     * {@link #setFilePathGenerator} instead. This method will be removed in the near future.
     *
     * @param logDirPath      the log file dir path,such as "/mnt/sdcard/snowdream/log"
     * @param logFileBaseName the log file base file name,such as "log"
     * @param logFileSuffix   the log file suffix,such as "log"
     */
    @Deprecated
    public static void setPath(String logDirPath, String logFileBaseName, String logFileSuffix) {
        setPath(logDirPath);
    }

    /**
     * Send a DEBUG log message.
     *
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        log(LEVEL.DEBUG, tag, msg, null);
    }

    /**
     * Send a DEBUG log message.
     */
    public static void d(String msg) {
        log(LEVEL.DEBUG, null, msg, null);
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void d(String tag, String msg, Throwable thr) {
        log(LEVEL.DEBUG, tag, msg, thr);
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void d(String msg, Throwable thr) {
        log(LEVEL.DEBUG, null, msg, thr);
    }

    /**
     * Send a ERROR log message.
     *
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        log(LEVEL.ERROR, tag, msg, null);
    }

    /**
     * Send an ERROR log message.
     *
     * @param msg The message you would like logged.
     */
    public static void e(String msg) {
        log(LEVEL.ERROR, null, msg, null);
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void e(String tag, String msg, Throwable thr) {
        log(LEVEL.ERROR, tag, msg, thr);
    }

    /**
     * Send an ERROR log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void e(String msg, Throwable thr) {
        log(LEVEL.ERROR, null, msg, thr);
    }

    /**
     * Send a INFO log message.
     *
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        log(LEVEL.INFO, tag, msg, null);
    }

    /**
     * Send an INFO log message.
     *
     * @param msg The message you would like logged.
     */
    public static void i(String msg) {
        log(LEVEL.INFO, null, msg, null);
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void i(String tag, String msg, Throwable thr) {
        log(LEVEL.INFO, tag, msg, thr);
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void i(String msg, Throwable thr) {
        log(LEVEL.INFO, null, msg, thr);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        log(LEVEL.VERBOSE, tag, msg, null);
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param msg The message you would like logged.
     */
    public static void v(String msg) {
        log(LEVEL.VERBOSE, null, msg, null);
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void v(String tag, String msg, Throwable thr) {
        log(LEVEL.VERBOSE, tag, msg, thr);
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void v(String msg, Throwable thr) {
        log(LEVEL.VERBOSE, null, msg, thr);
    }

    /**
     * Send an empty WARN log message and log the exception.
     *
     * @param thr An exception to log
     */
    public static void w(Throwable thr) {
        log(LEVEL.WARN, null, null, thr);
    }

    /**
     * Send a WARN log message.
     *
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        log(LEVEL.WARN, tag, msg, null);
    }

    /**
     * Send a WARN log message
     *
     * @param msg The message you would like logged.
     */
    public static void w(String msg) {
        log(LEVEL.WARN, null, msg, null);
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void w(String tag, String msg, Throwable thr) {
        log(LEVEL.WARN, tag, msg, thr);
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void w(String msg, Throwable thr) {
        log(LEVEL.WARN, null, msg, thr);
    }

    /**
     * Send an empty What a Terrible Failure log message and log the exception.
     *
     * @param thr An exception to log
     */
    public static void wtf(Throwable thr) {
        log(LEVEL.ASSERT, null, null, thr);
    }

    /**
     * Send a What a Terrible Failure log message.
     *
     * @param msg The message you would like logged.
     */
    public static void wtf(String tag, String msg) {
        log(LEVEL.ASSERT, tag, msg, null);
    }

    /**
     * Send a What a Terrible Failure log message
     *
     * @param msg The message you would like logged.
     */
    public static void wtf(String msg) {
        log(LEVEL.ASSERT, null, msg, null);
    }

    /**
     * Send a What a Terrible Failure log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void wtf(String tag, String msg, Throwable thr) {
        log(LEVEL.ASSERT, tag, msg, thr);
    }

    /**
     * Send a What a Terrible Failure log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param thr An exception to log
     */
    public static void wtf(String msg, Throwable thr) {
        log(LEVEL.ASSERT, null, msg, thr);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                d(message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                d(message);
            }
        } catch (JSONException e) {
            e(e.getCause().getMessage() + "\n" + json);
        }
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            d("Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(e.getCause().getMessage() + "\n" + xml);
        }
    }


    public enum LEVEL {
        VERBOSE(2, "V"),
        DEBUG(3, "D"),
        INFO(4, "I"),
        WARN(5, "W"),
        ERROR(6, "E"),
        ASSERT(7, "A");

        final String levelString;
        final int level;

        //Supress default constructor for noninstantiability
        private LEVEL() {
            throw new AssertionError();
        }

        private LEVEL(int level, String levelString) {
            this.level = level;
            this.levelString = levelString;
        }

        public String getLevelString() {
            return this.levelString;
        }

        public int getLevel() {
            return this.level;
        }
    }
}