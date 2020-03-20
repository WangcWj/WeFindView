package cn.wang.findview.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import cn.wang.findview.annotation.FindView;

public class DemoMainActivity extends AppCompatActivity {

    @FindView(WeFindR.id.demo_text)
    TextView mDemoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final View viewById = findViewById(R.id.demo_text);
       final View viewById2 = findViewById(R.id.demo_text2);
        viewById.post(new Runnable() {
            @Override
            public void run() {
                Log.e("cc.wang","DemoMainActivity.run.1111  "+viewById.getHeight());
            }
        });
        viewById2.post(new Runnable() {
            @Override
            public void run() {
                Log.e("cc.wang","DemoMainActivity.run.2222 "+viewById2.getHeight());
            }
        });

    }
}
