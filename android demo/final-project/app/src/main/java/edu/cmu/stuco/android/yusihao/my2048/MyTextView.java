package edu.cmu.stuco.android.yusihao.my2048;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MyTextView extends TextView {

    public MyTextView(Context context) {
        super(context);
    }
    @Override
    public boolean onTouchEvent(MotionEvent e){
        return true;
    }
}
