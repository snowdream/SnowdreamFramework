package com.github.snowdream.android.apidemos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.github.snowdream.android.support.v4.app.Fragment;
import com.github.snowdream.android.util.ShellUtil;
import com.github.snowdream.android.util.log.Log;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.IOException;

/**
 * Created by yanghui.yangh on 2016-07-08.
 */
public class TestFragment extends Fragment implements View.OnClickListener {
    Button mButton0;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    Button mButton5;

    public TestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton0 = (Button) view.findViewById(R.id.button_0);
        mButton0.setOnClickListener(this);

        mButton1 = (Button) view.findViewById(R.id.button_1);
        mButton1.setOnClickListener(this);

        mButton2 = (Button) view.findViewById(R.id.button_2);
        mButton2.setOnClickListener(this);

        mButton3 = (Button) view.findViewById(R.id.button_3);
        mButton3.setOnClickListener(this);

        mButton4 = (Button) view.findViewById(R.id.button_4);
        mButton4.setOnClickListener(this);

        mButton5 = (Button) view.findViewById(R.id.button_5);
        mButton5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        switch (id) {
            case R.id.button_0: {
                PumpStreamHandler handler = new PumpStreamHandler(new LogOutputStream() {
                    @Override
                    protected void processLine(String line, int logLevel) {
                        Log.i(line);
                    }
                });
                try {
                    ShellUtil.exec("ls -la", handler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.button_1: {
            }
            break;
            case R.id.button_2:
                break;
            case R.id.button_3:
                break;
            case R.id.button_4:
                break;
            case R.id.button_5:
                break;
        }
    }
}