package com.github.snowdream.android.crypto;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.text.TextUtils;
import com.github.snowdream.android.core.task.TaskListener;
import com.jayway.awaitility.Awaitility;
import com.jayway.awaitility.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jayway.awaitility.Awaitility.with;

/**
 * Created by snowdream on 4/8/14.
 */
public class CryptoTest extends AndroidTestCase {
    private final static String STR = "Hello World!";
    private final static String MD5STR = "ED076287532E86365E841E92BFC50D8C";
    private final static String APKFILE = Environment.getExternalStorageDirectory() +"/MiHome2_3_7_0_100_1_android.apk";
    private final static String MD5APKFILE = "B2ECDD0B5777829A2ADAF72FB2468FB7";
    private final static String EXEFILE = Environment.getExternalStorageDirectory() +"/VMware-workstation-full-12.0.1-3160714.exe";
    private final static String MD5EXEFILE = "3F8321EA391DD0450D361E0D670BCC69";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Awaitility.setDefaultTimeout(Duration.FIVE_SECONDS);

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testmd5HexSync(){
        String md5 = Crypto.md5HexSync(STR);
        assertEquals(MD5STR,md5.toUpperCase());

         md5 = Crypto.md5HexSync(STR.getBytes());
         assertEquals(MD5STR,md5.toUpperCase());
    }

    public void testAPKmd5HexASync(){
        final AtomicBoolean[] atomic = {new AtomicBoolean(false)};
        final String[] md5 = {""};

        //check md5 for apk
        try {
             Crypto.md5Hex(new FileInputStream(APKFILE), new TaskListener<String, Void>() {
                 @Override
                 public void onSuccessNonUI(String str) {
                     if (!TextUtils.isEmpty(str)) {
                         atomic[0].set(true);
                         md5[0] = str;
                     }
                 }
             });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        with().await().untilTrue(atomic[0]);
        assertEquals(MD5APKFILE, md5[0].toUpperCase());
    }

    public void testEXEmd5HexASync(){
        final AtomicBoolean[] atomic = {new AtomicBoolean(false)};
        final String[] md5 = {""};

        //check md5 for exe
        try {
            Crypto.md5Hex(new FileInputStream(EXEFILE), new TaskListener<String, Void>() {
                @Override
                public void onSuccessNonUI(String str) {
                    if (!TextUtils.isEmpty(str)) {
                        atomic[0].set(true);
                        md5[0] = str;
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        with().await().untilTrue(atomic[0]);
        assertTrue(TextUtils.isEmpty(md5[0]));
        assertTrue(!MD5EXEFILE.equalsIgnoreCase(md5[0]));
    }
}
