package com.github.snowdream.android.crypto;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.TimingLogger;
import org.apaches.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by snowdream on 4/8/14.
 */
public class CryptoTest extends AndroidTestCase {
    private final static String TAG = "SNOWDREAM";

    private final static String STR = "Hello World!";
    private final static String MD5STR = "ED076287532E86365E841E92BFC50D8C";
    private final static String APKFILE = Environment.getExternalStorageDirectory() +"/MiHome2_3_7_0_100_1_android.apk";
    private final static String MD5APKFILE = "B2ECDD0B5777829A2ADAF72FB2468FB7";
    private final static String EXEFILE = Environment.getExternalStorageDirectory() +"/VMware-workstation-full-12.0.1-3160714.exe";
    private final static String MD5EXEFILE = "3F8321EA391DD0450D361E0D670BCC69";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMD5String() {
        TimingLogger timings = new TimingLogger(TAG, "testMD5String");
        String md5 = DigestUtils.md5Hex(STR);
        timings.dumpToLog();

        assertNotNull(md5);
        assertEquals(MD5STR,md5.toUpperCase());
    }

    public void testAPKFile() {
        try {
            TimingLogger timings = new TimingLogger(TAG, "testAPKFile");
            String md5 = DigestUtils.md5Hex(new FileInputStream(APKFILE));
            timings.dumpToLog();

            assertNotNull(md5);
            assertEquals(MD5APKFILE,md5.toUpperCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testEXEFile() {
        try {
            TimingLogger timings = new TimingLogger(TAG, "testEXEFile");
            String md5 = DigestUtils.md5Hex(new FileInputStream(EXEFILE));
            timings.dumpToLog();
            assertNotNull(md5);
            assertEquals(MD5EXEFILE,md5.toUpperCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
