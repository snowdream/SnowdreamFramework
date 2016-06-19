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

package com.github.snowdream.android.apidemos;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.snowdream.android.support.v4.app.Fragment;
import com.github.snowdream.android.support.v4.app.FragmentActivity;
import com.github.snowdream.android.util.log.Log;
import com.github.snowdream.android.widget.Toast;

public class MainActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        Button mButton1;
        Button mButton2;
        Button mButton3;
        Button mButton4;
        Button mButton5;
        private static int inc = 0;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.toast_fragment, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

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

}
