package com.github.snowdream.android.util;

import android.text.TextUtils;

import java.security.InvalidParameterException;

/**
 * Decide which log will be written to the file.
 * 
 * Created by hui.yang on 2014/11/16.
 */
public abstract class LogFilter {

    /**
     * if true, filter the log.
     *
     * @param level
     * @param tag
     * @param msg
     * @return
     */
    public abstract boolean filter(Log.LEVEL level, String tag, String msg);


    public static class TagFilter extends LogFilter {
        private String tag = null;

        //Supress default constructor for noninstantiability
        private TagFilter() {
            throw new AssertionError();
        }

        /**
         * set the tag which will not be filtered.
         * 
         * if the tag is null or empty, then nothing will be filtered..
         *
         * @param tag
         */
        public TagFilter(String tag) {
            this.tag = tag;
        }

        @Override
        public boolean filter(Log.LEVEL level, String tag, String msg) {
            if (TextUtils.isEmpty(this.tag)) {
                return false;
            }

            if (this.tag.equals(tag)) {
                return false;
            }

            return true;
        }

    }

    public static class LevelFilter extends LogFilter {
        private Log.LEVEL level = null;

        //Supress default constructor for noninstantiability
        private LevelFilter() {
            throw new AssertionError();
        }

        /**
         * set the Level.Any log with the level below it will be filtered.
         * 
         *
         * @param level the minimum level which will not be filtered.
         */
        public LevelFilter(Log.LEVEL level) {
            if (level == null) {
                throw new InvalidParameterException("level is null or not valid.");
            }

            this.level = level;
        }

        @Override
        public boolean filter(Log.LEVEL level, String tag, String msg) {
            return level.getLevel() < this.level.getLevel();
        }
    }

    public static class ContentFilter extends LogFilter {
        private String msg = null;

        //Supress default constructor for noninstantiability
        private ContentFilter() {
            throw new AssertionError();
        }

        public ContentFilter(String msg) {
            this.msg = msg;
        }

        @Override
        public boolean filter(Log.LEVEL level, String tag, String msg) {
            if (level == null || TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)) {
                return true;
            }

            if (TextUtils.isEmpty(this.msg)) {
                return false;
            }

            if (tag.contains(this.msg) || msg.contains(this.msg)) {
                return false;
            }

            return true;
        }
    }
}
