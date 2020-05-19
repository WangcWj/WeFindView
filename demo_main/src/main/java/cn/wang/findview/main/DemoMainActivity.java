package cn.wang.findview.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.lang.reflect.Method;

import cn.wang.findview.annotation.FindView;

public class DemoMainActivity extends AppCompatActivity {

    @FindView(WeFindR.id.bnt)
    public TextView mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        try {
            Class<?> aClass = Class.forName("cn.wang.findview.DemoMainActivity_findView");
            Method[] methods = aClass.getMethods();
            Object o = aClass.newInstance();
            Method register = null;
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if ("register".equals(method.getName())) {
                    register = method;
                    break;
                }
            }
            register.invoke(o, this, this.getWindow().getDecorView());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("WANG", "MainActivity.Exception" + e);
            return;
        }
      Log.e("WANG","DemoMainActivity.onCreate"+mBtn);

    }
}
