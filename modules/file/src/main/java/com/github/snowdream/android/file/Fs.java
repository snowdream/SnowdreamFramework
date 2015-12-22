package com.github.snowdream.android.file;

import android.support.annotation.NonNull;
import com.github.snowdream.android.core.task.Cancelable;
import com.github.snowdream.android.core.task.Task;
import com.github.snowdream.android.core.task.TaskListener;
import com.github.snowdream.android.os.IoOnMainThreadException;
import com.github.snowdream.android.util.ThreadUtil;

import java.io.*;
import java.net.URI;

/**
 * Created by snowdream on 11/17/13.
 */
public class Fs {
    private static final String TAG = "File";

    /**
     * Constructs a new file using the specified directory and name.
     *
     * @param dir  the directory where the file is stored.
     * @param name the file's name.
     * @throws NullPointerException if {@code name} is {@code null}.
     */
    public static File open(@NonNull File dir, @NonNull String name) {
        return new File(dir, name);
    }

    /**
     * Constructs a new file using the specified path.
     *
     * @param path the path to be used for the file.
     */
    public static File open(@NonNull String path) {
        return new File(path);
    }

    /**
     * Constructs a new File using the specified directory path and file name,
     * placing a path separator between the two.
     *
     * @param dirPath the path to the directory where the file is stored.
     * @param name    the file's name.
     * @throws NullPointerException if {@code name == null}.
     */
    public static File open(@NonNull String dirPath, @NonNull String name) {
        return new File(dirPath, name);
    }

    /**
     * Constructs a new File using the path of the specified URI. {@code uri}
     * needs to be an absolute and hierarchical Unified Resource Identifier with
     * file scheme and non-empty path component, but with undefined authority,
     * query or fragment components.
     *
     * @param uri the Unified Resource Identifier that is used to construct this
     *            file.
     * @throws IllegalArgumentException if {@code uri} does not comply with the conditions above.
     * @see java.io.File#toURI
     * @see java.net.URI
     */
    public static File open(@NonNull URI uri) {
        return new File(uri);
    }

    /**
     * Returns the file system roots. On Android and other Unix systems, there is
     * a single root, {@code /}.
     */
    public static File[] listRoots() {
        return new File[]{new File("/")};
    }

    /**
     * assertIoOperationOnNonThread
     */
    private static void assertIoOperationOnNonThread() {
        if (ThreadUtil.isOnUIThread()) {
            throw new IoOnMainThreadException();
        }
    }

