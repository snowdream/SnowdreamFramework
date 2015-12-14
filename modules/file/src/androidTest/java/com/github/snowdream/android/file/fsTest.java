package com.github.snowdream.android.file;

import android.test.AndroidTestCase;
import org.apache.commons.io.IOCase;

/**
 * Created by snowdream on 4/8/14.
 */
public class FsTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testmd5HexSync() {
        // IOCase
        String str1 = "This is a new String.";
        String str2 = "This is another new String, yes!";

        System.out.println("Ends with string (case sensitive): " +
                IOCase.SENSITIVE.checkEndsWith(str1, "string."));
        System.out.println("Ends with string (case insensitive): " +
                IOCase.INSENSITIVE.checkEndsWith(str1, "string."));

        System.out.println("String equality: " +
                IOCase.SENSITIVE.checkEquals(str1, str2));
    }
}
