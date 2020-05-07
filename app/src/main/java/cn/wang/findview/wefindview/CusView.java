package cn.wang.findview.wefindview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/12/16
 */
public class CusView extends View {
    public CusView(Context context) {
        super(context);
    }

    public CusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionIndex = event.getActionIndex();
        int pointerId = event.getPointerId(actionIndex);
        float x = event.getX(event.findPointerIndex(pointerId));
        event.getAction();
        event.getActionMasked();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e("WANG", "CusView.onTouchEvent.ACTION_DOWN" + event.getAction());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e("WANG", "CusView.onTouchEvent.ACTION_POINTER_DOWN" + event.getAction());
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e("WANG", "CusView.onTouchEvent.ACTION_POINTER_UP" + event.getAction());
                break;
            default:
        }


        return true;
    }
}
