package com.github.snowdream.android.widget;

import java.lang.reflect.Method;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


/**
 * Created by snowdream on 16-7-24.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Toast.class})
public class ToastTest {

    @Test
    public void testCreateOrUpdateToastFromToastBean() throws Exception {
        mockStatic(Toast.class);
        PowerMockito.doReturn(null).when(Toast.class, "createOrUpdateToastFromToastBean", any(android.widget.Toast.class), any(Toast.ToastBean.class));

        Method m = PowerMockito.method(Toast.class,"createOrUpdateToastFromToastBean",android.widget.Toast.class,Toast.ToastBean.class);
        m.setAccessible(true);
        m.invoke(null,null,null);
        m.setAccessible(false);


        PowerMockito.verifyPrivate(Toast.class).invoke("createOrUpdateToastFromToastBean",null,null);
    }
}