    /**
     * Tests whether or not this process is allowed to execute this file.
     * Note that this is a best-effort result; the only way to be certain is
     * to actually attempt the operation.
     *
     * @return {@code true} if this file can be executed, {@code false} otherwise.
     * @since 1.6
     */
    public static boolean canExecuteSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.canExecute();
    }


    public static Cancelable canExecute(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "canExecute", listener) {
            @Override
            public void run() {
                super.run();

                Boolean canExecute = file.canExecute();
                performOnSuccess(canExecute);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Indicates whether the current context is allowed to read from this file.
     *
     * @return {@code true} if this file can be read, {@code false} otherwise.
     */
    public static boolean canReadSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.canRead();
    }


    public static Cancelable canRead(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "canRead", listener) {
            @Override
            public void run() {
                super.run();

                Boolean canRead = file.canRead();
                performOnSuccess(canRead);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Indicates whether the current context is allowed to write to this file.
     *
     * @return {@code true} if this file can be written, {@code false}
     * otherwise.
     */
    public static boolean canWriteSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.canWrite();
    }

    public static Cancelable canWrite(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "canWrite", listener) {
            @Override
            public void run() {
                super.run();

                Boolean canWrite = file.canWrite();
                performOnSuccess(canWrite);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Returns the relative sort ordering of the paths for this file and the
     * file {@code another}. The ordering is platform dependent.
     *
     * @param one     a file
     * @param another a file to compare this file to
     * @return an int determined by comparing the two paths. Possible values are
     * described in the Comparable interface.
     * @see Comparable
     */
    public static int compareTo(@NonNull File one, @NonNull File another) {
        return one.getPath().compareTo(another.getPath());
    }

    /**
     * Deletes this file. Directories must be empty before they will be deleted.
     * <p/>
     * <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value.
     *
     * @return {@code true} if this file was deleted, {@code false} otherwise.
     */
    public static boolean deleteSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.delete();
    }

    public static Cancelable delete(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "delete", listener) {
            @Override
            public void run() {
                super.run();

                Boolean isDeleted = file.delete();
                performOnSuccess(isDeleted);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Schedules this file to be automatically deleted when the VM terminates normally.
     * <p/>
     * <p><i>Note that on Android, the application lifecycle does not include VM termination,
     * so calling this method will not ensure that files are deleted</i>. Instead, you should
     * use the most appropriate out of:
     * <ul>
     * <li>Use a {@code finally} clause to manually invoke {@link #delete}.
     * <li>Maintain your own set of files to delete, and process it at an appropriate point
     * in your application's lifecycle.
     * <li>Use the Unix trick of deleting the file as soon as all readers and writers have
     * opened it. No new readers/writers will be able to access the file, but all existing
     * ones will still have access until the last one closes the file.
     * </ul>
     */
    public static void deleteOnExitSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        file.deleteOnExit();
    }

    public static Cancelable deleteOnExit(@NonNull final File file, @NonNull TaskListener<Void, Void> listener) {
        return new Task<Void, Void>(TAG, "deleteOnExit", listener) {
            @Override
            public void run() {
                super.run();

                file.deleteOnExit();
                performOnSuccess(null);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Returns a boolean indicating whether this file can be found on the
     * underlying file system.
     *
     * @return {@code true} if this file exists, {@code false} otherwise.
     */
    public static boolean existsSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.exists();
    }

    public static Cancelable exists(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "exists", listener) {
            @Override
            public void run() {
                super.run();

                Boolean exists = file.exists();
                performOnSuccess(exists);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Indicates if this file represents a <em>directory</em> on the
     * underlying file system.
     *
     * @return {@code true} if this file is a directory, {@code false}
     * otherwise.
     */
    public static boolean isDirectorySync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.isDirectory();
    }

    public static Cancelable isDirectory(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "isDirectory", listener) {
            @Override
            public void run() {
                super.run();

                Boolean isDirectory = file.isDirectory();
                performOnSuccess(isDirectory);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Indicates if this file represents a <em>file</em> on the underlying
     * file system.
     *
     * @return {@code true} if this file is a file, {@code false} otherwise.
     */
    public static boolean isFile(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.isFile();
    }

    public static Cancelable isFile(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "isFile", listener) {
            @Override
            public void run() {
                super.run();

                Boolean isFile = file.isFile();
                performOnSuccess(isFile);
            }
        }.runOnNonUiThread(null);
    }


    /**
     * Returns the time when this file was last modified, measured in
     * milliseconds since January 1st, 1970, midnight.
     * Returns 0 if the file does not exist.
     *
     * @return the time when this file was last modified.
     */
    public static long lastModifiedSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.lastModified();
    }

    public static Cancelable lastModified(@NonNull final File file, @NonNull TaskListener<Long, Void> listener) {
        return new Task<Long, Void>(TAG, "lastModified", listener) {
            @Override
            public void run() {
                super.run();

                Long lastModified = file.lastModified();
                performOnSuccess(lastModified);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Sets the time this file was last modified, measured in milliseconds since
     * January 1st, 1970, midnight.
     * <p/>
     * <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value.
     *
     * @param time the last modification time for this file.
     * @return {@code true} if the operation is successful, {@code false}
     * otherwise.
     * @throws IllegalArgumentException if {@code time < 0}.
     */
    public static boolean setLastModifiedSync(@NonNull File file, long time) {
        assertIoOperationOnNonThread();
        return file.setLastModified(time);
    }

    public static Cancelable setLastModified(@NonNull final File file, final long time, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "setLastModified", listener) {
            @Override
            public void run() {
                super.run();

                Boolean success = file.setLastModified(time);
                performOnSuccess(success);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Equivalent to setWritable(false, false).
     *
     * @see java.io.File#setWritable(boolean, boolean)
     */
    public static boolean setReadOnlySync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.setReadOnly();
    }

    public static Cancelable setReadOnly(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "setReadOnly", listener) {
            @Override
            public void run() {
                super.run();

                Boolean success = file.setReadOnly();
                performOnSuccess(success);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Manipulates the execute permissions for the abstract path designated by
     * this file.
     * <p/>
     * <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value.
     *
     * @param executable To allow execute permission if true, otherwise disallow
     * @param ownerOnly  To manipulate execute permission only for owner if true,
     *                   otherwise for everyone. The manipulation will apply to
     *                   everyone regardless of this value if the underlying system
     *                   does not distinguish owner and other users.
     * @return true if and only if the operation succeeded. If the user does not
     * have permission to change the access permissions of this abstract
     * pathname the operation will fail. If the underlying file system
     * does not support execute permission and the value of executable
     * is false, this operation will fail.
     * @since 1.6
     */
    public static boolean setExecutableSync(@NonNull File file, boolean executable, boolean ownerOnly) {
        assertIoOperationOnNonThread();
        return file.setExecutable(executable, ownerOnly);
    }

    public static Cancelable setExecutable(@NonNull final File file, final boolean executable, final boolean ownerOnly, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "setExecutable", listener) {
            @Override
            public void run() {
                super.run();

                Boolean success = file.setExecutable(executable, ownerOnly);
                performOnSuccess(success);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Equivalent to setExecutable(executable, true).
     *
     * @see java.io.File#setExecutable(boolean, boolean)
     * @since 1.6
     */
    public boolean setExecutableSync(@NonNull File file, boolean executable) {
        return setExecutableSync(file, executable, true);
    }

    public static Cancelable setExecutable(@NonNull final File file, final boolean executable, @NonNull TaskListener<Boolean, Void> listener) {
        return setExecutable(file, executable, true, listener);
    }


    /**
     * Manipulates the read permissions for the abstract path designated by this
     * file.
     *
     * @param readable  To allow read permission if true, otherwise disallow
     * @param ownerOnly To manipulate read permission only for owner if true,
     *                  otherwise for everyone. The manipulation will apply to
     *                  everyone regardless of this value if the underlying system
     *                  does not distinguish owner and other users.
     * @return true if and only if the operation succeeded. If the user does not
     * have permission to change the access permissions of this abstract
     * pathname the operation will fail. If the underlying file system
     * does not support read permission and the value of readable is
     * false, this operation will fail.
     * @since 1.6
     */
    public static boolean setReadableSync(@NonNull File file, boolean readable, boolean ownerOnly) {
        assertIoOperationOnNonThread();
        return file.setReadable(readable, ownerOnly);
    }

    public static Cancelable setReadable(@NonNull final File file, final boolean readable, final boolean ownerOnly, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "setReadable", listener) {
            @Override
            public void run() {
                super.run();

                Boolean success = file.setReadable(readable, ownerOnly);
                performOnSuccess(success);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Equivalent to setReadable(readable, true).
     *
     * @see java.io.File#setReadable(boolean, boolean)
     * @since 1.6
     */
    public boolean setReadableSync(@NonNull File file, boolean readable) {
        return setReadableSync(file, readable, true);
    }

    public static Cancelable setReadable(@NonNull final File file, final boolean readable, @NonNull TaskListener<Boolean, Void> listener) {
        return setReadable(file, readable, true, listener);
    }

    /**
     * Manipulates the write permissions for the abstract path designated by this
     * file.
     *
     * @param writable  To allow write permission if true, otherwise disallow
     * @param ownerOnly To manipulate write permission only for owner if true,
     *                  otherwise for everyone. The manipulation will apply to
     *                  everyone regardless of this value if the underlying system
     *                  does not distinguish owner and other users.
     * @return true if and only if the operation succeeded. If the user does not
     * have permission to change the access permissions of this abstract
     * pathname the operation will fail.
     * @since 1.6
     */
    public static boolean setWritableSync(@NonNull File file, boolean writable, boolean ownerOnly) {
        assertIoOperationOnNonThread();
        return file.setWritable(writable, ownerOnly);
    }


    public static Cancelable setWritable(@NonNull final File file, final boolean writable, final boolean ownerOnly, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "setWritable", listener) {
            @Override
            public void run() {
                super.run();

                Boolean success = file.setWritable(writable, ownerOnly);
                performOnSuccess(success);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Equivalent to setWritable(writable, true).
     *
     * @see java.io.File#setWritable(boolean, boolean)
     * @since 1.6
     */
    public boolean setWritableSync(@NonNull File file, boolean writable) {
        return setWritableSync(file, writable, true);
    }


    public static Cancelable setWritable(@NonNull final File file, final boolean writable, @NonNull TaskListener<Boolean, Void> listener) {
        return setWritable(file, writable, true, listener);
    }

    /**
     * Returns the length of this file in bytes.
     * Returns 0 if the file does not exist.
     * The result for a directory is not defined.
     *
     * @return the number of bytes in this file.
     */
    public static long lengthSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.length();
    }

    public static Cancelable length(@NonNull final File file, @NonNull TaskListener<Long, Void> listener) {
        return new Task<Long, Void>(TAG, "length", listener) {
            @Override
            public void run() {
                super.run();

                Long length = file.length();
                performOnSuccess(length);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Returns an array of strings with the file names in the directory
     * represented by this file. The result is {@code null} if this file is not
     * a directory.
     * <p/>
     * The entries {@code .} and {@code ..} representing the current and parent
     * directory are not returned as part of the list.
     *
     * @return an array of strings with file names or {@code null}.
     */
    public static String[] listSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.list();
    }

    public static Cancelable list(@NonNull final File file, @NonNull TaskListener<String[], Void> listener) {
        return new Task<String[], Void>(TAG, "list", listener) {
            @Override
            public void run() {
                super.run();

                String[] files = file.list();
                performOnSuccess(files);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Gets a list of the files in the directory represented by this file. This
     * list is then filtered through a FilenameFilter and the names of files
     * with matching names are returned as an array of strings. Returns
     * {@code null} if this file is not a directory. If {@code filter} is
     * {@code null} then all filenames match.
     * <p/>
     * The entries {@code .} and {@code ..} representing the current and parent
     * directories are not returned as part of the list.
     *
     * @param filter the filter to match names against, may be {@code null}.
     * @return an array of files or {@code null}.
     */
    public static String[] listSync(@NonNull File file, FilenameFilter filter) {
        assertIoOperationOnNonThread();
        return file.list(filter);
    }

    public static Cancelable list(@NonNull final File file, final FilenameFilter filter, @NonNull TaskListener<String[], Void> listener) {
        return new Task<String[], Void>(TAG, "list", listener) {
            @Override
            public void run() {
                super.run();

                String[] files = file.list(filter);
                performOnSuccess(files);
            }
        }.runOnNonUiThread(null);
    }


    /**
     * Returns an array of files contained in the directory represented by this
     * file. The result is {@code null} if this file is not a directory. The
     * paths of the files in the array are absolute if the path of this file is
     * absolute, they are relative otherwise.
     *
     * @return an array of files or {@code null}.
     */
    public static File[] listFilesSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.listFiles();
    }

    public static Cancelable listFiles(@NonNull final File file, @NonNull TaskListener<File[], Void> listener) {
        return new Task<File[], Void>(TAG, "listFiles", listener) {
            @Override
            public void run() {
                super.run();

                File[] files = file.listFiles();
                performOnSuccess(files);
            }
        }.runOnNonUiThread(null);
    }


    /**
     * Gets a list of the files in the directory represented by this file. This
     * list is then filtered through a FilenameFilter and files with matching
     * names are returned as an array of files. Returns {@code null} if this
     * file is not a directory. If {@code filter} is {@code null} then all
     * filenames match.
     * <p/>
     * The entries {@code .} and {@code ..} representing the current and parent
     * directories are not returned as part of the list.
     *
     * @param filter the filter to match names against, may be {@code null}.
     * @return an array of files or {@code null}.
     */
    public static File[] listFiles(@NonNull File file, FilenameFilter filter) {
        assertIoOperationOnNonThread();
        return file.listFiles(filter);
    }

    public static Cancelable listFiles(@NonNull final File file, final FilenameFilter filter, @NonNull TaskListener<File[], Void> listener) {
        return new Task<File[], Void>(TAG, "listFiles", listener) {
            @Override
            public void run() {
                super.run();

                File[] files = file.listFiles(filter);
                performOnSuccess(files);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Gets a list of the files in the directory represented by this file. This
     * list is then filtered through a FileFilter and matching files are
     * returned as an array of files. Returns {@code null} if this file is not a
     * directory. If {@code filter} is {@code null} then all files match.
     * <p/>
     * The entries {@code .} and {@code ..} representing the current and parent
     * directories are not returned as part of the list.
     *
     * @param filter the filter to match names against, may be {@code null}.
     * @return an array of files or {@code null}.
     */
    public static File[] listFilesSync(@NonNull File file, FileFilter filter) {
        assertIoOperationOnNonThread();
        return file.listFiles(filter);
    }

    public static Cancelable listFiles(@NonNull final File file, final FileFilter filter, @NonNull TaskListener<File[], Void> listener) {
        return new Task<File[], Void>(TAG, "listFiles", listener) {
            @Override
            public void run() {
                super.run();

                File[] files = file.listFiles(filter);
                performOnSuccess(files);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Creates the directory named by this file, assuming its parents exist.
     * Use {@link #mkdirs} if you also want to create missing parents.
     * <p/>
     * <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value. Note also that this method returns
     * false if the directory already existed. If you want to know whether the
     * directory exists on return, either use {@code (f.mkdir() || f.isDirectory())}
     * or simply ignore the return value from this method and simply call {@link #isDirectory}.
     *
     * @return {@code true} if the directory was created,
     * {@code false} on failure or if the directory already existed.
     */
    public static boolean mkdirSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.mkdir();
    }

    public static Cancelable mkdir(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "mkdir", listener) {
            @Override
            public void run() {
                super.run();

                Boolean success = file.mkdir();
                performOnSuccess(success);
            }
        }.runOnNonUiThread(null);
    }


    /**
     * Creates the directory named by this file, creating missing parent
     * directories if necessary.
     * Use {@link #mkdir} if you don't want to create missing parents.
     * <p/>
     * <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value. Note also that this method returns
     * false if the directory already existed. If you want to know whether the
     * directory exists on return, either use {@code (f.mkdirs() || f.isDirectory())}
     * or simply ignore the return value from this method and simply call {@link #isDirectory}.
     *
     * @return {@code true} if the directory was created,
     * {@code false} on failure or if the directory already existed.
     */
    public static boolean mkdirsSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.mkdirs();
    }

    public static Cancelable mkdirs(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "mkdirs", listener) {
            @Override
            public void run() {
                super.run();

                Boolean success = file.mkdirs();
                performOnSuccess(success);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Creates a new, empty file on the file system according to the path
     * information stored in this file. This method returns true if it creates
     * a file, false if the file already existed. Note that it returns false
     * even if the file is not a file (because it's a directory, say).
     * <p/>
     * <p>This method is not generally useful. For creating temporary files,
     * use {@link #createTempFile} instead. For reading/writing files, use {@link FileInputStream},
     * {@link FileOutputStream}, or {@link RandomAccessFile}, all of which can create files.
     * <p/>
     * <p>Note that this method does <i>not</i> throw {@code IOException} if the file
     * already exists, even if it's not a regular file. Callers should always check the
     * return value, and may additionally want to call {@link #isFile}.
     *
     * @return true if the file has been created, false if it
     * already exists.
     * @throws IOException if it's not possible to create the file.
     */
    public static boolean createNewFileSync(@NonNull File file) throws IOException {
        assertIoOperationOnNonThread();
        return file.createNewFile();
    }

    /**
     * createNewFile
     *
     * @param file
     * @param listener
     * @return
     * @throws IOException
     */
    public static Cancelable createNewFile(@NonNull final File file, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "createNewFile", listener) {
            @Override
            public void run() {
                super.run();

                try {
                    Boolean success = file.createNewFile();
                    performOnSuccess(success);
                } catch (IOException e) {
                    e.printStackTrace();
                    performOnError(e);
                }
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Creates an empty temporary file using the given prefix and suffix as part
     * of the file name. If {@code suffix} is null, {@code .tmp} is used. This
     * method is a convenience method that calls
     * {@link java.io.File#createTempFile(String, String, File)} with the third argument
     * being {@code null}.
     *
     * @param prefix the prefix to the temp file name.
     * @param suffix the suffix to the temp file name.
     * @return the temporary file.
     * @throws IOException if an error occurs when writing the file.
     */
    public static File createTempFileSync(@NonNull String prefix, String suffix) throws IOException {
        return createTempFileSync(prefix, suffix, null);
    }

    /**
     * @param prefix
     * @param suffix
     * @param listener
     * @return
     * @throws IOException
     */
    public static Cancelable createTempFile(@NonNull final String prefix, final String suffix, @NonNull TaskListener<File, Void> listener) {
        return createTempFile(prefix, suffix, null, listener);
    }


    /**
     * Creates an empty temporary file in the given directory using the given
     * prefix and suffix as part of the file name. If {@code suffix} is null, {@code .tmp} is used.
     * <p/>
     * <p>Note that this method does <i>not</i> call {@link #deleteOnExit}, but see the
     * documentation for that method before you call it manually.
     *
     * @param prefix    the prefix to the temp file name.
     * @param suffix    the suffix to the temp file name.
     * @param directory the location to which the temp file is to be written, or
     *                  {@code null} for the default location for temporary files,
     *                  which is taken from the "java.io.tmpdir" system property. It
     *                  may be necessary to set this property to an existing, writable
     *                  directory for this method to work properly.
     * @return the temporary file.
     * @throws IllegalArgumentException if the length of {@code prefix} is less than 3.
     * @throws IOException              if an error occurs when writing the file.
     */
    public static File createTempFileSync(@NonNull String prefix, String suffix, File directory)
            throws IOException {
        assertIoOperationOnNonThread();
        return File.createTempFile(prefix, suffix, directory);
    }

    /**
     * @param prefix
     * @param suffix
     * @param directory
     * @param listener
     * @return
     * @throws IOException
     */
    public static Cancelable createTempFile(@NonNull final String prefix, final String suffix, final File directory, @NonNull TaskListener<File, Void> listener) {
        return new Task<File, Void>(TAG, "createTempFile", listener) {
            @Override
            public void run() {
                super.run();

                try {
                    File file = File.createTempFile(prefix, suffix, directory);
                    performOnSuccess(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    performOnError(e);
                }
            }
        }.runOnNonUiThread(null);
    }


    /**
     * Renames this file to {@code newPath}. This operation is supported for both
     * files and directories.
     * <p/>
     * <p>Many failures are possible. Some of the more likely failures include:
     * <ul>
     * <li>Write permission is required on the directories containing both the source and
     * destination paths.
     * <li>Search permission is required for all parents of both paths.
     * <li>Both paths be on the same mount point. On Android, applications are most likely to hit
     * this restriction when attempting to copy between internal storage and an SD card.
     * </ul>
     * <p/>
     * <p>Note that this method does <i>not</i> throw {@code IOException} on failure.
     * Callers must check the return value.
     *
     * @param newPath the new path.
     * @return true on success.
     */
    public static boolean renameToSync(@NonNull File oldPath, @NonNull File newPath) {
        assertIoOperationOnNonThread();
        return oldPath.renameTo(newPath);
    }

    public static Cancelable renameTo(@NonNull final File oldPath, @NonNull final File newPath, @NonNull TaskListener<Boolean, Void> listener) {
        return new Task<Boolean, Void>(TAG, "renameTo", listener) {
            @Override
            public void run() {
                super.run();

                Boolean success = oldPath.renameTo(newPath);
                performOnSuccess(success);
            }
        }.runOnNonUiThread(null);
    }


    /**
     * Returns the total size in bytes of the partition containing this path.
     * Returns 0 if this path does not exist.
     *
     * @since 1.6
     */
    public static long getTotalSpaceSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.getTotalSpace();
    }

    public static Cancelable getTotalSpace(@NonNull final File file, @NonNull TaskListener<Long, Void> listener) {
        return new Task<Long, Void>(TAG, "getTotalSpace", listener) {
            @Override
            public void run() {
                super.run();

                Long size = file.getTotalSpace();
                performOnSuccess(size);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Returns the number of usable free bytes on the partition containing this path.
     * Returns 0 if this path does not exist.
     * <p/>
     * <p>Note that this is likely to be an optimistic over-estimate and should not
     * be taken as a guarantee your application can actually write this many bytes.
     * On Android (and other Unix-based systems), this method returns the number of free bytes
     * available to non-root users, regardless of whether you're actually running as root,
     * and regardless of any quota or other restrictions that might apply to the user.
     * (The {@code getFreeSpace} method returns the number of bytes potentially available to root.)
     *
     * @since 1.6
     */
    public static long getUsableSpaceSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.getUsableSpace();
    }

    public static Cancelable getUsableSpace(@NonNull final File file, @NonNull TaskListener<Long, Void> listener) {
        return new Task<Long, Void>(TAG, "getUsableSpace", listener) {
            @Override
            public void run() {
                super.run();

                Long size = file.getUsableSpace();
                performOnSuccess(size);
            }
        }.runOnNonUiThread(null);
    }

    /**
     * Returns the number of free bytes on the partition containing this path.
     * Returns 0 if this path does not exist.
     * <p/>
     * <p>Note that this is likely to be an optimistic over-estimate and should not
     * be taken as a guarantee your application can actually write this many bytes.
     *
     * @since 1.6
     */
    public static long getFreeSpaceSync(@NonNull File file) {
        assertIoOperationOnNonThread();
        return file.getFreeSpace();
    }

    public static Cancelable getFreeSpace(@NonNull final File file, @NonNull TaskListener<Long, Void> listener) {
        return new Task<Long, Void>(TAG, "getFreeSpace", listener) {
            @Override
            public void run() {
                super.run();

                Long size = file.getFreeSpace();
                performOnSuccess(size);
            }
        }.runOnNonUiThread(null);
    }
}
