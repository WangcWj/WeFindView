package cn.wang.findview.wefindview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.wang.findview.annotation.FindView;

public class MainActivity extends AppCompatActivity {

    @FindView(value = 123)
    private String a;

    @FindView(value = 3434)
    private TextView mView;

    @FindView(value = 787987)
    ImageView mImage;

    @FindView(value = 123)
    String b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
