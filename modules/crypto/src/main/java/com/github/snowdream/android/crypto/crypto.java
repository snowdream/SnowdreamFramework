package com.github.snowdream.android.crypto;

import android.support.annotation.NonNull;
import com.github.snowdream.android.core.task.Cancelable;
import com.github.snowdream.android.core.task.Task;
import com.github.snowdream.android.core.task.TaskListener;
import org.apaches.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by snowdream on 11/17/13.
 */
public class Crypto {
    private static final String TAG = "Crypto";

    public static String md5HexSync(@NonNull byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    public static Cancelable md5Hex(@NonNull final InputStream data, @NonNull TaskListener<String, Void> listener) {
         return new Task<String, Void>(TAG,"md5Hex",listener) {
            @Override
            public void run() {
                try {
                    String md5 = DigestUtils.md5Hex(data);
                    performOnSuccess(md5);
                } catch (IOException e) {
                    performOnError(e);
                    e.printStackTrace();
                }
            }
        };
    }

    public static String md5HexSync(@NonNull String data) {
        return DigestUtils.md5Hex(data);
    }

    public static String sha1HexSync(@NonNull byte[] data) {
        return DigestUtils.sha1Hex(data);
    }

    public static Cancelable sha1Hex(@NonNull final InputStream data ,@NonNull TaskListener<String, Void> listener){
        return new Task<String, Void>(TAG,"sha1Hex",listener) {
            @Override
            public void run() {
                try {
                    String sha1 = DigestUtils.sha1Hex(data);
                    performOnSuccess(sha1);
                } catch (IOException e) {
                    performOnError(e);
                    e.printStackTrace();
                }
            }
        };
    }

    public static String sha1HexSync(@NonNull String data) {
        return DigestUtils.sha1Hex(data);
    }

    public static String sha256HexSync(@NonNull byte[] data) {
        return DigestUtils.sha256Hex(data);
    }

    public static Cancelable sha256Hex(@NonNull final InputStream data ,@NonNull TaskListener<String, Void> listener){
        return new Task<String, Void>(TAG,"sha256Hex",listener) {
            @Override
            public void run() {
                try {
                    String sha1 = DigestUtils.sha256Hex(data);
                    performOnSuccess(sha1);
                } catch (IOException e) {
                    performOnError(e);
                    e.printStackTrace();
                }
            }
        };
    }

    public static String sha256HexSync(@NonNull String data) {
        return DigestUtils.sha256Hex(data);
    }

    public static String sha384HexSync(@NonNull byte[] data) {
        return DigestUtils.sha384Hex(data);
    }

    public static Cancelable sha384Hex(@NonNull final InputStream data ,@NonNull TaskListener<String, Void> listener){
        return new Task<String, Void>(TAG,"sha384Hex",listener) {
            @Override
            public void run() {
                try {
                    String sha1 = DigestUtils.sha384Hex(data);
                    performOnSuccess(sha1);
                } catch (IOException e) {
                    performOnError(e);
                    e.printStackTrace();
                }
            }
        };
    }

    public static String sha384HexSync(@NonNull String data) {
        return DigestUtils.sha384Hex(data);
    }

    public static String sha512HexSync(@NonNull byte[] data) {
        return DigestUtils.sha512Hex(data);
    }

    public static Cancelable sha512Hex(@NonNull final InputStream data ,@NonNull TaskListener<String, Void> listener){
        return new Task<String, Void>(TAG,"sha512Hex",listener) {
            @Override
            public void run() {
                try {
                    String sha1 = DigestUtils.sha512Hex(data);
                    performOnSuccess(sha1);
                } catch (IOException e) {
                    performOnError(e);
                    e.printStackTrace();
                }
            }
        };
    }

    public static String sha512HexSync(@NonNull String data) {
        return DigestUtils.sha512Hex(data);
    }
}
