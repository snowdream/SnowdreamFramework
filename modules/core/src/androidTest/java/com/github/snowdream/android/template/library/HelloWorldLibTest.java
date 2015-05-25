package com.github.snowdream.android.template.library;

import android.test.AndroidTestCase;

/**
 * Created by snowdream on 4/8/14.
 */
public class HelloWorldLibTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testgetHelloWorld() {
      assertEquals(HelloWorldLib.getHelloWorld(),"Hello World From HelloWorldLib!");
    }

}
