package com.github.snowdream.android.util;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.apache.commons.exec.*;

import java.io.IOException;

/**
 * execute commands with Apache Commons Exec
 * Created by yanghui.yangh on 2016-07-09.
 */
public class ShellUtil {

    /**
     * Execute the command sync
     * @param command
     * @param streamHandler  if null,then PumpStreamHandler was the default.
     * @return
     * @throws IOException
     */
    @Deprecated
    public static int exec(@NonNull String command, @Nullable ExecuteStreamHandler streamHandler) throws IOException {
        return exec(command, ExecuteWatchdog.INFINITE_TIMEOUT,streamHandler);
    }

    /**
     * Execute the command sync
     *
     * @param command
     * @param timeout
     * @param streamHandler  if null,then PumpStreamHandler was the default.
     * @return
     * @throws IOException
     */
    @Deprecated
    public static int exec(String command, @FloatRange(from = 0) long timeout, @Nullable ExecuteStreamHandler streamHandler) throws IOException {
        CommandLine cmdLine = CommandLine.parse(command);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(0);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
        executor.setWatchdog(watchdog);
        if (streamHandler != null){
            executor.setStreamHandler(streamHandler);
        }
        return executor.execute(cmdLine);
    }

    /**
     * Execute the command async
     *
     * @param command
     * @param timeout
     * @param resultHandler if null, then DefaultExecuteResultHandler was the default.
     * @param streamHandler  if null,then PumpStreamHandler was the default.
     * @throws IOException
     */
    public static void execAsync(String command, @FloatRange(from = 0) long timeout, @Nullable ExecuteResultHandler resultHandler, @Nullable ExecuteStreamHandler streamHandler) throws IOException {
        if (resultHandler == null) {
            resultHandler = new DefaultExecuteResultHandler();
        }

        CommandLine cmdLine = CommandLine.parse(command);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(0);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
        executor.setWatchdog(watchdog);
        if (streamHandler != null){
            executor.setStreamHandler(streamHandler);
        }
        executor.execute(cmdLine,resultHandler);
    }
}
