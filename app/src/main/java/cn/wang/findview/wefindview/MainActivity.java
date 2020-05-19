package cn.wang.findview.wefindview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;

import cn.wang.findview.MainActivity_findView;
import cn.wang.findview.annotation.FindView;
import cn.wang.findview.main.DemoMainActivity;

public class MainActivity extends AppCompatActivity {


    @FindView(R.id.demo_text2)
    public TextView mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Class<?> aClass = Class.forName("cn.wang.findview.MainActivity_findView");
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


        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DemoMainActivity.class);
                startActivity(intent);
            }
        });

    }
}
