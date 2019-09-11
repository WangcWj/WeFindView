package cn.wang.findview.wefindview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.wang.findview.annotation.FindView;

public class MainActivity extends AppCompatActivity {

    @FindView(value = 123)
    private String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
