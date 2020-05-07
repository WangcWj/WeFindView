package cn.wang.findview.wefindview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.wang.findview.MainActivity_findView;
import cn.wang.findview.annotation.FindView;
import cn.wang.findview.main.DemoMainActivity;

public class MainActivity extends AppCompatActivity {


    @FindView(R.id.demo_text)
    public TextView mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;

        float density = getResources().getDisplayMetrics().density;

        Log.e("cc.wang","MainActivity.onCreate."+scaledDensity+"  density  "+density);

    }
}
