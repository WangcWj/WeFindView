package cn.wang.findview.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cn.wang.findview.annotation.FindView;

public class DemoMainActivity extends AppCompatActivity {

    @FindView(R2.id.demo_text)
    TextView mDemoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
