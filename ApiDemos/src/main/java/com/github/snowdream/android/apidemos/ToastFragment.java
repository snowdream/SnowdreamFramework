package com.github.snowdream.android.apidemos;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.github.snowdream.android.support.v4.app.Fragment;
import com.github.snowdream.android.widget.Toast;

/**
 * Created by yanghui.yangh on 2016-07-08.
 */
public class ToastFragment  extends Fragment implements View.OnClickListener {
    Button mButton0;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    Button mButton5;
    private static int inc = 0;

    public ToastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.toast_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton0 = (Button) view.findViewById(R.id.systemtoastwithtext);
        mButton0.setOnClickListener(this);

        mButton1 = (Button) view.findViewById(R.id.toastwithtext);
        mButton1.setOnClickListener(this);

        mButton2 = (Button) view.findViewById(R.id.toastwithimage);
        mButton2.setOnClickListener(this);

        mButton3 = (Button) view.findViewById(R.id.toastwithtextimage);
        mButton3.setOnClickListener(this);

        mButton4 = (Button) view.findViewById(R.id.toastwithcustompos);
        mButton4.setOnClickListener(this);

        mButton5 = (Button) view.findViewById(R.id.toastbyoriention);
        mButton5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String orientation;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            orientation = "portrait";
        }else{
            orientation = "landscape";
        }

        LayoutInflater inflater = getLayoutInflater(null);
        View view = null;
        Toast.ToastBean bean = null;
        Toast.ToastBean bean1 = null;
        switch (id){
            case R.id.systemtoastwithtext:
                inc++;
                android.widget.Toast.makeText(getContext(),"toastwithtext： "+orientation +" "+inc, android.widget.Toast.LENGTH_LONG).show();
                break;
            case R.id.toastwithtext:
                inc++;
                Toast.show(getContext(),"toastwithtext： "+orientation +" "+inc, Toast.LENGTH_LONG);
                break;
            case R.id.toastwithimage:
                view =   inflater.inflate(R.layout.toast_image, null);
                bean = new Toast.ToastBean(getContext(),view, Toast.LENGTH_LONG);
                Toast.show(bean);
                break;
            case R.id.toastwithtextimage:
                view =   inflater.inflate(R.layout.toast_text_image, null);
                bean = new Toast.ToastBean(getContext(),view, Toast.LENGTH_LONG);
                Toast.show(bean);
                break;
            case R.id.toastwithcustompos:
                view =   inflater.inflate(R.layout.toast_text_image, null);
                bean = new Toast.ToastBean(getContext(),view, Toast.LENGTH_LONG);
                bean.setGravity(Gravity.LEFT | Gravity.CENTER,0,0);
                Toast.show(bean);
                break;
            case R.id.toastbyoriention:
                view =   inflater.inflate(R.layout.toast_text_image, null);
                View view1 =   inflater.inflate(R.layout.toast_image, null);
                bean = new Toast.ToastBean(getContext(),view, Toast.LENGTH_LONG);
                bean1 = new Toast.ToastBean(getContext(),view1, Toast.LENGTH_LONG);
                bean.setGravity(Gravity.LEFT | Gravity.CENTER,0,0);
                bean1.setGravity(Gravity.RIGHT| Gravity.CENTER,0,0);
                Toast.show(bean,bean1);
                break;
        }
    }
}