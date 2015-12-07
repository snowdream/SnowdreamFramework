/*
 * Copyright (C) 2014 Snowdream Mobile <yanghui1986527@gmail.com>
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

package com.github.snowdream.android.util;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

public final class FileUtil {
    private FileUtil() {
        throw new AssertionError("No constructor allowed here!");
    }

    /**
     * Get the MimeTypes for the url.
     *
     * @param url the url or uri
     * @return MimeTypes
     */
    public static String getMimeTypeFromUrl(@NonNull String url) {
        return getMimeTypeFromUrlByExtension(url);
    }

    /**
     * Get the MimeTypes for the url by extension.
     *
     * @param url
     * @return
     */
    private static String getMimeTypeFromUrlByExtension(String url) {
        String mimeType = "";

        if (TextUtils.isEmpty(url)) {
            return mimeType;
        }

        String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.encode(url));
        if (!TextUtils.isEmpty(extension)) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        return mimeType;
    }

}
