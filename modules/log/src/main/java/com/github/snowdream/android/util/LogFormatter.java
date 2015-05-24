package com.github.snowdream.android.util;

import android.text.TextUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Decide the format of log will be written to the file.
 * 
 * Created by hui.yang on 2014/11/16.
 */
public abstract class LogFormatter {
    /**
     * format the log.
     *
     * @param level
     * @param tag
     * @param msg
     * @return
     */
    public abstract String format(Log.LEVEL level, String tag, String msg, Throwable tr);

    /**
     * Eclipse Style
     */
    public static class EclipseFormatter extends LogFormatter{
        private final FastDateFormat formatter;

        public EclipseFormatter(){
            formatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSSZ");
        }

        public EclipseFormatter(String formatOfTime){
            if (TextUtils.isEmpty(formatOfTime)){
                formatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSSZ");
            }else{
                formatter = FastDateFormat.getInstance(formatOfTime);
            }
        }

        @Override
        public String format(Log.LEVEL level, String tag, String msg, Throwable tr) {
            if (level == null || TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
                return "";
            }

            StringBuffer buffer = new StringBuffer();
            buffer.append(level.getLevelString());
            buffer.append("\t");
            buffer.append(formatter.format(System.currentTimeMillis()));
            buffer.append("\t");
            buffer.append(android.os.Process.myPid());
            buffer.append("\t");
            buffer.append(android.os.Process.myTid());
            buffer.append("\t");
            buffer.append(tag);
            buffer.append("\t");
            buffer.append(msg);
            if (tr != null) {
                buffer.append(System.getProperty("line.separator"));
                buffer.append(android.util.Log.getStackTraceString(tr));
            }

            return buffer.toString();
        }
    }

    /**
     * IDEA Style
     */
    public static class IDEAFormatter extends LogFormatter{
        private final FastDateFormat formatter;

        public IDEAFormatter(){
            formatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSSZ");
        }

        public IDEAFormatter(String formatOfTime){
            if (TextUtils.isEmpty(formatOfTime)){
                formatter = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSSZ");
            }else{
                formatter = FastDateFormat.getInstance(formatOfTime);
            }
        }

        @Override
        public String format(Log.LEVEL level, String tag, String msg, Throwable tr) {
            if (level == null || TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
                return "";
            }

            StringBuffer buffer = new StringBuffer();
            buffer.append(formatter.format(System.currentTimeMillis()));
            buffer.append("\t");
            buffer.append(android.os.Process.myPid());
            buffer.append("-");
            buffer.append(android.os.Process.myTid());
            buffer.append("/?");
            buffer.append("\t");
            buffer.append(level.getLevelString());
            buffer.append("/");
            buffer.append(tag);
            buffer.append(":");
            buffer.append("\t");
            buffer.append(msg);
            if (tr != null) {
                buffer.append(System.getProperty("line.separator"));
                buffer.append(android.util.Log.getStackTraceString(tr));
            }

            return buffer.toString();
        }
    }
}
